package org.telosys.tools.repository;

import org.telosys.tools.repository.changelog.ChangeLog;
import org.telosys.tools.repository.model.RepositoryModel;

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
