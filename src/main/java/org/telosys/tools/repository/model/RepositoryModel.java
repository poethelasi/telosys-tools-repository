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

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

public class RepositoryModel 
{
	private String version ;
	
	private String databaseName ;

	private int    databaseId = -1 ; // Database Id in the ".dbcfg" file ( v 2.1.0 )

	private String databaseProductName ; 
	
	private Date   generationDate ;
	
	private Date   lastUpdateDate ;
	
	private Hashtable<String,Entity> htEntities = new Hashtable<String,Entity>() ; 

	//-----------------------------------------------
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	//-----------------------------------------------
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	//-----------------------------------------------
	/**
	 * Returns the database id ( ".dbcfg" id )
	 * @return
	 */
	public int getDatabaseId() {
		return databaseId;
	}

	/**
	 * Set the database id ( ".dbcfg" id )
	 * @param databaseId
	 */
	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}

	//-----------------------------------------------
	/**
	 * Returns the database product name retrieved from the meta-data
	 * @return
	 */
	public String getDatabaseProductName() {
		return databaseProductName;
	}

	/**
	 * Set the database product name retrieved from the meta-data
	 * @param databaseType
	 */
	public void setDatabaseProductName(String databaseType) {
		this.databaseProductName = databaseType;
	}

	//-----------------------------------------------
	public Date getGenerationDate() {
		return generationDate;
	}

	public void setGenerationDate(Date generationDate) {
		this.generationDate = generationDate;
	}

	//-----------------------------------------------
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	//-------------------------------------------------------------------------------
	
	public int getNumberOfEntities() {
		return htEntities.size();
	}

	/**
	 * Returns an array of all the entities of the model.<br>
	 * The entities are sorted by name.
	 * 
	 * @return
	 */
	public Entity[] getEntities() {
		Entity[] array = (Entity[]) htEntities.values().toArray( new Entity[htEntities.size()] );
		Arrays.sort(array);
		return array ;
	}
	
	/**
	 * Returns a collection of all the entities of the model.<br>
	 * The entities are sorted by name.
	 * 
	 * @return
	 */
	public Collection<Entity> getEntitiesCollection() {
		//return htEntities.values() ;
		Entity[] entities = getEntities();
		return Arrays.asList(entities);
	}
	
	/**
	 * Returns an array containing the names of all the entities of the model.<br>
	 * The names are sorted in alphabetic order.
	 * 
	 * @return
	 */
	public String[] getEntitiesNames() {
		Collection<Entity> values = htEntities.values();
		String[] names = new String[values.size()];
		int cpt = 0;
//		for (Iterator iterator = values.iterator(); iterator.hasNext();) {
//			Entity entity = (Entity) iterator.next();
		for ( Entity entity : values ) {
			names[cpt] = entity.getName();
			cpt++;
		}
		Arrays.sort(names);
		return names;
	}
	
	/**
	 * Returns the entity for the given name, or null if not found
	 * @param name
	 * @return
	 */
	public Entity getEntityByName(String name) {
		return htEntities.get(name);
	}
	
	public void storeEntity(Entity entity) {
		htEntities.put(entity.getName(), entity);
	}
	
	/**
	 * Removes the entity having the given name (if any)
	 * @param name
	 * @return the entity removed (or null if none)
	 */
	public Entity removeEntity(String name) {
		return htEntities.remove(name);
	}
	
	//-------------------------------------------------------------------------------
	// LINKS management
	//-------------------------------------------------------------------------------
	/**
	 * Returns the link for the given id
	 * @param id
	 * @return
	 */
	public Link getLinkById(String id) {
		if ( id != null ) {
			Entity [] entities = this.getEntities();
			for ( int i = 0 ; i < entities.length ; i++ ) {
				Entity entity = entities[i];
				Link [] links = entity.getLinks();
				for ( int j = 0 ; j < links.length ; j++ ) {
					Link link = links[j];
					if ( id.equals( link.getId() ) )  {
						return link;
					}
				}
			}
		}
		return null ;
	}

	//-------------------------------------------------------------------------------
	/**
	 * Removes all the links in the model (for all the entities)
	 */
	public void removeAllLinks() {
		Entity [] entities = this.getEntities();
		for ( int i = 0 ; i < entities.length ; i++ ) {
			Entity entity = entities[i];
			entity.removeAllLinks();
		}
	}
	
	//-------------------------------------------------------------------------------
	/**
	 * Removes the link corresponding to the given id
	 * @param id
	 * @return 1 if the link has been found and removed, 0 if the link has not been found
	 */
	public int removeLinkById(String id) {
		int count = 0 ;
		Link link = getLinkById(id);
		if ( link != null ) {
			//--- Remove link 
			Entity entity = getEntityByName( link.getSourceTableName() );
			if ( entity != null ) {
				count = entity.removeLink(link);
			}
		}
		return count ;
	}
	
	/**
	 * Removes all the links using the given entity name (as source entity or target entity)
	 * @param entityName
	 * @return the number of links removed
	 * @since 2.1.1
	 */
	public int removeLinksByEntityName(String entityName) {
		int count = 0 ;
		for ( Entity entity : this.getEntities() ) {
			for ( Link link : entity.getLinks() ) {
				if ( entityName.equals( link.getSourceTableName() ) || entityName.equals( link.getTargetTableName() ) ) {
					count = count + entity.removeLink(link);
				}
			}
		}
		return count ;
	}
	
	/**
	 * Removes the links based on the given Foreign Key
	 * @param foreignKey
	 * @return the number of links removes (usually 2)
	 */
	public int removeLinksByForeignKey(ForeignKey foreignKey) {
		int count = 0 ;
		//--- Build the 2 link id
		String owningSideLinkId  = Link.buildId(foreignKey, true) ;
		String inverseSideLinkId = Link.buildId(foreignKey, false) ;
		//--- Remove the links if they are already in the model
		count = count + this.removeLinkById(inverseSideLinkId);
		count = count + this.removeLinkById(owningSideLinkId);

		return count ;
	}
	
	//-------------------------------------------------------------------------------
	/**
	 * Removes all the links built on the given "join table" name <br>
	 * Each "join table" is supposed to have 2 links in the model (or 0 if not used)
	 * @param joinTableName
	 * @return the number of links deleted (0 or 2 expected)
	 * @since 2.1.1
	 */
	public int removeLinksByJoinTableName(String joinTableName) {
		int count = 0 ;
		if ( joinTableName != null ) {
			for ( Entity entity : this.getEntities() ) {
				for ( Link link : entity.getLinks() ) {
					String jtName = link.getJoinTableName() ;
					if ( jtName != null ) {
						if ( jtName.equals(joinTableName) ) {
							entity.removeLink(link);
							count++;
						}
					}
				}
			}
		}
		return count ;
	}
	//-------------------------------------------------------------------------------
	/**
	 * Returns the RelationLinks ( the 2 links of a relation ) for the given link id
	 * @param linkId the id of one of the 2 links of the relation
	 * @return
	 */
	public RelationLinks getRelationByLinkId(String linkId) 
	{
		Link link1 = getLinkById(linkId);
		if ( link1 != null ) {
			if ( link1.isOwningSide() ) {
				//--- Owning Side => try to found the inverse side
				Entity [] entities = this.getEntities();
				for ( int i = 0 ; i < entities.length ; i++ ) {
					Entity entity = entities[i];
					Link [] links = entity.getLinks();
					for ( int j = 0 ; j < links.length ; j++ ) {
						Link link2 = links[j];
						if ( link2.isOwningSide() == false ) {
							if ( linkId.equals( link2.getInverseSideOf() ) ) {
								return new RelationLinks ( link1, link2 );
							}
						}
					}
				}
				// inverse side not found 
				return new RelationLinks ( link1, null );
			}
			else {
				//--- Inverse Side => try to found the owning side
				Link link2 = getLinkById( link1.getInverseSideOf() ) ;
				return new RelationLinks ( link2, link1 );
			}
		}
		return null ;
	}
	
	//-------------------------------------------------------------------------------
	// FOREIGN KEYS management
	//-------------------------------------------------------------------------------
	/**
	 * Search and return a Foreign Key  
	 * @param fkName the name to e searched
	 * @return the Foreign Key or null if not found
	 */
	public ForeignKey getForeignKeyByName(String fkName)
	{
		Entity [] entities = this.getEntities();
		for ( Entity entity : entities ) {
			ForeignKey fk = entity.getForeignKey(fkName);
			if ( fk != null ) {
				return fk ; // FOUND 
			}
		}
		return null ;
	}
	
}
