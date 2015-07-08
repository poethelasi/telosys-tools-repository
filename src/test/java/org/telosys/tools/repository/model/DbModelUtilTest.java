package org.telosys.tools.repository.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.telosys.tools.generic.model.JoinColumn;

public class DbModelUtilTest {

	@Test
	public void test1() {
		
		List<JoinColumnInDbModel> listOfJoinColumnInDbModel = new LinkedList<JoinColumnInDbModel>();
		List<JoinColumn> listOfJoinColumn = DbModelUtil.toListOfJoinColumns(listOfJoinColumnInDbModel) ;
		assertNotNull(listOfJoinColumn);
		assertEquals(0, listOfJoinColumn.size());
	}

	@Test
	public void test2() {
		
		List<JoinColumn> listOfJoinColumn = DbModelUtil.toListOfJoinColumns(null) ;
		assertNull(listOfJoinColumn);
	}

	@Test
	public void test3() {
		
		List<JoinColumnInDbModel> listOfJoinColumnInDbModel = new LinkedList<JoinColumnInDbModel>();
		listOfJoinColumnInDbModel.add(new JoinColumnInDbModel());
		List<JoinColumn> listOfJoinColumn = DbModelUtil.toListOfJoinColumns(listOfJoinColumnInDbModel) ;
		assertNotNull(listOfJoinColumn);
		assertEquals(1, listOfJoinColumn.size());
	}

}
