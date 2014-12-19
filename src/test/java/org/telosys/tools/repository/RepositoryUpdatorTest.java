package org.telosys.tools.repository;

import java.io.ByteArrayOutputStream;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.dbcfg.DatabaseConfiguration;
import org.telosys.tools.commons.dbcfg.DatabasesConfigurations;
import org.telosys.tools.commons.dbcfg.DbConfigManager;
import org.telosys.tools.commons.jdbc.ConnectionManager;
import org.telosys.tools.repository.changelog.ChangeLog;
import org.telosys.tools.repository.changelog.ChangeOnEntity;
import org.telosys.tools.repository.changelog.ChangeOnForeignKey;
import org.telosys.tools.repository.changelog.ChangeType;
import org.telosys.tools.repository.model.Column;
import org.telosys.tools.repository.model.Entity;
import org.telosys.tools.repository.model.Link;
import org.telosys.tools.repository.model.RepositoryModel;
import org.telosys.tools.repository.rules.RepositoryRules;
import org.telosys.tools.repository.rules.RepositoryRulesProvider;

public class RepositoryUpdatorTest {
	
	private class UpdateResult {
		private final RepositoryModel repositoryModel ;
		private final ChangeLog changeLog ;
		public UpdateResult(RepositoryModel repositoryModel, ChangeLog changeLog) {
			super();
			this.repositoryModel = repositoryModel;
			this.changeLog = changeLog;
		}
		public RepositoryModel getRepositoryModel() {
			return repositoryModel;
		}
		public ChangeLog getChangeLog() {
			return changeLog;
		}
		
	}
	
	private DatabaseConfiguration getDatabaseConfiguration(int databaseId) throws TelosysToolsException {
		System.out.println("================================================================================");
		DbConfigManager dbConfigManager = new DbConfigManager( FileUtil.getFileByClassPath("/dbcfg/databases-test-H2.dbcfg") );
		DatabasesConfigurations databasesConfigurations = dbConfigManager.load();
		DatabaseConfiguration dbConfig = databasesConfigurations.getDatabaseConfiguration(databaseId);
		if ( dbConfig == null ) {
			throw new RuntimeException("No database configuration for database id #" + databaseId);
		}
		return dbConfig ;
	}

	private void printModel (RepositoryModel model)  {
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
	
	private void checkJavaName(String javaName1, String javaName2, String expectedName1, String expectedName2 ) {
		System.out.println("Check Java names : '" + javaName1 + "', '" + javaName2 + "' expected : '" + expectedName1 + "', '" + expectedName2 +"'");
		Assert.assertTrue( expectedName1.equals( javaName1 ) || expectedName2.equals( javaName1 ) );
		Assert.assertTrue( expectedName1.equals( javaName2 ) || expectedName2.equals( javaName2 ) );
		if ( expectedName1.equals( javaName1 ) ) {
			Assert.assertEquals(expectedName2, javaName2);
		}
		else if ( expectedName2.equals( javaName1 ) ) {
			Assert.assertEquals(expectedName1, javaName2);
		}
		else {
			Assert.fail("Unexpected jave name for link '" + javaName1 + "' or '" + javaName2 + "'");
		}
	}
	
	private void jdbcUrlWithAlterScript(DatabaseConfiguration databaseConfiguration, int testId) {
		String jdbcUrl = databaseConfiguration.getJdbcUrl();
		System.out.println("JDBC URL 1 : " + jdbcUrl );
		int index = jdbcUrl.indexOf(";INIT");
		String shortJdbcUrl = jdbcUrl.substring(0, index);
		System.out.println("JDBC URL 2 : " + shortJdbcUrl );
		String newJdbcUrl = shortJdbcUrl+";INIT=RUNSCRIPT FROM 'classpath:sql/alterdb" + testId + ".sql'" ;
		System.out.println("JDBC URL 3 : " + newJdbcUrl );
		databaseConfiguration.setJdbcUrl(newJdbcUrl);
	}
	
	private RepositoryGenerator getRepositoryGenerator() throws TelosysToolsException {
		
		TelosysToolsLogger logger = new ConsoleLogger() ;
		ConnectionManager connectionManager = new ConnectionManager(logger);
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;
		return new RepositoryGenerator(connectionManager, rules, logger);
	}
	
	private UpdateResult generateThenUpdateModel(int databaseId) throws TelosysToolsException {
		TelosysToolsLogger logger = new ConsoleLogger() ;
		DatabaseConfiguration databaseConfiguration = getDatabaseConfiguration(databaseId);
		System.out.println("DatabaseConfiguration ready (id="+databaseId+")");
		RepositoryGenerator repositoryGenerator = getRepositoryGenerator() ;
		System.out.println("Repository generation... ");
		RepositoryModel repositoryModel = repositoryGenerator.generate( databaseConfiguration );
		System.out.println("Repository update... ");
		ChangeLog changeLog = changeDatabaseSchemaAndUpdateRepositoryModel(databaseConfiguration, databaseId, repositoryModel, logger);
		new UpdateResult(repositoryModel, changeLog);
		return new UpdateResult(repositoryModel, changeLog);
	}
	
	private ChangeLog changeDatabaseSchemaAndUpdateRepositoryModel(DatabaseConfiguration databaseConfiguration, 
				int testId, RepositoryModel repositoryModel, TelosysToolsLogger logger) throws TelosysToolsException {
		//--- Set "alterdbX.sql" for database schema update
		jdbcUrlWithAlterScript(databaseConfiguration, testId);
		
		//--- Update repository model after database schema update
		ConnectionManager connectionManager = new ConnectionManager(logger);
		ByteArrayOutputStream baosUpdateLog = new ByteArrayOutputStream();
		UpdateLogWriter updateLogger = new UpdateLogWriter(baosUpdateLog);
		
		RepositoryUpdator repositoryUpdator = new RepositoryUpdator(connectionManager,
				RepositoryRulesProvider.getRepositoryRules(), new ConsoleLogger(), updateLogger);
		
		//int changesCount = repositoryUpdator.updateRepository(databaseConfiguration, repositoryModel);
		ChangeLog changeLog = repositoryUpdator.updateRepository(databaseConfiguration, repositoryModel);
		System.out.println(baosUpdateLog.toString());
		System.out.println("Repo entities count = " + repositoryModel.getNumberOfEntities() );
		System.out.println("ChangeLog number of entities = " + changeLog.getNumberOfEntities() );
		System.out.println("ChangeLog number of entities created = " + changeLog.getNumberOfEntitiesCreated() );
		System.out.println("ChangeLog number of entities updated = " + changeLog.getNumberOfEntitiesUpdated() );
		System.out.println("ChangeLog number of entities deleted = " + changeLog.getNumberOfEntitiesDeleted() );
		return changeLog ;
	}
	
	@Test
	public void test1() throws TelosysToolsException {
		UpdateResult result = generateThenUpdateModel(1);
		ChangeLog changeLog = result.getChangeLog();
		RepositoryModel repositoryModel = result.getRepositoryModel();
		
		//--- Check changes
		Assert.assertTrue(changeLog.getNumberOfEntities() == 3 );
		Assert.assertTrue(changeLog.getNumberOfEntitiesCreated() == 1 );
		Assert.assertTrue(changeLog.getNumberOfEntitiesUpdated() == 1 );
		Assert.assertTrue(changeLog.getNumberOfEntitiesDeleted() == 1 );
		Assert.assertTrue(repositoryModel.getNumberOfEntities() == 2 );
		
		Entity badgeEntity = repositoryModel.getEntityByName("BADGE") ;
		Assert.assertNotNull( badgeEntity );
		Assert.assertNotNull( badgeEntity.getColumn("CODE") );
		Assert.assertNotNull( badgeEntity.getColumn("NAME") );

		Entity countryEntity = repositoryModel.getEntityByName("COUNTRY") ;
		Assert.assertNotNull( countryEntity );
		Assert.assertNotNull( countryEntity.getColumn("BADGE_CODE") );
		Assert.assertTrue( countryEntity.getForeignKeys().length == 1 );
	}

	@Test
	public void test2() throws TelosysToolsException {
		UpdateResult result = generateThenUpdateModel(2);
		ChangeLog changeLog = result.getChangeLog();
		RepositoryModel repositoryModel = result.getRepositoryModel();
		
		//--- Check changes (NO CHANGE)
		Assert.assertTrue(changeLog.getNumberOfEntities() == 0 );
		Assert.assertTrue(changeLog.getNumberOfEntitiesCreated() == 0 );
		Assert.assertTrue(changeLog.getNumberOfEntitiesUpdated() == 0 );
		Assert.assertTrue(changeLog.getNumberOfEntitiesDeleted() == 0 );
		Assert.assertTrue(repositoryModel.getNumberOfEntities() == 2 );
	}

	@Test
	public void test5() throws TelosysToolsException {
		UpdateResult result = generateThenUpdateModel(5);
		ChangeLog changeLog = result.getChangeLog();
		RepositoryModel repositoryModel = result.getRepositoryModel();
		
		checkChangeLog(changeLog, repositoryModel );
		printChangeLog(changeLog ) ;
		
		//--- Check changes (NO CHANGE)
		Assert.assertEquals(4, changeLog.getNumberOfEntities() );
		Assert.assertEquals(2, changeLog.getNumberOfEntitiesCreated() );
		Assert.assertEquals(2, changeLog.getNumberOfEntitiesUpdated() );
		Assert.assertEquals(0, changeLog.getNumberOfEntitiesDeleted() );
		Assert.assertEquals(4, repositoryModel.getNumberOfEntities() );
		
		//--- Links in the updated model 
		printLinks(repositoryModel.getEntityByName("STUDENT").getLinks());
		printLinks(repositoryModel.getEntityByName("TEACHER").getLinks());
		printLinks(repositoryModel.getEntityByName("BADGE").getLinks());
		printLinks(repositoryModel.getEntityByName("TEAM").getLinks());
		
		List<ChangeOnEntity> entitiesUpdate = changeLog.getChangesByType(ChangeType.UPDATED);
		Assert.assertEquals(2, entitiesUpdate.size() );
		ChangeOnEntity changeOnEntity = entitiesUpdate.get(0);
		Entity entityBefore = changeOnEntity.getEntityBefore();
		Assert.assertNotNull(entityBefore);
		Assert.assertEquals(1, entityBefore.getForeignKeys().length );
		Assert.assertEquals(1, entityBefore.getLinks().length ); // Inverse side link
		
		Entity entityAfter = changeOnEntity.getEntityAfter();
//		Assert.assertNotNull(entityAfter);
//		Assert.assertEquals(1, entityAfter.getForeignKeys().length);
//		Assert.assertEquals(2, entityAfter.getLinks().length ); // Inverse side link
//		Assert.assertEquals(1, repositoryModel.getEntityByName("STUDENT").getLinks().length ); // Owning side unchanged
//		Assert.assertEquals(2, repositoryModel.getEntityByName("TEACHER").getLinks().length ); // 2 inv (Student+Team) + Owning side(badge)
//		Assert.assertEquals(0, repositoryModel.getEntityByName("BADGE").getLinks().length ); // Inverse side
	}

	/**
	 * Test "one entity DELETED with FK" (here "STUDENT" is deleted)
	 * @throws TelosysToolsException
	 */
	@Test
	public void test6() throws TelosysToolsException {
		UpdateResult result = generateThenUpdateModel(6);
		ChangeLog changeLog = result.getChangeLog();
		RepositoryModel repositoryModel = result.getRepositoryModel();
		
		checkChangeLog(changeLog, repositoryModel );
		printChangeLog(changeLog ) ;
		
		//--- Check changes (NO CHANGE)
		Assert.assertEquals(1, changeLog.getNumberOfEntities() );
		Assert.assertEquals(0, changeLog.getNumberOfEntitiesCreated() );
		Assert.assertEquals(0, changeLog.getNumberOfEntitiesUpdated() );
		Assert.assertEquals(1, changeLog.getNumberOfEntitiesDeleted() );
		Assert.assertEquals(1, repositoryModel.getNumberOfEntities() );
		
		Assert.assertNull(repositoryModel.getEntityByName("STUDENT")); // Deleted
		Assert.assertNotNull(repositoryModel.getEntityByName("TEACHER")); // Still present
		
		//--- Links in the updated model 
		printLinks(repositoryModel.getEntityByName("TEACHER").getLinks());
		
		List<ChangeOnEntity> entitiesDeleted = changeLog.getChangesByType(ChangeType.DELETED);
		Assert.assertEquals(1, entitiesDeleted.size() );
		ChangeOnEntity changeOnEntity = entitiesDeleted.get(0);
		Entity entityDeleted = changeOnEntity.getEntityDeleted();
		Assert.assertNotNull(entityDeleted);
		Assert.assertEquals(1, entityDeleted.getForeignKeys().length );
		
		Assert.assertEquals(0, repositoryModel.getEntityByName("TEACHER").getLinks().length ); // 0 Link 
	}
	
	/**
	 * Test "one entity CREATED with FK" (here "TEAM" is created)
	 * @throws TelosysToolsException
	 */
	@Test
	public void test8() throws TelosysToolsException {
		UpdateResult result = generateThenUpdateModel(8);
		ChangeLog changeLog = result.getChangeLog();
		RepositoryModel repositoryModel = result.getRepositoryModel();
		
		checkChangeLog(changeLog, repositoryModel );
		printChangeLog(changeLog ) ;
		
		//--- Check changes 
		Assert.assertEquals(1, changeLog.getNumberOfEntities() ); // 1 change
		Assert.assertEquals(1, changeLog.getNumberOfEntitiesCreated() );
		Assert.assertEquals(0, changeLog.getNumberOfEntitiesUpdated() );
		Assert.assertEquals(0, changeLog.getNumberOfEntitiesDeleted() );
		Assert.assertEquals(3, repositoryModel.getNumberOfEntities() ); // 3 in the model
		
		//--- Links in the updated model 
		printLinks(repositoryModel.getEntityByName("STUDENT").getLinks()); 
		printLinks(repositoryModel.getEntityByName("TEACHER").getLinks());
		printLinks(repositoryModel.getEntityByName("TEAM").getLinks()); // the new entity
		
		List<ChangeOnEntity> changesOnEntitiesCreated = changeLog.getChangesByType(ChangeType.CREATED);
		Assert.assertEquals(1, changesOnEntitiesCreated.size() );
		ChangeOnEntity changeOnEntity = changesOnEntitiesCreated.get(0);
		Entity entityCreated = changeOnEntity.getEntityCreated();
		Assert.assertNotNull(entityCreated);
		Assert.assertEquals(1, entityCreated.getForeignKeys().length );
		Assert.assertEquals(1, entityCreated.getLinks().length ); // Inverse side link

		//--- Links in the updated model
		
		//--- "STUDENT" links (unchanged)
		Link[] studentLinks = repositoryModel.getEntityByName("STUDENT").getLinks();
		Assert.assertEquals(1, studentLinks.length ); 
		Assert.assertEquals(true, studentLinks[0].isOwningSide() );
		Assert.assertEquals(true, studentLinks[0].isTypeManyToOne() );
		Assert.assertEquals(true, studentLinks[0].isBasedOnForeignKey() );
		Assert.assertEquals("TEACHER", studentLinks[0].getTargetTableName() );
		
		//--- "TEAM" links (created with the entity)
		Link[] teamLinks = repositoryModel.getEntityByName("TEAM").getLinks();
		Assert.assertEquals(1, teamLinks.length ); 
		Assert.assertEquals(true, teamLinks[0].isOwningSide() );
		Assert.assertEquals(true, teamLinks[0].isTypeManyToOne() );
		Assert.assertEquals(true, teamLinks[0].isBasedOnForeignKey() );
		Assert.assertEquals("TEACHER", teamLinks[0].getTargetTableName() );

		//--- "TEACHER" links : one more link ( inverse side / list of students )
		Link[] teacherLinks = repositoryModel.getEntityByName("TEACHER").getLinks();
		Assert.assertEquals(2, teacherLinks.length ); 
		
		List<Link> linksToStudent = repositoryModel.getEntityByName("TEACHER").getLinksTo("STUDENT");
		Assert.assertEquals(1, linksToStudent.size() ); 
		Link linkToStudent = linksToStudent.get(0);
		Assert.assertEquals(true, linkToStudent.isInverseSide() );
		Assert.assertEquals(true, linkToStudent.isTypeOneToMany() );
		Assert.assertEquals("STUDENT", linkToStudent.getTargetTableName() );

		List<Link> linksToTeam = repositoryModel.getEntityByName("TEACHER").getLinksTo("TEAM");
		Assert.assertEquals(1, linksToTeam.size() ); 
		Link linkToTeam = linksToTeam.get(0);
		Assert.assertEquals(true, linkToTeam.isInverseSide() );
		Assert.assertEquals(true, linkToTeam.isTypeOneToMany() );
		Assert.assertEquals("TEAM", linkToTeam.getTargetTableName() );
	}

	//===================================================================================================
	private void checkEntitiesCreated(ChangeLog changeLog) {
		List<ChangeOnEntity> entitiesUpdate = changeLog.getChangesByType(ChangeType.UPDATED);
		Assert.assertTrue(entitiesUpdate.size() == 1 );
		ChangeOnEntity changeOnEntity = entitiesUpdate.get(0);
		Entity entityBefore = changeOnEntity.getEntityBefore();
		Assert.assertNotNull(entityBefore);
		Assert.assertTrue(entityBefore.getForeignKeys().length == 0);
		Assert.assertTrue(entityBefore.getLinks().length == 1); // Inverse side link
	}
	
	private void checkChangeLog(ChangeLog changeLog, RepositoryModel repositoryModel ) {
		for ( ChangeOnEntity change : changeLog.getChanges() ) {
			switch ( change.getChangeType() ) {
			case CREATED :
				Assert.assertTrue( repositoryModel.getEntityByName(change.getEntityCreated().getName()) == change.getEntityCreated() );
				break;
			case UPDATED :
				Assert.assertTrue( repositoryModel.getEntityByName(change.getEntityAfter().getName()) == change.getEntityAfter() );
				break;
			case DELETED :
				Assert.assertTrue( repositoryModel.getEntityByName(change.getEntityDeleted().getName()) == null );
				break;
			}
		}
	}

	private void printLinks(Link[] links) {
		for ( Link link : links ) {
			System.out.println(" . Link : "  + link);
			System.out.println(" . " + link.getJavaFieldName() );
		}
	}

	private void printChangeLog(ChangeLog changeLog ) {
		System.out.println("CHANGE LOG : " );
		System.out.println("Date : " + changeLog.getDate() );
		System.out.println("Number of entities : " + changeLog.getNumberOfEntities() );
		System.out.println("Number of entities created : " + changeLog.getNumberOfEntitiesCreated() );
		System.out.println("Number of entities updated : " + changeLog.getNumberOfEntitiesUpdated() );
		System.out.println("Number of entities deleted : " + changeLog.getNumberOfEntitiesDeleted() );
		
		for ( ChangeOnEntity change : changeLog.getChanges() ) {
			System.out.println("- Entity : " + change.getEntityName() + " change type '" + change.getChangeType() + "'");
			System.out.println(" before = " + change.getEntityBefore());
			System.out.println(" after  = " + change.getEntityAfter());
			for ( ChangeOnForeignKey changeOnFK : change.getChangesOnForeignKey() ) {
				System.out.println("- FK : " + changeOnFK.getForeignKeyName() + " " + changeOnFK.getChangeType() );
			}
		}
	}
	
}
