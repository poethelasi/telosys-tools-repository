package org.telosys.tools.repository.model;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.commons.ObjectUtil;
import org.telosys.tools.generic.model.Cardinality;

public class EntityTest {
	
	private AttributeInDbModel getColumn(int i) {
		AttributeInDbModel c = new AttributeInDbModel();
		c.setDatabaseName("COL_NAME"+i);
		c.setDatabaseNotNull(true);
		c.setDatabaseComment("my comment " + i);
		return c;
	}
	
	private ForeignKeyInDbModel getForeignKey(int i) {
		ForeignKeyInDbModel fk = new ForeignKeyInDbModel();
		fk.setName("FK"+i);
		for ( int n = 1 ; n <= 3 ; n++ ) {
			ForeignKeyColumnInDbModel fkcol = new ForeignKeyColumnInDbModel();
			fkcol.setColumnName("FK_COL_NAME" + n);
			fkcol.setTableName("FK_TABLE_NAME" + n);
			
			fk.storeForeignKeyColumn(fkcol);
		}
		return fk;
	}
	
	private LinkInDbModel getLink(int i) {
		LinkInDbModel link = new LinkInDbModel();
		link.setId("id"+i);
//		link.setJavaFieldName("author"+i);
		link.setFieldName("author"+i);
//		link.setCardinality("ManyToOne"+i);
		link.setCardinality(Cardinality.MANY_TO_MANY);
		JoinTableInDbModel joinTable = new JoinTableInDbModel() ;
		joinTable.setCatalog("CATALOG");
		link.setJoinTable(joinTable);
		link.setJoinColumns(getJoinColumns(2));
		return link;
	}
	
//	private JoinColumnsInDbModel getJoinColumns(int i) {
	private List<JoinColumnInDbModel> getJoinColumns(int i) { // v 3.0.0
//		JoinColumnsInDbModel joinColumns = new JoinColumnsInDbModel();
		List<JoinColumnInDbModel> joinColumns = new LinkedList<JoinColumnInDbModel>(); // v 3.0.0
		for ( int n = 1 ; n <= 3 ; n++ ) {
			JoinColumnInDbModel jc = new JoinColumnInDbModel();
			jc.setName("JOIN_COLUMN" + i);
			jc.setReferencedColumnName("REF_COL" + i);
			
			joinColumns.add(jc);
		}
		return joinColumns;
	}
	
	@Test
	public void test() {
		EntityInDbModel entity = new EntityInDbModel();
//		e.setBeanJavaClass("Book");
//		e.setCatalog("MYCATALOG");
//		e.setName("BOOK");
		entity.setClassName("Book");
		entity.setDatabaseCatalog("MYCATALOG");
		entity.setDatabaseTable("BOOK");
		for ( int i = 1 ; i <= 5 ; i++ ) {
			entity.storeAttribute(getColumn(i));
		}
		for ( int i = 1 ; i <= 5 ; i++ ) {
			entity.storeForeignKey(getForeignKey(i));
		}
		for ( int i = 1 ; i <= 3 ; i++ ) {
			entity.storeLink(getLink(i));
		}		
		
		EntityInDbModel e2 = ObjectUtil.deepCopy(entity);
		for ( int i = 6 ; i <= 8 ; i++ ) {
			entity.storeAttribute(getColumn(i));
		}
		entity.removeAttribute(entity.getAttributeByColumnName("COL_NAME2"));
//		e.setName("BOOK_UPDATED");
		entity.setDatabaseTable("BOOK_UPDATED");
		Assert.assertTrue(entity.getAttributesArray().length == 7 );
		
//		Assert.assertTrue(e2.getName().equals("BOOK"));
		Assert.assertTrue(e2.getDatabaseTable().equals("BOOK"));
		Assert.assertTrue(e2.getAttributesArray().length == 5 );
	}

}
