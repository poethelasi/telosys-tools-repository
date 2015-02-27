package org.telosys.tools.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.repository.changelog.ChangeLog;
import org.telosys.tools.repository.changelog.ChangeOnEntity;
import org.telosys.tools.repository.changelog.ChangeOnForeignKey;
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
		
		
		assertEquals(2, repositoryModel.getNumberOfEntities() );
		
		
		
		//assertEquals(1, repositoryModel.getEntityByName("COUNTRY").getLinks().length );
		assertEquals(1, repositoryModel.getEntityByName("CUSTOMER").getLinks().length );
	}


}
