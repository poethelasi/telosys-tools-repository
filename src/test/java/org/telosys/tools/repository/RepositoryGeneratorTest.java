package org.telosys.tools.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.repository.model.Entity;
import org.telosys.tools.repository.model.Link;
import org.telosys.tools.repository.model.RepositoryModel;

public class RepositoryGeneratorTest extends AbstractTestCase {

	//private final static int DATABASE_ID_1 = 1 ;

	
//	private Connection getDatabaseConnection(DatabaseConfiguration databaseConfiguration) throws TelosysToolsException {
//		ConnectionManager connectionManager = new ConnectionManager(new ConsoleLogger());
//		return connectionManager.getConnection(databaseConfiguration);
//	}
	
//	private DatabaseConfiguration getDatabaseConfiguration(int databaseId) throws TelosysToolsException {
//		System.out.println("================================================================================");
//		DbConfigManager dbConfigManager = new DbConfigManager( FileUtil.getFileByClassPath("/dbcfg/databases-test-H2.dbcfg") );
//		DatabasesConfigurations databasesConfigurations = dbConfigManager.load();
//		return databasesConfigurations.getDatabaseConfiguration(databaseId);
//	}

//	private void printModel (RepositoryModel model)  {
//		System.out.println("Model generated : " );
//		System.out.println("Database ID = " + model.getDatabaseId() );
//		
//		System.out.println("Number of entities = " + model.getNumberOfEntities() );
//		String[] entitiesNames = model.getEntitiesNames();
//		for ( String name : entitiesNames ) {
//			System.out.println("Entity name = '" + name + "'");
//			Entity entity = model.getEntityByName(name);
//			Column[] columns = entity.getColumns();
//			for ( Column c : columns ) {
//				System.out.println(" . Column : " + c );
//			}
//			Link[] links = entity.getLinks();
//			for ( Link link : links ) {
//				System.out.println(" . Link : "  + link);
//				System.out.println(" . " + link.getJavaFieldName() );
//			}
//		}
//	}
	
//	private void checkJavaName(String javaName1, String javaName2, String expectedName1, String expectedName2 ) {
//		System.out.println("Check Java names : '" + javaName1 + "', '" + javaName2 + "' expected : '" + expectedName1 + "', '" + expectedName2 +"'");
//		assertTrue( expectedName1.equals( javaName1 ) || expectedName2.equals( javaName1 ) );
//		assertTrue( expectedName1.equals( javaName2 ) || expectedName2.equals( javaName2 ) );
//		if ( expectedName1.equals( javaName1 ) ) {
//			assertEquals(expectedName2, javaName2);
//		}
//		else if ( expectedName2.equals( javaName1 ) ) {
//			assertEquals(expectedName1, javaName2);
//		}
//		else {
//			fail("Unexpected jave name for link '" + javaName1 + "' or '" + javaName2 + "'");
//		}
//	}
	
//	private void jdbcUrlWithAlterScript(DatabaseConfiguration databaseConfiguration, int testId) {
//		String jdbcUrl = databaseConfiguration.getJdbcUrl();
//		System.out.println("JDBC URL 1 : " + jdbcUrl );
//		int index = jdbcUrl.indexOf(";INIT");
//		String shortJdbcUrl = jdbcUrl.substring(0, index);
//		System.out.println("JDBC URL 2 : " + shortJdbcUrl );
//		String newJdbcUrl = shortJdbcUrl+";INIT=RUNSCRIPT FROM 'classpath:sql/alterdb" + testId + ".sql'" ;
//		System.out.println("JDBC URL 3 : " + newJdbcUrl );
//		databaseConfiguration.setJdbcUrl(newJdbcUrl);
//	}
	
//	private RepositoryGenerator getRepositoryGenerator() throws TelosysToolsException {
//		
//		TelosysToolsLogger logger = new ConsoleLogger() ;
//		ConnectionManager connectionManager = new ConnectionManager(logger);
//		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;
//		return new RepositoryGenerator(connectionManager, rules, logger);
//	}
	
//	/**
//	 * Initialize an database in memory using the given SQL Script ID, <br>
//	 * then generate the RepositoryModel from this Database
//	 * 
//	 * @param sqlScriptId
//	 * @return
//	 * @throws TelosysToolsException
//	 */
//	private RepositoryModel generateRepositoryModel(int sqlScriptId) throws TelosysToolsException {
//		
//		System.out.println("Database initialization... ");
//		DatabaseInMemory databaseInMemory = new DatabaseInMemory(DATABASE_ID_1);
//		databaseInMemory.executeSqlInit(sqlScriptId);
//		
//		System.out.println("Repository generation... ");
//		RepositoryGenerator repositoryGenerator = getRepositoryGenerator() ;
//		//RepositoryModel repositoryModel = repositoryGenerator.generate( databaseConfiguration );
//		RepositoryModel repositoryModel = repositoryGenerator.generate( databaseInMemory.getDatabaseConfiguration() );
//		
//		databaseInMemory.close();
//		
//		return repositoryModel ;
//	}
	
//	private ChangeLog changeDatabaseSchemaAndUpdateRepositoryModel(DatabaseConfiguration databaseConfiguration, 
//				int testId, RepositoryModel repositoryModel, TelosysToolsLogger logger) throws TelosysToolsException {
//		//--- Set "alterdbX.sql" for database schema update
//		jdbcUrlWithAlterScript(databaseConfiguration, testId);
//		
//		//--- Update repository model after database schema update
//		ConnectionManager connectionManager = new ConnectionManager(logger);
//		ByteArrayOutputStream baosUpdateLog = new ByteArrayOutputStream();
//		UpdateLogWriter updateLogger = new UpdateLogWriter(baosUpdateLog);
//		
//		RepositoryUpdator repositoryUpdator = new RepositoryUpdator(connectionManager,
//				RepositoryRulesProvider.getRepositoryRules(), new ConsoleLogger(), updateLogger);
//		
//		//int changesCount = repositoryUpdator.updateRepository(databaseConfiguration, repositoryModel);
//		ChangeLog changeLog = repositoryUpdator.updateRepository(databaseConfiguration, repositoryModel);
//		System.out.println(baosUpdateLog.toString());
//		System.out.println("Repo entities count = " + repositoryModel.getNumberOfEntities() );
//		System.out.println("ChangeLog number of entities = " + changeLog.getNumberOfEntities() );
//		System.out.println("ChangeLog number of entities created = " + changeLog.getNumberOfEntitiesCreated() );
//		System.out.println("ChangeLog number of entities updated = " + changeLog.getNumberOfEntitiesUpdated() );
//		System.out.println("ChangeLog number of entities deleted = " + changeLog.getNumberOfEntitiesDeleted() );
//		return changeLog ;
//	}
	
	@Test
	public void test1() throws TelosysToolsException {
		printSeparator("test1");

//		int sqlScriptId = 1 ;
////		TelosysToolsLogger logger = new ConsoleLogger() ;
////		DatabaseConfiguration databaseConfiguration = getDatabaseConfiguration(1);
////		System.out.println("DatabaseConfiguration ready ");
//
//		System.out.println("Database initialization... ");
//		DatabaseInMemory databaseInMemory = new DatabaseInMemory(DATABASE_ID_1);
//		databaseInMemory.executeSqlInit(sqlScriptId);
//		
//		System.out.println("Repository generation... ");
//		RepositoryGenerator repositoryGenerator = getRepositoryGenerator() ;
//		//RepositoryModel repositoryModel = repositoryGenerator.generate( databaseConfiguration );
//		RepositoryModel repositoryModel = repositoryGenerator.generate( databaseInMemory.getDatabaseConfiguration() );
//		
//		databaseInMemory.close();
		
		RepositoryModel repositoryModel = generateRepositoryModel(1);
		
		printModel(repositoryModel);
		assertTrue(repositoryModel.getDatabaseId() == 1 );
		assertEquals(2, repositoryModel.getNumberOfEntities() );

//		System.out.println("Repository update... ");
//		ChangeLog changeLog = changeDatabaseSchemaAndUpdateRepositoryModel(databaseConfiguration, 1, repositoryModel, logger);
//		
//		//--- Check changes
//		assertTrue(changeLog.getNumberOfEntities() == 3 );
//		assertTrue(changeLog.getNumberOfEntitiesCreated() == 1 );
//		assertTrue(changeLog.getNumberOfEntitiesUpdated() == 1 );
//		assertTrue(changeLog.getNumberOfEntitiesDeleted() == 1 );
//		assertTrue(repositoryModel.getNumberOfEntities() == 2 );
//		
//		Entity badgeEntity = repositoryModel.getEntityByName("BADGE") ;
//		assertNotNull( badgeEntity );
//		assertNotNull( badgeEntity.getColumn("CODE") );
//		assertNotNull( badgeEntity.getColumn("NAME") );
//
//		Entity countryEntity = repositoryModel.getEntityByName("COUNTRY") ;
//		assertNotNull( countryEntity );
//		assertNotNull( countryEntity.getColumn("BADGE_CODE") );
//		assertTrue( countryEntity.getForeignKeys().length == 1 );
	}

	@Test
	public void test2() throws TelosysToolsException {
		printSeparator("test2");
		
//		int databaseID = 2 ;
//		DatabaseConfiguration databaseConfiguration = getDatabaseConfiguration(databaseID);
//		System.out.println("DatabaseConfiguration ready ");
//		RepositoryGenerator repositoryGenerator = getRepositoryGenerator() ;
//		System.out.println("Repository generation... ");
//		RepositoryModel model = repositoryGenerator.generate( databaseConfiguration );
		RepositoryModel repositoryModel = generateRepositoryModel(2);
		
		printModel(repositoryModel);
		
		assertTrue(repositoryModel.getDatabaseId() == DATABASE_ID_1 );
		//assertTrue(repositoryModel.getNumberOfEntities() == 2 );
		assertEquals(2, repositoryModel.getNumberOfEntities() );

		Entity studentEntity = repositoryModel.getEntityByName("STUDENT");
		assertNotNull(studentEntity);
		
		Entity teacherEntity = repositoryModel.getEntityByName("TEACHER");
		assertNotNull(teacherEntity);
		
		Link[] studentLinks = studentEntity.getLinks();
		System.out.println("STUDENT links : " + studentLinks.length);
		assertTrue(studentLinks.length == 2);

		Link[] teacherLinks = teacherEntity.getLinks();
		System.out.println("TEACHER links : " + teacherLinks.length);
		assertTrue(teacherLinks.length == 2);
		
//		String javaName1 = studentLinks[0].getJavaFieldName() ;
//		String javaName2 = studentLinks[1].getJavaFieldName() ;
//		if ( "teacher".equals( javaName1 ) ) {
//			assertEquals("teacher2", javaName2);
//		}
//		else if ( "teacher2".equals( javaName1 ) ) {
//			assertEquals("teacher", javaName2);
//		}
//		else {
//			fail("Unexpected jave name for link " + javaName1 );
//		}
		checkJavaName(studentLinks[0].getJavaFieldName(),studentLinks[1].getJavaFieldName(), "teacher", "teacher2" );
		checkJavaName(teacherLinks[0].getJavaFieldName(),teacherLinks[1].getJavaFieldName(), "listOfStudent", "listOfStudent2" );		
	}

	@Test
	public void test3() throws TelosysToolsException {
		printSeparator("test3");
		
//		int databaseID = 3 ;
//		DatabaseConfiguration databaseConfiguration = getDatabaseConfiguration(databaseID);
//		System.out.println("DatabaseConfiguration ready ");
//		RepositoryGenerator repositoryGenerator = getRepositoryGenerator() ;
//		System.out.println("Repository generation... ");
//		RepositoryModel model = repositoryGenerator.generate( databaseConfiguration );
		RepositoryModel repositoryModel = generateRepositoryModel(3);
		
		printModel(repositoryModel);
		assertTrue(repositoryModel.getDatabaseId() == DATABASE_ID_1 );
		//assertTrue(repositoryModel.getNumberOfEntities() == 2 );
		assertEquals(2, repositoryModel.getNumberOfEntities() );

		Entity studentEntity = repositoryModel.getEntityByName("STUDENT");
		assertNotNull(studentEntity);
		
		Entity teacherEntity = repositoryModel.getEntityByName("TEACHER");
		assertNotNull(teacherEntity);
		
		Link[] studentLinks = studentEntity.getLinks();
		System.out.println("STUDENT links : " + studentLinks.length);
		assertTrue(studentLinks.length == 2);

		Link[] teacherLinks = teacherEntity.getLinks();
		System.out.println("TEACHER links : " + teacherLinks.length);
		assertTrue(teacherLinks.length == 2);
		
		//--- "teacher" is already used for a "column" => links are suposed to be "teacher2" and "teacher3"
//		String javaName1 = studentLinks[0].getJavaFieldName() ;
//		assertTrue( "teacher2".equals( javaName1 ) || "teacher3".equals( javaName1 ) );
//		String javaName2 = studentLinks[1].getJavaFieldName() ;
//		assertTrue( "teacher2".equals( javaName2 ) || "teacher3".equals( javaName2 ) );
//		if ( "teacher2".equals( javaName1 ) ) {
//			assertEquals("teacher3", javaName2);
//		}
//		else if ( "teacher3".equals( javaName1 ) ) {
//			assertEquals("teacher2", javaName2);
//		}
//		else {
//			fail("Unexpected jave name for link " + javaName1 );
//		}
		checkJavaName(studentLinks[0].getJavaFieldName(),studentLinks[1].getJavaFieldName(), "teacher2", "teacher3" );
		checkJavaName(teacherLinks[0].getJavaFieldName(),teacherLinks[1].getJavaFieldName(), "listOfStudent2", "listOfStudent3" );		
	}

	@Test
	public void test4() throws TelosysToolsException {
		printSeparator("test4");
		
//		int databaseID = 4 ;
//		DatabaseConfiguration databaseConfiguration = getDatabaseConfiguration(databaseID);
//		System.out.println("DatabaseConfiguration ready ");
//		RepositoryGenerator repositoryGenerator = getRepositoryGenerator() ;
//		System.out.println("Repository generation... ");
//		RepositoryModel model = repositoryGenerator.generate( databaseConfiguration );
		RepositoryModel repositoryModel = generateRepositoryModel(4);
		
		printModel(repositoryModel);
		//assertTrue(repositoryModel.getNumberOfEntities() == 3 );
		assertEquals(3, repositoryModel.getNumberOfEntities() );

		Entity studentEntity = repositoryModel.getEntityByName("STUDENT");
		assertNotNull(studentEntity);
		assertFalse( studentEntity.isJoinTable() );
		
		Entity teacherEntity = repositoryModel.getEntityByName("TEACHER");
		assertNotNull(teacherEntity);
		assertFalse( teacherEntity.isJoinTable() );

		Entity relation1Entity = repositoryModel.getEntityByName("RELATION1");
		assertNotNull(relation1Entity);
		assertTrue( relation1Entity.isJoinTable() );
		
		
		Link[] studentLinks = studentEntity.getLinks();
		System.out.println("STUDENT links : " + studentLinks.length);
		assertTrue(studentLinks.length == 3);

		Link[] teacherLinks = teacherEntity.getLinks();
		System.out.println("TEACHER links : " + teacherLinks.length);
		assertTrue(teacherLinks.length == 3);
		
		Link[] relation1Links = relation1Entity.getLinks();
		System.out.println("RELATION1 links : " + relation1Links.length);
		assertTrue(relation1Links.length == 0);
		
//		checkJavaName(studentLinks[0].getJavaFieldName(),studentLinks[1].getJavaFieldName(), "teacher2", "teacher3" );
//		checkJavaName(teacherLinks[0].getJavaFieldName(),teacherLinks[1].getJavaFieldName(), "listOfStudent2", "listOfStudent3" );		
	}

	@Test
	public void test5() throws TelosysToolsException {
		printSeparator("test5");
		
//		int databaseID = 5 ;
//		DatabaseConfiguration databaseConfiguration = getDatabaseConfiguration(databaseID);
//		System.out.println("DatabaseConfiguration ready ");
//		RepositoryGenerator repositoryGenerator = getRepositoryGenerator() ;
//		System.out.println("Repository generation... ");
//		RepositoryModel model = repositoryGenerator.generate( databaseConfiguration );
		RepositoryModel repositoryModel = generateRepositoryModel(5);
		
		printModel(repositoryModel);
		//assertEquals(2, repositoryModel.getNumberOfEntities() );
		assertEquals(2, repositoryModel.getNumberOfEntities() );

		Entity studentEntity = repositoryModel.getEntityByName("STUDENT");
		assertNotNull(studentEntity);
		assertFalse( studentEntity.isJoinTable() );
		
		Entity teacherEntity = repositoryModel.getEntityByName("TEACHER");
		assertNotNull(teacherEntity);
		assertFalse( teacherEntity.isJoinTable() );

		Link[] studentLinks = studentEntity.getLinks();
		System.out.println("STUDENT links : " + studentLinks.length);
		assertEquals(1, studentLinks.length);

		Link[] teacherLinks = teacherEntity.getLinks();
		System.out.println("TEACHER links : " + teacherLinks.length);
		assertEquals(1, teacherLinks.length);
		
	}

}
