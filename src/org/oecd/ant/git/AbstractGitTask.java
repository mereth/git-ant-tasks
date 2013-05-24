package org.oecd.ant.git;

import static org.eclipse.jgit.lib.Constants.R_HEADS;
import static org.eclipse.jgit.lib.Constants.R_REMOTES;
import static org.eclipse.jgit.lib.Constants.R_TAGS;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.eclipse.jgit.api.Git;

public abstract class AbstractGitTask extends Task {

	public static final String REPO_ATTRIBUTE = "repo";

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

	protected String abbreviateRef(String dst, boolean abbreviateRemote) {
		if (dst.startsWith(R_HEADS))
			dst = dst.substring(R_HEADS.length());
		else if (dst.startsWith(R_TAGS))
			dst = dst.substring(R_TAGS.length());
		else if (abbreviateRemote && dst.startsWith(R_REMOTES))
			dst = dst.substring(R_REMOTES.length());
		return dst;
	}
}
