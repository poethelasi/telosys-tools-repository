package org.telosys.tools.repository.model;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.commons.ObjectUtil;

public class ColumnTest {

	@Test
	public void test() {
		Column c = new Column();
		c.setDatabaseName("FIRST_NAME");
		c.setDatabaseNotNull(true);
		c.setDatabaseComment("my comment");
		
		Column c2 = ObjectUtil.deepCopy(c);
		
		c.setDatabaseName("OTHER");
		
		Assert.assertTrue(c2.getDatabaseName().equals("FIRST_NAME"));
	}

}
