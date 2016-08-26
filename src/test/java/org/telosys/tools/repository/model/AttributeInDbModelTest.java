package org.telosys.tools.repository.model;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.commons.ObjectUtil;

public class AttributeInDbModelTest {

	@Test
	public void testDeepCopy() {
		
		EntityInDbModel entity = new EntityInDbModel("Employee","EMPLOYEE");
		AttributeInDbModel attribute = new AttributeInDbModel(entity);
		
		// Init instance 1
		attribute.setDatabaseName("FIRST_NAME");
		attribute.setDatabaseNotNull(true);
		attribute.setDatabaseComment("my comment");
		Assert.assertEquals("Employee", attribute.getEntity().getClassName() ) ;
		Assert.assertEquals("EMPLOYEE", attribute.getEntity().getDatabaseTable() ) ;
		
		// Deep copy in instance 2
		AttributeInDbModel attribute2 = ObjectUtil.deepCopy(attribute);
		
		// Change instance 1
		attribute.setDatabaseName("OTHER");
		attribute.setDatabaseNotNull(false);
		entity.setClassName("Employee2");
		entity.setDatabaseTable("EMPLOYEE2");
		Assert.assertEquals("Employee2", attribute.getEntity().getClassName() ) ;
		Assert.assertEquals("EMPLOYEE2", attribute.getEntity().getDatabaseTable() ) ;
		
		// Check everything is unchanged in instance 2
		Assert.assertEquals("FIRST_NAME", attribute2.getDatabaseName() );
		Assert.assertEquals(true,         attribute2.isDatabaseNotNull() );
		Assert.assertEquals("Employee",   attribute2.getEntity().getClassName() ) ;
		Assert.assertEquals("EMPLOYEE",   attribute2.getEntity().getDatabaseTable() ) ;
	}

}
