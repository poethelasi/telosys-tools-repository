package org.telosys.tools.repository;

import org.telosys.tools.repository.changelog.ChangeLog;
import org.telosys.tools.repository.model.RepositoryModel;

/**
 * Simple class just for holding the result after a "repository update" : <br>
 * - the RepositoryModel instance after the update <br>
 * - the ChangeLog instance <br>
 * 
 * @author L. Guerin
 *
 */
public class UpdateResult {
	
	private final RepositoryModel repositoryModel ;
	private final ChangeLog changeLog ;
	
	public UpdateResult(RepositoryModel repositoryModel, ChangeLog changeLog) {
		super();
		this.repositoryModel = repositoryModel;
		this.changeLog = changeLog;
	}
	
	public RepositoryModel getRepositoryModel() {
		return repositoryModel;
	}
	
	public ChangeLog getChangeLog() {
		return changeLog;
	}

}
