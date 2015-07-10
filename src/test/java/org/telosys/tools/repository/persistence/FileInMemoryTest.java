package org.telosys.tools.repository.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;

public class FileInMemoryTest {
	
	
	@Test
	public void test1() throws TelosysToolsException {
		
		System.out.println("test1");
		FileInMemory fileInMemory = new FileInMemory();
		
		assertNotNull(fileInMemory.getContent());
		assertEquals(0, fileInMemory.getContent().length);
		
		fileInMemory.setContent("Foo");
		System.out.println("New content : " + fileInMemory.getContentAsString());
		assertNotNull(fileInMemory.getContent());
		assertEquals(3, fileInMemory.getContent().length);
		
		byte[] content = { 'a', 'b', 'c', 'd', 'e' };
		fileInMemory.setContent(content);
		System.out.println("New content : " + fileInMemory.getContentAsString());
		assertNotNull(fileInMemory.getContent());
		assertEquals(5, fileInMemory.getContent().length);
	}
	
}
