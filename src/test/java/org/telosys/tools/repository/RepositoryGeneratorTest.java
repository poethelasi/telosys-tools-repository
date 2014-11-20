package org.telosys.tools.repository;

import junit.framework.Assert;

import org.junit.Test;
import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.dbcfg.DatabaseConfiguration;
import org.telosys.tools.commons.dbcfg.DatabasesConfigurations;
import org.telosys.tools.commons.dbcfg.DbConfigManager;
import org.telosys.tools.repository.model.Column;
import org.telosys.tools.repository.model.Entity;
import org.telosys.tools.repository.model.Link;
import org.telosys.tools.repository.model.RepositoryModel;
import org.telosys.tools.repository.rules.RepositoryRulesProvider;

public class RepositoryGeneratorTest {
	
//	private Connection getDatabaseConnection(DatabaseConfiguration databaseConfiguration) throws TelosysToolsException {
//		ConnectionManager connectionManager = new ConnectionManager(new ConsoleLogger());
//		return connectionManager.getConnection(databaseConfiguration);
//	}
	
	private DatabaseConfiguration getDatabaseConfiguration(int databaseId) throws TelosysToolsException {
		System.out.println("================================================================================");
		DbConfigManager dbConfigManager = new DbConfigManager( FileUtil.getFileByClassPath("/dbcfg/databases-test-H2.dbcfg") );
		DatabasesConfigurations databasesConfigurations = dbConfigManager.load();
		return databasesConfigurations.getDatabaseConfiguration(databaseId);
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
	
	@Test
	public void test1() throws TelosysToolsException {
		
		DatabaseConfiguration databaseConfiguration = getDatabaseConfiguration(1);
		System.out.println("DatabaseConfiguration ready ");
		RepositoryGenerator repositoryGenerator = new RepositoryGenerator(RepositoryRulesProvider.getRepositoryRules(), new ConsoleLogger());
		System.out.println("Repository generation... ");
		RepositoryModel model = repositoryGenerator.generate( databaseConfiguration );
		
		printModel(model);
		Assert.assertTrue(model.getDatabaseId() == 1 );
		Assert.assertTrue(model.getNumberOfEntities() == 2 );
	}

	@Test
	public void test2() throws TelosysToolsException {
		
		int databaseID = 2 ;
		DatabaseConfiguration databaseConfiguration = getDatabaseConfiguration(databaseID);
		System.out.println("DatabaseConfiguration ready ");
		RepositoryGenerator repositoryGenerator = new RepositoryGenerator(RepositoryRulesProvider.getRepositoryRules(), new ConsoleLogger());
		System.out.println("Repository generation... ");
		RepositoryModel model = repositoryGenerator.generate( databaseConfiguration );
		
		printModel(model);
		
		Assert.assertTrue(model.getDatabaseId() == databaseID );
		Assert.assertTrue(model.getNumberOfEntities() == 2 );

		Entity studentEntity = model.getEntityByName("STUDENT");
		Assert.assertNotNull(studentEntity);
		
		Entity teacherEntity = model.getEntityByName("TEACHER");
		Assert.assertNotNull(teacherEntity);
		
		Link[] studentLinks = studentEntity.getLinks();
		System.out.println("STUDENT links : " + studentLinks.length);
		Assert.assertTrue(studentLinks.length == 2);

		Link[] teacherLinks = teacherEntity.getLinks();
		System.out.println("TEACHER links : " + teacherLinks.length);
		Assert.assertTrue(teacherLinks.length == 2);
		
//		String javaName1 = studentLinks[0].getJavaFieldName() ;
//		String javaName2 = studentLinks[1].getJavaFieldName() ;
//		if ( "teacher".equals( javaName1 ) ) {
//			Assert.assertEquals("teacher2", javaName2);
//		}
//		else if ( "teacher2".equals( javaName1 ) ) {
//			Assert.assertEquals("teacher", javaName2);
//		}
//		else {
//			Assert.fail("Unexpected jave name for link " + javaName1 );
//		}
		checkJavaName(studentLinks[0].getJavaFieldName(),studentLinks[1].getJavaFieldName(), "teacher", "teacher2" );
		checkJavaName(teacherLinks[0].getJavaFieldName(),teacherLinks[1].getJavaFieldName(), "listOfStudent", "listOfStudent2" );		
	}

	@Test
	public void test3() throws TelosysToolsException {
		int databaseID = 3 ;
		DatabaseConfiguration databaseConfiguration = getDatabaseConfiguration(databaseID);
		System.out.println("DatabaseConfiguration ready ");
		RepositoryGenerator repositoryGenerator = new RepositoryGenerator(RepositoryRulesProvider.getRepositoryRules(), new ConsoleLogger());
		System.out.println("Repository generation... ");
		RepositoryModel model = repositoryGenerator.generate( databaseConfiguration );
		
		printModel(model);
		Assert.assertTrue(model.getDatabaseId() == databaseID );
		Assert.assertTrue(model.getNumberOfEntities() == 2 );

		Entity studentEntity = model.getEntityByName("STUDENT");
		Assert.assertNotNull(studentEntity);
		
		Entity teacherEntity = model.getEntityByName("TEACHER");
		Assert.assertNotNull(teacherEntity);
		
		Link[] studentLinks = studentEntity.getLinks();
		System.out.println("STUDENT links : " + studentLinks.length);
		Assert.assertTrue(studentLinks.length == 2);

		Link[] teacherLinks = teacherEntity.getLinks();
		System.out.println("TEACHER links : " + teacherLinks.length);
		Assert.assertTrue(teacherLinks.length == 2);
		
		//--- "teacher" is already used for a "column" => links are suposed to be "teacher2" and "teacher3"
//		String javaName1 = studentLinks[0].getJavaFieldName() ;
//		Assert.assertTrue( "teacher2".equals( javaName1 ) || "teacher3".equals( javaName1 ) );
//		String javaName2 = studentLinks[1].getJavaFieldName() ;
//		Assert.assertTrue( "teacher2".equals( javaName2 ) || "teacher3".equals( javaName2 ) );
//		if ( "teacher2".equals( javaName1 ) ) {
//			Assert.assertEquals("teacher3", javaName2);
//		}
//		else if ( "teacher3".equals( javaName1 ) ) {
//			Assert.assertEquals("teacher2", javaName2);
//		}
//		else {
//			Assert.fail("Unexpected jave name for link " + javaName1 );
//		}
		checkJavaName(studentLinks[0].getJavaFieldName(),studentLinks[1].getJavaFieldName(), "teacher2", "teacher3" );
		checkJavaName(teacherLinks[0].getJavaFieldName(),teacherLinks[1].getJavaFieldName(), "listOfStudent2", "listOfStudent3" );		
	}
}
