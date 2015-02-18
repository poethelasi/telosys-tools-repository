package org.telosys.tools.repository.changelog;

import java.util.List;

import static org.junit.Assert.*;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.repository.changelog.ChangeLog;
import org.telosys.tools.repository.changelog.ChangeOnColumn;
import org.telosys.tools.repository.changelog.ChangeOnEntity;
import org.telosys.tools.repository.changelog.ChangeType;
import org.telosys.tools.repository.model.Column;
import org.telosys.tools.repository.model.Entity;

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
