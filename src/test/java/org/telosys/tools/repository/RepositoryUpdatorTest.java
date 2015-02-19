package org.telosys.tools.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.repository.changelog.ChangeLog;
import org.telosys.tools.repository.changelog.ChangeOnEntity;
import org.telosys.tools.repository.changelog.ChangeOnForeignKey;
import org.telosys.tools.repository.changelog.ChangeType;
import org.telosys.tools.repository.model.Entity;
import org.telosys.tools.repository.model.Link;
import org.telosys.tools.repository.model.RepositoryModel;

public class RepositoryUpdatorTest extends AbstractTestCase {
	
	@Test
	public void test2() throws TelosysToolsException {
		printSeparator("test2");
		//--------------------------------------------------------------------
		// Script 2 :
		// Init  : Tables "teacher", "student"
		// Alter : No change
		//--------------------------------------------------------------------
		UpdateResult result = generateAndUpdateRepositoryModel(2);
		
		ChangeLog changeLog = result.getChangeLog();
		RepositoryModel repositoryModel = result.getRepositoryModel();
		
		//--- Check changes (NO CHANGE)
		//assertTrue(changeLog.getNumberOfEntities() == 0 );
		assertEquals(0, changeLog.getNumberOfEntities());
		assertTrue(changeLog.getNumberOfEntitiesCreated() == 0 );
		assertTrue(changeLog.getNumberOfEntitiesUpdated() == 0 );
		assertTrue(changeLog.getNumberOfEntitiesDeleted() == 0 );
		assertTrue(repositoryModel.getNumberOfEntities() == 2 );
	}

	@Test
	public void test5() throws TelosysToolsException {
		printSeparator("test5");
		//--------------------------------------------------------------------
		// Script 5 :
		// Init  : Tables "teacher", "student"
		// Alter : Create tables "badge", "team"
		//         "teacher" : add column "badge_code" ( FK on badge )
		//         "student" : remove FK on "teacher"
		//--------------------------------------------------------------------
		UpdateResult result = generateAndUpdateRepositoryModel(5);
		
		ChangeLog changeLog = result.getChangeLog();
		RepositoryModel repositoryModel = result.getRepositoryModel();
		
		checkChangeLog(changeLog, repositoryModel );
		printChangeLog(changeLog ) ;
		
		//--- Check changes (NO CHANGE)
		assertEquals(4, changeLog.getNumberOfEntities() ); // 4 entities changed (created, updated,... )
		assertEquals(2, changeLog.getNumberOfEntitiesCreated() ); // 2 entities created
		assertEquals(2, changeLog.getNumberOfEntitiesUpdated() ); // 2 entities updated
		assertEquals(0, changeLog.getNumberOfEntitiesDeleted() ); // 0 entities deleted
		assertEquals(4, repositoryModel.getNumberOfEntities() );
		
		//--- Links in the updated model 
		printLinks(repositoryModel.getEntityByName("STUDENT").getLinks());
		printLinks(repositoryModel.getEntityByName("TEACHER").getLinks());
		printLinks(repositoryModel.getEntityByName("BADGE").getLinks());
		printLinks(repositoryModel.getEntityByName("TEAM").getLinks());
		
		//--- Entities created 
		List<ChangeOnEntity> entitiesCreated = changeLog.getChangesByType(ChangeType.CREATED);
		printEntitiesChanged(entitiesCreated);
		assertEquals(2, entitiesCreated.size() ); // 2 entities created

		ChangeOnEntity badgeChange = changeLog.getChangeByEntityName("BADGE");
		assertNotNull(badgeChange);
		assertEquals(ChangeType.CREATED, badgeChange.getChangeType());
		ChangeOnEntity teamChange = changeLog.getChangeByEntityName("TEAM");
		assertNotNull(teamChange);
		assertEquals(ChangeType.CREATED, teamChange.getChangeType());
		//printEntityChanged(badgeChange);
		
		//--- Entities UPDATED 
		List<ChangeOnEntity> entitiesUpdated = changeLog.getChangesByType(ChangeType.UPDATED);
		printEntitiesChanged(entitiesUpdated);
		assertEquals(2, entitiesUpdated.size() ); // 2 entities updated

		ChangeOnEntity teacherChange = changeLog.getChangeByEntityName("TEACHER");
		assertNotNull(teacherChange);
		assertEquals(ChangeType.UPDATED, teacherChange.getChangeType());
		ChangeOnEntity studentChange = changeLog.getChangeByEntityName("STUDENT");
		assertNotNull(studentChange);
		assertEquals(ChangeType.UPDATED, studentChange.getChangeType());

		//ChangeOnEntity firstEntityUpdated = entitiesUpdated.get(0);
		
		//--- Entity "STUDENT BEFORE/AFTER"
		Entity studentBefore = studentChange.getEntityBefore();
		assertNotNull(studentBefore);
		assertEquals(1, studentBefore.getForeignKeys().length ); // One FK
		assertEquals(1, studentBefore.getLinks().length ); // Owning side link for FK 
		Entity studentAfter = studentChange.getEntityAfter();
		assertNotNull(studentAfter);
		assertEquals(0, studentAfter.getForeignKeys().length );// One FK removed 
		assertEquals(1, studentAfter.getLinks().length ); // Inverse side link

		//--- Entity "TEACHER BEFORE/AFTER"
		Entity teacherBefore = teacherChange.getEntityBefore();
		assertNotNull(teacherBefore);
		assertEquals(0, teacherBefore.getForeignKeys().length ); // No FK
		assertEquals(1, teacherBefore.getLinks().length ); // Inverse side link
		Entity teacherAfter = teacherChange.getEntityAfter();
		assertNotNull(teacherAfter);
		assertEquals(1, teacherAfter.getForeignKeys().length );// One FK added 
		assertEquals(1, teacherBefore.getLinks().length ); // Owning side link for FK

//		assertEquals(1, repositoryModel.getEntityByName("STUDENT").getLinks().length ); // Owning side unchanged
//		assertEquals(2, repositoryModel.getEntityByName("TEACHER").getLinks().length ); // 2 inv (Student+Team) + Owning side(badge)
//		assertEquals(0, repositoryModel.getEntityByName("BADGE").getLinks().length ); // Inverse side
	}

	/**
	 * Test "one entity DELETED with FK" (here "STUDENT" is deleted)
	 * @throws TelosysToolsException
	 */
	@Test
	public void test6() throws TelosysToolsException {
		printSeparator("test6");
		UpdateResult result = generateAndUpdateRepositoryModel(6);

		ChangeLog changeLog = result.getChangeLog();
		RepositoryModel repositoryModel = result.getRepositoryModel();
		
		checkChangeLog(changeLog, repositoryModel );
		printChangeLog(changeLog ) ;
		
		//--- Check changes (NO CHANGE)
		assertEquals(1, changeLog.getNumberOfEntities() );
		assertEquals(0, changeLog.getNumberOfEntitiesCreated() );
		assertEquals(0, changeLog.getNumberOfEntitiesUpdated() );
		assertEquals(1, changeLog.getNumberOfEntitiesDeleted() );
		assertEquals(1, repositoryModel.getNumberOfEntities() );
		
		assertNull(repositoryModel.getEntityByName("STUDENT")); // Deleted
		assertNotNull(repositoryModel.getEntityByName("TEACHER")); // Still present
		
		//--- Links in the updated model 
		printLinks(repositoryModel.getEntityByName("TEACHER").getLinks());
		
		List<ChangeOnEntity> entitiesDeleted = changeLog.getChangesByType(ChangeType.DELETED);
		assertEquals(1, entitiesDeleted.size() );
		ChangeOnEntity changeOnEntity = entitiesDeleted.get(0);
		Entity entityDeleted = changeOnEntity.getEntityDeleted();
		assertNotNull(entityDeleted);
		assertEquals(1, entityDeleted.getForeignKeys().length );
		
		assertEquals(0, repositoryModel.getEntityByName("TEACHER").getLinks().length ); // 0 Link 
	}
	
	/**
	 * Test "one entity CREATED with FK" (here "TEAM" is created)
	 * @throws TelosysToolsException
	 */
	@Test
	public void test8() throws TelosysToolsException {
		printSeparator("test8");
		UpdateResult result = generateAndUpdateRepositoryModel(8);

		ChangeLog changeLog = result.getChangeLog();
		RepositoryModel repositoryModel = result.getRepositoryModel();
		
		checkChangeLog(changeLog, repositoryModel );
		printChangeLog(changeLog ) ;
		
		//--- Check changes 
		assertEquals(1, changeLog.getNumberOfEntities() ); // 1 change
		assertEquals(1, changeLog.getNumberOfEntitiesCreated() );
		assertEquals(0, changeLog.getNumberOfEntitiesUpdated() );
		assertEquals(0, changeLog.getNumberOfEntitiesDeleted() );
		assertEquals(3, repositoryModel.getNumberOfEntities() ); // 3 in the model
		
		//--- Links in the updated model 
		printLinks(repositoryModel.getEntityByName("STUDENT").getLinks()); 
		printLinks(repositoryModel.getEntityByName("TEACHER").getLinks());
		printLinks(repositoryModel.getEntityByName("TEAM").getLinks()); // the new entity
		
		List<ChangeOnEntity> changesOnEntitiesCreated = changeLog.getChangesByType(ChangeType.CREATED);
		assertEquals(1, changesOnEntitiesCreated.size() );
		ChangeOnEntity changeOnEntity = changesOnEntitiesCreated.get(0);
		Entity entityCreated = changeOnEntity.getEntityCreated();
		assertNotNull(entityCreated);
		assertEquals(1, entityCreated.getForeignKeys().length );
		assertEquals(1, entityCreated.getLinks().length ); // Inverse side link

		//--- Links in the updated model
		
		//--- "STUDENT" links (unchanged)
		Link[] studentLinks = repositoryModel.getEntityByName("STUDENT").getLinks();
		assertEquals(1, studentLinks.length ); 
		assertEquals(true, studentLinks[0].isOwningSide() );
		assertEquals(true, studentLinks[0].isTypeManyToOne() );
		assertEquals(true, studentLinks[0].isBasedOnForeignKey() );
		assertEquals("TEACHER", studentLinks[0].getTargetTableName() );
		
		//--- "TEAM" links (created with the entity)
		Link[] teamLinks = repositoryModel.getEntityByName("TEAM").getLinks();
		assertEquals(1, teamLinks.length ); 
		assertEquals(true, teamLinks[0].isOwningSide() );
		assertEquals(true, teamLinks[0].isTypeManyToOne() );
		assertEquals(true, teamLinks[0].isBasedOnForeignKey() );
		assertEquals("TEACHER", teamLinks[0].getTargetTableName() );

		//--- "TEACHER" links : one more link ( inverse side / list of students )
		Link[] teacherLinks = repositoryModel.getEntityByName("TEACHER").getLinks();
		assertEquals(2, teacherLinks.length ); 
		
		List<Link> linksToStudent = repositoryModel.getEntityByName("TEACHER").getLinksTo("STUDENT");
		assertEquals(1, linksToStudent.size() ); 
		Link linkToStudent = linksToStudent.get(0);
		assertEquals(true, linkToStudent.isInverseSide() );
		assertEquals(true, linkToStudent.isTypeOneToMany() );
		assertEquals("STUDENT", linkToStudent.getTargetTableName() );

		List<Link> linksToTeam = repositoryModel.getEntityByName("TEACHER").getLinksTo("TEAM");
		assertEquals(1, linksToTeam.size() ); 
		Link linkToTeam = linksToTeam.get(0);
		assertEquals(true, linkToTeam.isInverseSide() );
		assertEquals(true, linkToTeam.isTypeOneToMany() );
		assertEquals("TEAM", linkToTeam.getTargetTableName() );
	}

	//===================================================================================================
	
	private void checkChangeLog(ChangeLog changeLog, RepositoryModel repositoryModel ) {
		for ( ChangeOnEntity change : changeLog.getChanges() ) {
			switch ( change.getChangeType() ) {
			case CREATED :
				assertTrue( repositoryModel.getEntityByName(change.getEntityCreated().getName()) == change.getEntityCreated() );
				break;
			case UPDATED :
				assertTrue( repositoryModel.getEntityByName(change.getEntityAfter().getName()) == change.getEntityAfter() );
				break;
			case DELETED :
				assertTrue( repositoryModel.getEntityByName(change.getEntityDeleted().getName()) == null );
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
	
	private void printEntitiesChanged(List<ChangeOnEntity> entitiesChanged) {
		System.out.println("--- LIST OF ENTITIES CHANGED : " );
		for ( ChangeOnEntity entityChanged : entitiesChanged ) {
			printEntityChanged(entityChanged);
		}
	}
	
	private void printEntityChanged(ChangeOnEntity entityChanged) {
		System.out.println("ENTITY CHANGED : " );
		System.out.println(" . name         : " + entityChanged.getEntityName() ) ;
		System.out.println(" . change type  : " + entityChanged.getChangeType() ) ;
		switch ( entityChanged.getChangeType() ) {
		case CREATED :
			System.out.println("ENTITY CREATED : " );
			printEntity( entityChanged.getEntityCreated() );
			break;
		case DELETED :
			System.out.println("ENTITY DELETED : " );
			printEntity( entityChanged.getEntityDeleted() );
			break;
		case UPDATED :
			System.out.println("ENTITY UPDATED / BEFORE : " );
			printEntity( entityChanged.getEntityBefore() );
			System.out.println("ENTITY UPDATED / AFTER : " );
			printEntity( entityChanged.getEntityAfter() );
			break;
		}
	}

}
