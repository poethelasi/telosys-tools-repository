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

import org.telosys.tools.commons.DatabaseUtil;
import org.telosys.tools.commons.JavaTypeUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.javatypes.JavaTypes;
import org.telosys.tools.commons.javatypes.JavaTypesManager;
import org.telosys.tools.commons.jdbctypes.JdbcTypes;
import org.telosys.tools.commons.jdbctypes.JdbcTypesManager;

/**
 * Column of a table/entity in the Repository Model <br>
 * 
 * A column contains the database informations and the mapped Java attribute informations
 * 
 * @author Laurent Guerin
 *
 */
public class Column implements Comparable<Column>
{
	public final static String SPECIAL_DATE_ONLY      = "D";
	public final static String SPECIAL_TIME_ONLY      = "T";
	public final static String SPECIAL_DATE_AND_TIME  = "DT";
		 
	public final static String SPECIAL_LONG_TEXT_TRUE = "true";
	
	private JdbcTypes _jdbcTypes = JdbcTypesManager.getJdbcTypes() ;
	
	//----- DATABASE -----
	
	private String  _sDatabaseName     = null ;  // dbName=""

	private String  _sDatabaseTypeName = null ;  // dbTypeName="INTEGER" - dbTypeName="VARCHAR"
	
	private int     _iDatabaseSize     = 0 ;     // dbSize=""
	
	private boolean _bDatabaseNotNull  = false ; // dbNotNull="true|false" ( false by default )
	
	private boolean _bPrimaryKey       = false ; // primaryKey="true|false" ( false by default )
	
	private boolean _bForeignKey       = false ; // foreignKey="true|false" ( false by default )

	private boolean _bAutoIncremented  = false ; // autoIncremented="true|false" ( false by default )
	
	private int     _iDatabasePosition = 0 ;     // position="" ( database ordinal position ) #LGU 10/08/2011
	
	private String  _sDatabaseDefaultValue = null ;  // dbDefaultValue="" ( database default value ) #LGU 10/08/2011
	
	private String  _sDatabaseComment  = null ;  // comment=""
	
	//----- JDBC -----
	
	private int     _iJdbcTypeCode     = 0 ;     // dbTypeCode="4" - dbTypeCode="12"
	
	//----- JAVA -----

	private String  _sJavaName    = null ;  // javaName=""
	
	private String  _sJavaType    = null ;  // javaType="int|...." 
	
	private boolean _bJavaNotNull = false ;  // javaNotNull="true|false" 
	
	private String  _sJavaDefaultValue = null ;  // javaDefaultValue="..." 
	
	private boolean _bSelected    = true ;  // selected by default
	
	//----- SPECIAL DATA for ALL -----
	private String  _sLabel     = null ;
	private String  _sInputType = null ;
	
	//----- SPECIAL DATA for STRING -----	
	private boolean _bLongText = false ;  //  
	private boolean _bNotEmpty = false ;  // notEmpty="true|false" 
	private boolean _bNotBlank = false ;  // notBlank="true|false" 
	private String  _sMinLength = null ;
	private String  _sMaxLength = null ;
	private String  _sPattern   = null ;
	
	//----- SPECIAL DATA for DATE & TIME -----
	private String  _sDateType = null ; // "D", "T" or "DT" ( see constants )
	private boolean _bDatePast   = false ;
	private boolean _bDateFuture = false ;
	private boolean _bDateBefore = false ;
	private boolean _bDateAfter  = false ;
	private String  _sDateBeforeValue = null ;
	private String  _sDateAfterValue  = null ;
	
	//----- SPECIAL DATA for NUMERIC -----
	private String  _sMinValue = null ;
	private String  _sMaxValue = null ;
	
	//----- SPECIAL DATA for BOOLEAN -----
	private String  _sBooleanTrueValue  = null ; // the special value for TRUE 
	private String  _sBooleanFalseValue = null ; // the special value for FALSE 
	
	//----- OTHER SPECIAL DATA -----
	private String  _sFormat = null ;  // Used with NUMERIC, DATE/TIME

	//----- SPECIAL DATA for key generation -----
	
	private GeneratedValue generatedValue = null ;
	
	private TableGenerator tableGenerator = null ;
	
	private SequenceGenerator sequenceGenerator = null ;
	
	public GeneratedValue getGeneratedValue() {
		return generatedValue;
	}

	public void setGeneratedValue(GeneratedValue generatedValue) {
		this.generatedValue = generatedValue;
	}

	public TableGenerator getTableGenerator() {
		return tableGenerator;
	}

	public void setTableGenerator(TableGenerator tableGenerator) {
		this.tableGenerator = tableGenerator;
	}

	public SequenceGenerator getSequenceGenerator() {
		return sequenceGenerator;
	}

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	//-----------------------------------------------------------------------------

	public String getDatabaseName() {
		return _sDatabaseName ;
	}

	public void setDatabaseName(String name) {
		_sDatabaseName = name ;
	}

	//-----------------------------------------------------------------------------

	public void setPrimaryKey(boolean b) {
		_bPrimaryKey = b ;
	}

	public boolean isPrimaryKey() {
		return _bPrimaryKey ;
	}

	//-----------------------------------------------------------------------------

	public void setForeignKey(boolean b) {
		_bForeignKey = b ;
	}

	public boolean isForeignKey() {
		return _bForeignKey ;
	}

	//-----------------------------------------------------------------------------

	public void setAutoIncremented(boolean b) {
		_bAutoIncremented = b ;
	}

	public boolean isAutoIncremented() {
		return _bAutoIncremented ;
	}

	
	//-----------------------------------------------------------------------------

	public void setDatabaseNotNull(boolean flag) {
		_bDatabaseNotNull = flag ;
	}

	public void setDatabaseNotNull(String flag) {
		_bDatabaseNotNull = "true".equalsIgnoreCase(flag) ;
	}

	public boolean isDatabaseNotNull() {
		return _bDatabaseNotNull ;
	}

	public String getDatabaseNotNullAsString() {
		return ( _bDatabaseNotNull ? "true" : "false" ) ;
	}

	//-----------------------------------------------------------------------------
	public void setDatabaseSize(int size) {
		_iDatabaseSize = size ;
	}
	public int getDatabaseSize() {
		return _iDatabaseSize ;
	}

	//-----------------------------------------------------------------------------
	/**
	 * Returns the ordinal position of the column in the database table
	 * @param v
	 */
	public void setDatabasePosition(int v) { // #LGU 10/08/2011
		_iDatabasePosition = v ;
	}
	/**
	 * Set the ordinal position of the column in the database table
	 * @return
	 */
	public int getDatabasePosition() { // #LGU 10/08/2011
		return _iDatabasePosition ;
	}
	
	//-----------------------------------------------------------------------------

	/**
	 * Returns the database default value 
	 * @return
	 */
	public String getDatabaseDefaultValue() { // #LGU 10/08/2011
		return _sDatabaseDefaultValue;
	}

	/**
	 * Set the database default value 
	 * @param v
	 */
	public void setDatabaseDefaultValue(String v) { // #LGU 10/08/2011
		_sDatabaseDefaultValue = v;
	}

	//-----------------------------------------------------------------------------

	/**
	 * Returns the column comment 
	 * @return comment
	 */
	public String getDatabaseComment() {
		return _sDatabaseComment;
	}

	/**
	 * Set the column comment
	 * @param databaseComment comment
	 */
	public void setDatabaseComment(String databaseComment) {
		_sDatabaseComment = databaseComment;
	}
	
	//-----------------------------------------------------------------------------

	public int getJdbcTypeCode() {
		return _iJdbcTypeCode ;
	}

	public void setJdbcTypeCode(int typeCode) {
		_iJdbcTypeCode = typeCode ;
	}

	public String getJdbcTypeName() {
		String text = _jdbcTypes.getTextForCode( getJdbcTypeCode() );
		return text != null ? text : "???" ;
	}


	public String getJdbcTypeCodeWithText() {
		int code = getJdbcTypeCode();
		String text = _jdbcTypes.getTextForCode(code);
		if ( text == null ) text = "???" ;
		return code + " : " + text.toLowerCase() ;
	}

	//-----------------------------------------------------------------------------

	/**
     * Returns the database native type name <br>
     * Examples : INTEGER, VARCHAR, NUMBER, CHAR, etc... 
	 * @return
	 */
	public String getDatabaseTypeName() {
		return _sDatabaseTypeName;
	}
	
	/**
     * Returns the database native type name with its size if the size make sense.<br>
     * Examples : INTEGER, VARCHAR(24), NUMBER, CHAR(3), etc... 
	 * @return
	 */
	public String getDatabaseTypeNameWithSize() {
		return DatabaseUtil.getNativeTypeWithSize(_sDatabaseTypeName, _iDatabaseSize, _iJdbcTypeCode);
	}

	public void setDatabaseTypeName(String databaseTypeName) {
		_sDatabaseTypeName = databaseTypeName;
	}

	//-----------------------------------------------------------------------------

	public String getJavaName() {
		return _sJavaName;
	}

	public void setJavaName(String s) {
		_sJavaName = s ;
	}

	//-----------------------------------------------------------------------------
	
	/**
	 * Returns the primitive Java type or the full java class name ( with package )
	 * e.g. : "boolean", "java.lang.Boolean", "java.util.Date", etc...
	 * @return
	 */
	public String getJavaType() {
		String sType = _sJavaType ;
		
		
		// Backward compatibility with old repository
		
		JavaTypes javaTypes = JavaTypesManager.getJavaTypes();
		if ( javaTypes.getTypeIndex(sType) < 0 )
		{
			// Type NOT FOUND : may be a short type ( from an old repository ) 
			return javaTypes.getTypeForShortType(sType); 
		}
		else
		{
			// Type found => Type OK
			return sType ;
		}
	}

	public void setJavaType(String s) {
		_sJavaType = s ;
	}

	//-----------------------------------------------------------------------------
	/**
	 * Returns the Java bean default value if any 
	 * @return the default value ( "0", "false" ) or null if none
	 */
	public String getJavaDefaultValue() {
		return _sJavaDefaultValue ;
	}
	/**
	 * Set the Java bean default value
	 * @param s the default value ( "0", "false" )
	 */
	public void setJavaDefaultValue(String s) {
		_sJavaDefaultValue = s ;
	}

	//-----------------------------------------------------------------------------
	/**
	 * Returns true is the Java type is "boolean" or "java.lang.Boolean"
	 * @return
	 */
	public boolean isJavaTypeBoolean() 
	{
		return JavaTypeUtil.isCategoryBoolean( getJavaType() ) ;
	}
	
	/**
	 * Returns true is the Java type is "java.lang.String"
	 * @return
	 */
	public boolean isJavaTypeString() 
	{
		return JavaTypeUtil.isCategoryString( getJavaType() ) ;
	}

	/**
	 * Returns true if the Java type is a numeric type : <br>
	 * "byte", "short", "int", "long", "double", "float" <br>
	 * or respective wrappers, or "BigDecimal", or "BigInteger"<br>
	 * @return
	 */
	public boolean isJavaTypeNumber() 
	{
		return JavaTypeUtil.isCategoryNumber( getJavaType() ) ;
	}
	
	/**
	 * Returns true if the Java type is "java.util.Date" or "java.sql.Date" <br>
	 * or "java.sql.Time" or "java.sql.Timestamp" <br>
	 * @return
	 */
	public boolean isJavaTypeDateOrTime() 
	{
		return JavaTypeUtil.isCategoryDateOrTime( getJavaType() ) ;
	}
	
	/**
	 * Returns true if the Java type is a "primitive type" ( "int", "boolean", "short", ... )
	 * @return
	 */
	public boolean isJavaPrimitiveType()
	{
		return JavaTypeUtil.isPrimitiveType( getJavaType() );
	}

	//-----------------------------------------------------------------------------
	public boolean getJavaNotNull() {
		return _bJavaNotNull;
	}
	public void setJavaNotNull(boolean v) {
		_bJavaNotNull = v ;
	}

	//-----------------------------------------------------------------------------
	public boolean getNotEmpty() {
		return _bNotEmpty;
	}
	public void setNotEmpty(boolean v) {
		_bNotEmpty = v ;
	}

	//-----------------------------------------------------------------------------
	public boolean getNotBlank() {
		return _bNotBlank;
	}
	public void setNotBlank(boolean v) {
		_bNotBlank = v ;
	}
	//-----------------------------------------------------------------------------
	public String getMinLength() {
		return _sMinLength;
	}
	public void setMinLength(String v) {
		_sMinLength = v ;
	}
	//-----------------------------------------------------------------------------
	public String getMaxLength() {
		return _sMaxLength;
	}
	public void setMaxLength(String v) {
		_sMaxLength = v ;
	}
	//-----------------------------------------------------------------------------
	public String getPattern() {
		return _sPattern;
	}
	public void setPattern(String v) {
		_sPattern = v ;
	}
	//-----------------------------------------------------------------------------
	public boolean getSelected() {
		return _bSelected ;
	}
	public void setSelected(boolean b) {
		_bSelected = b ;
	}

	//-----------------------------------------------------------------------------
	// Special infos
	//-----------------------------------------------------------------------------
	public String getLabel() {  // V 2.0.3
		return _sLabel ;
	}
	public void setLabel(String s) { // V 2.0.3
		_sLabel = s ;
	}
	
	//-----------------------------------------------------------------------------
	public String getInputType() { // V 2.0.3
		return _sInputType ;
	}
	public void setInputType(String s) { // V 2.0.3
		_sInputType = s ;
	}
	
	//-----------------------------------------------------------------------------
	public boolean getLongText() {
		return _bLongText ;
	}
	public void setLongText(String flag) {
		setLongText( "true".equalsIgnoreCase(flag) ) ;
	}
	public void setLongText(boolean b) {
		_bLongText = b ;
	}

	//-----------------------------------------------------------------------------
	/**
	 * Returns the special date type : "D", "T", "DT" or "" if none
	 * @return
	 */
	public String getDateType() {
		return ( _sDateType != null ? _sDateType : "" ); 
	}

	/**
	 * Set the special date type
	 * @param v : "D", "T", "DT" or null if none
	 */
	public void setDateType(String v) {
		if ( SPECIAL_DATE_ONLY.equals(v) || SPECIAL_TIME_ONLY.equals(v) 
				|| SPECIAL_DATE_AND_TIME.equals(v) || null == v )
		{
			_sDateType = v ;
		}
	}

	public boolean isDatePast() {
		return _bDatePast;
	}
	public void setDatePast(boolean v) {
		_bDatePast = v;
	}

	public boolean isDateFuture() {
		return _bDateFuture;
	}
	public void setDateFuture(boolean v) {
		_bDateFuture = v;
	}

	public boolean isDateBefore() {
		return _bDateBefore;
	}
	public void setDateBefore(boolean v) {
		_bDateBefore = v;
	}
	public String getDateBeforeValue() {
		return _sDateBeforeValue;
	}
	public void setDateBeforeValue(String v) {
		_sDateBeforeValue = v;
	}

	public boolean isDateAfter() {
		return _bDateAfter;
	}
	public void setDateAfter(boolean v) {
		_bDateAfter = v;
	}
	public String getDateAfterValue() {
		return _sDateAfterValue;
	}
	public void setDateAfterValue(String v) {
		_sDateAfterValue = v;
	}
	//-----------------------------------------------------------------------------

	/**
	 * The value used to store a TRUE in the database ( never null )
	 * @return the value or "" if none (never null)
	 */
	public String getBooleanTrueValue() {
		return ( _sBooleanTrueValue != null ? _sBooleanTrueValue : "" );
	}
	/**
	 * The value used to store a FALSE in the database ( never null )
	 * @return the value or "" if none (never null)
	 */
	public String getBooleanFalseValue() {
		return ( _sBooleanFalseValue != null ? _sBooleanFalseValue : "" );
	}

	public void setBooleanTrueValue(String v) {
		_sBooleanTrueValue = v ;
	}
	public void setBooleanFalseValue(String v) {
		_sBooleanFalseValue = v ;
	}

	//-----------------------------------------------------------------------------

	public String getFormat() {
		return _sFormat ; 
	}
	public void setFormat(String v) {
		_sFormat = v ;
	}
	
	//-----------------------------------------------------------------------------

	public String getMinValue() {
		return _sMinValue ; 
	}
	public void setMinValue(String v) {
		_sMinValue = v ;
	}
	
	public String getMaxValue() {
		return _sMaxValue ; 
	}
	public void setMaxValue(String v) {
		_sMaxValue = v ;
	}
	
	//-----------------------------------------------------------------------------

	/**
	 * Returns the "special type informations" for this column if any ( else "", never null )
	 * @return : Special information, ie "Long Text", "Date only", "Time only", boolean true/false value
	 */
	public String getSpecialTypeInfo() 
	{
//		String sJavaType = getJavaType();
//		if ( sJavaType != null )
//		{
//			if ( "java.lang.String".equals(sJavaType) ) {
//				if ( getLongText() ) return "Long Text" ;
//			}
//			if ( "java.lang.Boolean".equals(sJavaType) || "boolean".equals(sJavaType) ) {
//				return getBooleanTrueValue() + ":" + getBooleanFalseValue() ;
//			}
//			if ( "java.util.Date".equals(sJavaType) ) {
//				String sDateType = getDateType() ;
//				if ( SPECIAL_DATE_ONLY.equals(sDateType) )     return "Date only" ;
//				if ( SPECIAL_TIME_ONLY.equals(sDateType) )     return "Time only" ;
//				if ( SPECIAL_DATE_AND_TIME.equals(sDateType) ) return "Date + Time" ;
//			}
//		}
		
		StringBuffer sb = new StringBuffer();
		if ( this.isJavaTypeString() ) {
			if ( getLongText() ) addStr(sb, "Long Text") ;
			if ( getNotEmpty() ) addStr(sb, "NE") ;
			if ( getNotBlank() ) addStr(sb, "NB") ;
			if ( ( ! StrUtil.nullOrVoid( getMinLength() ) ) || ( ! StrUtil.nullOrVoid( getMaxLength() ) ) )
			{
				addStr( sb, "[" + str(getMinLength()) + ";" + str(getMaxLength()) + "]" );
			}
			if ( ! StrUtil.nullOrVoid( getPattern() ) ) addStr(sb, "P" ) ;
		}
		else if ( this.isJavaTypeBoolean() ) {
			if ( ! StrUtil.nullOrVoid( getBooleanTrueValue() ) ) {
				addStr( sb, getBooleanTrueValue() + ":" + getBooleanFalseValue() );
			}
		}
		else if ( this.isJavaTypeNumber() ) {
			if ( ! StrUtil.nullOrVoid( getJavaDefaultValue() ) )
			{
				addStr( sb, getJavaDefaultValue() );
			}
			if ( ( ! StrUtil.nullOrVoid( getMinValue() ) ) || ( ! StrUtil.nullOrVoid( getMaxValue() ) ) )
			{
				addStr( sb, "[" + str(getMinValue()) + ";" + str(getMaxValue()) + "]" );
			}
		}
		else if ( this.isJavaTypeDateOrTime() ) {
			String sDateType = getDateType() ;
			if ( SPECIAL_DATE_ONLY.equals(sDateType) )     addStr( sb, "Date only" );
			if ( SPECIAL_TIME_ONLY.equals(sDateType) )     addStr( sb, "Time only" );
			if ( SPECIAL_DATE_AND_TIME.equals(sDateType) ) addStr( sb, "Date & Time" );
			if ( isDatePast() ) addStr( sb, "P" ); 
			if ( isDateFuture() ) addStr( sb, "F" ); 
			if ( isDateBefore() ) addStr( sb, "B" ); 
			if ( isDateAfter() ) addStr( sb, "A" ); 
		}
		return sb.toString();
	}
	private String str(String s)
	{
		return s != null ? s : "" ;
	}
	private void addStr(StringBuffer sb, String s)
	{
		if ( sb.length() > 0 ) sb.append(",");
		sb.append(s);
	}
	
	/**
	 * Clear all the "special type informations" for this column
	 */
	public void clearSpecialTypeInfo() 
	{
		setJavaNotNull(false);
		//--- Boolean category 
		setBooleanTrueValue(null);
		setBooleanFalseValue(null);
		//--- Date category 
		setDateType(null);
		setDatePast(false);
		setDateFuture(false);
		setDateBefore(false);
		setDateBeforeValue(null);
		setDateAfter(false);
		setDateAfterValue(null);
		//--- Number category 
		setMinValue(null);
		setMaxValue(null);
		//--- String category 
		setLongText(false);
		setNotEmpty(false);
		setNotBlank(false);
		setMinLength(null);
		setMaxLength(null);
		setPattern(null);
	}
	
	//public int compareTo(Object o) {
	public int compareTo(Column other) {
		if ( other != null )
		{
			//Column other = (Column) o;
			return ( this.getDatabasePosition() - other.getDatabasePosition() );
		}
		return 0;
	}

	//-----------------------------------------------------------------------------

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return 
		  getDatabaseName() + "|" 
			+ ( isPrimaryKey() ? "PK" : "" ) + "|" 
			// + getDatabaseType() + "|" 
			+ getJdbcTypeCode() + "|" 
			+ getJavaName() + "|" 
			+ getJavaType() ;
	}
}
