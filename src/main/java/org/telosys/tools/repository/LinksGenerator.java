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

import java.util.LinkedList;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.repository.model.Entity;
import org.telosys.tools.repository.model.ForeignKey;
import org.telosys.tools.repository.model.ForeignKeyColumn;
import org.telosys.tools.repository.model.InverseJoinColumns;
import org.telosys.tools.repository.model.JoinColumn;
import org.telosys.tools.repository.model.JoinColumns;
import org.telosys.tools.repository.model.JoinTable;
import org.telosys.tools.repository.model.Link;
import org.telosys.tools.repository.model.RepositoryModel;
import org.telosys.tools.repository.persistence.util.RepositoryConst;

/**
 * Links generator <br>
 * Generates inter-entity links from Foreign Keys and Join Tables 
 * 
 * @author S.Labbe, L.Guerin
 */
public class LinksGenerator {

	//private static final String COLLECTION_JAVA_TYPE = "java.util.List";
	private final TelosysToolsLogger _logger;

	/**
	 * Constructor
	 * @param logger
	 */
	public LinksGenerator(TelosysToolsLogger logger) {
		_logger = logger;
	}
	
	private void log(String msg) {
		if ( _logger != null ) {
			_logger.log("[LOG] " + this.getClass().getName() + " : " + msg);
		}
	}

	/**
	 * Generates all the links from all the Foreign Keys defined in the repository
	 * @param model the repository to update with generated links
	 * @return the number of links generated 
	 * @throws TelosysToolsException 
	 */
	public int generateAllLinks(RepositoryModel model) throws TelosysToolsException 
	{
		log("generateAllLinks()...");
		
		int count = 0 ;
		Entity[] entities = model.getEntities();
		for ( Entity entity : entities ) {
			count = count + generateEntityLinks(model, entity);
		}
		return count ;
	}

	/**
	 * Generate the links ( owning side and inverse side ) for the given entity
	 * @param model
	 * @param entity
	 * @return the number of links generated 
	 * @throws TelosysToolsException
	 */
	public int generateEntityLinks(RepositoryModel model, Entity entity) throws TelosysToolsException 
	{
		log("generateEntityLinks()...");
		int count = 0 ;
		
		if ( entity.isJoinTable() )
		{
			//--- This entity can be considered as a "Join Table" ( all columns are Foreign Keys )
			ForeignKey[] foreignKeys = entity.getForeignKeys() ;
			if ( foreignKeys.length == 2 ) 
			{
				//--- Generate a bidirectional "ManyToMany" relation for this "Join Table"
				ForeignKey owningSideForeignKey  = foreignKeys[0]; // Arbitrary choice
				ForeignKey inverseSideForeignKey = foreignKeys[1]; // Arbitrary choice
				count = count + generateManyToManyLinks( model, entity, owningSideForeignKey, inverseSideForeignKey); 				
			}
			// else ( a join table with more than 2 FK ) : do nothing !
		}
		else
		{
			//--- Generate one relation ( 2 links ) for each FK 
			ForeignKey[] foreignKeys = entity.getForeignKeys();
			for ( ForeignKey fk : foreignKeys ) {
				count = count + generateBasicLinks(model, entity, fk);
			}
		}
		return count ;
	}

	/**
	 * Returns the Java attribute name for the given type <br>
	 * Returns the same string but with the 1st char in Lower Case 
	 * ie  "Book" --> "book"
	 * @param javaType
	 * @return
	 * @throws TelosysToolsException
	 */
	public String getAttributeName(String javaType) throws TelosysToolsException 
	{
		String end = javaType.substring(1);
		char c = javaType.charAt(0);
		String firstChar = Character.toString(c);
		return firstChar.toLowerCase() + end ;
	}
	
	/**
	 * Returns the Java attribute name for a collection of the given type <br>
	 * ie  "Book" --> "listOfBook"
	 * @param javaType
	 * @return
	 * @throws TelosysToolsException
	 */
	public String getCollectionAttributeName(String javaType) throws TelosysToolsException {
		return "listOf" + javaType ;
	}

	public String getBasicLinkId(RepositoryModel model, Entity entity, ForeignKey fk, boolean owningSide) throws TelosysToolsException {
		String end = owningSide ? "O" : "I" ;
		return "LINK_FK_" + fk.getName() + "_" + end ;
	}
	
	//----------------------------------------------------------------------------------------------------
	// RELATION "* --> 1" ( "ManyToOne" and "OneToMany" links )
	//----------------------------------------------------------------------------------------------------
	/**
	 * Generates the two links for a "ManyToOne" relation based on the the given Foreign Key
	 * @param model
	 * @param owningSideEntity : the owning side entity
	 * @param owningSideForeignKey : the Foreign Key that defines the relation 
	 * @return the number of links generated (always 2)
	 * @throws TelosysToolsException
	 */
	private int generateBasicLinks(RepositoryModel model, Entity owningSideEntity, ForeignKey owningSideForeignKey) throws TelosysToolsException 
	{
		log("generateBasicLinks()...");

		Entity inverseSideEntity = model.getEntityByName( owningSideForeignKey.getTableRef() );
		if ( null == inverseSideEntity ) {
			throw new TelosysToolsException("No referenced table for Foreign Key '" + owningSideForeignKey.getName() + "'");
		}
		
		//--- Build the 2 link id
		String owningSideId  = Link.buildId(owningSideForeignKey, true) ;
		String inverseSideId = Link.buildId(owningSideForeignKey, false) ;

//		String originAttributeName = getAttributeName( inverseSideEntity.getBeanJavaClass() ) ;
		
		//--- Remove the links if they are already in the model
		model.removeLinkById(inverseSideId);
		model.removeLinkById(owningSideId);

		//--- Generates the 2 links 
		Link owningSideLink = generateBasicLinkOwningSide( owningSideId, owningSideEntity, inverseSideEntity, owningSideForeignKey);
		generateBasicLinkInverseSide( inverseSideId, owningSideEntity, inverseSideEntity, owningSideForeignKey, owningSideLink);
		
		return 2 ; // 2 links generated
	}
	
	/**
	 * Generates the owning side link of a "ManyToOne" relation 
	 * @param linkId
	 * @param owningSideEntity
	 * @param inverseSideEntity
	 * @param owningSideForeignKey
	 * @return
	 * @throws TelosysToolsException
	 */
	private Link generateBasicLinkOwningSide( String linkId, Entity owningSideEntity, Entity inverseSideEntity, 
			ForeignKey owningSideForeignKey  ) throws TelosysToolsException 
	{		
		Link link = new Link();
		link.setId(linkId);
		link.setForeignKeyName( owningSideForeignKey.getName() );
		
		link.setOwningSide(true); // Owning Side
		link.setInverseSideOf("");
		link.setCardinality(RepositoryConst.MAPPING_MANY_TO_ONE);
		link.setFetch(RepositoryConst.FETCH_DEFAULT);
		link.setSourceTableName(owningSideForeignKey.getTableName());
		link.setTargetTableName(owningSideForeignKey.getTableRef());
		
		//--- Define the "Join Columns"
		LinkedList<JoinColumn> joinColumns = buildJoinColumns(owningSideForeignKey);
		link.setJoinColumns( new JoinColumns(joinColumns) );
		
		link.setTargetEntityJavaType( inverseSideEntity.getBeanJavaClass() ); // ie "Book" 
		link.setJavaFieldType( inverseSideEntity.getBeanJavaClass() ); // ie "Book" 
		link.setJavaFieldName( getAttributeName( inverseSideEntity.getBeanJavaClass() ) ); // ie "book"

		//--- Store the link in the entity
		owningSideEntity.storeLink(link);
		return link;		
	}
	
	/**
	 * Generates the inverse side link of a "ManyToOne" relation ( generates a "OneToMany" link with a collection )
	 * @param linkId
	 * @param owningSideEntity
	 * @param inverseSideEntity
	 * @param owningSideForeignKey
	 * @param owningSideLink
	 * @return
	 * @throws TelosysToolsException
	 */
	private Link generateBasicLinkInverseSide(String linkId, Entity owningSideEntity, Entity inverseSideEntity, 
			ForeignKey owningSideForeignKey, Link owningSideLink ) throws TelosysToolsException 
	{
		Link link = new Link();
		link.setId(linkId);
		link.setForeignKeyName( owningSideForeignKey.getName() );

		link.setOwningSide(false); // Inverse Side
		link.setInverseSideOf(owningSideLink.getId());
		
		//--- Inverse side => No "Join Table", No "Join Columns", No "Inverse Join Columns"

		//--- Inverse side => "Mapped By"
		link.setMappedBy( owningSideLink.getJavaFieldName() );

		link.setCardinality(RepositoryConst.MAPPING_ONE_TO_MANY);
		link.setFetch(RepositoryConst.FETCH_DEFAULT);
		link.setSourceTableName(owningSideForeignKey.getTableRef());
		link.setTargetTableName(owningSideForeignKey.getTableName());

		link.setJavaFieldType(RepositoryConst.COLLECTION_JAVA_TYPE); // ie "List"
		link.setJavaFieldName( "listOf" + owningSideEntity.getBeanJavaClass() ); // ie "listOfBook"
		link.setTargetEntityJavaType( owningSideEntity.getBeanJavaClass() ); // ie "Book"

		//--- Store the link in the entity
		inverseSideEntity.storeLink(link);
		return link;		
	}

	//----------------------------------------------------------------------------------------------------
	// RELATION "* --> *"  ( 2 "ManyToMany" links )
	//----------------------------------------------------------------------------------------------------
	/**
	 * Generates the links for a "Many To Many" relation based on the given "Join Table" entity
	 * The 2 sides links will be generated 
	 * @param model
	 * @param joinTable
	 * @param owningSideForeignKey
	 * @param inverseSideForeignKey
	 * @return the number of links generated (always 2)
	 * @throws TelosysToolsException
	 */
	private int generateManyToManyLinks(RepositoryModel model, Entity joinTable, 
			ForeignKey owningSideForeignKey, ForeignKey inverseSideForeignKey) throws TelosysToolsException 
	{
		log("generateManyToManyLinks()...");

		//--- Build the 2 id
		String owningSideId  = Link.buildId(joinTable, true) ;
		String inverseSideId = Link.buildId(joinTable, false) ;
		
		//--- Remove the links if they are already in the model
		model.removeLinkById(inverseSideId);
		model.removeLinkById(owningSideId);

		//--- One entity is referenced by one of the two foreign keys
		Entity owningSideEntity  = model.getEntityByName( owningSideForeignKey.getTableRef() );		
		//--- The other entity is referenced by the other foreign key
		Entity inverseSideEntity = model.getEntityByName( inverseSideForeignKey.getTableRef() );
		
		//--- Generates the 2 links 
		Link owningSideLink = generateManyToManyLinkOwningSide( owningSideId, 
				owningSideEntity, inverseSideEntity, 
				joinTable, 
				owningSideForeignKey, inverseSideForeignKey );
		
		generateManyToManyLinkInverseSide( inverseSideId, 
				owningSideEntity, inverseSideEntity, 
				joinTable, 
				owningSideForeignKey, inverseSideForeignKey, 
				owningSideLink );
		
		return 2 ; // 2 links generated
	}
	
	/**
	 * Generates the owning side of the "ManyToMany" relation 
	 * @param linkId
	 * @param owningSideEntity
	 * @param inverseSideEntity
	 * @param joinTableEntity
	 * @param owningSideForeignKey
	 * @param inverseSideForeignKey
	 * @return
	 * @throws TelosysToolsException
	 */
	private Link generateManyToManyLinkOwningSide( String linkId, Entity owningSideEntity, Entity inverseSideEntity, 
			Entity joinTableEntity, ForeignKey owningSideForeignKey, ForeignKey inverseSideForeignKey
			 ) throws TelosysToolsException 
	{		
		Link link = new Link();
		link.setId(linkId);
		link.setJoinTableName( joinTableEntity.getName() );

		link.setOwningSide(true); // Owning Side
		link.setInverseSideOf("");
		link.setCardinality(RepositoryConst.MAPPING_MANY_TO_MANY);
		link.setFetch(RepositoryConst.FETCH_DEFAULT);
		
		//--- Define the "Join Table"
		JoinTable joinTable = new JoinTable();
		joinTable.setName( joinTableEntity.getName() );
		joinTable.setSchema( joinTableEntity.getSchema() );
		joinTable.setCatalog( joinTableEntity.getCatalog() );
		link.setJoinTable(joinTable);
		
		//--- Define the "Join Columns" of the "Join Table"
		LinkedList<JoinColumn> joinColumns = buildJoinColumns(owningSideForeignKey);
		joinTable.setJoinColumns( new JoinColumns(joinColumns) );
		
		//--- Define the "Inverse Join Columns" of the "Join Table"
		LinkedList<JoinColumn> inverseJoinColumns = buildJoinColumns(inverseSideForeignKey);
		joinTable.setInverseJoinColumns( new InverseJoinColumns(inverseJoinColumns) );
		
		
		link.setSourceTableName(owningSideForeignKey.getTableRef());
		link.setTargetTableName(inverseSideForeignKey.getTableRef());
		
		//--- Java attribute for this link
		link.setJavaFieldType( RepositoryConst.COLLECTION_JAVA_TYPE ); // ie "java.util.List"
		link.setJavaFieldName( getCollectionAttributeName( inverseSideEntity.getBeanJavaClass() ) ); // ie "listOfBook"
		link.setTargetEntityJavaType( inverseSideEntity.getBeanJavaClass() ); // ie "Book"

		//--- Store the link in the entity
		owningSideEntity.storeLink(link);
		return link;		
	}
	
	/**
	 * Generates the inverse side of the "ManyToMany" relation 
	 * @param linkId
	 * @param owningSideEntity
	 * @param inverseSideEntity
	 * @param joinTableEntity
	 * @param owningSideForeignKey
	 * @param inverseSideForeignKey
	 * @param owningSideLink
	 * @return
	 * @throws TelosysToolsException
	 */
	private Link generateManyToManyLinkInverseSide( String linkId, Entity owningSideEntity, Entity inverseSideEntity, 
			Entity joinTableEntity, ForeignKey owningSideForeignKey, ForeignKey inverseSideForeignKey, 
			 Link owningSideLink ) throws TelosysToolsException 
	{		
		Link link = new Link();
		link.setId(linkId);
		link.setJoinTableName( joinTableEntity.getName() );
		
		link.setOwningSide(false); // Owning Side
		link.setInverseSideOf( owningSideLink.getId() );
		link.setCardinality(RepositoryConst.MAPPING_MANY_TO_MANY);
		link.setFetch(RepositoryConst.FETCH_DEFAULT);
		
		//--- Inverse side => No "Join Table", No "Join Columns", No "Inverse Join Columns"

		//--- Inverse side => "Mapped By"
		link.setMappedBy( owningSideLink.getJavaFieldName() );
		
		link.setSourceTableName(inverseSideForeignKey.getTableRef());
		link.setTargetTableName(owningSideForeignKey.getTableRef()); 
		
		//--- Java attribute for this link
		link.setJavaFieldType( RepositoryConst.COLLECTION_JAVA_TYPE ); // ie "java.util.List"
		link.setJavaFieldName( getCollectionAttributeName( owningSideEntity.getBeanJavaClass() ) ); // ie "listOfBook"
		link.setTargetEntityJavaType( owningSideEntity.getBeanJavaClass() ); // ie "Book"

		//--- Store the link in the entity
		inverseSideEntity.storeLink(link);
		return link;		
	}
	
	private LinkedList<JoinColumn>  buildJoinColumns( ForeignKey foreignKey ) throws TelosysToolsException 
	{
		LinkedList<JoinColumn> joinColumns = new LinkedList<JoinColumn>();
		//JoinColumns joinColumns = new JoinColumns();
		
		ForeignKeyColumn[] fkColumns = foreignKey.getForeignKeyColumns();
		
		for ( ForeignKeyColumn fkColumn : fkColumns ) {
			JoinColumn joinColumn = new JoinColumn();
			joinColumn.setName(fkColumn.getColumnName());
			joinColumn.setReferencedColumnName(fkColumn.getColumnRef());
			// TODO ???
//			joinColumn.setNullable(xx); 
//			joinColumn.setInsertable(xxx);
//			joinColumn.setUpdatable(xx); 
//			joinColumn.setUnique(xxx); 
			
			joinColumns.add(joinColumn);
		}
		return joinColumns ;
	}
	
}
