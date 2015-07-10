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
package org.telosys.tools.repository.persistence;

import java.io.InputStream;
import java.io.OutputStream;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.repository.conversion.XmlConverter;
import org.telosys.tools.repository.model.RepositoryModel;
import org.telosys.tools.repository.persistence.util.Xml;
import org.w3c.dom.Document;

public abstract class GenericPersistenceManager implements PersistenceManager {

	private final TelosysToolsLogger _logger;

	/**
	 * Constructor
	 * 
	 * @param logger
	 */
	public GenericPersistenceManager(TelosysToolsLogger logger) {
		_logger = logger;
	}

	private void log(String msg) {
		if ( _logger != null ) {
			_logger.log("[LOG] " + this.getClass().getName() + " : " + msg);
		}
	}

//	/**
//	 * Loads the repository model from the given XML file
//	 * 
//	 * @param is
//	 * @return
//	 * @throws TelosysToolsException
//	 * @throws CommandException 
//	 */
//	public RepositoryModel load(InputStream is) throws TelosysToolsException, CommandException {
//		final Document xmlDocument = Xml.load(is);
//
//		// Instanciation parser
//		ICommandManager manager = CommandManager.getCommandManager();
//		
//		// Tables
//		NodeList nlListe = xmlDocument.getElementsByTagName(RepositoryConst.TABLELIST);
//		final Element tableListe = (Element) nlListe.item(0);
//		final RepositoryModel model = RepositoryConst.BASE_WRAPPER.getBase(tableListe);
//
//		// Traitement des tables
//		NodeList nl = xmlDocument.getElementsByTagName(RepositoryConst.TABLE);
//		
//		// Lancement parsing
//		ProcessContext context = new ProcessContext(nl);
//		ICommandContext commandContext = manager.searchCommand(context);
//		ProcessContext resultat = commandContext.runProcess(context, manager);
//		for (Iterator<?> iterator = resultat.getList().iterator(); iterator.hasNext();) {
//			EntityInDbModel entity = (EntityInDbModel) iterator.next();
//			model.storeEntity(entity);
//		}
//
//		return model;
//	}

	/**
	 * Loads the repository model from the given XML file
	 * @param is
	 * @return
	 * @throws TelosysToolsException
	 */
	public RepositoryModel load(InputStream is) throws TelosysToolsException {
		
		log(" . load XML from input stream");
		final Document xmlDocument = Xml.load(is);

		log(" . convert XML to model");
		XmlConverter  xmlConverter = new XmlConverter(_logger);
		RepositoryModel model = xmlConverter.xmlDocumentToModel(xmlDocument);
		
		return model;
	}

//	/**
//	 * Saves the repository model in the given XML file
//	 * 
//	 * @param os
//	 * @param model
//	 * @throws TelosysToolsException
//	 */
//	public void save(OutputStream os, RepositoryModel model) throws TelosysToolsException 
//	{
//		log("save(OutputStream, RepositoryModel)... ");
//		if (model != null) {
//			Document doc = Xml.createDomDocument();
//
//			log(" . generate root tags ");
//
//			Comment comment = doc.createComment(" Telosys Database Repository ");
//			doc.appendChild(comment);
//			
//			Element root = doc.createElement(RepositoryConst.ROOT_ELEMENT);
//
//			Element entitiesElement = RepositoryConst.BASE_WRAPPER.getXmlDesc(model, doc);
//			root.appendChild(entitiesElement);
//			
//			doc.appendChild(root);
//
//			
//			log(" . generate entities tag");
//			addAllEntities(doc, model, entitiesElement);
//
//			log(" . save in repository file");
//			Xml.save(doc, os);
//		} else {
//			throw new TelosysToolsException("Cannot create XML/DOM document");
//		}
//	}
	/**
	 * Saves the repository model in the given XML file
	 * 
	 * @param os
	 * @param model
	 * @throws TelosysToolsException
	 */
	public void save(OutputStream os, RepositoryModel model) throws TelosysToolsException 
	{
		log("save(OutputStream, RepositoryModel)... ");
		if (model != null) {

			log(" . convert model to XML ");
			XmlConverter  xmlConverter = new XmlConverter(_logger);
			Document doc = xmlConverter.modelToXmlDocument(model);
			
			log(" . save XML in output stream");
			Xml.save(doc, os);
		} else {
			throw new TelosysToolsException("Cannot save model : model is null");
		}
	}

	// ---------------------------------------------------------------------------------------

//	private void addAllEntities(Document doc, RepositoryModel model, Element entitiesElement) 
//	{
////		EntityInDbModel[] entities = model.getEntities();
//		EntityInDbModel[] entities = model.getEntitiesArray();
//		for ( EntityInDbModel entity : entities ) {
////			log("entity : " + entity.getName() );
//			log("entity : " + entity.getDatabaseTable() );
//			addEntity(doc, entity, entitiesElement);
//		}
//	}

//	/**
//	 * Adds an entity in the DOM
//	 * 
//	 * @param doc
//	 * @param entity
//	 */
//	private void addEntity(Document doc, EntityInDbModel entity, Element parentElement) 
//	{
//		log("addEntity()");
//		// --- Create XML element "table"
//		Element entityElement = RepositoryConst.ENTITY_WRAPPER.getXmlDesc(entity, doc);
//
//		//doc.getElementsByTagName(RepositoryConst.TABLELIST).item(0).appendChild(entityElement);
//
//		addColumns(doc, entity, entityElement);
//
//		addForeignKeys(doc, entity, entityElement);
//
//		addLinks(doc, entity, entityElement);
//
//		parentElement.appendChild(entityElement);
//	}

//	private void addLinks(Document doc, EntityInDbModel entity, Element entityElement) 
//	{
////		LinkInDbModel[] links = entity.getLinks();
//		LinkInDbModel[] links = entity.getLinksArray();
//		for ( LinkInDbModel link : links )
//		{
//			Element linkElement = RepositoryConst.LINK_WRAPPER.getXmlDesc(link, doc);
//
//			//--- "Join Table" 
////			JoinTableInDbModel joinTable = link.getJoinTable();
//			JoinTableInDbModel joinTable = (JoinTableInDbModel) link.getJoinTable(); // v 3.0.0
//			if ( joinTable != null ) {
//				//--- Add the "JoinTable" element
//				addJoinTable(doc, joinTable, linkElement);
//			}
//			else {
////				JoinColumnsInDbModel joinColumns = link.getJoinColumns();
//				List<JoinColumn> joinColumns = link.getJoinColumns();
//				//--- List of "Join Columns" for this link 
//				addJoinColumns(doc, joinColumns, linkElement) ;
//			}
//			
//			entityElement.appendChild(linkElement);
//		}
//	}
	
////	private void addJoinColumns(Document doc, JoinColumnsInDbModel joinColumns, Element linkElement) 
//	private void addJoinColumns(Document doc, List<JoinColumn> joinColumns, Element linkElement)  // v 3.0.0
//	{
//		log("addJoinColumns...");
//		if ( joinColumns != null ) {
//			log("joinColumns is not null");
////			Element joinColumnsElement = RepositoryConst.JOIN_COLUMNS_WRAPPER.getXmlDesc(joinColumns, doc);
//			Element joinColumnsElement = RepositoryConst.JOIN_COLUMNS_WRAPPER.getXmlDesc(doc); // v 3.0.0
////			for ( JoinColumnInDbModel joinColumn : joinColumns ) 
//			for ( JoinColumn joinColumn : joinColumns ) // v 3.0.0
//			{
//				log("process joinColumn '" + joinColumn.getName() + "'");
//				JoinColumnInDbModel joinColumnInDbModel = (JoinColumnInDbModel) joinColumn ; // v 3.0.0 
////				Element joinColumnElement = RepositoryConst.JOIN_COLUMN_WRAPPER.getXmlDesc(joinColumn, doc);
//				Element joinColumnElement = RepositoryConst.JOIN_COLUMN_WRAPPER.getXmlDesc(joinColumnInDbModel, doc); // v 3.0.0 
//				joinColumnsElement.appendChild(joinColumnElement);
//			}
//			linkElement.appendChild(joinColumnsElement);
//		}
//	}
	
//	private void addJoinTable(Document doc, JoinTableInDbModel joinTable, Element linkElement) 
//	{
//		//--- Add the "JoinTable" element
//		Element joinTableElement = RepositoryConst.JOIN_TABLE_WRAPPER.getXmlDesc(joinTable, doc);
//		
//		//--- Add the "Join Columns" elements
////		JoinColumnsInDbModel joinColumns = joinTable.getJoinColumns();
//		List<JoinColumn> joinColumns = joinTable.getJoinColumns(); // v 3.0.0
//		if ( joinColumns != null ) {
////			Element joinColumnsElement = RepositoryConst.JOIN_COLUMNS_WRAPPER.getXmlDesc(joinColumns, doc);
//			Element joinColumnsElement = RepositoryConst.JOIN_COLUMNS_WRAPPER.getXmlDesc(doc); // v 3.0.0
////			for ( JoinColumnInDbModel joinColumn : joinColumns ) 
//			for ( JoinColumn joinColumn : joinColumns ) // v 3.0.0
//			{
//				JoinColumnInDbModel joinColumnInDbModel = (JoinColumnInDbModel) joinColumn ; // v 3.0.0 
////				Element joinColumnElement = RepositoryConst.JOIN_COLUMN_WRAPPER.getXmlDesc(joinColumn, doc);
//				Element joinColumnElement = RepositoryConst.JOIN_COLUMN_WRAPPER.getXmlDesc(joinColumnInDbModel, doc); // v 3.0.0 
//				joinColumnsElement.appendChild(joinColumnElement);
//			}
//			joinTableElement.appendChild(joinColumnsElement);
//		}
//
//		//--- Add the "Inverse Join Columns" elements
////		InverseJoinColumnsInDbModel inverseJoinColumns = joinTable.getInverseJoinColumns();
//		List<JoinColumn> inverseJoinColumns = joinTable.getJoinColumns(); // v 3.0.0
//		if ( inverseJoinColumns != null ) {
////			Element inverseJoinColumnsElement = RepositoryConst.INVERSE_JOIN_COLUMNS_WRAPPER.getXmlDesc(inverseJoinColumns, doc);
//			Element inverseJoinColumnsElement = RepositoryConst.INVERSE_JOIN_COLUMNS_WRAPPER.getXmlDesc(doc); // v 3.0.0
////			for ( JoinColumnInDbModel joinColumn : inverseJoinColumns ) 
//			for ( JoinColumn joinColumn : inverseJoinColumns ) 
//			{
//				JoinColumnInDbModel joinColumnInDbModel = (JoinColumnInDbModel) joinColumn ; // v 3.0.0 
////				Element joinColumnElement = RepositoryConst.JOIN_COLUMN_WRAPPER.getXmlDesc(joinColumn, doc);
//				Element joinColumnElement = RepositoryConst.JOIN_COLUMN_WRAPPER.getXmlDesc(joinColumnInDbModel, doc); // v 3.0.0
//				inverseJoinColumnsElement.appendChild(joinColumnElement);
//			}
//			joinTableElement.appendChild(inverseJoinColumnsElement);
//		}
//
//		linkElement.appendChild(joinTableElement);
//	}

//	private void addJoinColumn(Document doc, Element element, JoinColumn joinColumn ) {
//		Element joinColumnElement = RepositoryConst.JOIN_COLUMN_WRAPPER.getXmlDesc(joinColumn, doc);
//		element.appendChild(joinColumnElement);
//	}
	
//	private void addForeignKeys(Document doc, EntityInDbModel entity, Element table) {
//		// --- Foreign Keys
////		Collection<ForeignKeyInDbModel> foreignKeys = entity.getForeignKeysCollection();
//		ForeignKeyInDbModel[] foreignKeys = entity.getForeignKeys();
//		for ( ForeignKeyInDbModel foreignKey : foreignKeys ) {
//			final Element fkElement = RepositoryConst.FOREIGNKEY_WRAPPER.getXmlDesc(foreignKey, doc);
//
//			this.addForeignKeyColumns(doc, foreignKey, fkElement);
//
//			table.appendChild(fkElement);
//		}
//	}

//	private void addForeignKeyColumns(Document doc, ForeignKeyInDbModel foreignKey, final Element fkElement) {
//		// --- Foreign Key Columns
//		ForeignKeyColumnInDbModel[] foreignKeyColumns = foreignKey.getForeignKeyColumns();
//		for (int i = 0; i < foreignKeyColumns.length; i++) {
//			ForeignKeyColumnInDbModel foreignKeyColumn = foreignKeyColumns[i];
//			Element fkColElement = RepositoryConst.FOREIGNKEY_COLUMN_WRAPPER.getXmlDesc(foreignKeyColumn, doc);
//			fkElement.appendChild(fkColElement);
//		}
//	}

//	private void addColumns(Document doc, EntityInDbModel entity, Element parentElement) {
//		// --- Columns/attributes
////		Collection<AttributeInDbModel> colcols = entity.getColumnsCollection();
////		for ( AttributeInDbModel column : colcols ) {
//		for ( AttributeInDbModel column : entity.getAttributesArray() ) {
//			final Element colonne = RepositoryConst.COLUMN_WRAPPER.getXmlDesc(column, doc);
//
//			if (column.getGeneratedValue() != null) {
//				final Element gv = RepositoryConst.GENERATED_VALUE_WRAPPER.getXmlDesc(column.getGeneratedValue(), doc);
//				colonne.appendChild(gv);
//			}
//			if (column.getSequenceGenerator() != null) {
//				final Element sg = RepositoryConst.SEQUENCE_GENERATOR_WRAPPER.getXmlDesc(column.getSequenceGenerator(), doc);
//				colonne.appendChild(sg);
//			}
//			if (column.getTableGenerator() != null) {
//				final Element tg = RepositoryConst.TABLE_GENERATOR_WRAPPER.getXmlDesc(column.getTableGenerator(), doc);
//				colonne.appendChild(tg);
//			}
//			parentElement.appendChild(colonne);
//		}
//	}

//	private void joinFkToXml(Document doc, final Element elem, JoinFK[] joinFKs) {
//		for (int i = 0; i < joinFKs.length; i++) {
//			JoinFK joinFK = joinFKs[i];
//			Element element = RepositoryConst.JOIN_FK_WRAPPER.getXmlDesc(joinFK, doc);
//			
//			this.addJoinColumns(doc, joinFK, element);
//			
//			elem.appendChild(element);
//		}
//	}

//	private void addJoinColumns(Document doc, JoinFK joinFK, Element element) {
//		JoinColumn[] JoinColumns = joinFK.getJoinColumns();
//		
//		for (int j = 0; j < JoinColumns.length; j++) {
//			JoinColumn joinColumn = JoinColumns[j];
//			Element joinColumnElement = RepositoryConst.JOIN_COLUMN_WRAPPER.getXmlDesc(joinColumn, doc);
//			element.appendChild(joinColumnElement);
//		}
//	}
	
}
