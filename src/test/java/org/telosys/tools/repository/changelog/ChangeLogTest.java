package org.telosys.tools.repository.changelog;

import java.util.List;

import junit.framework.Assert;

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
		Assert.assertNotNull( changeLog.getDate() );
		List<ChangeOnEntity> allChanges = changeLog.getChanges();
		Assert.assertNotNull( allChanges );
		Assert.assertTrue( allChanges.size() == 0 );
		
		for ( ChangeType changeType : ChangeType.values() ) {
			System.out.println("getChanges("+ changeType +")");
			Assert.assertNotNull( changeLog.getChangesByType(changeType) );
		}
		Assert.assertNotNull( changeLog.getChangesByType(ChangeType.UPDATED) );
		Assert.assertNotNull( changeLog.getChangesByType(ChangeType.DELETED) );
		
		
		System.out.println("test entity updated");
		Entity entityUpdatedBefore = new Entity("FOO");
		Entity entityUpdatedAfter  = new Entity("FOO");
		ChangeOnEntity change = new ChangeOnEntity(ChangeType.UPDATED, entityUpdatedBefore, entityUpdatedAfter);
		Assert.assertTrue( change.getEntityBefore() == entityUpdatedBefore );
		Assert.assertTrue( change.getEntityAfter()  == entityUpdatedAfter );
		
		//change.addChangeOnColumn(changeOnColumn);
		changeLog.log(change);
		Assert.assertNotNull( changeLog.getChangesByType(ChangeType.UPDATED).size() == 1 );

	}
	
	@Test 
	public void testCreated() throws TelosysToolsException {
		
		System.out.println("testCreated");
		ChangeLog changeLog = new ChangeLog();

		Entity entityCreated = new Entity();
		
		try {
			new ChangeOnEntity(ChangeType.CREATED, entityCreated, entityCreated); // IMPOSSIBLE
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// Expected exception 
		}

		try {
			new ChangeOnEntity(ChangeType.CREATED, entityCreated, null); // IMPOSSIBLE
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// Expected exception 
		}
		
		ChangeOnEntity changeOnEntity = new ChangeOnEntity(ChangeType.CREATED, null, entityCreated);  // CORRECT
		Assert.assertTrue( changeOnEntity.getEntityBefore() == null );
		Assert.assertTrue( changeOnEntity.getEntityAfter()  == entityCreated );

		changeLog.log(changeOnEntity);
		Assert.assertTrue( changeLog.getChanges().size() == 1 );
		Assert.assertTrue( changeLog.getChangesByType(ChangeType.CREATED).size() == 1 );
		Assert.assertTrue( changeLog.getChangesByType(ChangeType.DELETED).size() == 0 );
		Assert.assertTrue( changeLog.getChangesByType(ChangeType.UPDATED).size() == 0 );
	}

	@Test
	public void testUpdated() throws TelosysToolsException {
		
		System.out.println("testUpdated");
		ChangeLog changeLog = new ChangeLog();

		Entity entityUpdatedBefore = new Entity();
		Entity entityUpdatedAfter  = new Entity();
		ChangeOnEntity changeOnEntity = new ChangeOnEntity(ChangeType.UPDATED, entityUpdatedBefore, entityUpdatedAfter);
		Assert.assertTrue( changeOnEntity.getEntityBefore() == entityUpdatedBefore );
		Assert.assertTrue( changeOnEntity.getEntityAfter()  == entityUpdatedAfter );
		
		Column columnBefore = new Column();
		Column columnAfter  = new Column();
		changeOnEntity.addChangeOnColumn( new ChangeOnColumn(ChangeType.UPDATED, columnBefore, columnAfter ) );
		changeLog.log(changeOnEntity);
		Assert.assertNotNull( changeLog.getChangesByType(ChangeType.UPDATED).size() == 1 );
		
		List<ChangeOnEntity> updatedList = changeLog.getChangesByType(ChangeType.UPDATED);
		Assert.assertTrue( updatedList.size()  == 1 );
		for ( ChangeOnEntity change : updatedList ) {
			Assert.assertTrue( change.getChangesOnColumn().size() == 1 ) ;
		}
	}

	@Test
	public void testDeleted() throws TelosysToolsException {
		
		System.out.println("testDeleted");
		ChangeLog changeLog = new ChangeLog();

		Entity entityDeleted = new Entity();
		try {
			new ChangeOnEntity(ChangeType.DELETED, entityDeleted, entityDeleted); // IMPOSSIBLE
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// Expected exception 
		}

		try {
			new ChangeOnEntity(ChangeType.DELETED, null, entityDeleted); // IMPOSSIBLE
			Assert.fail("Exception expected");
		} catch (Exception e) {
			// Expected exception 
		}

		ChangeOnEntity changeOnEntity = new ChangeOnEntity(ChangeType.DELETED, entityDeleted, null); // CORRECT
		Assert.assertTrue( changeOnEntity.getEntityBefore() == entityDeleted );
		Assert.assertTrue( changeOnEntity.getEntityAfter()  == null );

		changeLog.log(changeOnEntity); // CORRECT
		Assert.assertTrue( changeLog.getChanges().size() == 1 );
		Assert.assertTrue( changeLog.getChangesByType(ChangeType.CREATED).size() == 0 );
		Assert.assertTrue( changeLog.getChangesByType(ChangeType.DELETED).size() == 1 );
		Assert.assertTrue( changeLog.getChangesByType(ChangeType.UPDATED).size() == 0 );
	}

}
