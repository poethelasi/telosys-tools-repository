/**
 *  Copyright (C) 2008-2013  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.repository;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.repository.model.Entity;
import org.telosys.tools.repository.model.ForeignKey;
import org.telosys.tools.repository.model.ForeignKeyColumn;

/**
 * 
 * @author L.Guerin
 */
public class LinksUtil {

	/**
	 * Returns true if the 2 given ForeignKey will result as the same link 
	 * @param fk1
	 * @param fk2
	 * @return
	 * @throws TelosysToolsException
	 */
	public static boolean sameLink ( ForeignKey fk1, ForeignKey fk2 ) // throws TelosysToolsException 
	{
		if ( fk1 == null || fk2 == null ) throw new RuntimeException("sameLink : null argument ");
		// fk1.getName() ; // Foreign Key name is not significant for a link
		if ( ! StrUtil.identical(fk1.getTableName(), fk2.getTableName()) ) return false ;
		if ( ! StrUtil.identical(fk1.getTableRef(),  fk2.getTableRef())  ) return false ;
		ForeignKeyColumn[] columns1 = fk1.getForeignKeyColumns() ;
		ForeignKeyColumn[] columns2 = fk2.getForeignKeyColumns() ;
		if ( columns1.length != columns2.length ) return false ;
		for ( ForeignKeyColumn c1 : columns1 ) {
			ForeignKeyColumn c2 = fk2.getForeignKeyColumn(c1.getColumnName());
			if ( c2 == null ) return false ;
			if ( ! StrUtil.identical(c1.getTableName(),  c2.getTableName())  ) return false ;
			if ( ! StrUtil.identical(c1.getColumnName(), c2.getColumnName()) ) return false ;
			if ( ! StrUtil.identical(c1.getTableRef(),   c2.getTableRef())   ) return false ;
			if ( ! StrUtil.identical(c1.getColumnRef(),  c2.getColumnRef())  ) return false ;
			// Following information is not significant for a link
			// c1.getDeferrableCode(); 
			// c1.getUpdateRuleCode();
			// c1.getDeleteRuleCode();
			// c1.getSequence() ;
		}
		//--- All data used to build a link are identical
		return true ;
	}
	
	/**
	 * Returns a copy of the current array of Foreign Keys for the given entity
	 * @param entity
	 * @return
	 */
	public static ForeignKey[] getForeignKeysCopy ( Entity entity ) {
		ForeignKey[] foreignKeys = entity.getForeignKeys();
		ForeignKey[] foreignKeysCopy = new ForeignKey[foreignKeys.length];
		for ( int i = 0 ; i < foreignKeys.length ; i++ ) {
			foreignKeysCopy[i] = foreignKeys[i];
		}
		return foreignKeysCopy ;
	}
}
