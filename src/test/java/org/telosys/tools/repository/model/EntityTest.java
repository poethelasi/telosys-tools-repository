package org.telosys.tools.repository.model;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.commons.ObjectUtil;

public class EntityTest {
	
	private Column getColumn(int i) {
		Column c = new Column();
		c.setDatabaseName("COL_NAME"+i);
		c.setDatabaseNotNull(true);
		c.setDatabaseComment("my comment " + i);
		return c;
	}
	
	private ForeignKey getForeignKey(int i) {
		ForeignKey fk = new ForeignKey();
		fk.setName("FK"+i);
		for ( int n = 1 ; n <= 3 ; n++ ) {
			ForeignKeyColumn fkcol = new ForeignKeyColumn();
			fkcol.setColumnName("FK_COL_NAME" + n);
			fkcol.setTableName("FK_TABLE_NAME" + n);
			
			fk.storeForeignKeyColumn(fkcol);
		}
		return fk;
	}
	
	private Link getLink(int i) {
		Link link = new Link();
		link.setId("id"+i);
		link.setJavaFieldName("author"+i);
		link.setCardinality("ManyToOne"+i);
		JoinTable jt = new JoinTable() ;
		jt.setCatalog("CATALOG");
		link.setJoinTable(jt);
		link.setJoinColumns(getJoinColumns(2));
		return link;
	}
	
	private JoinColumns getJoinColumns(int i) {
		JoinColumns joinColumns = new JoinColumns();
		for ( int n = 1 ; n <= 3 ; n++ ) {
			JoinColumn jc = new JoinColumn();
			jc.setName("JOIN_COLUMN" + i);
			jc.setReferencedColumnName("REF_COL" + i);
			
			joinColumns.add(jc);
		}
		return joinColumns;
	}
	
	@Test
	public void test() {
		Entity e = new Entity();
		e.setBeanJavaClass("Book");
		e.setCatalog("MYCATALOG");
		e.setName("BOOK");
		for ( int i = 1 ; i <= 5 ; i++ ) {
			e.storeColumn(getColumn(i));
		}
		for ( int i = 1 ; i <= 5 ; i++ ) {
			e.storeForeignKey(getForeignKey(i));
		}
		for ( int i = 1 ; i <= 3 ; i++ ) {
			e.storeLink(getLink(i));
		}		
		
		Entity e2 = ObjectUtil.deepCopy(e);
		for ( int i = 6 ; i <= 8 ; i++ ) {
			e.storeColumn(getColumn(i));
		}
		e.removeColumn(e.getColumn("COL_NAME2"));
		e.setName("BOOK_UPDATED");
		Assert.assertTrue(e.getColumns().length == 7 );
		
		Assert.assertTrue(e2.getName().equals("BOOK"));
		Assert.assertTrue(e2.getColumns().length == 5 );
	}

}
