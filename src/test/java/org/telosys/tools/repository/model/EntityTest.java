package org.telosys.tools.repository.model;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.telosys.tools.commons.ObjectUtil;
import org.telosys.tools.generic.model.Cardinality;

public class EntityTest {
	
	private AttributeInDbModel buildAttribute(int i) {
		AttributeInDbModel c = new AttributeInDbModel();
		c.setDatabaseName("COL_NAME"+i);
		c.setDatabaseNotNull(true);
		c.setDatabaseComment("my comment " + i);
		return c;
	}
	
	private ForeignKeyInDbModel buildForeignKey(int i) {
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
	
	private LinkInDbModel buildLink(int i) {
		LinkInDbModel link = new LinkInDbModel();
		link.setId("id"+i);
//		link.setJavaFieldName("author"+i);
		link.setFieldName("author"+i);
//		link.setCardinality("ManyToOne"+i);
		link.setCardinality(Cardinality.MANY_TO_MANY);
		JoinTableInDbModel joinTable = new JoinTableInDbModel() ;
		joinTable.setCatalog("CATALOG");
		link.setJoinTable(joinTable);
		link.setJoinColumns(buildJoinColumns(2));
		return link;
	}
	
//	private JoinColumnsInDbModel getJoinColumns(int i) {
	private List<JoinColumnInDbModel> buildJoinColumns(int i) { // v 3.0.0
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
			entity.storeAttribute(buildAttribute(i));
		}
		for ( int i = 1 ; i <= 5 ; i++ ) {
			entity.storeForeignKey(buildForeignKey(i));
		}
		for ( int i = 1 ; i <= 3 ; i++ ) {
			entity.storeLink(buildLink(i));
		}		
		
		//--- Clone entity
		EntityInDbModel e2 = ObjectUtil.deepCopy(entity);
		
		//--- Add 3 new attributes
		for ( int i = 6 ; i <= 8 ; i++ ) { 
			entity.storeAttribute(buildAttribute(i));
		}
		//--- Remove 1 attribute
		entity.removeAttribute(entity.getAttributeByColumnName("COL_NAME2"));
		//--- Change db table name
		entity.setDatabaseTable("BOOK_UPDATED");


		//----- Original entity after changes
		assertEquals("BOOK_UPDATED", entity.getDatabaseTable() );
		//--- Check attributes 
		assertEquals(7, entity.getAttributesCount() );
		assertEquals(7, entity.getAttributesArray().length);
		assertEquals(7, entity.getAttributes().size());
		//--- Check links 
		assertEquals(3, entity.getLinksCount());
		assertEquals(3, entity.getLinksArray().length);
		assertEquals(3, entity.getLinks().size());
		
		//----- Cloned entity
		assertEquals("BOOK", e2.getDatabaseTable() );
		//--- Check attributes 
		assertEquals(5, e2.getAttributesCount() );
		assertEquals(5, e2.getAttributesArray().length);
		assertEquals(5, e2.getAttributes().size());
		
	}

}
