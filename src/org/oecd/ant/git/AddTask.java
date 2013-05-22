package org.oecd.ant.git;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;

public class AddTask extends AbstractGitTask {

	private boolean update;
	private String filepatterns = ".";

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public String getFilepatterns() {
		return filepatterns;
	}

	public void setFilepatterns(String filePatterns) {
		this.filepatterns = filePatterns;
	}

	@Override
	protected void checkProperties() throws Exception {

	}

	@Override
	protected void executeCustom(Git git) throws Exception {
		AddCommand ac = git.add();

		ac.setUpdate(isUpdate());

		if (filepatterns != null) {
			for (String filePattern : filepatterns.split("[ ,]")) {
				ac.addFilepattern(filePattern);
			}
		}

		ac.call();
	}
}
