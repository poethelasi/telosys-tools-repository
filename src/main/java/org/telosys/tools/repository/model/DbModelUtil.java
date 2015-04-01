package org.telosys.tools.repository.model;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.generic.model.JoinColumn;

public class DbModelUtil {
	
	protected static List<JoinColumn> toListOfJoinColumns(List<JoinColumnInDbModel> listOfJoinColumnInDbModel) {
		LinkedList<JoinColumn> joinColumns = new LinkedList<JoinColumn>();
		for ( JoinColumn jc : listOfJoinColumnInDbModel ) {
			joinColumns.add(jc);
		}
		return joinColumns ;
	}

}
