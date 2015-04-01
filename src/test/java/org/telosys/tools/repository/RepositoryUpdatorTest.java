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
import org.telosys.tools.repository.changelog.ChangeType;
import org.telosys.tools.repository.model.EntityInDbModel;
import org.telosys.tools.repository.model.LinkInDbModel;
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
		assertEquals(0, changeLog.getNumberOfEntities());
		assertEquals(0, changeLog.getNumberOfEntitiesCreated() );
		assertEquals(0, changeLog.getNumberOfEntitiesUpdated() );
		assertEquals(0, changeLog.getNumberOfEntitiesDeleted() );
		assertEquals(2, repositoryModel.getNumberOfEntities() );
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
//		printLinks(repositoryModel.getEntityByName("STUDENT").getLinks());
//		printLinks(repositoryModel.getEntityByName("TEACHER").getLinks());
//		printLinks(repositoryModel.getEntityByName("BADGE").getLinks());
//		printLinks(repositoryModel.getEntityByName("TEAM").getLinks());
		printLinks(repositoryModel.getEntityByTableName("STUDENT").getLinksArray() );
		printLinks(repositoryModel.getEntityByTableName("TEACHER").getLinksArray());
		printLinks(repositoryModel.getEntityByTableName("BADGE").getLinksArray());
		printLinks(repositoryModel.getEntityByTableName("TEAM").getLinksArray());
		
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
		EntityInDbModel studentBefore = studentChange.getEntityBefore();
		assertNotNull(studentBefore);
		assertEquals(1, studentBefore.getForeignKeys().length ); // One FK
//		assertEquals(1, studentBefore.getLinks().length ); // Owning side link for FK 
		assertEquals(1, studentBefore.getLinks().size() ); // Owning side link for FK 
		EntityInDbModel studentAfter = studentChange.getEntityAfter();
		assertNotNull(studentAfter);
		assertEquals(0, studentAfter.getForeignKeys().length );// One FK removed 
//		assertEquals(0, studentAfter.getLinks().length ); 
		assertEquals(0, studentAfter.getLinks().size() ); 

		//--- Entity "TEACHER BEFORE/AFTER"
		EntityInDbModel teacherBefore = teacherChange.getEntityBefore();
		assertNotNull(teacherBefore);
		assertEquals(0, teacherBefore.getForeignKeys().length ); // No FK
//		assertEquals(1, teacherBefore.getLinks().length ); // Inverse side link
		assertEquals(1, teacherBefore.getLinks().size() ); // Inverse side link
		EntityInDbModel teacherAfter = teacherChange.getEntityAfter();
		assertNotNull(teacherAfter);
		assertEquals(1, teacherAfter.getForeignKeys().length );// One FK added 
//		assertEquals(1, teacherBefore.getLinks().length ); // Owning side link for FK
		assertEquals(1, teacherBefore.getLinks().size() ); // Owning side link for FK

		//--- Result in the model
//		assertEquals(0, repositoryModel.getEntityByName("STUDENT").getLinks().length ); // No link
//		assertEquals(2, repositoryModel.getEntityByName("TEACHER").getLinks().length ); // Inverse side (from TEAM) + Owning side(to BADGE)
//		assertEquals(1, repositoryModel.getEntityByName("BADGE").getLinks().length ); // Inverse side (from TEACHER)
//		assertEquals(1, repositoryModel.getEntityByName("TEAM").getLinks().length ); // Owning side (to TEACHER)
		assertEquals(0, repositoryModel.getEntityByTableName("STUDENT").getLinks().size() ); // No link
		assertEquals(2, repositoryModel.getEntityByTableName("TEACHER").getLinks().size() ); // Inverse side (from TEAM) + Owning side(to BADGE)
		assertEquals(1, repositoryModel.getEntityByTableName("BADGE").getLinks().size() ); // Inverse side (from TEACHER)
		assertEquals(1, repositoryModel.getEntityByTableName("TEAM").getLinks().size() ); // Owning side (to TEACHER)
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
		
		assertNull(repositoryModel.getEntityByTableName("STUDENT")); // Deleted
		assertNotNull(repositoryModel.getEntityByTableName("TEACHER")); // Still present
		
		//--- Links in the updated model 
//		printLinks(repositoryModel.getEntityByName("TEACHER").getLinks());
		printLinks(repositoryModel.getEntityByTableName("TEACHER").getLinksArray());
		
		List<ChangeOnEntity> entitiesDeleted = changeLog.getChangesByType(ChangeType.DELETED);
		assertEquals(1, entitiesDeleted.size() );
		ChangeOnEntity changeOnEntity = entitiesDeleted.get(0);
		EntityInDbModel entityDeleted = changeOnEntity.getEntityDeleted();
		assertNotNull(entityDeleted);
		assertEquals(1, entityDeleted.getForeignKeys().length );
		
//		assertEquals(0, repositoryModel.getEntityByName("TEACHER").getLinks().length ); // 0 Link 
		assertEquals(0, repositoryModel.getEntityByTableName("TEACHER").getLinks().size() ); // 0 Link 
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
//		printLinks(repositoryModel.getEntityByName("STUDENT").getLinks()); 
//		printLinks(repositoryModel.getEntityByName("TEACHER").getLinks());
//		printLinks(repositoryModel.getEntityByName("TEAM").getLinks()); // the new entity
		printLinks(repositoryModel.getEntityByTableName("STUDENT").getLinksArray()); 
		printLinks(repositoryModel.getEntityByTableName("TEACHER").getLinksArray());
		printLinks(repositoryModel.getEntityByTableName("TEAM").getLinksArray()); // the new entity
		
		List<ChangeOnEntity> changesOnEntitiesCreated = changeLog.getChangesByType(ChangeType.CREATED);
		assertEquals(1, changesOnEntitiesCreated.size() );
		ChangeOnEntity changeOnEntity = changesOnEntitiesCreated.get(0);
		EntityInDbModel entityCreated = changeOnEntity.getEntityCreated();
		assertNotNull(entityCreated);
		assertEquals(1, entityCreated.getForeignKeys().length );
//		assertEquals(1, entityCreated.getLinks().length ); // Inverse side link
		assertEquals(1, entityCreated.getLinks().size() ); // Inverse side link

		//--- Links in the updated model
		
		//--- "STUDENT" links (unchanged)
//		LinkInDbModel[] studentLinks = repositoryModel.getEntityByName("STUDENT").getLinks();
		LinkInDbModel[] studentLinks = repositoryModel.getEntityByTableName("STUDENT").getLinksArray();
		assertEquals(1, studentLinks.length ); 
		assertEquals(true, studentLinks[0].isOwningSide() );
//		assertEquals(true, studentLinks[0].isTypeManyToOne() );
		assertEquals(true, studentLinks[0].isCardinalityManyToOne() );
		assertEquals(true, studentLinks[0].isBasedOnForeignKey() );
		assertEquals("TEACHER", studentLinks[0].getTargetTableName() );
		
		//--- "TEAM" links (created with the entity)
//		LinkInDbModel[] teamLinks = repositoryModel.getEntityByName("TEAM").getLinks();
		LinkInDbModel[] teamLinks = repositoryModel.getEntityByTableName("TEAM").getLinksArray();
		assertEquals(1, teamLinks.length ); 
		assertEquals(true, teamLinks[0].isOwningSide() );
//		assertEquals(true, teamLinks[0].isTypeManyToOne() );
		assertEquals(true, teamLinks[0].isCardinalityManyToOne() );
		assertEquals(true, teamLinks[0].isBasedOnForeignKey() );
		assertEquals("TEACHER", teamLinks[0].getTargetTableName() );

		//--- "TEACHER" links : one more link ( inverse side / list of students )
//		LinkInDbModel[] teacherLinks = repositoryModel.getEntityByName("TEACHER").getLinks();
		LinkInDbModel[] teacherLinks = repositoryModel.getEntityByTableName("TEACHER").getLinksArray();
		assertEquals(2, teacherLinks.length ); 
		
		List<LinkInDbModel> linksToStudent = repositoryModel.getEntityByTableName("TEACHER").getLinksTo("STUDENT");
		assertEquals(1, linksToStudent.size() ); 
		LinkInDbModel linkToStudent = linksToStudent.get(0);
		assertEquals(true, linkToStudent.isInverseSide() );
//		assertEquals(true, linkToStudent.isTypeOneToMany() );
		assertEquals(true, linkToStudent.isCardinalityOneToMany() );
		assertEquals("STUDENT", linkToStudent.getTargetTableName() );

		List<LinkInDbModel> linksToTeam = repositoryModel.getEntityByTableName("TEACHER").getLinksTo("TEAM");
		assertEquals(1, linksToTeam.size() ); 
		LinkInDbModel linkToTeam = linksToTeam.get(0);
		assertEquals(true, linkToTeam.isInverseSide() );
//		assertEquals(true, linkToTeam.isTypeOneToMany() );
		assertEquals(true, linkToTeam.isCardinalityOneToMany() );
		assertEquals("TEAM", linkToTeam.getTargetTableName() );
	}

	//===================================================================================================
	
	private void checkChangeLog(ChangeLog changeLog, RepositoryModel repositoryModel ) {
		for ( ChangeOnEntity change : changeLog.getChanges() ) {
			switch ( change.getChangeType() ) {
			case CREATED :
//				assertTrue( repositoryModel.getEntityByName(change.getEntityCreated().getName()) == change.getEntityCreated() );
				assertTrue( repositoryModel.getEntityByTableName(change.getEntityCreated().getDatabaseTable()) == change.getEntityCreated() );
				break;
			case UPDATED :
//				assertTrue( repositoryModel.getEntityByName(change.getEntityAfter().getName()) == change.getEntityAfter() );
				assertTrue( repositoryModel.getEntityByTableName(change.getEntityAfter().getDatabaseTable()) == change.getEntityAfter() );
				break;
			case DELETED :
//				assertTrue( repositoryModel.getEntityByName(change.getEntityDeleted().getName()) == null );
				assertTrue( repositoryModel.getEntityByTableName(change.getEntityDeleted().getDatabaseTable()) == null );
				break;
			}
		}
	}

	private void printLinks(LinkInDbModel[] links) {
		for ( LinkInDbModel link : links ) {
			System.out.println(" . Link : "  + link);
//			System.out.println(" . " + link.getJavaFieldName() );
			System.out.println(" . " + link.getFieldName() );
		}
	}

}
