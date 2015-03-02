package org.telosys.tools.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.repository.changelog.ChangeLog;
import org.telosys.tools.repository.changelog.ChangeOnEntity;
import org.telosys.tools.repository.changelog.ChangeOnForeignKey;
import org.telosys.tools.repository.changelog.ChangeType;
import org.telosys.tools.repository.model.ForeignKey;
import org.telosys.tools.repository.model.RepositoryModel;

public class RepositoryUpdatorTest2 extends AbstractTestCase {
	
	/**
	 * TEST : Foreign Key added
	 */
	@Test
	public void test91() throws TelosysToolsException {
		printSeparator("test91");
		//--------------------------------------------------------------------
		// Script 2 :
		// Init  : Tables "customer", "country" (no FK, no link)
		// Alter : Just add a FK customer --> country
		//--------------------------------------------------------------------
		UpdateResult result = generateAndUpdateRepositoryModel(91);
		ChangeLog changeLog = result.getChangeLog();
		RepositoryModel repositoryModel = result.getRepositoryModel();
		
		printChangeLog(changeLog);
		
		//--- Check ChangeLog
		assertEquals(1, changeLog.getNumberOfEntities()); // 1 entity changed		
		assertEquals(0, changeLog.getNumberOfEntitiesCreated() );
		assertEquals(1, changeLog.getNumberOfEntitiesUpdated() );
		assertEquals(0, changeLog.getNumberOfEntitiesDeleted() );
		
		ChangeOnEntity changeOnCustomerEntity= changeLog.getChangeByEntityName("CUSTOMER");
		assertNotNull(changeOnCustomerEntity );
		List<ChangeOnForeignKey> changesOnCustomerForeignKey = changeOnCustomerEntity.getChangesOnForeignKey() ;
		assertEquals(1, changesOnCustomerForeignKey.size() );
		assertEquals(ChangeType.CREATED, changeOnCustomerEntity.getChangesOnForeignKey().get(0).getChangeType() );
		
		assertEquals(2, repositoryModel.getNumberOfEntities() );
		
		assertEquals(1, repositoryModel.getEntityByName("COUNTRY").getLinks().length );
		assertEquals(1, repositoryModel.getEntityByName("CUSTOMER").getLinks().length );
	}

	/**
	 * TEST : Foreign Key removed
	 */
	@Test
	public void test92() throws TelosysToolsException {
		printSeparator("test92");
		//--------------------------------------------------------------------
		// Init  : Tables "customer", "country" (1 FK)
		// Alter : Just remove a FK customer --> country
		//--------------------------------------------------------------------
		UpdateResult result = generateAndUpdateRepositoryModel(92);
		ChangeLog changeLog = result.getChangeLog();
		RepositoryModel repositoryModel = result.getRepositoryModel();
		
		printChangeLog(changeLog);
		
		//--- Check ChangeLog
		assertEquals(1, changeLog.getNumberOfEntities()); // 1 entity changed		
		assertEquals(0, changeLog.getNumberOfEntitiesCreated() );
		assertEquals(1, changeLog.getNumberOfEntitiesUpdated() );
		assertEquals(0, changeLog.getNumberOfEntitiesDeleted() );
		
		ChangeOnEntity changeOnCustomerEntity= changeLog.getChangeByEntityName("CUSTOMER");
		assertNotNull(changeOnCustomerEntity );
		List<ChangeOnForeignKey> changesOnCustomerForeignKey = changeOnCustomerEntity.getChangesOnForeignKey() ;
		assertEquals(1, changesOnCustomerForeignKey.size() );
		assertEquals(ChangeType.DELETED, changeOnCustomerEntity.getChangesOnForeignKey().get(0).getChangeType() );
		
		assertEquals(2, repositoryModel.getNumberOfEntities() );
		
		assertEquals(0, repositoryModel.getEntityByName("COUNTRY").getLinks().length );
		assertEquals(0, repositoryModel.getEntityByName("CUSTOMER").getLinks().length );
	}

	/**
	 * TEST : Foreign Key updated (same FK name, but different)
	 */
	@Test
	public void test93() throws TelosysToolsException {
		printSeparator("test93");
		//--------------------------------------------------------------------
		// Init  : Tables "customer", "country", "department" (1 FK)
		// Alter : Just remove "FK1" customer --> country, add "FK1" customer --> department
		//--------------------------------------------------------------------
		UpdateResult result = generateAndUpdateRepositoryModel(93);
		ChangeLog changeLog = result.getChangeLog();
		RepositoryModel repositoryModel = result.getRepositoryModel();
		
		printChangeLog(changeLog);
		
		//--- Check ChangeLog
		assertEquals(1, changeLog.getNumberOfEntities()); // 1 entity changed		
		assertEquals(0, changeLog.getNumberOfEntitiesCreated() );
		assertEquals(1, changeLog.getNumberOfEntitiesUpdated() );
		assertEquals(0, changeLog.getNumberOfEntitiesDeleted() );
		
		ChangeOnEntity changeOnCustomerEntity= changeLog.getChangeByEntityName("CUSTOMER");
		assertNotNull(changeOnCustomerEntity );
		List<ChangeOnForeignKey> changesOnCustomerForeignKey = changeOnCustomerEntity.getChangesOnForeignKey() ;
		assertEquals(1, changesOnCustomerForeignKey.size() );
		assertEquals(ChangeType.UPDATED, changeOnCustomerEntity.getChangesOnForeignKey().get(0).getChangeType() );
		
		assertEquals(3, repositoryModel.getNumberOfEntities() );
		
		//--- 1 Foreign Key
		assertEquals(1, repositoryModel.getEntityByName("CUSTOMER").getForeignKeys().length ) ;
		//--- 1 Foreign Key referencing "DEPARTMENT"
		ForeignKey fk = repositoryModel.getEntityByName("CUSTOMER").getForeignKey("FK1");
		assertNotNull(fk);
		assertEquals("DEPARTMENT", fk.getTableRef() ) ;
		
		//--- Links
		assertEquals(0, repositoryModel.getEntityByName("COUNTRY").getLinks().length );
		assertEquals(1, repositoryModel.getEntityByName("CUSTOMER").getLinks().length );
		assertEquals(1, repositoryModel.getEntityByName("DEPARTMENT").getLinks().length );
	}

}
