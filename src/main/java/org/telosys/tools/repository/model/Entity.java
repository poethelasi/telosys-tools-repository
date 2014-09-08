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
import java.util.Hashtable;

/**
 * "Entity" model class ( a Database Table mapped to a Java Class ) <br>
 * An entity contains : <br>
 * - 1..N columns <br>
 * - 0..N foreign keys <br>
 * - 0..N links <br>
 * 
 * This class is "Comparable" ( based on the entity name )
 * 
 * @author Laurent Guerin
 *
 */
public class Entity implements Comparable<Entity>
{

	private String name ;
	
	private String catalog ; 
	
	private String schema ; 

	private String databaseType ; // v 2.0.7 #LGU
	
	private String beanJavaClass ;
	
// REMOVED in ver 2.0.7
//	private String listJavaClass ; 
//	
//	private String daoJavaClass ;
//
//	private String converterJavaClass ;
	
	private Hashtable<String,Column>     columns     = new Hashtable<String,Column>() ; 

	private Hashtable<String,ForeignKey> foreignKeys = new Hashtable<String,ForeignKey>() ;

	private Hashtable<String,Link>       links       = new Hashtable<String,Link>() ;

	/**
	 * Returns true if the entity can be considered as a "Join Table" <br>
	 * Conditions : <br>
	 * . the entity has 2 Foreign Keys <br>
	 * . all the columns are in the Primary Key <br>
	 * . all the columns are in a Foreign Key <br>
	 * 
	 * @return
	 */
	public boolean isJoinTable() 
	{
		//--- Check if there are 2 FK
		if ( foreignKeys.size() != 2 ) {
			return false;
		} 
				
		//--- Check if all the columns are in the Primary Key
		for ( Column column : getColumns() ) {
			if ( ! column.isPrimaryKey() ) {
				return false ;
			}
		}
		
		//--- Check if all the columns are in a Foreign Key
		for ( Column column : getColumns() ) {
			if ( ! column.isForeignKey() ) {
				return false ;
			}
		}

		return true ;
	}

	//--------------------------------------------------------------------------
	
	/**
	 * Returns the name of the entity ( i.e. the database table name )
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	//--------------------------------------------------------------------------
	
	/**
	 * Returns the database schema of the entity 
	 * @return
	 * @since 1.0
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * Set the database schema of the entity 
	 * @param s
	 * @since 1.0
	 */
	public void setSchema(String s) {
		this.schema = s;
	}
	
	//--------------------------------------------------------------------------
	
	/**
	 * Returns the database type of the entity ( TABLE, VIEW, ... )
	 * @return
	 * @since 2.0.7
	 */
	public String getDatabaseType() {
		return databaseType;
	}

	/**
	 * Set the database type of the entity ( TABLE, VIEW, ... )
	 * @param s
	 * @since 2.0.7
	 */
	public void setDatabaseType(String s) {
		this.databaseType = s;
	}
	
	//--------------------------------------------------------------------------
	
	/**
	 * Returns the database catalog of the entity 
	 * @return
	 * @since 1.0
	 */
	public String getCatalog() {
		return catalog ;
	}

	/**
	 * Set the database catalog of the entity 
	 * @param s
	 * @since 1.0
	 */
	public void setCatalog(String s) {
		this.catalog = s;
	}
	
	//--------------------------------------------------------------------------
	
	/**
	 * Returns the short name of the VO bean Java Class ( without the package ) 
	 * Example : "Book" or "BookVO"
	 * @return
	 */
	public String getBeanJavaClass() {
		return beanJavaClass;
	}

	public void setBeanJavaClass(String beanJavaClass) {
		this.beanJavaClass = beanJavaClass;
	}

	//--------------------------------------------------------------------------
//	
//	/**
//	 * Returns the short name of the bean convertor Java Class ( without the package ) <br>
//	 * Example : "BookMapper"
//	 * @return
//	 */
//	public String getConverterJavaClass() {
//		return converterJavaClass;
//	}
//
//	public void setConverterJavaClass(String covertorJavaClass) {
//		this.converterJavaClass = covertorJavaClass;
//	}

	//--------------------------------------------------------------------------
//	
//	/**
//	 * Returns the short name of the bean DAO Java Class ( without the package ) <br>
//	 * Example : "BookDAO"
//	 * @return
//	 */
//	public String getDaoJavaClass() {
//		return daoJavaClass;
//	}
//
//	public void setDaoJavaClass(String daoJavaClass) {
//		this.daoJavaClass = daoJavaClass;
//	}

	//--------------------------------------------------------------------------
	
//	/**
//	 * Returns the short name of the bean list Java Class ( without the package ) <br>
//	 * Example : "BookList"
//	 * @return
//	 */
//	public String getListJavaClass() {
//		return listJavaClass;
//	}
//
//	public void setListJavaClass(String listJavaClass) {
//		this.listJavaClass = listJavaClass;
//	}

	//--------------------------------------------------------------------------
	// COLUMNS management
	//--------------------------------------------------------------------------
	
	/**
	 * Returns an array containing all the columns of the entity<br>
	 * The columns are sorted by ordinal position (the original database order).
	 * 
	 * @return
	 */
	public Column[] getColumns()
	{
		//return (Column[]) columns.values().toArray(new Column[columns.size()]);
		Column[] cols = (Column[]) ( columns.values().toArray( new Column[columns.size()] ) );
		Arrays.sort(cols);
		return cols ;
	}

	/**
	 * Returns a collection of all the columns of the entity.<br>
	 * The columns are sorted by ordinal position (the original database order).
	 * 
	 * @return
	 */
	public Collection<Column> getColumnsCollection()
	{
		//return this.columns.values();
		Column[] cols = getColumns();
		return Arrays.asList(cols);
		
	}

	public void storeColumn(Column column)
	{
		columns.put(column.getDatabaseName(), column);
	}

	public Column getColumn(String name)
	{
		return (Column) columns.get(name);
	}

	public void removeColumn(Column column)
	{
		columns.remove(column.getDatabaseName());
	}

	//--------------------------------------------------------------------------
	// FOREIGN KEYS management
	//--------------------------------------------------------------------------
	
	/**
	 * Returns an array of all the foreign keys of the entity (table).<br>
	 * The foreign keys are sorted by name.
	 * @return
	 */
	public ForeignKey[] getForeignKeys()
	{
		ForeignKey[] array = (ForeignKey[]) foreignKeys.values().toArray(new ForeignKey[foreignKeys.size()]);
		Arrays.sort(array);
		return array ;
		
	}
	
	/**
	 * Returns a collection of all the foreign keys of the entity (table).<br>
	 * The foreign keys are sorted by name.
	 * @return
	 */
	public Collection<ForeignKey> getForeignKeysCollection()
	{
		//return foreignKeys.values() ;
		ForeignKey[] array = getForeignKeys();
		return Arrays.asList(array);
		
	}
	
	public void storeForeignKey(ForeignKey foreignKey)
	{
		foreignKeys.put(foreignKey.getName(), foreignKey);
	}
	
	public ForeignKey getForeignKey(String name)
	{
		return (ForeignKey) foreignKeys.get(name);
	}
	
	public void removeForeignKey(ForeignKey foreignKey)
	{
		foreignKeys.remove(foreignKey.getName() );
	}
	
	//--------------------------------------------------------------------------
	// LINKS management
	//--------------------------------------------------------------------------
	
	/**
	 * Returns all the links of the entity
	 * @return
	 */
	public Link[] getLinks()
	{
		return (Link[]) links.values().toArray(new Link[links.size()]);
	}
	
	/**
	 * Returns all the links of the entity
	 * @return
	 */
	public Collection<Link> getLinksCollection()
	{
		return links.values() ;
	}
	
	/**
	 * Store (add or update the given link)
	 * @param link
	 */
	public void storeLink(Link link)
	{
		links.put(link.getId(), link);
	}
	
	/**
	 * Get a link by its id
	 * @param id
	 * @return
	 */
	public Link getLink(String id)
	{
		return (Link) links.get(id);
	}
	
	/**
	 * Remove the given link from the entity
	 * @param link
	 */
	public void removeLink(Link link)
	{
		links.remove( link.getId() );
	}

	/**
	 * Remove all the links from the entity
	 */
	public void removeAllLinks()
	{
		links.clear();
	}

	//--------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(T)
	 */
	//public int compareTo(Object o) {
	public int compareTo(Entity other) {
		if ( other != null )
		{
			//Entity other = (Entity) o;
			String sThisName = this.getName() ;
			String sOtherName = other.getName();
			if ( sThisName != null && sOtherName != null )
			{
				return this.name.compareTo(other.getName());
			}
		}
		return 0;
	}
	
}
