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
package org.telosys.tools.repository.persistence.wrapper;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.repository.model.Column;
import org.telosys.tools.repository.persistence.util.RepositoryConst;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ColumnWrapper {
	
	public ColumnWrapper() {
		super();
	}

	public Column getColumn(final Element elem) 
	{
		final Column column = new Column();

		column.setSelected(StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_SELECTED)));

		//--- DATABASE INFOS
		column.setDatabaseName(elem.getAttribute(RepositoryConst.COLUMN_DB_NAME));
		column.setPrimaryKey(StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_DB_PRIMARY_KEY)));
		column.setDatabaseTypeName(elem.getAttribute(RepositoryConst.COLUMN_DB_TYPE_NAME));
		column.setDatabaseSize(StrUtil.getInt(elem.getAttribute(RepositoryConst.COLUMN_DB_SIZE)));
		column.setDatabaseNotNull(StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_DB_NOTNULL)));
		column.setForeignKey(StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_DB_FOREIGN_KEY)));
		column.setAutoIncremented(StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_DB_AUTO_INCREMENTED))); // #LGU 04/08/2011
		column.setDatabaseDefaultValue( elem.getAttribute(RepositoryConst.COLUMN_DB_DEFAULT_VALUE) ); // #LGU 10/08/2011
		column.setDatabaseComment( elem.getAttribute(RepositoryConst.COLUMN_DB_COMMENT) ); // v 2.1.1 #LCH 20/08/2014
		column.setDatabasePosition(StrUtil.getInt(elem.getAttribute(RepositoryConst.COLUMN_DB_POSITION))); // #LGU 10/08/2011

		column.setJdbcTypeCode(StrUtil.getInt(elem.getAttribute(RepositoryConst.COLUMN_JDBC_TYPE_CODE)));

		//--- JAVA OBJECT
		column.setJavaName(elem.getAttribute(RepositoryConst.COLUMN_JAVA_NAME));
		column.setJavaType(elem.getAttribute(RepositoryConst.COLUMN_JAVA_TYPE));
		column.setJavaNotNull(  StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_NOT_NULL )) );// #LGU 30/08/2011
		column.setNotEmpty( StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_NOT_EMPTY)) );// #LGU 30/08/2011
		column.setNotBlank( StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_NOT_BLANK)) );// #LGU 30/08/2011
		
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_JAVA_DEFAULT_VALUE)) == false) {
			column.setJavaDefaultValue( elem.getAttribute(RepositoryConst.COLUMN_JAVA_DEFAULT_VALUE) );// #LGU 17/10/2011
		}
		
		//--- Retrieve BOOLEAN informations if any
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_BOOL_TRUE)) == false) {
			column.setBooleanTrueValue(elem.getAttribute(RepositoryConst.COLUMN_BOOL_TRUE));
		}
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_BOOL_FALSE)) == false) {
			column.setBooleanFalseValue(elem.getAttribute(RepositoryConst.COLUMN_BOOL_FALSE));
		}

		//--- Retrieve STRING informations if any
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_LONG_TEXT)) == false) {
			column.setLongText(StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_LONG_TEXT)));
		}
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_MIN_LENGTH)) == false) {
			column.setMinLength( elem.getAttribute(RepositoryConst.COLUMN_MIN_LENGTH) );
		}
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_MAX_LENGTH)) == false) {
			column.setMaxLength( elem.getAttribute(RepositoryConst.COLUMN_MAX_LENGTH) );
		}
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_PATTERN)) == false) {
			column.setPattern( elem.getAttribute(RepositoryConst.COLUMN_PATTERN) );
		}
		
		//--- Retrieve DATE/TIME informations if any
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_DATE_TYPE)) == false) {
			column.setDateType(elem.getAttribute(RepositoryConst.COLUMN_DATE_TYPE));
		}
		column.setDatePast  ( StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_DATE_PAST)) ); // #LGU 30/08/2011
		column.setDateFuture( StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_DATE_FUTURE)) );// #LGU 30/08/2011
		column.setDateBefore( StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_DATE_BEFORE)) );// #LGU 30/08/2011
		column.setDateBeforeValue( elem.getAttribute(RepositoryConst.COLUMN_DATE_BEFORE_VALUE) );// #LGU 30/08/2011
		column.setDateAfter ( StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_DATE_AFTER)) );// #LGU 30/08/2011
		column.setDateAfterValue ( elem.getAttribute(RepositoryConst.COLUMN_DATE_AFTER_VALUE) );// #LGU 30/08/2011
		
		//--- Retrieve NUMBER informations if any
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_MIN_VALUE)) == false) {
			column.setMinValue(elem.getAttribute(RepositoryConst.COLUMN_MIN_VALUE));
		}
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_MAX_VALUE)) == false) {
			column.setMaxValue(elem.getAttribute(RepositoryConst.COLUMN_MAX_VALUE));
		}
		
		column.setLabel    ( elem.getAttribute(RepositoryConst.COLUMN_LABEL)     ) ; // #LGU 20/02/2013
		column.setInputType( elem.getAttribute(RepositoryConst.COLUMN_INPUT_TYPE)) ; // #LGU 20/02/2013
		
		return column;
	}

	public Element getXmlDesc(final Column column, final Document doc) 
	{
		final Element element = doc.createElement(RepositoryConst.COLUMN);

		element.setAttribute(RepositoryConst.COLUMN_SELECTED, Boolean.toString(column.getSelected()));

		//--- DATABASE INFOS
		element.setAttribute(RepositoryConst.COLUMN_DB_NAME, column.getDatabaseName());
		element.setAttribute(RepositoryConst.COLUMN_DB_NOTNULL, Boolean.toString(column.isDatabaseNotNull()));
		element.setAttribute(RepositoryConst.COLUMN_DB_SIZE, Integer.toString(column.getDatabaseSize()));
		element.setAttribute(RepositoryConst.COLUMN_DB_TYPE_NAME, column.getDatabaseTypeName());
		if (column.isForeignKey()) {
			element.setAttribute(RepositoryConst.COLUMN_DB_FOREIGN_KEY, Boolean.toString(column.isForeignKey()));
		}
		if (column.isPrimaryKey()) {
			element.setAttribute(RepositoryConst.COLUMN_DB_PRIMARY_KEY, Boolean.toString(column.isPrimaryKey()));
		}
		if ( column.isAutoIncremented() ) { // #LGU 04/08/2011
			element.setAttribute(RepositoryConst.COLUMN_DB_AUTO_INCREMENTED, Boolean.toString(column.isAutoIncremented()));
		}		
		element.setAttribute(RepositoryConst.COLUMN_DB_POSITION, Integer.toString( column.getDatabasePosition() ) ); // #LGU 10/08/2011
		element.setAttribute(RepositoryConst.COLUMN_DB_DEFAULT_VALUE, column.getDatabaseDefaultValue() ); // #LGU 10/08/2011
		element.setAttribute(RepositoryConst.COLUMN_DB_COMMENT, column.getDatabaseComment() ); // v 2.1.1 #LCH 20/08/2014
		
		element.setAttribute(RepositoryConst.COLUMN_JDBC_TYPE_CODE, Integer.toString(column.getJdbcTypeCode()));
		
		//--- JAVA OBJECT
		element.setAttribute(RepositoryConst.COLUMN_JAVA_NAME, column.getJavaName());
		element.setAttribute(RepositoryConst.COLUMN_JAVA_TYPE, column.getJavaType());
				
		if (StrUtil.nullOrVoid(column.getJavaDefaultValue()) == false) {
			element.setAttribute(RepositoryConst.COLUMN_JAVA_DEFAULT_VALUE, column.getJavaDefaultValue()); // #LGU 17/10/2011
		}
		
		//--- SPECIAL INFO & VALIDATION 

		element.setAttribute(RepositoryConst.COLUMN_NOT_NULL,  Boolean.toString(column.getJavaNotNull())  );
		
		
		if ( column.isJavaTypeBoolean() )  //--- Keep boolean informations
		{
			if (StrUtil.nullOrVoid(column.getBooleanFalseValue()) == false) {
				element.setAttribute(RepositoryConst.COLUMN_BOOL_FALSE, column.getBooleanFalseValue());
			}
			if (StrUtil.nullOrVoid(column.getBooleanTrueValue()) == false) {
				element.setAttribute(RepositoryConst.COLUMN_BOOL_TRUE, column.getBooleanTrueValue());
			}
		}

		if ( column.isJavaTypeString() )  //--- Keep String informations
		{
			if (column.getLongText()) {
				element.setAttribute(RepositoryConst.COLUMN_LONG_TEXT, Boolean.toString(column.getLongText()));
			}
			if (column.getNotEmpty()) {
				element.setAttribute(RepositoryConst.COLUMN_NOT_EMPTY, Boolean.toString(column.getNotEmpty()) );
			}
			if (column.getNotBlank()) {
				element.setAttribute(RepositoryConst.COLUMN_NOT_BLANK, Boolean.toString(column.getNotBlank()) );
			}
			if ( ! StrUtil.nullOrVoid(column.getMinLength()) ) {
				element.setAttribute(RepositoryConst.COLUMN_MIN_LENGTH, column.getMinLength() );
			}
			if ( ! StrUtil.nullOrVoid(column.getMaxLength()) ) {
				element.setAttribute(RepositoryConst.COLUMN_MAX_LENGTH, column.getMaxLength() );
			}
			if ( ! StrUtil.nullOrVoid(column.getPattern()) ) {
				element.setAttribute(RepositoryConst.COLUMN_PATTERN, column.getPattern() );
			}
		}

		if ( column.isJavaTypeDateOrTime() )  //--- Keep Date/Time informations
		{
			if (StrUtil.nullOrVoid(column.getDateType()) == false) {
				element.setAttribute(RepositoryConst.COLUMN_DATE_TYPE, column.getDateType()); 
			}
			if ( column.isDatePast() ) {
				element.setAttribute(RepositoryConst.COLUMN_DATE_PAST,  Boolean.toString(true)); // #LGU 30/08/2011
			}
			if ( column.isDateFuture() ) {
				element.setAttribute(RepositoryConst.COLUMN_DATE_FUTURE,  Boolean.toString(true)); // #LGU 30/08/2011
			}
			if ( column.isDateBefore() ) {
				element.setAttribute(RepositoryConst.COLUMN_DATE_BEFORE,  Boolean.toString(true)); // #LGU 30/08/2011
				element.setAttribute(RepositoryConst.COLUMN_DATE_BEFORE_VALUE, column.getDateBeforeValue()); // #LGU 30/08/2011
			}
			if ( column.isDateAfter() ) {
				element.setAttribute(RepositoryConst.COLUMN_DATE_AFTER,  Boolean.toString(true)); // #LGU 30/08/2011
				element.setAttribute(RepositoryConst.COLUMN_DATE_AFTER_VALUE, column.getDateAfterValue()); // #LGU 30/08/2011
			}
		}

		if ( column.isJavaTypeNumber() )  //--- Keep Number informations
		{
			if ( ! StrUtil.nullOrVoid(column.getMinValue()) ) {
				element.setAttribute(RepositoryConst.COLUMN_MIN_VALUE, column.getMinValue() ); // #LGU 31/08/2011
			}
			if ( ! StrUtil.nullOrVoid(column.getMaxValue()) ) {
				element.setAttribute(RepositoryConst.COLUMN_MAX_VALUE, column.getMaxValue() ); // #LGU 31/08/2011
			}
		}
		
		if ( column.isJavaTypeNumber() || column.isJavaTypeDateOrTime() )  
		{
			if ( ! StrUtil.nullOrVoid(column.getFormat()) ) {
				element.setAttribute(RepositoryConst.COLUMN_FORMAT, column.getFormat() );
			}
		}

		element.setAttribute(RepositoryConst.COLUMN_LABEL,      column.getLabel()     ); // #LGU 20/02/2013
		element.setAttribute(RepositoryConst.COLUMN_INPUT_TYPE, column.getInputType() ); // #LGU 20/02/2013

		return element;
	}

}
