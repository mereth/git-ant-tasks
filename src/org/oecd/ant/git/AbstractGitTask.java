package org.oecd.ant.git;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.eclipse.jgit.api.Git;

public abstract class AbstractGitTask extends Task {

	private File repo;
	private boolean verbose;

	public File getRepo() {
		return repo;
	}

	public void setRepo(File repo) {
		this.repo = repo;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	protected abstract void checkProperties() throws Exception;

	protected abstract void executeCustom(Git git) throws Exception;

	@Override
	public final void execute() throws BuildException {
		try {
			checkProperties();

			Git git = Git.open(repo);
			executeCustom(git);
		} catch (BuildException ex) {
			throw ex;
		} catch (Exception ex) {
			BuildException be = new BuildException(ex);
			throw be;
		}
	}

}
