package org.telosys.tools.repository.model;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.repository.persistence.PersistenceManager;
import org.telosys.tools.repository.persistence.StandardFilePersistenceManager;

public class RepoModelTest {
	
	public void print(File file) {
		System.out.println("Repository model file : " + file.toString());
	}
	
	public void print(RepositoryModel model) {
		System.out.println("MODEL : " );
		System.out.println("Database : " + model.getDatabaseId() + " " + model.getDatabaseName() );
		for ( Entity entity : model.getEntities() ) {
			System.out.println(" . entity : " + entity );
		}
	}
	
	public void print(Entity entity) {
		System.out.println("ENTITY : " );
		for ( Column column : entity.getColumns() ) {
			System.out.println(" . column : " + column );
		}
		for ( ForeignKey  fk : entity.getForeignKeys() ) {
			System.out.println(" . foreign key : " + fk );
		}
		for ( Link  link : entity.getLinks()) {
			System.out.println(" . link : " + link );
		}
	}
	
	@Test
	public void test() throws TelosysToolsException {
		File modelFile = new File("src/test/resources/repo/BookStoreDERBY.dbrep");
		print(modelFile);
		TelosysToolsLogger logger = new ConsoleLogger() ;
		PersistenceManager pm = new StandardFilePersistenceManager(modelFile, logger);
		RepositoryModel model = pm.load();
		print(model);
		String entityName = "BOOK";
		Entity entity = model.getEntityByName(entityName);
		Assert.assertNotNull(entity);
		print(entity);
		
		int nbLinks = entity.getLinks().length ;
		System.out.println(nbLinks + " link(s) in entity " + entity.getName());
		int n = model.removeLinksByEntityName(entity.getName());
		System.out.println(n + " link(s) removed");
		Assert.assertTrue(n == nbLinks * 2 );
	}

}
