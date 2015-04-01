package org.telosys.tools.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.repository.model.EntityInDbModel;
import org.telosys.tools.repository.model.LinkInDbModel;
import org.telosys.tools.repository.model.RepositoryModel;

public class RepositoryGeneratorTest extends AbstractTestCase {

	@Test
	public void test1() throws TelosysToolsException {
		printSeparator("test1");

		RepositoryModel repositoryModel = generateRepositoryModel(1);
		printModel(repositoryModel);
		assertTrue(repositoryModel.getDatabaseId() == DATABASE_ID_1 );
		assertEquals(2, repositoryModel.getNumberOfEntities() );

//		EntityInDbModel customerEntity = repositoryModel.getEntityByName("CUSTOMER") ;
		EntityInDbModel customerEntity = repositoryModel.getEntityByTableName("CUSTOMER") ;
		assertNotNull( customerEntity );
		assertNotNull( customerEntity.getAttributeByColumnName("CODE") );
		assertNotNull( customerEntity.getAttributeByColumnName("FIRST_NAME") );
		assertNotNull( customerEntity.getAttributeByColumnName("LAST_NAME") );
		//assertNotNull( customerEntity.getColumn("xxxx") );

//		EntityInDbModel countryEntity = repositoryModel.getEntityByName("COUNTRY") ;
		EntityInDbModel countryEntity = repositoryModel.getEntityByTableName("COUNTRY") ;
		assertNotNull( countryEntity );
		assertNotNull( countryEntity.getAttributeByColumnName("CODE") );
		assertNotNull( countryEntity.getAttributeByColumnName("NAME") );
	}

	@Test
	public void test2() throws TelosysToolsException {
		printSeparator("test2");
		
		RepositoryModel repositoryModel = generateRepositoryModel(2);
		printModel(repositoryModel);
		assertTrue(repositoryModel.getDatabaseId() == DATABASE_ID_1 );
		assertEquals(2, repositoryModel.getNumberOfEntities() );

//		EntityInDbModel studentEntity = repositoryModel.getEntityByName("STUDENT");
		EntityInDbModel studentEntity = repositoryModel.getEntityByTableName("STUDENT");
		assertNotNull(studentEntity);
		
//		EntityInDbModel teacherEntity = repositoryModel.getEntityByName("TEACHER");
		EntityInDbModel teacherEntity = repositoryModel.getEntityByTableName("TEACHER");
		assertNotNull(teacherEntity);
		
//		LinkInDbModel[] studentLinks = studentEntity.getLinks();
		LinkInDbModel[] studentLinks = studentEntity.getLinksArray();
		System.out.println("STUDENT links : " + studentLinks.length);
		assertTrue(studentLinks.length == 2);

//		LinkInDbModel[] teacherLinks = teacherEntity.getLinks();
		LinkInDbModel[] teacherLinks = teacherEntity.getLinksArray();
		System.out.println("TEACHER links : " + teacherLinks.length);
		assertTrue(teacherLinks.length == 2);
		
//		checkJavaName(studentLinks[0].getJavaFieldName(),studentLinks[1].getJavaFieldName(), "teacher", "teacher2" );
//		checkJavaName(teacherLinks[0].getJavaFieldName(),teacherLinks[1].getJavaFieldName(), "listOfStudent", "listOfStudent2" );		
		checkJavaName(studentLinks[0].getFieldName(),studentLinks[1].getFieldName(), "teacher", "teacher2" );
		checkJavaName(teacherLinks[0].getFieldName(),teacherLinks[1].getFieldName(), "listOfStudent", "listOfStudent2" );		
	}

	@Test
	public void test3() throws TelosysToolsException {
		printSeparator("test3");
		
		RepositoryModel repositoryModel = generateRepositoryModel(3);
		printModel(repositoryModel);
		assertTrue(repositoryModel.getDatabaseId() == DATABASE_ID_1 );
		assertEquals(2, repositoryModel.getNumberOfEntities() );

//		EntityInDbModel studentEntity = repositoryModel.getEntityByName("STUDENT");
		EntityInDbModel studentEntity = repositoryModel.getEntityByTableName("STUDENT");
		assertNotNull(studentEntity);
		
//		EntityInDbModel teacherEntity = repositoryModel.getEntityByName("TEACHER");
		EntityInDbModel teacherEntity = repositoryModel.getEntityByTableName("TEACHER");
		assertNotNull(teacherEntity);
		
//		LinkInDbModel[] studentLinks = studentEntity.getLinks();
		LinkInDbModel[] studentLinks = studentEntity.getLinksArray();
		System.out.println("STUDENT links : " + studentLinks.length);
		assertTrue(studentLinks.length == 2);

//		LinkInDbModel[] teacherLinks = teacherEntity.getLinks();
		LinkInDbModel[] teacherLinks = teacherEntity.getLinksArray();
		System.out.println("TEACHER links : " + teacherLinks.length);
		assertTrue(teacherLinks.length == 2);
		
//		checkJavaName(studentLinks[0].getJavaFieldName(),studentLinks[1].getJavaFieldName(), "teacher2", "teacher3" );
//		checkJavaName(teacherLinks[0].getJavaFieldName(),teacherLinks[1].getJavaFieldName(), "listOfStudent2", "listOfStudent3" );		
		checkJavaName(studentLinks[0].getFieldName(),studentLinks[1].getFieldName(), "teacher2", "teacher3" );
		checkJavaName(teacherLinks[0].getFieldName(),teacherLinks[1].getFieldName(), "listOfStudent2", "listOfStudent3" );		
	}

	@Test
	public void test4() throws TelosysToolsException {
		printSeparator("test4");
		
		RepositoryModel repositoryModel = generateRepositoryModel(4);
		printModel(repositoryModel);
		assertEquals(3, repositoryModel.getNumberOfEntities() );

//		EntityInDbModel studentEntity = repositoryModel.getEntityByName("STUDENT");
		EntityInDbModel studentEntity = repositoryModel.getEntityByTableName("STUDENT");
		assertNotNull(studentEntity);
		assertFalse( studentEntity.isJoinTable() );
		
//		EntityInDbModel teacherEntity = repositoryModel.getEntityByName("TEACHER");
		EntityInDbModel teacherEntity = repositoryModel.getEntityByTableName("TEACHER");
		assertNotNull(teacherEntity);
		assertFalse( teacherEntity.isJoinTable() );

//		EntityInDbModel relation1Entity = repositoryModel.getEntityByName("RELATION1");
		EntityInDbModel relation1Entity = repositoryModel.getEntityByTableName("RELATION1");
		assertNotNull(relation1Entity);
		assertTrue( relation1Entity.isJoinTable() );
		
		
//		LinkInDbModel[] studentLinks = studentEntity.getLinks();
		LinkInDbModel[] studentLinks = studentEntity.getLinksArray();
		System.out.println("STUDENT links : " + studentLinks.length);
		assertEquals(3, studentLinks.length);

//		LinkInDbModel[] teacherLinks = teacherEntity.getLinks();
		LinkInDbModel[] teacherLinks = teacherEntity.getLinksArray();
		System.out.println("TEACHER links : " + teacherLinks.length);
		assertEquals(3, teacherLinks.length);
		
//		LinkInDbModel[] relation1Links = relation1Entity.getLinks();
		LinkInDbModel[] relation1Links = relation1Entity.getLinksArray();
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

//		EntityInDbModel studentEntity = repositoryModel.getEntityByName("STUDENT");
		EntityInDbModel studentEntity = repositoryModel.getEntityByTableName("STUDENT");
		assertNotNull(studentEntity);
		assertFalse( studentEntity.isJoinTable() );
		assertEquals(1, studentEntity.getForeignKeys().length ) ;
		assertEquals("STUDENT", studentEntity.getForeignKeys()[0].getTableName() ) ;
//		assertEquals("TEACHER", studentEntity.getForeignKeys()[0].getTableRef() ) ;
		assertEquals("TEACHER", studentEntity.getForeignKeys()[0].getReferencedTableName() ) ;
		
//		EntityInDbModel teacherEntity = repositoryModel.getEntityByName("TEACHER");
		EntityInDbModel teacherEntity = repositoryModel.getEntityByTableName("TEACHER");
		assertNotNull(teacherEntity);
		assertFalse( teacherEntity.isJoinTable() );
		assertEquals(0, teacherEntity.getForeignKeys().length ) ;

//		LinkInDbModel[] studentLinks = studentEntity.getLinks();
		LinkInDbModel[] studentLinks = studentEntity.getLinksArray();
		System.out.println("STUDENT links : " + studentLinks.length);
		assertEquals(1, studentLinks.length);

//		LinkInDbModel[] teacherLinks = teacherEntity.getLinks();
		LinkInDbModel[] teacherLinks = teacherEntity.getLinksArray();
		System.out.println("TEACHER links : " + teacherLinks.length);
		assertEquals(1, teacherLinks.length);
	}

}
