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
package org.telosys.tools.repository.model;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.jdbctypes.MetadataUtil;

public class ForeignKeyColumn implements Comparable<ForeignKeyColumn>
{

	private String _tableName ;
	
	private String _columnName ;
	
	private int    _sequence ;
	
	private String _tableRef ;
	
	private String _columnRef ;
	
//	private String _updateRule ;
//	private String _deleteRule ;
//	private String _deferrable ;
	private int  _updateRuleCode ; // v 2.0.7
	private int  _deleteRuleCode ; // v 2.0.7
	private int  _deferrableCode ; // v 2.0.7

	//-------------------------------------------------------------------------------
	
	public String getTableName() {
		return _tableName;
	}

	public void setTableName(String name) {
		_tableName = name;
	}

	//-------------------------------------------------------------------------------
	
	public String getColumnName() {
		return _columnName;
	}

	public void setColumnName(String v) {
		_columnName = v;
	}

	//-------------------------------------------------------------------------------
	
	public int getSequence() {
		return _sequence;
	}

	public void setSequence(int v) {
		_sequence = v;
	}

	//-------------------------------------------------------------------------------

	public String getTableRef() {
		return _tableRef;
	}

	public void setTableRef(String v) {
		_tableRef = v;
	}

	//-------------------------------------------------------------------------------

	public String getColumnRef() {
		return _columnRef;
	}

	public void setColumnRef(String v) {
		_columnRef = v;
	}

	//-------------------------------------------------------------------------------

	public String getDeferrableText() {
		return MetadataUtil.getForeignKeyDeferrability(_deferrableCode);
	}
	public int getDeferrableCode() {
		return _deferrableCode;
	}
	public void setDeferrableCode(int v) {
		_deferrableCode = v;
	}

	//-------------------------------------------------------------------------------

	public String getDeleteRuleText() {
		return MetadataUtil.getForeignKeyDeleteRule(_deleteRuleCode);
	}
	public int getDeleteRuleCode() {
		return _deleteRuleCode;
	}
	public void setDeleteRuleCode(int v) {
		_deleteRuleCode = v;
	}

	//-------------------------------------------------------------------------------

	public String getUpdateRuleText() {
		return MetadataUtil.getForeignKeyUpdateRule(_updateRuleCode);
	}
	public int getUpdateRuleCode() {
		return _updateRuleCode;
	}
	public void setUpdateRuleCode(int v) {
		_updateRuleCode = v;
	}

	//-------------------------------------------------------------------------------
	public boolean sameStrings(String s1, String s2) 
	{
		if ( null == s1 )
		{
			if ( null == s2 ) return true ;
		}
		else
		{
			if ( null == s2 ) return false ;
		}
		return s1.equals(s2);
	}

	//-------------------------------------------------------------------------------
	public boolean equals(Object o) 
	{
		if ( null == o ) return false ;
		if ( this == o ) return true ;
		if ( o.getClass() != this.getClass() ) return false ;
		
		ForeignKeyColumn fkcol = (ForeignKeyColumn) o ;
		
		return     StrUtil.identical(_columnName, fkcol.getColumnName() )
				&& StrUtil.identical(_columnRef, fkcol.getColumnRef() )
				&& StrUtil.identical(_tableName, fkcol.getTableName() )
				&& StrUtil.identical(_tableRef, fkcol.getTableRef() )
//				&& StrUtil.identical(_deferrable, fkcol.getDeferrable() )
//				&& StrUtil.identical(_deleteRule, fkcol.getDeleteRule() )
//				&& StrUtil.identical(_updateRule, fkcol.getUpdateRule() )
				&& _deferrableCode == fkcol.getDeferrableCode()
				&& _deleteRuleCode == fkcol.getDeleteRuleCode()
				&& _updateRuleCode == fkcol.getUpdateRuleCode()
				&& ( this.getSequence() == fkcol.getSequence() )
				;
	}
	
	//-------------------------------------------------------------------------------
	//public int compareTo(Object o) {
	public int compareTo(ForeignKeyColumn other) {
		if ( other != null )
		{
			//ForeignKeyColumn other = (ForeignKeyColumn) o;
			return ( this.getSequence() - other.getSequence() );
		}
		return 0;
	}
	
}
