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
import org.telosys.tools.repository.changelog.ChangeLog;
import org.telosys.tools.repository.changelog.ChangeOnEntity;
import org.telosys.tools.repository.changelog.ChangeOnForeignKey;
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
import org.telosys.tools.repository.rules.RepositoryRules;

/**
 * Links generator <br>
 * Generates inter-entity links from Foreign Keys and Join Tables 
 * 
 * @author Laurent Guerin
 */
public class LinksManager {

	private final RepositoryRules repositoryRules ;
	
	private final TelosysToolsLogger logger;

	/**
	 * Constructor
	 * @param repositoryRules
	 * @param logger
	 */
	public LinksManager(RepositoryRules repositoryRules, TelosysToolsLogger logger) {
		this.logger = logger;
		this.repositoryRules = 	repositoryRules ;
	}
	
	private void log(String msg) {
		if ( logger != null ) {
			logger.log("[LOG] " + this.getClass().getName() + " : " + msg);
		}
	}

	/**
	 * Generates all the links from all the Foreign Keys defined in the repository <br>
	 * Existing links (if any) are removed and re-generate
	 * 
	 * @param model the repository to update with generated links
	 * @return the number of links generated 
	 * @throws TelosysToolsException 
	 */
	public int generateAllLinks(RepositoryModel model) throws TelosysToolsException 
	{
		log("generateAllLinks()...");
		
		int count = 0 ;
		//Entity[] entities = model.getEntities();
		for ( Entity entity : model.getEntities() ) {
			count = count + createRelations(model, entity);
		}
		return count ;
	}
	
	/**
	 * Removes all the relations regarding the given entity
	 * @param model
	 * @param entity
	 * @return
	 * @throws TelosysToolsException
	 */
	private int removeRelations(RepositoryModel model, Entity entity) throws TelosysToolsException 
	{
		log("removeRelations() : entity = " + entity);
		//--- Remove all the links using this entity 
		return model.removeLinksByEntityName(entity.getName());
	}
	
	/**
	 * Removes the relation (2 links) based on the given Foreign Key
	 * @param model
	 * @param foreignKey
	 * @return
	 * @throws TelosysToolsException
	 */
	private int removeRelation(RepositoryModel model, ForeignKey foreignKey) throws TelosysToolsException 
	{
		log("removeRelations() : foreignKey = " + foreignKey);
		//--- Remove all the links using this Foreign Key 
		return model.removeLinksByForeignKey(foreignKey);
	}
	
	/**
	 * Generate all the relations ( owning side and inverse side links ) <br>
	 * for the given entity using its foreign keys (if any) <br>
	 * The entity can be a 'standard entity' or a 'join table entity'
	 * @param model
	 * @param entity
	 * @return the number of links generated 
	 * @throws TelosysToolsException
	 */
	private int createRelations(RepositoryModel model, Entity entity) throws TelosysToolsException 
	{
		log("createRelations() : entity = " + entity);
		int count = 0 ;
		
		if ( entity.isJoinTable() )
		{
			log("createRelations() : entity is a Join Table ");
			//--- This entity can be considered as a "Join Table" ( all columns are Foreign Keys )
//			ForeignKey[] foreignKeys = entity.getForeignKeys() ;
//			if ( foreignKeys.length == 2 ) 
//			{
//				//--- Generate a bidirectional "ManyToMany" relation for this "Join Table"
//				ForeignKey owningSideForeignKey  = foreignKeys[0]; // Arbitrary choice
//				ForeignKey inverseSideForeignKey = foreignKeys[1]; // Arbitrary choice
//				count = count + createRelationManyToMany( model, entity, owningSideForeignKey, inverseSideForeignKey); 				
//			}
//			// else ( a join table with more than 2 FK ) : do nothing !
			count = count + createRelationManyToMany( model, entity);
		}
		else
		{
			log("createRelations() : entity is standard entity (not a Join Table) ");
			//--- Generate one relation ( 2 links ) for each FK 
			for ( ForeignKey fk : entity.getForeignKeys() ) {
				count = count + createRelationManyToOne(model, entity, fk);
			}
		}
		return count ;
	}

//	/**
//	 * Returns the Java attribute name for the given type <br>
//	 * Returns the same string but with the 1st char in Lower Case 
//	 * ie  "Book" --> "book"
//	 * @param javaType
//	 * @return
//	 * @throws TelosysToolsException
//	 */
//	private String getAttributeName(String javaType) throws TelosysToolsException 
//	{
//		String end = javaType.substring(1);
//		char c = javaType.charAt(0);
//		String firstChar = Character.toString(c);
//		return firstChar.toLowerCase() + end ;
//	}
	
//	/**
//	 * Returns the Java attribute name for a collection of the given type <br>
//	 * ie  "Book" --> "listOfBook"
//	 * @param javaType
//	 * @return
//	 * @throws TelosysToolsException
//	 */
//	private String getCollectionAttributeName(String javaType) throws TelosysToolsException {
//		return "listOf" + javaType ;
//	}

//	public String getBasicLinkId(RepositoryModel model, Entity entity, ForeignKey fk, boolean owningSide) throws TelosysToolsException {
//		String end = owningSide ? "O" : "I" ;
//		return "LINK_FK_" + fk.getName() + "_" + end ;
//	}
	
	//----------------------------------------------------------------------------------------------------
	// RELATION "* --> 1" ( "ManyToOne" and "OneToMany" links )
	//----------------------------------------------------------------------------------------------------
	/**
	 * Creates a "ManyToOne" relation (2 links) based on the the given Foreign Key
	 * @param model
	 * @param owningSideEntity : the owning side entity
	 * @param owningSideForeignKey : the Foreign Key that defines the relation 
	 * @return the number of links generated (always 2)
	 * @throws TelosysToolsException
	 */
	private int createRelationManyToOne(RepositoryModel model, Entity owningSideEntity, ForeignKey owningSideForeignKey) throws TelosysToolsException 
	{
		log("createRelationManyToOne() : Owning Side FK = " + owningSideForeignKey);

		Entity inverseSideEntity = model.getEntityByName( owningSideForeignKey.getTableRef() );
		if ( null == inverseSideEntity ) {
			throw new TelosysToolsException("No referenced table for Foreign Key '" + owningSideForeignKey.getName() + "'");
		}
		log("createRelationManyToOne() : Inverse Side Entity = " + inverseSideEntity);
		
		//--- Build the 2 link id
		String owningSideLinkId  = Link.buildId(owningSideForeignKey, true) ;
		String inverseSideLinkId = Link.buildId(owningSideForeignKey, false) ;

//		String originAttributeName = getAttributeName( inverseSideEntity.getBeanJavaClass() ) ;
		
		//--- Remove the links if they are already in the model
		model.removeLinkById(inverseSideLinkId);
		model.removeLinkById(owningSideLinkId);

		//--- Generates the 2 links 
		Link owningSideLink = generateManyToOneLinkOwningSide( owningSideLinkId, owningSideEntity, inverseSideEntity, owningSideForeignKey);
		generateManyToOneLinkInverseSide( inverseSideLinkId, owningSideEntity, inverseSideEntity, owningSideForeignKey, owningSideLink);
		
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
	private Link generateManyToOneLinkOwningSide( String linkId, Entity owningSideEntity, Entity inverseSideEntity, 
			ForeignKey owningSideForeignKey  ) throws TelosysToolsException 
	{
		log("generateManyToOneLinkOwningSide() : linkId = " + linkId + " "+ owningSideEntity.getName() + " --> " + inverseSideEntity.getName() );
		
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
		//--- Updated in ver 2.1.1 (the link manages multiple references to the same inverse-side entity)
		//link.setJavaFieldName( getAttributeName( inverseSideEntity.getBeanJavaClass() ) ); // ie "book"
		link.setJavaFieldName( repositoryRules.getAttributeNameForLinkToOne(owningSideEntity, inverseSideEntity) ) ; // #LGU v 2.1.1


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
	private Link generateManyToOneLinkInverseSide(String linkId, Entity owningSideEntity, Entity inverseSideEntity, 
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
		
		//link.setJavaFieldName( "listOf" + owningSideEntity.getBeanJavaClass() ); // ie "listOfBook"
		link.setJavaFieldName( repositoryRules.getAttributeNameForLinkToMany(inverseSideEntity, owningSideEntity ) ) ; // #LGU v 2.1.1
		
		link.setTargetEntityJavaType( owningSideEntity.getBeanJavaClass() ); // ie "Book"

		//--- Store the link in the entity
		inverseSideEntity.storeLink(link);
		return link;		
	}

	//----------------------------------------------------------------------------------------------------
	// RELATION "* --> *"  ( 2 "ManyToMany" links )
	//----------------------------------------------------------------------------------------------------
	/**
	 * Creates a "Many To Many" relation (2 links) based on the given "Join Table" entity <br>
	 * The 2 sides links will be generated 
	 * @param model
	 * @param joinTableEntity
	 * @return
	 * @throws TelosysToolsException
	 */
	private int createRelationManyToMany(RepositoryModel model, Entity joinTableEntity) throws TelosysToolsException 
	{
		log("createRelationManyToMany()...");
		int count = 0 ;
		//--- This entity can be considered as a "Join Table" ( all columns are Foreign Keys )
		ForeignKey[] foreignKeys = joinTableEntity.getForeignKeys() ;
		if ( foreignKeys.length == 2 ) {
			//--- Generate a bidirectional "ManyToMany" relation for this "Join Table"
			ForeignKey owningSideForeignKey  = foreignKeys[0]; // Arbitrary choice
			ForeignKey inverseSideForeignKey = foreignKeys[1]; // Arbitrary choice
			count = createRelationManyToMany( model, joinTableEntity, owningSideForeignKey, inverseSideForeignKey); 				
		}
		else {
			throw new TelosysToolsException("Entity '" + joinTableEntity.getName() 
					+ "' (Join Table) has " + foreignKeys.length + " Foreign Key(s) (2 FK expected)") ;
		}
		return count ;
	}
	
	/**
	 * Creates a "Many To Many" relation (2 links) based on the given "Join Table" entity <br>
	 * The 2 sides links will be generated 
	 * @param model
	 * @param joinTable
	 * @param owningSideForeignKey
	 * @param inverseSideForeignKey
	 * @return the number of links generated (always 2)
	 * @throws TelosysToolsException
	 */
	private int createRelationManyToMany(RepositoryModel model, Entity joinTable, 
			ForeignKey owningSideForeignKey, ForeignKey inverseSideForeignKey) throws TelosysToolsException 
	{
		log("createRelationManyToMany()...");

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
		//link.setJavaFieldName( getCollectionAttributeName( inverseSideEntity.getBeanJavaClass() ) ); // ie "listOfBook"
		link.setJavaFieldName( repositoryRules.getAttributeNameForLinkToMany(owningSideEntity, inverseSideEntity) ) ; // #LGU v 2.1.1
		
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
		//link.setJavaFieldName( getCollectionAttributeName( owningSideEntity.getBeanJavaClass() ) ); // ie "listOfBook"
		link.setJavaFieldName( repositoryRules.getAttributeNameForLinkToMany(inverseSideEntity, owningSideEntity ) ) ; // #LGU v 2.1.1

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
	
	//-----------------------------------------------------------------------------------------
	// LINKS UPDATE
	//-----------------------------------------------------------------------------------------
	/**
	 * Updates the model's links according to the given change log
	 * @param model
	 * @param changeLog
	 * @return
	 * @throws TelosysToolsException
	 */
	public int updateLinks(RepositoryModel model, ChangeLog changeLog ) throws TelosysToolsException 
	{
		int count = 0 ;
		//--- For each entity change...
		for ( ChangeOnEntity change : changeLog.getChanges() ) {
			switch ( change.getChangeType() ) {
			case CREATED :
				//--- An entity as been created
				Entity entityCreated = change.getEntityCreated() ;
				log("updateLinks() : entity CREATED = " + entityCreated);
				//--- Create all the links based on this entity (for a standard Table or a  Join Table )
				count = count + this.createRelations(model, entityCreated);
				break;
			case UPDATED :
				//--- An entity as been updated
				Entity entityUpdated = change.getEntityAfter() ;
				log("updateLinks() : entity UPDATED = " + entityUpdated);
				count = count + this.updateEntityLinks(model, entityUpdated, change);
				break;
			case DELETED :
				//--- An entity as been deleted
				Entity entityDeleted = change.getEntityDeleted() ;
				log("updateLinks() : entity DELETED = " + entityDeleted);
				//--- Remove all the links using this entity 
				count = count + this.removeRelations(model, entityDeleted);
				break;
			}
		}
		return count ;
	}
	
	/**
	 * Updates all the links for the given entity according with the given 'change'
	 * @param model
	 * @param entity
	 * @param change
	 * @return
	 * @throws TelosysToolsException
	 */
	private int updateEntityLinks(RepositoryModel model, Entity entity, ChangeOnEntity change ) throws TelosysToolsException 
	{
		int count = 0 ;
		if ( entity.isJoinTable() ) {
			if ( change.getChangesOnForeignKey().size() > 0 ) {
				//--- Something has changed in the Foreign Keys
				//--- 1) Remove existing links
				model.removeLinksByJoinTableName(entity.getName());
				//--- 2) Create new links based on the new Foreign Keys
				count = count + createRelationManyToMany(model, entity);
			}
		}
		else {
			//--- For each Foreign Key change...
			for ( ChangeOnForeignKey fkChange : change.getChangesOnForeignKey() ) {
				switch ( fkChange.getChangeType() ) {
				case CREATED :
					//--- A Foreign Key as been created 
					//--- Generates the relation ( 2 links ) based on the created FK 
					count = count + this.createRelationManyToOne(model, entity, fkChange.getForeignKeyCreated() );
					break;
				case UPDATED :
					//--- A Foreign Key as been updated 
					// 1) remove the OLD links
					//count = count + model.removeLinksByForeignKey(fkChange.getForeignKeyBefore());
					count = count + this.removeRelation(model, fkChange.getForeignKeyBefore());
					// 2) create the NEW links
					count = count + this.createRelationManyToOne(model, entity, fkChange.getForeignKeyAfter() );
					break;
				case DELETED :
					//--- A Foreign Key as been deleted 
					// linksManager.deleteLinks(model,change.getEntityDeleted() );
					//count = count + this.removeManyToOneLinks(model, entity, foreignKeyDeleted );
					//--- Removes the relation ( 2 links ) based on the deleted foreign key
					//count = count + model.removeLinksByForeignKey(foreignKeyDeleted);
					count = count + this.removeRelation(model, fkChange.getForeignKeyDeleted());
					break;
				}
			}
		}
		return 0 ;
	}
	
//	private int removeManyToOneLinks(RepositoryModel model, Entity owningSideEntity, ForeignKey owningSideForeignKey) throws TelosysToolsException 
//	{
//		//log("removeManyToOneLinks()...");
//
//		Entity inverseSideEntity = model.getEntityByName( owningSideForeignKey.getTableRef() );
//		if ( null == inverseSideEntity ) {
//			return 0 ; // It's possible that the referenced table/entity has been removed
//		}
//		
//		//--- Build the 2 link id
//		String owningSideLinkId  = Link.buildId(owningSideForeignKey, true) ;
//		String inverseSideLinkId = Link.buildId(owningSideForeignKey, false) ;
//
//		//--- Remove the links if they are already in the model
//		model.removeLinkById(inverseSideLinkId);
//		model.removeLinkById(owningSideLinkId);
//		
//		return 2 ;
//	}
	
}
