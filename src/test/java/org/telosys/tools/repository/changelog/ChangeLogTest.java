package org.telosys.tools.repository.changelog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.repository.model.Column;
import org.telosys.tools.repository.model.Entity;
import org.telosys.tools.repository.model.ForeignKey;

public class ChangeLogTest {
	
	
	@Test
	public void test1() throws TelosysToolsException {
		
		System.out.println("test1");
		ChangeLog changeLog = new ChangeLog();
		assertNotNull( changeLog.getDate() );
		List<ChangeOnEntity> allChanges = changeLog.getChanges();
		assertNotNull( allChanges );
		assertTrue( allChanges.size() == 0 );
		
		for ( ChangeType changeType : ChangeType.values() ) {
			System.out.println("getChanges("+ changeType +")");
			assertNotNull( changeLog.getChangesByType(changeType) );
		}
		assertNotNull( changeLog.getChangesByType(ChangeType.UPDATED) );
		assertNotNull( changeLog.getChangesByType(ChangeType.DELETED) );
		
		
		System.out.println("test entity updated");
		Entity entityUpdatedBefore = new Entity("FOO");
		Entity entityUpdatedAfter  = new Entity("FOO");
		ChangeOnEntity change = new ChangeOnEntity(ChangeType.UPDATED, entityUpdatedBefore, entityUpdatedAfter);
		assertTrue( change.getEntityBefore() == entityUpdatedBefore );
		assertTrue( change.getEntityAfter()  == entityUpdatedAfter );
		
		//change.addChangeOnColumn(changeOnColumn);
		changeLog.log(change);
		assertNotNull( changeLog.getChangesByType(ChangeType.UPDATED).size() == 1 );

	}
	
	@Test 
	public void testCreated() throws TelosysToolsException {
		
		System.out.println("testCreated");
		ChangeLog changeLog = new ChangeLog();

		Entity entityCreated = new Entity();
		
		try {
			new ChangeOnEntity(ChangeType.CREATED, entityCreated, entityCreated); // IMPOSSIBLE
			fail("Exception expected");
		} catch (Exception e) {
			// Expected exception 
		}

		try {
			new ChangeOnEntity(ChangeType.CREATED, entityCreated, null); // IMPOSSIBLE
			fail("Exception expected");
		} catch (Exception e) {
			// Expected exception 
		}
		
		ChangeOnEntity changeOnEntity = new ChangeOnEntity(ChangeType.CREATED, null, entityCreated);  // CORRECT
		assertTrue( changeOnEntity.getEntityBefore() == null );
		assertTrue( changeOnEntity.getEntityAfter()  == entityCreated );

		changeLog.log(changeOnEntity);
		assertTrue( changeLog.getChanges().size() == 1 );
		assertTrue( changeLog.getChangesByType(ChangeType.CREATED).size() == 1 );
		assertTrue( changeLog.getChangesByType(ChangeType.DELETED).size() == 0 );
		assertTrue( changeLog.getChangesByType(ChangeType.UPDATED).size() == 0 );
	}

	@Test
	public void testUpdated() throws TelosysToolsException {
		
		System.out.println("testUpdated");
		ChangeLog changeLog = new ChangeLog();

		Entity entityUpdatedBefore = new Entity();
		Entity entityUpdatedAfter  = new Entity();
		ChangeOnEntity changeOnEntity = new ChangeOnEntity(ChangeType.UPDATED, entityUpdatedBefore, entityUpdatedAfter);
		assertTrue( changeOnEntity.getEntityBefore() == entityUpdatedBefore );
		assertTrue( changeOnEntity.getEntityAfter()  == entityUpdatedAfter );
		
		Column columnBefore = new Column();
		Column columnAfter  = new Column();
		changeOnEntity.addChangeOnColumn( new ChangeOnColumn(ChangeType.UPDATED, columnBefore, columnAfter ) );
		changeLog.log(changeOnEntity);
		assertNotNull( changeLog.getChangesByType(ChangeType.UPDATED).size() == 1 );
		
		List<ChangeOnEntity> updatedList = changeLog.getChangesByType(ChangeType.UPDATED);
		assertTrue( updatedList.size()  == 1 );
		for ( ChangeOnEntity change : updatedList ) {
			assertTrue( change.getChangesOnColumn().size() == 1 ) ;
		}
	}

	@Test
	public void testUpdatedWithFK() throws TelosysToolsException {
		
		System.out.println("testUpdatedWithFK");

		Entity entityUpdatedBefore = new Entity();
		Entity entityUpdatedAfter  = new Entity();
		ChangeOnEntity changeOnEntity = new ChangeOnEntity(ChangeType.UPDATED, entityUpdatedBefore, entityUpdatedAfter);
		assertTrue( changeOnEntity.getEntityBefore() == entityUpdatedBefore );
		assertTrue( changeOnEntity.getEntityAfter()  == entityUpdatedAfter );
		assertEquals(0, changeOnEntity.getNumberOfChanges() );
		
//		Column columnBefore = new Column();
//		Column columnAfter  = new Column();
//		changeOnEntity.addChangeOnColumn( new ChangeOnColumn(ChangeType.UPDATED, columnBefore, columnAfter ) );
//		changeLog.log(changeOnEntity);
//		assertNotNull( changeLog.getChangesByType(ChangeType.UPDATED).size() == 1 );
		
		//--- Foreign Key created
		ForeignKey fk = new ForeignKey();
		ChangeOnForeignKey changeOnForeignKey = new ChangeOnForeignKey(ChangeType.CREATED, null, fk);
		changeOnEntity.addChangeOnForeignKey(changeOnForeignKey);
		assertEquals(1, changeOnEntity.getNumberOfChanges() );
		
		ChangeLog changeLog = new ChangeLog();
		changeLog.log(changeOnEntity);
		assertEquals(1, changeOnEntity.getNumberOfChanges() );
		
		List<ChangeOnEntity> updatedList = changeLog.getChangesByType(ChangeType.UPDATED);
		assertEquals(1, updatedList.size() );
		changeOnEntity = updatedList.get(0);
		assertEquals(0, changeOnEntity.getChangesOnColumn().size());
		assertEquals(1, changeOnEntity.getChangesOnForeignKey().size());
		assertEquals(1, changeOnEntity.getNumberOfChanges() );
	}

	@Test
	public void testDeleted() throws TelosysToolsException {
		
		System.out.println("testDeleted");
		ChangeLog changeLog = new ChangeLog();

		Entity entityDeleted = new Entity();
		try {
			new ChangeOnEntity(ChangeType.DELETED, entityDeleted, entityDeleted); // IMPOSSIBLE
			fail("Exception expected");
		} catch (Exception e) {
			// Expected exception 
		}

		try {
			new ChangeOnEntity(ChangeType.DELETED, null, entityDeleted); // IMPOSSIBLE
			fail("Exception expected");
		} catch (Exception e) {
			// Expected exception 
		}

		ChangeOnEntity changeOnEntity = new ChangeOnEntity(ChangeType.DELETED, entityDeleted, null); // CORRECT
		assertTrue( changeOnEntity.getEntityBefore() == entityDeleted );
		assertTrue( changeOnEntity.getEntityAfter()  == null );

		changeLog.log(changeOnEntity); // CORRECT
		assertTrue( changeLog.getChanges().size() == 1 );
		assertTrue( changeLog.getChangesByType(ChangeType.CREATED).size() == 0 );
		assertTrue( changeLog.getChangesByType(ChangeType.DELETED).size() == 1 );
		assertTrue( changeLog.getChangesByType(ChangeType.UPDATED).size() == 0 );
	}

}
