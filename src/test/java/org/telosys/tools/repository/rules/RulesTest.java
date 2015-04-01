package org.telosys.tools.repository.rules;

import static org.junit.Assert.assertEquals;

import java.sql.Types;

import org.junit.Test;
import org.telosys.tools.generic.model.Cardinality;
import org.telosys.tools.repository.model.AttributeInDbModel;
import org.telosys.tools.repository.model.EntityInDbModel;
import org.telosys.tools.repository.model.LinkInDbModel;

public class RulesTest {

	@Test
	public void testEntityClassName() {
		
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;
		
		assertEquals("", rules.getEntityClassName(null) );
		assertEquals("", rules.getEntityClassName("") );
		assertEquals("A", rules.getEntityClassName("A") );
		assertEquals("Ab", rules.getEntityClassName("AB") );
		assertEquals("AbCd", rules.getEntityClassName("AB_CD") );
		assertEquals("Abcd", rules.getEntityClassName("_ABCD") );
		assertEquals("Abcd", rules.getEntityClassName("ABCD_") );
		assertEquals("Abcd", rules.getEntityClassName("_ABCD_") );
		assertEquals("AbCd", rules.getEntityClassName("ab_cd") );
		assertEquals("AbCd", rules.getEntityClassName("aB_cD") );
	}

	@Test
	public void testAttributeName() {
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;
		
		assertEquals("", rules.getAttributeName(null) ) ;
		assertEquals("", rules.getAttributeName("") ) ;
		assertEquals("a", rules.getAttributeName("A") ) ;
		assertEquals("ab", rules.getAttributeName("AB") ) ;
		assertEquals("ab", rules.getAttributeName("ab") ) ;
		assertEquals("abCde", rules.getAttributeName("AB_CDE") ) ;
		assertEquals("abcde", rules.getAttributeName("ABCDE_") ) ;
		assertEquals("abcde", rules.getAttributeName("_ABCDE") ) ;
		assertEquals("abcde", rules.getAttributeName("_ABCDE_") ) ;
	}

	@Test
	public void testAttributeType() {
		boolean NOT_NULL = true ;
		boolean NULLABLE = false ;
		
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;
		
		assertEquals("java.lang.String", rules.getAttributeType("xxxx", Types.VARCHAR, NULLABLE) ) ;
		assertEquals("java.lang.String", rules.getAttributeType("xxxx", Types.VARCHAR, NOT_NULL) ) ;

		assertEquals("java.lang.Integer", rules.getAttributeType("xxxx", Types.INTEGER, NULLABLE) ) ;
		assertEquals("java.lang.Integer", rules.getAttributeType("xxxx", Types.INTEGER, NOT_NULL) ) ;

		assertEquals("java.lang.Boolean", rules.getAttributeType("xxxx", Types.BOOLEAN, NULLABLE) ) ;
		assertEquals("java.lang.Boolean", rules.getAttributeType("xxxx", Types.BOOLEAN, NOT_NULL) ) ;
	}

	@Test
	public void testAttributeGuiLabel() {
		
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;
		
		assertEquals("", rules.getAttributeGuiLabel(null) ) ;
		assertEquals("", rules.getAttributeGuiLabel("") ) ;
		assertEquals("A", rules.getAttributeGuiLabel("A") ) ;

		assertEquals("Date naissance", rules.getAttributeGuiLabel("DATE_NAISSANCE") ) ;
		assertEquals("Date naissance", rules.getAttributeGuiLabel("date_NAISSANCE") ) ;
		assertEquals("Date naissance", rules.getAttributeGuiLabel("date_naissance") ) ;
		assertEquals("Date naissance", rules.getAttributeGuiLabel("_DATE_NAISSANCE") ) ;
		assertEquals("Date naissance", rules.getAttributeGuiLabel("_DATE_NAISSANCE__") ) ;
		assertEquals("Date", rules.getAttributeGuiLabel("DATE") ) ;
		assertEquals("Date", rules.getAttributeGuiLabel("_DATE__") ) ;
		assertEquals("Ab cd", rules.getAttributeGuiLabel("AB_CD") ) ;
		assertEquals("Ab cd", rules.getAttributeGuiLabel("ab_cd") ) ;
	}

	private AttributeInDbModel buildColumn(String dbName, String javaName, String javaType ) {
		AttributeInDbModel col = new AttributeInDbModel();
		col.setDatabaseName(dbName);
		//col.setJavaName("name");
		col.setName("name"); // v 3.0.0
		//col.setJavaType("java.lang.String");
		col.setFullType("java.lang.String"); // v 3.0.0
		return col ;
	}
	
//	private LinkInDbModel buildLink(String id, String javaFieldName, String cardinality ) {
	private LinkInDbModel buildLink(String id, String javaFieldName, Cardinality cardinality ) {
		LinkInDbModel link = new LinkInDbModel();
		link.setId(id);
//		link.setJavaFieldName(javaFieldName);
		link.setFieldName(javaFieldName);
//		link.setCardinality(cardinality);
		link.setCardinality(cardinality);
		return link ;
	}
	
	@Test
	public void testAttributeNameForLinkManyToOne() {
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;

		EntityInDbModel owningSideEntity = new EntityInDbModel();
//		owningSideEntity.setName("EMPLOYEE");
		owningSideEntity.setDatabaseTable("EMPLOYEE");
//		owningSideEntity.setBeanJavaClass("Employee");
		owningSideEntity.setClassName("Employee");
		owningSideEntity.storeAttribute(buildColumn("ID", "id", "int"));
		owningSideEntity.storeAttribute(buildColumn("FIRST_NAME", "firstName", "java.lang.String"));
		owningSideEntity.storeAttribute(buildColumn("LAST_NAME", "lastName", "java.lang.String"));
		
		EntityInDbModel inverseSideEntity = new EntityInDbModel();
//		inverseSideEntity.setName("COMPANY");
		inverseSideEntity.setDatabaseTable("COMPANY");
//		inverseSideEntity.setBeanJavaClass("Company");
		inverseSideEntity.setClassName("Company");
		inverseSideEntity.storeAttribute(buildColumn("CODE", "code", "short"));
		inverseSideEntity.storeAttribute(buildColumn("NAME", "name", "java.lang.String"));

		assertEquals("company", rules.getAttributeNameForLinkToOne(owningSideEntity, inverseSideEntity) ) ;
		
//		owningSideEntity.storeLink( buildLink("LINK_FK_AAAA_O", "company", RepositoryConst.MAPPING_MANY_TO_ONE ) );
		owningSideEntity.storeLink( buildLink("LINK_FK_AAAA_O", "company", Cardinality.MANY_TO_ONE ) );
		
		assertEquals("company2", rules.getAttributeNameForLinkToOne(owningSideEntity, inverseSideEntity) ) ;
		
//		owningSideEntity.storeLink( buildLink("LINK_FK_BBBB_O", "company2", RepositoryConst.MAPPING_MANY_TO_ONE ) );
		owningSideEntity.storeLink( buildLink("LINK_FK_BBBB_O", "company2", Cardinality.MANY_TO_ONE ) );
		
		assertEquals("company3", rules.getAttributeNameForLinkToOne(owningSideEntity, inverseSideEntity) ) ;
	}

	@Test
	public void testAttributeNameForLinkOneToMany() {
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;

		EntityInDbModel entity = new EntityInDbModel();
//		entity.setName("EMPLOYEE");
		entity.setDatabaseTable("EMPLOYEE");
//		entity.setBeanJavaClass("Employee");
		entity.setClassName("Employee");
		entity.storeAttribute(buildColumn("ID", "id", "int"));
		entity.storeAttribute(buildColumn("FIRST_NAME", "firstName", "java.lang.String"));
		entity.storeAttribute(buildColumn("LAST_NAME", "lastName", "java.lang.String"));
		
		EntityInDbModel referencedEntity = new EntityInDbModel();
//		referencedEntity.setName("COMPANY");
		referencedEntity.setDatabaseTable("COMPANY");
//		referencedEntity.setBeanJavaClass("Company");
		referencedEntity.setClassName("Company");
		referencedEntity.storeAttribute(buildColumn("CODE", "code", "short"));
		referencedEntity.storeAttribute(buildColumn("NAME", "name", "java.lang.String"));

		assertEquals("listOfCompany", rules.getAttributeNameForLinkToMany(entity, referencedEntity) ) ;
		
//		entity.storeLink( buildLink("LINK_FK_AAAA_I", "listOfCompany", RepositoryConst.MAPPING_ONE_TO_MANY ) );
		entity.storeLink( buildLink("LINK_FK_AAAA_I", "listOfCompany", Cardinality.ONE_TO_MANY ) );
		
		assertEquals("listOfCompany2", rules.getAttributeNameForLinkToMany(entity, referencedEntity) ) ;
		
//		entity.storeLink( buildLink("LINK_FK_BBBB_I", "listOfCompany2", RepositoryConst.MAPPING_ONE_TO_MANY ) );
		entity.storeLink( buildLink("LINK_FK_BBBB_I", "listOfCompany2", Cardinality.ONE_TO_MANY ) );
		
		assertEquals("listOfCompany3", rules.getAttributeNameForLinkToMany(entity, referencedEntity) ) ;

		EntityInDbModel referencedEntity2 = new EntityInDbModel();
//		referencedEntity2.setName("MANAGER");
		referencedEntity2.setDatabaseTable("MANAGER");
//		referencedEntity2.setBeanJavaClass("Manager");
		referencedEntity2.setClassName("Manager");

		assertEquals("listOfManager", rules.getAttributeNameForLinkToMany(entity, referencedEntity2) ) ;
//		entity.storeLink( buildLink("LINK_FK_MMMM_I", "listOfManager", RepositoryConst.MAPPING_ONE_TO_MANY ) );
		entity.storeLink( buildLink("LINK_FK_MMMM_I", "listOfManager", Cardinality.ONE_TO_MANY ) );
		assertEquals("listOfManager2", rules.getAttributeNameForLinkToMany(entity, referencedEntity2) ) ;
	}

}
