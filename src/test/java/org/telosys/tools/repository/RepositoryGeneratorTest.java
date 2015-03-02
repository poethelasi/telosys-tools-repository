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

	@Test
	public void test1() throws TelosysToolsException {
		printSeparator("test1");

		RepositoryModel repositoryModel = generateRepositoryModel(1);
		printModel(repositoryModel);
		assertTrue(repositoryModel.getDatabaseId() == DATABASE_ID_1 );
		assertEquals(2, repositoryModel.getNumberOfEntities() );

		Entity customerEntity = repositoryModel.getEntityByName("CUSTOMER") ;
		assertNotNull( customerEntity );
		assertNotNull( customerEntity.getColumn("CODE") );
		assertNotNull( customerEntity.getColumn("FIRST_NAME") );
		assertNotNull( customerEntity.getColumn("LAST_NAME") );
		//assertNotNull( customerEntity.getColumn("xxxx") );

		Entity countryEntity = repositoryModel.getEntityByName("COUNTRY") ;
		assertNotNull( countryEntity );
		assertNotNull( countryEntity.getColumn("CODE") );
		assertNotNull( countryEntity.getColumn("NAME") );
	}

	@Test
	public void test2() throws TelosysToolsException {
		printSeparator("test2");
		
		RepositoryModel repositoryModel = generateRepositoryModel(2);
		printModel(repositoryModel);
		assertTrue(repositoryModel.getDatabaseId() == DATABASE_ID_1 );
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
		
		checkJavaName(studentLinks[0].getJavaFieldName(),studentLinks[1].getJavaFieldName(), "teacher", "teacher2" );
		checkJavaName(teacherLinks[0].getJavaFieldName(),teacherLinks[1].getJavaFieldName(), "listOfStudent", "listOfStudent2" );		
	}

	@Test
	public void test3() throws TelosysToolsException {
		printSeparator("test3");
		
		RepositoryModel repositoryModel = generateRepositoryModel(3);
		printModel(repositoryModel);
		assertTrue(repositoryModel.getDatabaseId() == DATABASE_ID_1 );
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
		
		checkJavaName(studentLinks[0].getJavaFieldName(),studentLinks[1].getJavaFieldName(), "teacher2", "teacher3" );
		checkJavaName(teacherLinks[0].getJavaFieldName(),teacherLinks[1].getJavaFieldName(), "listOfStudent2", "listOfStudent3" );		
	}

	@Test
	public void test4() throws TelosysToolsException {
		printSeparator("test4");
		
		RepositoryModel repositoryModel = generateRepositoryModel(4);
		printModel(repositoryModel);
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
		assertEquals(3, studentLinks.length);

		Link[] teacherLinks = teacherEntity.getLinks();
		System.out.println("TEACHER links : " + teacherLinks.length);
		assertEquals(3, teacherLinks.length);
		
		Link[] relation1Links = relation1Entity.getLinks();
		System.out.println("RELATION1 links : " + relation1Links.length);
		assertEquals(0, relation1Links.length);
		
//		checkJavaName(studentLinks[0].getJavaFieldName(),studentLinks[1].getJavaFieldName(), "teacher2", "teacher3" );
//		checkJavaName(teacherLinks[0].getJavaFieldName(),teacherLinks[1].getJavaFieldName(), "listOfStudent2", "listOfStudent3" );		
	}

	@Test
	public void test5() throws TelosysToolsException {
		printSeparator("test5");
		
		RepositoryModel repositoryModel = generateRepositoryModel(5);
		printModel(repositoryModel);
		assertEquals(2, repositoryModel.getNumberOfEntities() );

		Entity studentEntity = repositoryModel.getEntityByName("STUDENT");
		assertNotNull(studentEntity);
		assertFalse( studentEntity.isJoinTable() );
		assertEquals(1, studentEntity.getForeignKeys().length ) ;
		assertEquals("STUDENT", studentEntity.getForeignKeys()[0].getTableName() ) ;
		assertEquals("TEACHER", studentEntity.getForeignKeys()[0].getTableRef() ) ;
		
		Entity teacherEntity = repositoryModel.getEntityByName("TEACHER");
		assertNotNull(teacherEntity);
		assertFalse( teacherEntity.isJoinTable() );
		assertEquals(0, teacherEntity.getForeignKeys().length ) ;

		Link[] studentLinks = studentEntity.getLinks();
		System.out.println("STUDENT links : " + studentLinks.length);
		assertEquals(1, studentLinks.length);

		Link[] teacherLinks = teacherEntity.getLinks();
		System.out.println("TEACHER links : " + teacherLinks.length);
		assertEquals(1, teacherLinks.length);
	}

}
