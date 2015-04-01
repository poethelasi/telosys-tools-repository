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
package org.telosys.tools.repository;

import java.util.LinkedList;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.generic.model.Cardinality;
import org.telosys.tools.generic.model.FetchType;
import org.telosys.tools.repository.model.EntityInDbModel;
import org.telosys.tools.repository.model.ForeignKeyColumnInDbModel;
import org.telosys.tools.repository.model.ForeignKeyInDbModel;
import org.telosys.tools.repository.model.JoinColumnInDbModel;
import org.telosys.tools.repository.model.JoinTableInDbModel;
import org.telosys.tools.repository.model.LinkInDbModel;
import org.telosys.tools.repository.model.RepositoryModel;
import org.telosys.tools.repository.persistence.util.RepositoryConst;
import org.telosys.tools.repository.rules.RepositoryRules;

/**
 * Links generator <br>
 * Generates inter-entity links from Foreign Keys and Join Tables 
 * 
 * @author S.Labbe, L.Guerin
 */
public class LinksGenerator {

	private final RepositoryRules repositoryRules ;
	
	private final TelosysToolsLogger logger;

	/**
	 * Constructor
	 * @param repositoryRules
	 * @param logger
	 */
	public LinksGenerator(RepositoryRules repositoryRules, TelosysToolsLogger logger) {
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
		EntityInDbModel[] entities = model.getEntitiesArray();
		for ( EntityInDbModel entity : entities ) {
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
	private int generateEntityLinks(RepositoryModel model, EntityInDbModel entity) throws TelosysToolsException 
	{
		log("generateEntityLinks()...");
		int count = 0 ;
		
		if ( entity.isJoinTable() )
		{
			//--- This entity can be considered as a "Join Table" ( all columns are Foreign Keys )
			ForeignKeyInDbModel[] foreignKeys = entity.getForeignKeys() ;
			if ( foreignKeys.length == 2 ) 
			{
				//--- Generate a bidirectional "ManyToMany" relation for this "Join Table"
				ForeignKeyInDbModel owningSideForeignKey  = foreignKeys[0]; // Arbitrary choice
				ForeignKeyInDbModel inverseSideForeignKey = foreignKeys[1]; // Arbitrary choice
				count = count + generateManyToManyLinks( model, entity, owningSideForeignKey, inverseSideForeignKey); 				
			}
			// else ( a join table with more than 2 FK ) : do nothing !
		}
		else
		{
			//--- Generate one relation ( 2 links ) for each FK 
			ForeignKeyInDbModel[] foreignKeys = entity.getForeignKeys();
			for ( ForeignKeyInDbModel fk : foreignKeys ) {
				count = count + generateManyToOneLinks(model, entity, fk);
			}
		}
		return count ;
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
	protected int generateManyToOneLinks(RepositoryModel model, EntityInDbModel owningSideEntity, ForeignKeyInDbModel owningSideForeignKey) throws TelosysToolsException 
	{
		log("generateBasicLinks()...");

//		EntityInDbModel inverseSideEntity = model.getEntityByName( owningSideForeignKey.getTableRef() );
		EntityInDbModel inverseSideEntity = model.getEntityByTableName( owningSideForeignKey.getReferencedTableName() ); // v 3.0.0
		if ( null == inverseSideEntity ) {
			throw new TelosysToolsException("No referenced table for Foreign Key '" + owningSideForeignKey.getName() + "'");
		}
		
		//--- Build the 2 link id
		String owningSideLinkId  = LinkInDbModel.buildId(owningSideForeignKey, true) ;
		String inverseSideLinkId = LinkInDbModel.buildId(owningSideForeignKey, false) ;

		//--- Remove the links if they are already in the model
		model.removeLinkById(inverseSideLinkId);
		model.removeLinkById(owningSideLinkId);

		//--- Generates the 2 links 
		LinkInDbModel owningSideLink = generateManyToOneLinkOwningSide( owningSideLinkId, owningSideEntity, inverseSideEntity, owningSideForeignKey);
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
	private LinkInDbModel generateManyToOneLinkOwningSide( String linkId, EntityInDbModel owningSideEntity, EntityInDbModel inverseSideEntity, 
			ForeignKeyInDbModel owningSideForeignKey  ) throws TelosysToolsException 
	{		
		LinkInDbModel link = new LinkInDbModel();
		link.setId(linkId);
		link.setForeignKeyName( owningSideForeignKey.getName() );
		
		link.setOwningSide(true); // Owning Side
//		link.setInverseSideOf(""); 
		link.setInverseSideLinkId(""); // v 3.0.0
//		link.setCardinality(RepositoryConst.MAPPING_MANY_TO_ONE);
		link.setCardinality(Cardinality.MANY_TO_ONE); // v 3.0.0
//		link.setFetch(RepositoryConst.FETCH_DEFAULT);
		link.setFetchType(FetchType.DEFAULT); // v 3.0.0
		link.setSourceTableName(owningSideForeignKey.getTableName());
//		link.setTargetTableName(owningSideForeignKey.getTableRef());
		link.setTargetTableName(owningSideForeignKey.getReferencedTableName()); // v 3.0.0
		
		//--- Define the "Join Columns"
		LinkedList<JoinColumnInDbModel> joinColumns = buildJoinColumns(owningSideForeignKey);
//		link.setJoinColumns( new JoinColumnsInDbModel(joinColumns) );
		link.setJoinColumns( joinColumns ); // v 3.0.0
		
//		link.setTargetEntityJavaType( inverseSideEntity.getBeanJavaClass() ); // ie "Book" 
		link.setTargetEntityClassName( inverseSideEntity.getClassName() ); // ie "Book"  // v 3.0.0
//		link.setJavaFieldType( inverseSideEntity.getBeanJavaClass() ); // ie "Book" 
		link.setFieldType( inverseSideEntity.getClassName() ); // ie "Book"  // v 3.0.0
		//--- Updated in ver 2.1.1 (the link manages multiple references to the same inverse-side entity)
//		link.setJavaFieldName( repositoryRules.getAttributeNameForLinkToOne(owningSideEntity, inverseSideEntity) ) ; // #LGU v 2.1.1
		link.setFieldName( repositoryRules.getAttributeNameForLinkToOne(owningSideEntity, inverseSideEntity) ) ; // v 3.0.0

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
	private LinkInDbModel generateManyToOneLinkInverseSide(String linkId, EntityInDbModel owningSideEntity, EntityInDbModel inverseSideEntity, 
			ForeignKeyInDbModel owningSideForeignKey, LinkInDbModel owningSideLink ) throws TelosysToolsException 
	{
		LinkInDbModel link = new LinkInDbModel();
		link.setId(linkId);
		link.setForeignKeyName( owningSideForeignKey.getName() );

		link.setOwningSide(false); // Inverse Side
//		link.setInverseSideOf(owningSideLink.getId());
		link.setInverseSideLinkId(owningSideLink.getId()); // v 3.0.0
		
		//--- Inverse side => No "Join Table", No "Join Columns", No "Inverse Join Columns"

		//--- Inverse side => "Mapped By"
//		link.setMappedBy( owningSideLink.getJavaFieldName() );
		link.setMappedBy( owningSideLink.getFieldName() ); // v 3.0.0

//		link.setCardinality(RepositoryConst.MAPPING_ONE_TO_MANY);
		link.setCardinality(Cardinality.ONE_TO_MANY); // v 3.0.0
//		link.setFetch(RepositoryConst.FETCH_DEFAULT);
		link.setFetchType(FetchType.DEFAULT); // v 3.0.0
//		link.setSourceTableName(owningSideForeignKey.getTableRef());
		link.setSourceTableName(owningSideForeignKey.getReferencedTableName()); // v 3.0.0
		link.setTargetTableName(owningSideForeignKey.getTableName());

//		link.setJavaFieldType(RepositoryConst.COLLECTION_JAVA_TYPE); // ie "List"
		link.setFieldType(RepositoryConst.COLLECTION_JAVA_TYPE); // ie "List"  // v 3.0.0
		
//		link.setJavaFieldName( repositoryRules.getAttributeNameForLinkToMany(inverseSideEntity, owningSideEntity ) ) ; // #LGU v 2.1.1
		link.setFieldName( repositoryRules.getAttributeNameForLinkToMany(inverseSideEntity, owningSideEntity ) ) ; // v 3.0.0
		
//		link.setTargetEntityJavaType( owningSideEntity.getBeanJavaClass() ); // ie "Book"
		link.setTargetEntityClassName( owningSideEntity.getClassName() ); // ie "Book" // v 3.0.0

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
	protected int generateManyToManyLinks(RepositoryModel model, EntityInDbModel joinTable, 
			ForeignKeyInDbModel owningSideForeignKey, ForeignKeyInDbModel inverseSideForeignKey) throws TelosysToolsException 
	{
		log("generateManyToManyLinks()...");

		//--- Build the 2 id
		String owningSideId  = LinkInDbModel.buildId(joinTable, true) ;
		String inverseSideId = LinkInDbModel.buildId(joinTable, false) ;
		
		//--- Remove the links if they are already in the model
		model.removeLinkById(inverseSideId);
		model.removeLinkById(owningSideId);

		//--- One entity is referenced by one of the two foreign keys
//		EntityInDbModel owningSideEntity  = model.getEntityByName( owningSideForeignKey.getTableRef() );		
		EntityInDbModel owningSideEntity  = model.getEntityByTableName( owningSideForeignKey.getReferencedTableName() ); // v 3.0.0
		//--- The other entity is referenced by the other foreign key
//		EntityInDbModel inverseSideEntity = model.getEntityByName( inverseSideForeignKey.getTableRef() );
		EntityInDbModel inverseSideEntity = model.getEntityByTableName( inverseSideForeignKey.getReferencedTableName() ); // v 3.0.0
		
		//--- Generates the 2 links 
		LinkInDbModel owningSideLink = generateManyToManyLinkOwningSide( owningSideId, 
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
	private LinkInDbModel generateManyToManyLinkOwningSide( String linkId, EntityInDbModel owningSideEntity, EntityInDbModel inverseSideEntity, 
			EntityInDbModel joinTableEntity, ForeignKeyInDbModel owningSideForeignKey, ForeignKeyInDbModel inverseSideForeignKey
			 ) throws TelosysToolsException 
	{		
		LinkInDbModel link = new LinkInDbModel();
		link.setId(linkId);
//		link.setJoinTableName( joinTableEntity.getName() );
		link.setJoinTableName( joinTableEntity.getDatabaseTable() ); // v 3.0.0

		link.setOwningSide(true); // Owning Side
//		link.setInverseSideOf("");
		link.setInverseSideLinkId(""); // v 3.0.0
//		link.setCardinality(RepositoryConst.MAPPING_MANY_TO_MANY);
		link.setCardinality(Cardinality.MANY_TO_MANY); // v 3.0.0
//		link.setFetch(RepositoryConst.FETCH_DEFAULT);
		link.setFetchType(FetchType.DEFAULT); // v 3.0.0
		
		//--- Define the "Join Table"
		JoinTableInDbModel joinTable = new JoinTableInDbModel();
//		joinTable.setName( joinTableEntity.getName() );
		joinTable.setName( joinTableEntity.getDatabaseTable() ); // v 3.0.0
//		joinTable.setSchema( joinTableEntity.getSchema() );
		joinTable.setSchema( joinTableEntity.getDatabaseSchema() ); // v 3.0.0
//		joinTable.setCatalog( joinTableEntity.getCatalog() );
		joinTable.setCatalog( joinTableEntity.getDatabaseCatalog() ); // v 3.0.0
		link.setJoinTable(joinTable);
		
		//--- Define the "Join Columns" of the "Join Table"
		LinkedList<JoinColumnInDbModel> joinColumns = buildJoinColumns(owningSideForeignKey);
//		joinTable.setJoinColumns( new JoinColumnsInDbModel(joinColumns) );
		joinTable.setJoinColumns( joinColumns ); // v 3.0.0
		
		//--- Define the "Inverse Join Columns" of the "Join Table"
		LinkedList<JoinColumnInDbModel> inverseJoinColumns = buildJoinColumns(inverseSideForeignKey);
//		joinTable.setInverseJoinColumns( new InverseJoinColumnsInDbModel(inverseJoinColumns) );
		joinTable.setInverseJoinColumns( inverseJoinColumns ); // v 3.0.0
		
		
//		link.setSourceTableName(owningSideForeignKey.getTableRef());
		link.setSourceTableName(owningSideForeignKey.getReferencedTableName()); // v 3.0.0
//		link.setTargetTableName(inverseSideForeignKey.getTableRef());
		link.setTargetTableName(inverseSideForeignKey.getReferencedTableName()); // v 3.0.0
		
		//--- Java attribute for this link
//		link.setJavaFieldType( RepositoryConst.COLLECTION_JAVA_TYPE ); // ie "java.util.List"
		link.setFieldType( RepositoryConst.COLLECTION_JAVA_TYPE ); // ie "java.util.List" // v 3.0.0
//		link.setJavaFieldName( repositoryRules.getAttributeNameForLinkToMany(owningSideEntity, inverseSideEntity) ) ; // #LGU v 2.1.1
		link.setFieldName( repositoryRules.getAttributeNameForLinkToMany(owningSideEntity, inverseSideEntity) ) ; // v 3.0.0
		
//		link.setTargetEntityJavaType( inverseSideEntity.getBeanJavaClass() ); // ie "Book"
		link.setTargetEntityClassName( inverseSideEntity.getClassName() ); // ie "Book" // v 3.0.0

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
	private LinkInDbModel generateManyToManyLinkInverseSide( String linkId, EntityInDbModel owningSideEntity, EntityInDbModel inverseSideEntity, 
			EntityInDbModel joinTableEntity, ForeignKeyInDbModel owningSideForeignKey, ForeignKeyInDbModel inverseSideForeignKey, 
			 LinkInDbModel owningSideLink ) throws TelosysToolsException 
	{		
		LinkInDbModel link = new LinkInDbModel();
		link.setId(linkId);
//		link.setJoinTableName( joinTableEntity.getName() );
		link.setJoinTableName( joinTableEntity.getDatabaseTable() ); // v 3.0.0
		
		link.setOwningSide(false); // Owning Side
//		link.setInverseSideOf( owningSideLink.getId() );
		link.setInverseSideLinkId( owningSideLink.getId() ); // v 3.0.0
//		link.setCardinality(RepositoryConst.MAPPING_MANY_TO_MANY);
		link.setCardinality(Cardinality.MANY_TO_MANY); // v 3.0.0
//		link.setFetch(RepositoryConst.FETCH_DEFAULT);
		link.setFetchType(FetchType.DEFAULT); // v 3.0.0
		
		//--- Inverse side => No "Join Table", No "Join Columns", No "Inverse Join Columns"

		//--- Inverse side => "Mapped By"
//		link.setMappedBy( owningSideLink.getJavaFieldName() );
		link.setMappedBy( owningSideLink.getFieldName() ); // v 3.0.0
		
//		link.setSourceTableName(inverseSideForeignKey.getTableRef());
		link.setSourceTableName(inverseSideForeignKey.getReferencedTableName()); // v 3.0.0
//		link.setTargetTableName(owningSideForeignKey.getTableRef()); 
		link.setTargetTableName(owningSideForeignKey.getReferencedTableName()); // v 3.0.0
		
		//--- Java attribute for this link
//		link.setJavaFieldType( RepositoryConst.COLLECTION_JAVA_TYPE ); // ie "java.util.List"
		link.setFieldType( RepositoryConst.COLLECTION_JAVA_TYPE ); // ie "java.util.List" // v 3.0.0
//		link.setJavaFieldName( repositoryRules.getAttributeNameForLinkToMany(inverseSideEntity, owningSideEntity ) ) ; // #LGU v 2.1.1
		link.setFieldName( repositoryRules.getAttributeNameForLinkToMany(inverseSideEntity, owningSideEntity ) ) ; // v 3.0.0

//		link.setTargetEntityJavaType( owningSideEntity.getBeanJavaClass() ); // ie "Book"
		link.setTargetEntityClassName( owningSideEntity.getClassName() ); // ie "Book" // v 3.0.0

		//--- Store the link in the entity
		inverseSideEntity.storeLink(link);
		return link;		
	}
	
	private LinkedList<JoinColumnInDbModel>  buildJoinColumns( ForeignKeyInDbModel foreignKey ) throws TelosysToolsException 
	{
		LinkedList<JoinColumnInDbModel> joinColumns = new LinkedList<JoinColumnInDbModel>();
		//JoinColumns joinColumns = new JoinColumns();
		
		ForeignKeyColumnInDbModel[] fkColumns = foreignKey.getForeignKeyColumns();
		
		for ( ForeignKeyColumnInDbModel fkColumn : fkColumns ) {
			JoinColumnInDbModel joinColumn = new JoinColumnInDbModel();
			joinColumn.setName(fkColumn.getColumnName());
//			joinColumn.setReferencedColumnName(fkColumn.getColumnRef());
			joinColumn.setReferencedColumnName(fkColumn.getReferencedColumnName()); // v 3.0.0
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
