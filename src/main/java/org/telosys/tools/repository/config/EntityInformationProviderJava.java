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
package org.telosys.tools.repository.config;

import java.sql.Types;
import java.util.StringTokenizer;

import org.telosys.tools.commons.jdbctypes.JdbcTypes;
import org.telosys.tools.commons.jdbctypes.JdbcTypesManager;

/**
 * EntityInformationProvider implementation for JAVA
 * 
 * @author L.GUERIN
 * 
 */
public class EntityInformationProviderJava implements EntityInformationProvider
{   
	
	//------------------------------------------------------------------------------
    public EntityInformationProviderJava() {
		super();
	}

	//------------------------------------------------------------------------------
    /**
     * Transform the given token with only the first char in Upper Case  
     * ie : "aBcDeFg" --> "Abcdefg"
     * @param s 
     * @return transformed string
     */
    private String transformToken(String s)
    {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    //------------------------------------------------------------------------------
    /**
     * Transform the given string to "CamelCase", using "_" as word separator  
     * ie : "ORDER_ITEM" --> "OrderItem"
     * @param sName
     * @return transformed string
     */
    private String toCamelCase(String sName)
    {
        if (sName != null)
        {
            StringBuffer sb = new StringBuffer( sName.length() );
            String sToken = null;
            String s = sName.trim(); // to be secure
            StringTokenizer st = new StringTokenizer(s, "_");
            while (st.hasMoreTokens())
            {
                sToken = st.nextToken();
                sb.append( transformToken( sToken ) );
            }
            return sb.toString();
        }
        else
        {
            return null;
        }
    }
    
//    private String toSeparatedWords(String sName)
//    {
//        if (sName != null)
//        {
//            StringBuffer sb = new StringBuffer( sName.length() );
//            String sToken = null;
//            String s = sName.trim(); // to be secure
//            StringTokenizer st = new StringTokenizer(s, "_");
//            int i = 0 ;
//            while (st.hasMoreTokens())
//            {
//            	i++ ;
//                sToken = st.nextToken();
//                if ( i > 1 ) {
//                    sb.append( " " );
//                }
//                sb.append( transformToken( sToken ) );
//            }
//            return sb.toString();
//        }
//        else
//        {
//            return null;
//        }
//    }
	
    //------------------------------------------------------------------------------
    // Classes
    //------------------------------------------------------------------------------
    /* (non-Javadoc)
     * @see org.telosys.daogen.common.InitializerChecker#getClassNameForVO(java.lang.String)
     */
    public String getEntityClassName(String sTableName)
    {
    	return toCamelCase(sTableName);
    }

    //------------------------------------------------------------------------------
    // Value Object Attribute Name from Database column name and type
    //------------------------------------------------------------------------------
    /* (non-Javadoc)
     * @see org.telosys.daogen.common.InitializerChecker#getAttributeName(java.lang.String, java.lang.String)
     */
    public String getAttributeName(String sColumnName, String sColumnTypeName, int iColumnTypeCode)
    {
        //--- Colum name converted in "CamelCase"
        String s = toCamelCase(sColumnName);
        
        //--- Java attribute => Force the first char to LowerCase
        return s.substring(0,1).toLowerCase() + s.substring(1,s.length()) ;
    }

    //------------------------------------------------------------------------------
    // Value Object Attribute Type from Database column type
    //------------------------------------------------------------------------------
    /* (non-Javadoc)
     * @see org.telosys.daogen.common.InitializerChecker#getAttributeType(java.lang.String)
     */
    public String getAttributeType(String databaseColumnType, int iJdbcTypeCode, boolean bColumnNotNull)
    {
    	//--- Special cases for Date/Time/Timestamp
    	if ( iJdbcTypeCode == Types.DATE  || iJdbcTypeCode == Types.TIME || iJdbcTypeCode == Types.TIMESTAMP ) 
    	{
    		// Force "java.util.Date" : see also getAttributeDateType() for DATE_ONLY, TIME_ONLY, etc...
    		return "java.util.Date";
    	}
    	JdbcTypes types = JdbcTypesManager.getJdbcTypes();
    	String sJavaType = types.getJavaTypeForCode(iJdbcTypeCode, bColumnNotNull);
    	return ( sJavaType != null ? sJavaType : "java.lang.String" );    	
    }

//    //------------------------------------------------------------------------------
//    /* (non-Javadoc)
//     * @see org.telosys.daogen.common.InitializerChecker#getAttributeType(java.lang.String)
//     */
//    public String getAttributeLongTextFlag (String databaseColumnType, int jdbcColumnType, String javaType )
//    {
//    	if ( javaType.equals("java.lang.String") )
//    	{
//    		if (   jdbcColumnType == Types.LONGVARCHAR
//    			|| jdbcColumnType == Types.CLOB
//    			|| jdbcColumnType == Types.BLOB )
//    		{
//    			// Considered as a "Long Text"
//    			return Column.SPECIAL_LONG_TEXT_TRUE ; 
//    		}
//    	}
//    	return null ;
//    }
    
//    public String getAttributeDateType(String databaseColumnType, int jdbcColumnType, String javaType )
//    {
//    	if ( javaType.equals("java.util.Date") )
//    	{
//        	switch ( jdbcColumnType )
//        	{
//        		//--- Type of Date :
//        		case Types.DATE : 
//        			return Column.SPECIAL_DATE_ONLY ; 
//        		case Types.TIME : 
//        			return Column.SPECIAL_TIME_ONLY ; 
//        		case Types.TIMESTAMP : 
//        			return Column.SPECIAL_DATE_AND_TIME ;
//        	}
//    	}
//    	return null ;
//    }
    
//    public String getAttributeLabel(String sColumnName, String sColumnTypeName, int iJdbcTypeCode) 
//    {
//        //--- Colum name converted in "Word1 Word2"
//        return toSeparatedWords(sColumnName);
//        
//    }
//    
//    /* (non-Javadoc)
//     * @see org.telosys.tools.repository.config.InitializerChecker#getAttributeInputType(java.lang.String, java.lang.String, int, java.lang.String)
//     */
//    public String getAttributeInputType(String sColumnName, String sColumnTypeName, int iJdbcTypeCode, String sJavaType)
//    {
//    	//--- Returns the HTML 5 input type
//    	switch ( iJdbcTypeCode ) {
//	    	case Types.CHAR :
//	    	case Types.VARCHAR :
//	    	case Types.LONGVARCHAR :
//	    		return "text" ;
//    		
//	    	case Types.NUMERIC :
//	    	case Types.DECIMAL :
//	    	case Types.TINYINT :
//	    	case Types.SMALLINT :
//	    	case Types.INTEGER :
//	    	case Types.BIGINT :    		
//	    	case Types.REAL :
//	    	case Types.FLOAT :
//	    	case Types.DOUBLE :
//	    		return "number" ;
//    		
//    		case Types.DATE :
//    			return "date" ;
//    			
//    		case Types.TIME :
//    			return "time" ;
//    			
//    		case Types.BIT :
//    		case Types.BOOLEAN :
//    			return "checkbox" ;
//    			
//    	}
//    	return "" ;
//    }

}