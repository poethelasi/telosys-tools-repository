/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
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
import org.telosys.tools.generic.model.DateType;
import org.telosys.tools.repository.model.AttributeInDbModel;
import org.telosys.tools.repository.persistence.util.RepositoryConst;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ColumnWrapper {
	
	public ColumnWrapper() {
		super();
	}

	public AttributeInDbModel getColumn(final Element elem) 
	{
		final AttributeInDbModel column = new AttributeInDbModel();

		column.setSelected(StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_SELECTED)));

		//--- DATABASE INFOS
		column.setDatabaseName(elem.getAttribute(RepositoryConst.COLUMN_DB_NAME));
		//column.setPrimaryKey(StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_DB_PRIMARY_KEY)));
		column.setKeyElement(StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_DB_PRIMARY_KEY))); // v 3.0.0
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
		//column.setJavaName(elem.getAttribute(RepositoryConst.COLUMN_JAVA_NAME));
		column.setName(elem.getAttribute(RepositoryConst.COLUMN_JAVA_NAME)); // v 3.0.0
		//column.setJavaType(elem.getAttribute(RepositoryConst.COLUMN_JAVA_TYPE));
		column.setFullType(elem.getAttribute(RepositoryConst.COLUMN_JAVA_TYPE)); // v 3.0.0
		//column.setJavaNotNull(  StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_NOT_NULL )) );// #LGU 30/08/2011
		column.setNotNull(  StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_NOT_NULL )) ); // v 3.0.0
		column.setNotEmpty( StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_NOT_EMPTY)) );// #LGU 30/08/2011
		column.setNotBlank( StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_NOT_BLANK)) );// #LGU 30/08/2011
		
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_JAVA_DEFAULT_VALUE)) == false) {
			column.setDefaultValue( elem.getAttribute(RepositoryConst.COLUMN_JAVA_DEFAULT_VALUE) );// #LGU 17/10/2011
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
			//column.setMinLength( elem.getAttribute(RepositoryConst.COLUMN_MIN_LENGTH) );
			column.setMinLength( StrUtil.getIntegerObject( elem.getAttribute(RepositoryConst.COLUMN_MIN_LENGTH) ) ); // v 3.0.0
		}
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_MAX_LENGTH)) == false) {
			//column.setMaxLength( elem.getAttribute(RepositoryConst.COLUMN_MAX_LENGTH) );
			column.setMaxLength( StrUtil.getIntegerObject( elem.getAttribute(RepositoryConst.COLUMN_MAX_LENGTH) ) ); // v 3.0.0
		}
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_PATTERN)) == false) {
			column.setPattern( elem.getAttribute(RepositoryConst.COLUMN_PATTERN) );
		}
		
		//--- Retrieve DATE/TIME informations if any
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_DATE_TYPE)) == false) {
//			column.setDateType(elem.getAttribute(RepositoryConst.COLUMN_DATE_TYPE));
			column.setDateType(convertStringToDateType(elem.getAttribute(RepositoryConst.COLUMN_DATE_TYPE))); // v 3.0.0
		}
		column.setDatePast  ( StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_DATE_PAST)) ); // #LGU 30/08/2011
		column.setDateFuture( StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_DATE_FUTURE)) );// #LGU 30/08/2011
		column.setDateBefore( StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_DATE_BEFORE)) );// #LGU 30/08/2011
		column.setDateBeforeValue( elem.getAttribute(RepositoryConst.COLUMN_DATE_BEFORE_VALUE) );// #LGU 30/08/2011
		column.setDateAfter ( StrUtil.getBoolean(elem.getAttribute(RepositoryConst.COLUMN_DATE_AFTER)) );// #LGU 30/08/2011
		column.setDateAfterValue ( elem.getAttribute(RepositoryConst.COLUMN_DATE_AFTER_VALUE) );// #LGU 30/08/2011
		
		//--- Retrieve NUMBER informations if any
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_MIN_VALUE)) == false) {
			//column.setMinValue(elem.getAttribute(RepositoryConst.COLUMN_MIN_VALUE));
			column.setMinValue( StrUtil.getIntegerObject( elem.getAttribute(RepositoryConst.COLUMN_MIN_VALUE) ) ); // v 3.0.0
		}
		if (StrUtil.nullOrVoid(elem.getAttribute(RepositoryConst.COLUMN_MAX_VALUE)) == false) {
			//column.setMaxValue(elem.getAttribute(RepositoryConst.COLUMN_MAX_VALUE));
			column.setMaxValue( StrUtil.getIntegerObject( elem.getAttribute(RepositoryConst.COLUMN_MAX_VALUE) ) ); // v 3.0.0
		}
		
		column.setLabel    ( elem.getAttribute(RepositoryConst.COLUMN_LABEL)     ) ; // #LGU 20/02/2013
		column.setInputType( elem.getAttribute(RepositoryConst.COLUMN_INPUT_TYPE)) ; // #LGU 20/02/2013
		
		return column;
	}

	/**
	 * @param column
	 * @param doc
	 * @return
	 */
	public Element getXmlDesc(final AttributeInDbModel column, final Document doc) 
	{
		final Element element = doc.createElement(RepositoryConst.COLUMN);

		//element.setAttribute(RepositoryConst.COLUMN_SELECTED, Boolean.toString(column.getSelected()));
		element.setAttribute(RepositoryConst.COLUMN_SELECTED, Boolean.toString(column.isSelected())); // v 3.0.0

		//--- DATABASE INFOS
		element.setAttribute(RepositoryConst.COLUMN_DB_NAME, column.getDatabaseName());
		element.setAttribute(RepositoryConst.COLUMN_DB_NOTNULL, Boolean.toString(column.isDatabaseNotNull()));
		element.setAttribute(RepositoryConst.COLUMN_DB_SIZE, Integer.toString(column.getDatabaseSize()));
		//element.setAttribute(RepositoryConst.COLUMN_DB_TYPE_NAME, column.getDatabaseTypeName());
		element.setAttribute(RepositoryConst.COLUMN_DB_TYPE_NAME, column.getDatabaseType()); // v 3.0.0
//		if (column.isForeignKey()) {
//			element.setAttribute(RepositoryConst.COLUMN_DB_FOREIGN_KEY, Boolean.toString(column.isForeignKey()));
//		}
		if ( column.isUsedInForeignKey() ) { // v 3.0.0
			element.setAttribute(RepositoryConst.COLUMN_DB_FOREIGN_KEY, Boolean.toString(column.isUsedInForeignKey())); // v 3.0.0
		}
//		if (column.isPrimaryKey()) {
//			element.setAttribute(RepositoryConst.COLUMN_DB_PRIMARY_KEY, Boolean.toString(column.isPrimaryKey()));
//		}
		if ( column.isKeyElement() ) {
			element.setAttribute(RepositoryConst.COLUMN_DB_PRIMARY_KEY, Boolean.toString(column.isKeyElement())); // v 3.0.0
		}
		if ( column.isAutoIncremented() ) { // #LGU 04/08/2011
			element.setAttribute(RepositoryConst.COLUMN_DB_AUTO_INCREMENTED, Boolean.toString(column.isAutoIncremented()));
		}		
		element.setAttribute(RepositoryConst.COLUMN_DB_POSITION, Integer.toString( column.getDatabasePosition() ) ); // #LGU 10/08/2011
		element.setAttribute(RepositoryConst.COLUMN_DB_DEFAULT_VALUE, column.getDatabaseDefaultValue() ); // #LGU 10/08/2011
		element.setAttribute(RepositoryConst.COLUMN_DB_COMMENT, column.getDatabaseComment() ); // v 2.1.1 #LCH 20/08/2014
		
		element.setAttribute(RepositoryConst.COLUMN_JDBC_TYPE_CODE, Integer.toString(column.getJdbcTypeCode()));
		
		//--- JAVA OBJECT
		//element.setAttribute(RepositoryConst.COLUMN_JAVA_NAME, column.getJavaName());
		element.setAttribute(RepositoryConst.COLUMN_JAVA_NAME, column.getName()); // v 3.0.0
		//element.setAttribute(RepositoryConst.COLUMN_JAVA_TYPE, column.getJavaType());
		element.setAttribute(RepositoryConst.COLUMN_JAVA_TYPE, column.getFullType()); // v 3.0.0
				
		if (StrUtil.nullOrVoid(column.getDefaultValue()) == false) {
			element.setAttribute(RepositoryConst.COLUMN_JAVA_DEFAULT_VALUE, column.getDefaultValue()); // #LGU 17/10/2011
		}
		
		//--- SPECIAL INFO & VALIDATION 

		//element.setAttribute(RepositoryConst.COLUMN_NOT_NULL,  Boolean.toString(column.getJavaNotNull())  );
		element.setAttribute(RepositoryConst.COLUMN_NOT_NULL,  Boolean.toString(column.isNotNull() ) );
		
		
		if ( column.isJavaTypeBoolean() ) { //--- Keep boolean informations
			if (StrUtil.nullOrVoid(column.getBooleanFalseValue()) == false) {
				element.setAttribute(RepositoryConst.COLUMN_BOOL_FALSE, column.getBooleanFalseValue());
			}
			if (StrUtil.nullOrVoid(column.getBooleanTrueValue()) == false) {
				element.setAttribute(RepositoryConst.COLUMN_BOOL_TRUE, column.getBooleanTrueValue());
			}
		}

		if ( column.isJavaTypeString() )  { //--- Keep String informations
//			if (column.getLongText()) {
//				element.setAttribute(RepositoryConst.COLUMN_LONG_TEXT, Boolean.toString(column.getLongText()));
//			}
			if (column.isLongText()) {
				element.setAttribute(RepositoryConst.COLUMN_LONG_TEXT, Boolean.toString(column.isLongText())); // v 3.0.0
			}
//			if (column.getNotEmpty()) {
//				element.setAttribute(RepositoryConst.COLUMN_NOT_EMPTY, Boolean.toString(column.getNotEmpty()) );
//			}
			if (column.isNotEmpty()) {
				element.setAttribute(RepositoryConst.COLUMN_NOT_EMPTY, Boolean.toString(column.isNotEmpty()) ); // v 3.0.0
			}
//			if (column.getNotBlank()) {
//				element.setAttribute(RepositoryConst.COLUMN_NOT_BLANK, Boolean.toString(column.getNotBlank()) );
//			}
			if (column.isNotBlank()) {
				element.setAttribute(RepositoryConst.COLUMN_NOT_BLANK, Boolean.toString(column.isNotBlank()) ); // v 3.0.0
			}
//			if ( ! StrUtil.nullOrVoid(column.getMinLength()) ) {
//				element.setAttribute(RepositoryConst.COLUMN_MIN_LENGTH, column.getMinLength() );
//			}
			if ( column.getMinLength() != null ) {
				element.setAttribute(RepositoryConst.COLUMN_MIN_LENGTH, column.getMinLength().toString() ); // v 3.0.0
			}
//			if ( ! StrUtil.nullOrVoid(column.getMaxLength()) ) {
//				element.setAttribute(RepositoryConst.COLUMN_MAX_LENGTH, column.getMaxLength() );
//			}
			if ( column.getMaxLength() != null ) {
				element.setAttribute(RepositoryConst.COLUMN_MAX_LENGTH, column.getMaxLength().toString() ); // v 3.0.0
			}
			if ( ! StrUtil.nullOrVoid(column.getPattern()) ) {
				element.setAttribute(RepositoryConst.COLUMN_PATTERN, column.getPattern() );
			}
		}

		if ( column.isJavaTypeDateOrTime() )  //--- Keep Date/Time informations
		{
//			if (StrUtil.nullOrVoid(column.getDateType()) == false) {
//				element.setAttribute(RepositoryConst.COLUMN_DATE_TYPE, column.getDateType()); 
//			}
			if ( column.getDateType() != null  &&  column.getDateType() != DateType.UNDEFINED ) {
				element.setAttribute( RepositoryConst.COLUMN_DATE_TYPE, convertDateTypeToString(column.getDateType() ) );  // v 3.0.0
				
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
//			if ( ! StrUtil.nullOrVoid(column.getMinValue()) ) {
//				element.setAttribute(RepositoryConst.COLUMN_MIN_VALUE, column.getMinValue() ); // #LGU 31/08/2011
//			}
			if ( column.getMinValue() != null ) {
				element.setAttribute(RepositoryConst.COLUMN_MIN_VALUE, column.getMinValue().toString() ); // v 3.0.0
			}
//			if ( ! StrUtil.nullOrVoid(column.getMaxValue()) ) {
//				element.setAttribute(RepositoryConst.COLUMN_MAX_VALUE, column.getMaxValue() ); // #LGU 31/08/2011
//			}
			if ( column.getMaxValue() != null ) {
				element.setAttribute(RepositoryConst.COLUMN_MAX_VALUE, column.getMaxValue().toString() ); // v 3.0.0
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

	//-----------------------------------------------------------------------------------------------------------
	// DateType management ( v 3.0.0 )
	//-----------------------------------------------------------------------------------------------------------
	private final static String STRING_DATE_ONLY      = "D";
	private final static String STRING_TIME_ONLY      = "T";
	private final static String STRING_DATE_AND_TIME  = "DT";

	/**
	 * Convert the given DateType to a String value
	 * @param dateType
	 * @return the String value ( eg : "D", "T", "DT" ) or null if the DateType is null or UNDEFINED
	 */
	private String convertDateTypeToString(DateType dateType) {
		if ( dateType != null ) {
			switch (dateType) {
				case DATE_ONLY : return STRING_DATE_ONLY ;
				case TIME_ONLY : return STRING_TIME_ONLY ;
				case DATE_AND_TIME : return STRING_DATE_AND_TIME ;
				case UNDEFINED : return null ;
			}
		}
		return null ;
	}
	
	/**
	 * Convert the given string to DateType
	 * @param s ( eg : "D", "T", "DT" )
	 * @return the DateType instance, or null if the string has an unexpected value
	 */
	private DateType convertStringToDateType(String s) {
		if ( s != null ) {
			if ( STRING_DATE_ONLY.equals(s)) return DateType.DATE_ONLY ;
			if ( STRING_TIME_ONLY.equals(s)) return DateType.TIME_ONLY ;
			if ( STRING_DATE_AND_TIME.equals(s)) return DateType.DATE_AND_TIME ;
		}
		return null ;
	}
}
