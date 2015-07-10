package org.telosys.tools.repository.conversion.wrapper;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.repository.conversion.Wrappers;
import org.telosys.tools.repository.model.EntityInDbModel;
import org.telosys.tools.repository.persistence.util.RepositoryConst;
import org.telosys.tools.repository.persistence.util.Xml;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EntityWrapperTest {

	@Test
	public void test1() throws TelosysToolsException {
		System.out.println("test1");

		EntityInDbModel entity = createEntity();
		Document doc = Xml.createDomDocument();

		Element element = Wrappers.ENTITY_WRAPPER.getXmlDesc(entity, doc);
		
		checkAttribute(element, RepositoryConst.TABLE_NAME, "MYTABLE");
		checkAttribute(element, RepositoryConst.TABLE_CATALOG, "MYCATALOG");
		checkAttribute(element, RepositoryConst.TABLE_SCHEMA, "MYSCHEMA");
		checkAttribute(element, RepositoryConst.TABLE_DATABASE_TYPE, "TABLE");
		checkAttribute(element, RepositoryConst.TABLE_JAVA_BEAN, "MyClass");
	}

	private EntityInDbModel createEntity() {
		EntityInDbModel entity = new EntityInDbModel();
		entity.setDatabaseTable("MYTABLE");
		entity.setDatabaseCatalog("MYCATALOG");
		entity.setDatabaseSchema("MYSCHEMA");
		entity.setDatabaseType("TABLE");
		entity.setClassName("MyClass");
		
//		entity.storeAttribute(attribute);
//		entity.storeForeignKey(foreignKey);
//		entity.storeLink(link);
		
		return entity ;
	}

	private void checkAttribute(Element element, String attributeName, String expectedValue) {
		Attr attribute = element.getAttributeNode(attributeName);
		assertNotNull( attribute );
		assertEquals(expectedValue, attribute.getNodeValue() );
	}
}
