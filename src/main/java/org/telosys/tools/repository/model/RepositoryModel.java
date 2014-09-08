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
	
	public Entity getEntityByName(String name) {
		return (Entity) htEntities.get(name);
	}
	
	public void storeEntity(Entity entity) {
		htEntities.put(entity.getName(), entity);
	}
	
	public void removeEntity(String name) {
		htEntities.remove(name);
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
	 * Removes all the links 
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
	 */
	public void removeLinkById(String id) {
		Link link = getLinkById(id);
		if ( link != null ) {
			//--- Remove link 
			Entity entity = getEntityByName( link.getSourceTableName() );
			if ( entity != null ) {
				entity.removeLink(link);
			}
		}
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
