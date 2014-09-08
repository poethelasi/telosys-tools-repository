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

/**
 * Default implementation 
 * 
 * @author L.GUERIN
 * 
 */
public class UserInterfaceInformationProviderHTML5 implements UserInterfaceInformationProvider
{   
	
	//------------------------------------------------------------------------------
    public UserInterfaceInformationProviderHTML5() {
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

    
    private String toSeparatedWords(String sName)
    {
        if (sName != null)
        {
            StringBuffer sb = new StringBuffer( sName.length() );
            String sToken = null;
            String s = sName.trim(); // to be secure
            StringTokenizer st = new StringTokenizer(s, "_");
            int i = 0 ;
            while (st.hasMoreTokens())
            {
            	i++ ;
                sToken = st.nextToken();
                if ( i > 1 ) {
                    sb.append( " " );
                }
                sb.append( transformToken( sToken ) );
            }
            return sb.toString();
        }
        else
        {
            return null;
        }
    }
    
    /* (non-Javadoc)
     * @see org.telosys.tools.repository.config.UserInterfaceInformationProvider#getAttributeLabel(java.lang.String, java.lang.String, int)
     */
    public String getAttributeLabel(String databaseColumnName, String databaseColumnType, int jdbcTypeCode) 
    {
        //--- Colum name converted in "Word1 Word2"
        return toSeparatedWords(databaseColumnName);
        
    }
    	
    /* (non-Javadoc)
     * @see org.telosys.tools.repository.config.UserInterfaceInformationProvider#getAttributeInputType(java.lang.String, java.lang.String, int)
     */
    public String getAttributeInputType(String sColumnName, String sColumnTypeName, int iJdbcTypeCode )
    {
    	//--- Returns the HTML 5 input type
    	switch ( iJdbcTypeCode ) {
	    	case Types.CHAR :
	    	case Types.VARCHAR :
	    	case Types.LONGVARCHAR :
	    		return "text" ;
    		
	    	case Types.NUMERIC :
	    	case Types.DECIMAL :
	    	case Types.TINYINT :
	    	case Types.SMALLINT :
	    	case Types.INTEGER :
	    	case Types.BIGINT :    		
	    	case Types.REAL :
	    	case Types.FLOAT :
	    	case Types.DOUBLE :
	    		return "number" ;
    		
    		case Types.DATE :
    			return "date" ;
    			
    		case Types.TIME :
    			return "time" ;
    			
    		case Types.BIT :
    		case Types.BOOLEAN :
    			return "checkbox" ;
    			
    	}
    	return "" ;
    }

}