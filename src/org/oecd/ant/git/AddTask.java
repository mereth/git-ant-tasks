package org.oecd.ant.git;

import org.apache.tools.ant.BuildException;
import org.eclipse.jgit.api.Git;
import org.oecd.ant.git.custom.CustomAddCommand;

public class AddTask extends AbstractGitTask {

	private boolean all;
	private boolean update;
	private String filepatterns = ".";

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

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
		if (all && update) {
			throw new BuildException("The 'all' and 'update' attributes are mutually incompatible.");
		}
	}

	@Override
	protected void executeCustom(Git git) throws Exception {
		CustomAddCommand ac = new CustomAddCommand(git.getRepository());

		ac.setUpdate(isUpdate());
		ac.setAll(isAll());

		if (filepatterns != null) {
			for (String filePattern : filepatterns.split("[ ,]")) {
				ac.addFilepattern(filePattern);
			}
		}

		ac.call();
	}
}
