package org.telosys.tools.repository.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.telosys.tools.generic.model.Cardinality;
import org.telosys.tools.generic.model.FetchType;
import org.telosys.tools.generic.model.Optional;

public class LinkInDbModelTest {

	@Test
	public void testInitialState() {
		LinkInDbModel link = new LinkInDbModel();
		
		assertNotNull(link.getCardinality());
		assertEquals(Cardinality.UNDEFINED, link.getCardinality());
		
		assertNotNull(link.getFetchType());
		assertEquals(FetchType.UNDEFINED, link.getFetchType());
		
		assertNotNull(link.getOptional());
		assertEquals(Optional.UNDEFINED, link.getOptional());
		
		assertNotNull(link.getCascadeOptions());
		assertFalse(link.getCascadeOptions().isCascadeAll());
		assertFalse(link.getCascadeOptions().isCascadeMerge());
		assertFalse(link.getCascadeOptions().isCascadePersist());
		assertFalse(link.getCascadeOptions().isCascadeRefresh());
		assertFalse(link.getCascadeOptions().isCascadeRemove());
	}

	@Test
	public void testSetNullValues() {
		LinkInDbModel link = new LinkInDbModel();
		
		//--- Check set null = set UNDEFINED
		link.setCardinality(null);
		System.out.println( " getCardinality : " + link.getCardinality() );
		assertNotNull(link.getCardinality());
		assertEquals(Cardinality.UNDEFINED, link.getCardinality());
		
		link.setFetchType(null);
		System.out.println( " getFetchType : " + link.getFetchType() );
		assertNotNull(link.getFetchType());
		assertEquals(FetchType.UNDEFINED, link.getFetchType());
		
		link.setOptional(null);
		System.out.println( " getOptional : " + link.getOptional() );
		assertNotNull(link.getOptional());
		assertEquals(Optional.UNDEFINED, link.getOptional());
		
		//--- Check set null = set void list of cascade options
		link.setCascadeOptions(null);
		System.out.println( " getCascadeOptions : " + link.getCascadeOptions() );
		assertNotNull(link.getCascadeOptions());
		assertFalse(link.getCascadeOptions().isCascadeAll());
		assertFalse(link.getCascadeOptions().isCascadeMerge());
		assertFalse(link.getCascadeOptions().isCascadePersist());
		assertFalse(link.getCascadeOptions().isCascadeRefresh());
		assertFalse(link.getCascadeOptions().isCascadeRemove());
	}

}
