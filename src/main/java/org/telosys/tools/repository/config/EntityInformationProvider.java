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

/**
 * @author L.GUERIN
 * 
 */
public interface EntityInformationProvider
{
    /**
     * Returns the ENTITY class name for the given database table name
     * @param databaseTableName
     * @return
     */
    public String getEntityClassName(String databaseTableName);
    

    /**
     * Returns the ENTITY attribute name for the given database column 
     * @param databaseColumnName the name of the column in the database
     * @param databaseColumnType the native type of the column in the database
     * @param jdbcTypeCode the JDBC type code
     * @return the attribute name
     */
    public String getAttributeName(String databaseColumnName, String databaseColumnType, int jdbcTypeCode);

    
    /**
     * Returns the ENTITY attribute type for the given column 
     * @param databaseColumnType the native database type
     * @param jdbcTypeCode the JDBC type code
     * @param columnNotNull flag "NOT NULL" for the column
     * @return the attribute type
     */
    public String getAttributeType(String databaseColumnType, int jdbcTypeCode, boolean columnNotNull );


//    /**
//     * Returns the "long text flag" for the given column/attribute 
//     * @param databaseColumnType
//     * @param jdbcTypeCode
//     * @param attributeType the attribute type in the target language ( Java type, C++ type, etc )
//     * @return 
//     */
//    public String getAttributeLongTextFlag(String databaseColumnType, int jdbcTypeCode, String attributeType );

    
//    /**
//     * Return the date type for the given column/attribute 
//     * @param databaseColumnType
//     * @param jdbcTypeCode
//     * @param attributeType the attribute type in the target language ( Java type, C++ type, etc )
//     * @return "D" for "Date only", "T" for "Time only", "DT" for "Date and Time" or null if not a date/time
//     */
//    public String getAttributeDateType(String databaseColumnType, int jdbcTypeCode, String attributeType );

//    /**
//     * Returns the Java attribute label for the given column 
//     * @param sColumnName the name of the column in the database
//     * @param sColumnTypeName the native database type
//     * @param iJdbcTypeCode the JDBC type code
//     * @return the attribute name
//     */
//    public String getAttributeLabel(String sColumnName, String sColumnTypeName, int iJdbcTypeCode);
//
//    /**
//     * Returns the Java attribute input type for the given column 
//     * @param sColumnName the name of the column in the database
//     * @param sColumnTypeName the native database type
//     * @param iJdbcTypeCode the JDBC type code
//     * @return the attribute name
//     */
//    public String getAttributeInputType(String sColumnName, String sColumnTypeName, int iJdbcTypeCode, String sJavaType);

}