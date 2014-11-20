package org.telosys.tools.repository.rules;

import java.sql.Types;

import junit.framework.Assert;

import org.junit.Test;
import org.telosys.tools.repository.model.Column;
import org.telosys.tools.repository.model.Entity;
import org.telosys.tools.repository.model.Link;
import org.telosys.tools.repository.persistence.util.RepositoryConst;

public class RulesTest {

	@Test
	public void testEntityClassName() {
		
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;
		
		Assert.assertEquals("", rules.getEntityClassName(null) );
		Assert.assertEquals("", rules.getEntityClassName("") );
		Assert.assertEquals("A", rules.getEntityClassName("A") );
		Assert.assertEquals("Ab", rules.getEntityClassName("AB") );
		Assert.assertEquals("AbCd", rules.getEntityClassName("AB_CD") );
		Assert.assertEquals("Abcd", rules.getEntityClassName("_ABCD") );
		Assert.assertEquals("Abcd", rules.getEntityClassName("ABCD_") );
		Assert.assertEquals("Abcd", rules.getEntityClassName("_ABCD_") );
		Assert.assertEquals("AbCd", rules.getEntityClassName("ab_cd") );
		Assert.assertEquals("AbCd", rules.getEntityClassName("aB_cD") );
	}

	@Test
	public void testAttributeName() {
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;
		
		Assert.assertEquals("", rules.getAttributeName(null) ) ;
		Assert.assertEquals("", rules.getAttributeName("") ) ;
		Assert.assertEquals("a", rules.getAttributeName("A") ) ;
		Assert.assertEquals("ab", rules.getAttributeName("AB") ) ;
		Assert.assertEquals("ab", rules.getAttributeName("ab") ) ;
		Assert.assertEquals("abCde", rules.getAttributeName("AB_CDE") ) ;
		Assert.assertEquals("abcde", rules.getAttributeName("ABCDE_") ) ;
		Assert.assertEquals("abcde", rules.getAttributeName("_ABCDE") ) ;
		Assert.assertEquals("abcde", rules.getAttributeName("_ABCDE_") ) ;
	}

	@Test
	public void testAttributeType() {
		boolean NOT_NULL = true ;
		boolean NULLABLE = false ;
		
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;
		
		Assert.assertEquals("java.lang.String", rules.getAttributeType("xxxx", Types.VARCHAR, NULLABLE) ) ;
		Assert.assertEquals("java.lang.String", rules.getAttributeType("xxxx", Types.VARCHAR, NOT_NULL) ) ;

		Assert.assertEquals("java.lang.Integer", rules.getAttributeType("xxxx", Types.INTEGER, NULLABLE) ) ;
		Assert.assertEquals("java.lang.Integer", rules.getAttributeType("xxxx", Types.INTEGER, NOT_NULL) ) ;

		Assert.assertEquals("java.lang.Boolean", rules.getAttributeType("xxxx", Types.BOOLEAN, NULLABLE) ) ;
		Assert.assertEquals("java.lang.Boolean", rules.getAttributeType("xxxx", Types.BOOLEAN, NOT_NULL) ) ;
	}

	@Test
	public void testAttributeGuiLabel() {
		
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;
		
		Assert.assertEquals("", rules.getAttributeGuiLabel(null) ) ;
		Assert.assertEquals("", rules.getAttributeGuiLabel("") ) ;
		Assert.assertEquals("A", rules.getAttributeGuiLabel("A") ) ;

		Assert.assertEquals("Date naissance", rules.getAttributeGuiLabel("DATE_NAISSANCE") ) ;
		Assert.assertEquals("Date naissance", rules.getAttributeGuiLabel("date_NAISSANCE") ) ;
		Assert.assertEquals("Date naissance", rules.getAttributeGuiLabel("date_naissance") ) ;
		Assert.assertEquals("Date naissance", rules.getAttributeGuiLabel("_DATE_NAISSANCE") ) ;
		Assert.assertEquals("Date naissance", rules.getAttributeGuiLabel("_DATE_NAISSANCE__") ) ;
		Assert.assertEquals("Date", rules.getAttributeGuiLabel("DATE") ) ;
		Assert.assertEquals("Date", rules.getAttributeGuiLabel("_DATE__") ) ;
		Assert.assertEquals("Ab cd", rules.getAttributeGuiLabel("AB_CD") ) ;
		Assert.assertEquals("Ab cd", rules.getAttributeGuiLabel("ab_cd") ) ;
	}

	private Column buildColumn(String dbName, String javaName, String javaType ) {
		Column col = new Column();
		col.setDatabaseName(dbName);
		col.setJavaName("name");
		col.setJavaType("java.lang.String");
		return col ;
	}
	private Link buildLink(String id, String javaFieldName, String cardinality ) {
		Link link = new Link();
		link.setId(id);
		link.setJavaFieldName(javaFieldName);
		link.setCardinality(cardinality);
		return link ;
	}
	
	@Test
	public void testAttributeNameForLinkManyToOne() {
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;

		Entity owningSideEntity = new Entity();
		owningSideEntity.setName("EMPLOYEE");
		owningSideEntity.setBeanJavaClass("Employee");
		owningSideEntity.storeColumn(buildColumn("ID", "id", "int"));
		owningSideEntity.storeColumn(buildColumn("FIRST_NAME", "firstName", "java.lang.String"));
		owningSideEntity.storeColumn(buildColumn("LAST_NAME", "lastName", "java.lang.String"));
		
		Entity inverseSideEntity = new Entity();
		inverseSideEntity.setName("COMPANY");
		inverseSideEntity.setBeanJavaClass("Company");
		inverseSideEntity.storeColumn(buildColumn("CODE", "code", "short"));
		inverseSideEntity.storeColumn(buildColumn("NAME", "name", "java.lang.String"));

		Assert.assertEquals("company", rules.getAttributeNameForLinkToOne(owningSideEntity, inverseSideEntity) ) ;
		
		owningSideEntity.storeLink( buildLink("LINK_FK_AAAA_O", "company", RepositoryConst.MAPPING_MANY_TO_ONE ) );
		
		Assert.assertEquals("company2", rules.getAttributeNameForLinkToOne(owningSideEntity, inverseSideEntity) ) ;
		
		owningSideEntity.storeLink( buildLink("LINK_FK_BBBB_O", "company2", RepositoryConst.MAPPING_MANY_TO_ONE ) );
		
		Assert.assertEquals("company3", rules.getAttributeNameForLinkToOne(owningSideEntity, inverseSideEntity) ) ;
	}

	@Test
	public void testAttributeNameForLinkOneToMany() {
		RepositoryRules rules = RepositoryRulesProvider.getRepositoryRules() ;

		Entity entity = new Entity();
		entity.setName("EMPLOYEE");
		entity.setBeanJavaClass("Employee");
		entity.storeColumn(buildColumn("ID", "id", "int"));
		entity.storeColumn(buildColumn("FIRST_NAME", "firstName", "java.lang.String"));
		entity.storeColumn(buildColumn("LAST_NAME", "lastName", "java.lang.String"));
		
		Entity referencedEntity = new Entity();
		referencedEntity.setName("COMPANY");
		referencedEntity.setBeanJavaClass("Company");
		referencedEntity.storeColumn(buildColumn("CODE", "code", "short"));
		referencedEntity.storeColumn(buildColumn("NAME", "name", "java.lang.String"));

		Assert.assertEquals("listOfCompany", rules.getAttributeNameForLinkToMany(entity, referencedEntity) ) ;
		
		entity.storeLink( buildLink("LINK_FK_AAAA_I", "listOfCompany", RepositoryConst.MAPPING_ONE_TO_MANY ) );
		
		Assert.assertEquals("listOfCompany2", rules.getAttributeNameForLinkToMany(entity, referencedEntity) ) ;
		
		entity.storeLink( buildLink("LINK_FK_BBBB_I", "listOfCompany2", RepositoryConst.MAPPING_ONE_TO_MANY ) );
		
		Assert.assertEquals("listOfCompany3", rules.getAttributeNameForLinkToMany(entity, referencedEntity) ) ;

		Entity referencedEntity2 = new Entity();
		referencedEntity2.setName("MANAGER");
		referencedEntity2.setBeanJavaClass("Manager");

		Assert.assertEquals("listOfManager", rules.getAttributeNameForLinkToMany(entity, referencedEntity2) ) ;
		entity.storeLink( buildLink("LINK_FK_MMMM_I", "listOfManager", RepositoryConst.MAPPING_ONE_TO_MANY ) );
		Assert.assertEquals("listOfManager2", rules.getAttributeNameForLinkToMany(entity, referencedEntity2) ) ;
	}

}
