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
import org.telosys.tools.repository.persistence.util.RepositoryConst;

/**
 * "Link" model class <br>
 * 
 * @author S.Labbe, L.Guerin
 *
 */
//public class Link extends JoinFKCollection 
public class Link 
{
	private String  id;

	private boolean used = true; // flag : used or not used
	
	private String  sourceTableName; // the source of the link
	private String  targetTableName; // the target of the link
	
	/**
	 * Relationship's cardinality
	 * "OneToOne", "OneToMany", "ManyToOne", "ManyToMany"
	 */
	private String  cardinality;

	private String javaFieldName; // name of the java field holding the link 
	private String javaFieldType; // type of the java field holding the link

	private boolean owningSide = true; // porteur de la relation ou non --- pas besoin si "non-owning OneToMany entity side must used the mappedBy element to specify the relationship field"

	private String inverseSideOf; // identifie le link id porteur de la relation
	private String mappedBy;  // inverse side, mapped by define property

	/**
	 * The operations that must be cascaded to the target of the association. By default no operations are cascaded : 
	 * ALL,MERGE,PERSIT,REFRESH,REMOVE
	 */
	// The operations that must be cascaded to the target of the association. 
	// By default no operations are cascaded.
	private boolean cascadeALL     = false;
	private boolean cascadeMERGE   = false;
	private boolean cascadePERSIST = false;
	private boolean cascadeREFRESH = false;
	private boolean cascadeREMOVE  = false;
	
	/**
	 * Fetch strategy
	 * DEFAULT|EAGER|LAZY
	 */
	private String fetch = RepositoryConst.FETCH_DEFAULT ;
//	private boolean fetchDEFAULT = false;
//	private boolean fetchEAGER = false;
//	private boolean fetchLAZY = false;
	
	/**
	 * Whether the association is optional. If set to false then a non-null relationship must always exist. Default to true
	 */
//	private boolean  optional = true;
	private String  optional = RepositoryConst.OPTIONAL_UNDEFINED ;
	
	private String   targetEntityJavaType ;

	private String   foreignKeyName = null ;
	private String   joinTableName  = null ;
	
	//--- ManyToOne or OneToOne link based on "Join Columns"
	private JoinColumns  joinColums = null ;

	//--- ManyToMany link based on "Join Table"
	private JoinTable    joinTable  = null ;	

	//--------------------------------------------------------------------------
	public final static String buildId(ForeignKey foreignKey, boolean owningSide) 
	{
		return "LINK_FK_" + foreignKey.getName() + "_" + ( owningSide ? "O" : "I" ) ;
	}
	//--------------------------------------------------------------------------
	public final static String buildId(Entity joinTable, boolean owningSide) 
	{
		String tableId = "" ;
		if ( StrUtil.nullOrVoid( joinTable.getSchema() ) ) {
			tableId = joinTable.getName() ;
		}
		else
		{
			tableId = joinTable.getSchema() + "." + joinTable.getName() ;
		}
		return "LINK_JT_" + tableId + "_" + ( owningSide ? "O" : "I" ) ;
	}
	//--------------------------------------------------------------------------
	/**
	 * Constructor for basic link creation (without source information) 
	 */
	public Link() {
		super();
	}

//	//--------------------------------------------------------------------------
//	public String getCheckSum() {
//		return this.getSourceTableName() + "#" +  this.getTargetTableName();
//	}
//
	
	//--------------------------------------------------------------------------
	public void setJoinColumns( JoinColumns joinColumns )
	{
		joinColums = joinColumns ;
	}
	public JoinColumns getJoinColumns()
	{
		return joinColums ;
	}
	
	//--------------------------------------------------------------------------
	public JoinTable getJoinTable()
	{
		return joinTable ;
	}
	public void setJoinTable( JoinTable v )
	{
		joinTable = v ;
	}
	
	//--------------------------------------------------------------------------
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	//--------------------------------------------------------------------------
	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	//--------------------------------------------------------------------------
	public String getSourceTableName() {
		return sourceTableName;
	}

	public void setSourceTableName(String srcTableName) {
		this.sourceTableName = srcTableName;
	}

	//--------------------------------------------------------------------------
	public String getTargetTableName() {
		return targetTableName;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}

	//--------------------------------------------------------------------------
	/**
	 * Returns the name of the Java field holding the link  <br>
	 * ie : "book", "customer", "books", "customers", ...
	 * @return
	 */
	public String getJavaFieldName() {
		return javaFieldName;
	}

	/**
	 * Set the name of the Java field holding the link   <br>
	 * ie : "book", "customer", "books", "listOfCustomers", ...
	 * @param javaName
	 */
	public void setJavaFieldName(String javaName) {
		this.javaFieldName = javaName;
	}

	//--------------------------------------------------------------------------
	/**
	 * Returns the Java type of the link reference <br>
	 * ie : "Book", "Customer", or collection type ( "java.util.List", ... )
	 * @return
	 */
	public String getJavaFieldType() {
		if ( ! StrUtil.nullOrVoid(javaFieldType) ) {
			return javaFieldType; // Specific type : ie "java.util.List"
		}
		else {
			return getTargetEntityJavaType() ; // "Book", "Customer", ...
		}
	}

	/**
	 * Set the type of the Java field holding the link   <br>
	 * ie : "Book", "Customer", or collection type ( "java.util.List", ... )
	 * @param javaType
	 */
	public void setJavaFieldType(String javaType) {
		this.javaFieldType = javaType;
	}

	//--------------------------------------------------------------------------
	public boolean isOwningSide() {
		return owningSide;
	}

	public boolean isInverseSide() {
		return ( owningSide == false ) ;
	}

	public void setOwningSide(boolean owningSide) {
		this.owningSide = owningSide;
	}
	
	//--------------------------------------------------------------------------
	/**
	 * Returns the link id of the inverse side 
	 */
	public String getInverseSideOf() {
		return inverseSideOf;
	}
	
	/**
	 * Set the link id of the inverse side
	 */
	public void setInverseSideOf(String inverseSideOf) {
		this.inverseSideOf = inverseSideOf;
	}

	//--------------------------------------------------------------------------
	public String getMappedBy() {
		return mappedBy;
	}

	public void setMappedBy(String mappedBy) {
		this.mappedBy = mappedBy;
	}

	//--------------------------------------------------------------------------
//	public boolean isOptional() {
//		return optional;
//	}

//	public void setOptional(boolean optional) {
//		this.optional = optional;
//	}
	public void setOptional(String v) {
		this.optional = RepositoryConst.OPTIONAL_UNDEFINED;
		if (v != null) {
			if (RepositoryConst.OPTIONAL_TRUE.equalsIgnoreCase(v)) 
			{
				this.optional = RepositoryConst.OPTIONAL_TRUE;
			} 
			else if (RepositoryConst.OPTIONAL_FALSE.equalsIgnoreCase(v)) 
			{
				this.optional = RepositoryConst.OPTIONAL_FALSE;
			}
		}
	}
	/**
	 * Returns optional value : "TRUE"/"FALSE"/"UNDEFINED"
	 * @return
	 */
	public String getOptional() {
		return this.optional ;
	}
	
	public boolean isOptionalTrue() {
		return ( RepositoryConst.OPTIONAL_TRUE.equals( this.optional ) ) ;
	}
	public boolean isOptionalFalse() {
		return ( RepositoryConst.OPTIONAL_FALSE.equals( this.optional ) ) ;
	}
	public boolean isOptionalUndefined() {
		if ( isOptionalTrue() || isOptionalFalse() ) return false ;
		return true ;
	}
	

	//--------------------------------------------------------------------------
	/**
	 * Returns the short Java Class Name for an ENTITY BEAN <br>
	 * ie "Book", "Customer", ... 
	 * @return
	 */
	public String getTargetEntityJavaType() {
		return targetEntityJavaType;
	}

	/**
	 * Set the short Java Class Name for an ENTITY BEAN <br>
	 * @param v the short Java type ( ie "Book", "Customer", ... )
	 */
	public void setTargetEntityJavaType(String v) {
		this.targetEntityJavaType = v;
	}

	//--------------------------------------------------------------------------
	/**
	 * Returns the link type  : "OneToMany", "ManyToOne", "OneToOne", "ManyToMany" 
	 * @return
	 */
	public String getCardinality() {
		return cardinality;
	}

	public void setCardinality(String p_type) 
	{
		if (p_type != null) {
			if (RepositoryConst.MAPPING_ONE_TO_ONE.equalsIgnoreCase(p_type)) {
				this.cardinality = RepositoryConst.MAPPING_ONE_TO_ONE;
			} else if (RepositoryConst.MAPPING_ONE_TO_MANY.equalsIgnoreCase(p_type)) {
				this.cardinality = RepositoryConst.MAPPING_ONE_TO_MANY;
			} else if (RepositoryConst.MAPPING_MANY_TO_ONE.equalsIgnoreCase(p_type)) {
				this.cardinality = RepositoryConst.MAPPING_MANY_TO_ONE;
			} else if (RepositoryConst.MAPPING_MANY_TO_MANY.equalsIgnoreCase(p_type)) {
				this.cardinality = RepositoryConst.MAPPING_MANY_TO_MANY;
			} else {
				this.cardinality = RepositoryConst.MAPPING_UNKNOWN;
			}
		} else {
			this.cardinality = RepositoryConst.MAPPING_UNKNOWN;
		}
	}

	public boolean isTypeOneToOne() {
		return RepositoryConst.MAPPING_ONE_TO_ONE.equals(cardinality);
	}

	public boolean isTypeOneToMany() {
		return RepositoryConst.MAPPING_ONE_TO_MANY.equals(cardinality);
	}

	public boolean isTypeManyToOne() {
		return RepositoryConst.MAPPING_MANY_TO_ONE.equals(cardinality);
	}

	public boolean isTypeManyToMany() {
		return RepositoryConst.MAPPING_MANY_TO_MANY.equals(cardinality);
	}

	public String getCascade() {
		if ( this.cascadeALL ) {
			return RepositoryConst.CASCADE_ALL ;
		}
		else {
			StringBuffer sb = new StringBuffer();
			if ( this.cascadeMERGE ) {
				sb.append(" ");
				sb.append(RepositoryConst.CASCADE_MERGE);
			}
			if ( this.cascadePERSIST ) {
				sb.append(" ");
				sb.append(RepositoryConst.CASCADE_PERSIST);
			}
			if ( this.cascadeREFRESH ) {
				sb.append(" ");
				sb.append(RepositoryConst.CASCADE_REFRESH);
			}
			if ( this.cascadeREMOVE ) {
				sb.append(" ");
				sb.append(RepositoryConst.CASCADE_REMOVE);
			}
			return sb.toString();
		}
	}

	public void setCascade(String cascade) {
		// Traitement normatif, NON exclusif (cumul possible) et NON obligatoire de la strategie de cascade
		this.cascadeALL     = false;
		this.cascadeMERGE   = false;
		this.cascadePERSIST = false;
		this.cascadeREFRESH = false;
		this.cascadeREMOVE  = false;
		
		if ( cascade != null ) {
			String cascadeUC = cascade.toUpperCase();
			if (cascadeUC.contains(RepositoryConst.CASCADE_ALL)) {
				this.cascadeALL = true;
			}
			if (cascadeUC.contains(RepositoryConst.CASCADE_MERGE)) {
				this.cascadeMERGE = true;
			}
			if (cascadeUC.contains(RepositoryConst.CASCADE_PERSIST)) {
				this.cascadePERSIST = true;
			}
			if (cascadeUC.contains(RepositoryConst.CASCADE_REFRESH)) {
				this.cascadeREFRESH = true;
			}
			if (cascadeUC.contains(RepositoryConst.CASCADE_REMOVE)) {
				this.cascadeREMOVE = true;
			}
		}
	}

	public boolean isCascadeALL() {
		return cascadeALL;
	}
	public void setCascadeALL(boolean v) {
		cascadeALL = v ;
	}

	public boolean isCascadeMERGE() {
		return cascadeMERGE;
	}
	public void setCascadeMERGE(boolean v) {
		cascadeMERGE = v ;
	}

	public boolean isCascadePERSIST() {
		return cascadePERSIST;
	}
	public void setCascadePERSIST(boolean v) {
		cascadePERSIST = v ;
	}

	public boolean isCascadeREFRESH() {
		return cascadeREFRESH;
	}
	public void setCascadeREFRESH(boolean v) {
		cascadeREFRESH = v;
	}

	public boolean isCascadeREMOVE() {
		return cascadeREMOVE;
	}
	public void setCascadeREMOVE(boolean v) {
		cascadeREMOVE = v;
	}

	/**
	 * Returns "fetch" property : "DEFAULT" or "EAGER" or "LAZY"
	 * @return
	 */
	public String getFetch() {
		return fetch;
	}

	/**
	 * @param p_fetch
	 */
	public void setFetch(String p_fetch) {
		// Traitement normatif, exclusif (cumul possible) et NON obligatoire de la strategie du Fetch
//		this.fetchDEFAULT = false;
//		this.fetchEAGER = false;
//		this.fetchLAZY = false;
		if (p_fetch != null) {
			if (RepositoryConst.FETCH_EAGER.equalsIgnoreCase(p_fetch)) {
				this.fetch = RepositoryConst.FETCH_EAGER;
				//this.fetchEAGER = true;
			} else if (RepositoryConst.FETCH_LAZY.equalsIgnoreCase(p_fetch)) {
				this.fetch = RepositoryConst.FETCH_LAZY;
				//this.fetchLAZY = true;
			} else {
				this.fetch = RepositoryConst.FETCH_DEFAULT;
				//this.fetchDEFAULT = true;
			}
		} else {
			this.fetch = RepositoryConst.FETCH_DEFAULT;
			//this.fetchDEFAULT = true;
		}
	}

	public boolean isFetchDEFAULT() {
		//return fetchDEFAULT;
		return ( RepositoryConst.FETCH_DEFAULT.equals( this.fetch ) ) ;
	}

	public boolean isFetchEAGER() {
		//return fetchEAGER;
		return ( RepositoryConst.FETCH_EAGER.equals( this.fetch ) ) ;
	}

	public boolean isFetchLAZY() {
		//return fetchLAZY;
		return ( RepositoryConst.FETCH_LAZY.equals( this.fetch ) ) ;
	}
	
	//--------------------------------------------------------------------------
	// FOREIGN KEY management
	//--------------------------------------------------------------------------
	public boolean isBasedOnForeignKey() {
		return StrUtil.nullOrVoid( this.foreignKeyName ) != true ;
	}
	
	/**
	 * Returns the name of the Foreign Key used to generate the link <br>
	 * There's no guarantee that this Foreign Key still exist
	 * @return
	 */
	public String getForeignKeyName() {
		return foreignKeyName ;
	}
	
	/**
	 * Set the name of the Foreign Key used to generate the link
	 * @param v
	 */
	public void setForeignKeyName(String v) {
		foreignKeyName = v ;
	}

	//--------------------------------------------------------------------------
	// JOIN TABLE management
	//--------------------------------------------------------------------------
	public boolean isBasedOnJoinTable() {
		return StrUtil.nullOrVoid( getJoinTableName() ) != true ;
	}

	/**
	 * Returns the name of the Join Table used to generate the link <br>
	 * There's no guarantee that this Foreign Key still exist
	 * @return
	 */
	public String getJoinTableName() {
		if ( joinTable != null ) {
			return joinTable.getName() ; // Only available with Owning Side
		}
		else {
			return joinTableName ; // Available on both Owning Side and Inverse Side
		}
	}
	
	/**
	 * Set the name of the Join Table used to generate the link
	 * @param v
	 */
	public void setJoinTableName(String v) {
		joinTableName = v ;
	}

	/**
	 * Returns a string that can be used to compare 2 fields, and to know if they are different or identical<br>
	 * NB : all the significant fields must be in this string !
	 * @return
	 */
	public String getComparableString() 
	{
		StringBuffer sb = new StringBuffer();
		sb.append( "#" );
		sb.append( this.getId() );
		sb.append( ":" );
		sb.append( this.getCascade() );
		sb.append( "/" );
		sb.append( this.getFetch() );
		sb.append( "/" );
		sb.append( this.getInverseSideOf() );
		sb.append( "/" );
		sb.append( this.getJavaFieldName() );
		sb.append( "/" );
		sb.append( this.getJavaFieldType() );
		sb.append( "/" );
		sb.append( this.getMappedBy() );
		sb.append( "/" );
		sb.append( this.getTargetEntityJavaType() );
		sb.append( "/" );
		sb.append( this.getTargetTableName() );
		sb.append( "/" );
		sb.append( this.getForeignKeyName() );
		sb.append( "/" );
		sb.append( this.getJoinTableName() );
		sb.append( "/" );
		sb.append( this.getSourceTableName() );
		sb.append( "/" );
		sb.append( this.getCardinality() );
		sb.append( "/" );
		sb.append( this.isOwningSide() );
		sb.append( "/" );
		sb.append( this.getOptional() );
		sb.append( "/" );
		sb.append( this.isUsed() );
		
		return sb.toString();
	}
}
