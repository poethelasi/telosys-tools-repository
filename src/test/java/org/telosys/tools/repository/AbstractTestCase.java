package org.telosys.tools.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;

import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.jdbc.ConnectionManager;
import org.telosys.tools.repository.changelog.ChangeLog;
import org.telosys.tools.repository.model.Column;
import org.telosys.tools.repository.model.Entity;
import org.telosys.tools.repository.model.Link;
import org.telosys.tools.repository.model.RepositoryModel;
import org.telosys.tools.repository.rules.RepositoryRules;
import org.telosys.tools.repository.rules.RepositoryRulesProvider;

public abstract class AbstractTestCase {
	
	protected final static int DATABASE_ID_1 = 1 ;
	
	protected void printSeparator(String s ) {
		System.out.println("==================================================================================================");
		System.out.println("=== " + s );
		System.out.println("==================================================================================================");
	}
	
	protected void printEntity(Entity entity) {
		System.out.println("ENTITY : name = '" + entity.getName() 
				+ "',  class = '" + entity.getBeanJavaClass() 
				+ "',  nb columns = " + entity.getColumns().length
				+ ",  nb FK = " + entity.getForeignKeys().length
				+ ",  nb LINKS = " + entity.getLinks().length );
	}

	protected void printModel (RepositoryModel model)  {
		System.out.println("Model generated : " );
		System.out.println("Database ID = " + model.getDatabaseId() );
		
		System.out.println("Number of entities = " + model.getNumberOfEntities() );
		String[] entitiesNames = model.getEntitiesNames();
		for ( String name : entitiesNames ) {
			System.out.println("Entity name = '" + name + "'");
			Entity entity = model.getEntityByName(name);
			Column[] columns = entity.getColumns();
			for ( Column c : columns ) {
				System.out.println(" . Column : " + c );
			}
			Link[] links = entity.getLinks();
			for ( Link link : links ) {
				System.out.println(" . Link : "  + link);
				System.out.println(" . " + link.getJavaFieldName() );
			}
		}
	}
	
	/**
	 * Initialize an database in memory using the given SQL Script ID, <br>
	 * then generate the RepositoryModel from this Database
	 * 
	 * @param sqlScriptId
	 * @return
	 * @throws TelosysToolsException
	 */
	protected RepositoryModel generateRepositoryModel(int sqlScriptId) throws TelosysToolsException {
		
		System.out.println("Database initialization... ");
		DatabaseInMemory databaseInMemory = new DatabaseInMemory(DATABASE_ID_1);
		databaseInMemory.executeSqlInit(sqlScriptId);
		
		System.out.println("Repository generation... ");
		RepositoryGenerator repositoryGenerator = getRepositoryGenerator() ;
		RepositoryModel repositoryModel = repositoryGenerator.generate( databaseInMemory.getDatabaseConfiguration() );
		
		databaseInMemory.close();
		
		return repositoryModel ;
	}
	
	/**
	 * Initialize an database in memory using the given SQL Script ID, <br>
	 * then generate the RepositoryModel from this Database, <br>
	 * then change the database schema using "alter" SQL Script for the given script ID, <br>
	 * then update the RepositoryModel
	 * 
	 * @param sqlScriptId
	 * @return
	 * @throws TelosysToolsException
	 */
	protected UpdateResult generateAndUpdateRepositoryModel(int sqlScriptId) throws TelosysToolsException {
		
		System.out.println("Database initialization... ");
		DatabaseInMemory databaseInMemory = new DatabaseInMemory(DATABASE_ID_1);
		databaseInMemory.executeSqlInit(sqlScriptId);
		
		System.out.println("Repository model generation... ");
		RepositoryGenerator repositoryGenerator = getRepositoryGenerator() ;
		RepositoryModel repositoryModel = repositoryGenerator.generate( databaseInMemory.getDatabaseConfiguration() );
		
		System.out.println("Database changes... ");
		databaseInMemory.executeSqlAlter(sqlScriptId);
		
		System.out.println("Repository model update... ");
		ByteArrayOutputStream baosUpdateLog = new ByteArrayOutputStream();
		RepositoryUpdator repositoryUpdator = getRepositoryUpdator(baosUpdateLog);

		ChangeLog changeLog = repositoryUpdator.updateRepository(databaseInMemory.getDatabaseConfiguration(), repositoryModel);
		
		System.out.println(baosUpdateLog.toString());
		System.out.println("Repo entities count = " + repositoryModel.getNumberOfEntities() );
		System.out.println("ChangeLog number of entities = " + changeLog.getNumberOfEntities() );
		System.out.println("ChangeLog number of entities created = " + changeLog.getNumberOfEntitiesCreated() );
		System.out.println("ChangeLog number of entities updated = " + changeLog.getNumberOfEntitiesUpdated() );
		System.out.println("ChangeLog number of entities deleted = " + changeLog.getNumberOfEntitiesDeleted() );
		
		databaseInMemory.close();
		
		return new UpdateResult(repositoryModel, changeLog);
	}
	
	private RepositoryGenerator getRepositoryGenerator() throws TelosysToolsException {
		
		TelosysToolsLogger logger = new ConsoleLogger() ;
		ConnectionManager connectionManager = new ConnectionManager(logger);
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;
		return new RepositoryGenerator(connectionManager, rules, logger);
	}
	
	private RepositoryUpdator getRepositoryUpdator(ByteArrayOutputStream baosUpdateLog) throws TelosysToolsException {
		
		TelosysToolsLogger logger = new ConsoleLogger() ;
		ConnectionManager connectionManager = new ConnectionManager(logger);
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;
		UpdateLogWriter updateLogger = new UpdateLogWriter( baosUpdateLog );
		return new RepositoryUpdator(connectionManager, rules, logger, updateLogger);
	}
	
	protected void checkJavaName(String javaName1, String javaName2, String expectedName1, String expectedName2 ) {
		System.out.println("Check Java names : '" + javaName1 + "', '" + javaName2 + "' expected : '" + expectedName1 + "', '" + expectedName2 +"'");
		assertTrue( expectedName1.equals( javaName1 ) || expectedName2.equals( javaName1 ) );
		assertTrue( expectedName1.equals( javaName2 ) || expectedName2.equals( javaName2 ) );
		if ( expectedName1.equals( javaName1 ) ) {
			assertEquals(expectedName2, javaName2);
		}
		else if ( expectedName2.equals( javaName1 ) ) {
			assertEquals(expectedName1, javaName2);
		}
		else {
			fail("Unexpected jave name for link '" + javaName1 + "' or '" + javaName2 + "'");
		}
	}

}
