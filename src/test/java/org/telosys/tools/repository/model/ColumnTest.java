package org.telosys.tools.repository.model;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.commons.ObjectUtil;

public class ColumnTest {

	@Test
	public void testDeepCopy() {
		AttributeInDbModel c = new AttributeInDbModel();
		c.setDatabaseName("FIRST_NAME");
		c.setDatabaseNotNull(true);
		c.setDatabaseComment("my comment");
		
		AttributeInDbModel c2 = ObjectUtil.deepCopy(c);
		
		c.setDatabaseName("OTHER");
		
		Assert.assertTrue(c2.getDatabaseName().equals("FIRST_NAME"));
	}

}
