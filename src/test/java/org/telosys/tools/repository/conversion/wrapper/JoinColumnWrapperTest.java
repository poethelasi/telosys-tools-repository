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
import org.telosys.tools.repository.model.JoinColumnInDbModel;
import org.telosys.tools.repository.persistence.util.RepositoryConst;
import org.telosys.tools.repository.persistence.util.Xml;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class JoinColumnWrapperTest {

	@Test
	public void test1() throws TelosysToolsException {
		System.out.println("test1");

		JoinColumnInDbModel joinColumn = createJoinColumn();
		Document doc = Xml.createDomDocument();

		Element element = Wrappers.JOIN_COLUMN_WRAPPER.getXmlDesc(joinColumn, doc);
		
		checkAttribute(element, RepositoryConst.JOIN_COLUMN_NAME, "MYNAME");
		checkAttribute(element, RepositoryConst.JOIN_COLUMN_REFERENCEDCOLUMNNAME, "MYREFNAME");
		checkAttribute(element, RepositoryConst.JOIN_COLUMN_INSERTABLE, "true");
		checkAttribute(element, RepositoryConst.JOIN_COLUMN_NULLABLE, "true");
		checkAttribute(element, RepositoryConst.JOIN_COLUMN_UNIQUE, "true");
		checkAttribute(element, RepositoryConst.JOIN_COLUMN_UPDATABLE, "true");
	}

	private JoinColumnInDbModel createJoinColumn() {
		JoinColumnInDbModel joinColumn = new JoinColumnInDbModel();

		joinColumn.setName("MYNAME");
		joinColumn.setReferencedColumnName("MYREFNAME");
		joinColumn.setInsertable(true);
		joinColumn.setNullable(true);
		joinColumn.setUnique(true);
		joinColumn.setUpdatable(true);
		return joinColumn ;
	}

	private void checkAttribute(Element element, String attributeName, String expectedValue) {
		Attr attribute = element.getAttributeNode(attributeName);
		System.out.println("checkAttribute '" + attributeName + "' : value = '" + attribute.getNodeValue() + "'" );
		assertNotNull( attribute );
		assertEquals(expectedValue, attribute.getNodeValue() );
	}
}
