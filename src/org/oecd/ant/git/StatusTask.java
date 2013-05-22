package org.oecd.ant.git;

import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.types.resources.FileResource;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.StatusCommand;
import org.oecd.ant.git.types.GitFiles;

public class StatusTask extends AbstractGitTask {

	private String isclean;

	private String untracked;
	private String added;

	private String modified;
	private String changed;

	private String missing;
	private String removed;

	public void setIsclean(String isclean) {
		this.isclean = isclean;
	}

	public void setUntracked(String untracked) {
		this.untracked = untracked;
	}

	public void setAdded(String added) {
		this.added = added;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public void setChanged(String changed) {
		this.changed = changed;
	}

	public void setMissing(String missing) {
		this.missing = missing;
	}

	public void setRemoved(String removed) {
		this.removed = removed;
	}

	@Override
	protected void checkProperties() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void executeCustom(Git git) throws Exception {
		StatusCommand sc = git.status();

		Status status = sc.call();

		if (status.isClean() && isclean != null) {
			PropertyHelper.getPropertyHelper(getProject()).setNewProperty(isclean, "true");
		}

		if (untracked != null) {
			GitFiles resources = new GitFiles();

			for (String filename : status.getUntracked()) {
				resources.add(new FileResource(getRepo(), filename));
			}

			getProject().addReference(untracked, resources);
		}

		if (added != null) {
			GitFiles resources = new GitFiles();

			for (String filename : status.getAdded()) {
				resources.add(new FileResource(getRepo(), filename));
			}

			getProject().addReference(added, resources);
		}

		if (modified != null) {
			GitFiles resources = new GitFiles();

			for (String filename : status.getModified()) {
				resources.add(new FileResource(getRepo(), filename));
			}

			getProject().addReference(modified, resources);
		}

		if (changed != null) {
			GitFiles resources = new GitFiles();

			for (String filename : status.getChanged()) {
				resources.add(new FileResource(getRepo(), filename));
			}

			getProject().addReference(changed, resources);
		}

		if (missing != null) {
			GitFiles resources = new GitFiles();

			for (String filename : status.getMissing()) {
				resources.add(new FileResource(getRepo(), filename));
			}

			getProject().addReference(missing, resources);
		}

		if (removed != null) {
			GitFiles resources = new GitFiles();

			for (String filename : status.getRemoved()) {
				resources.add(new FileResource(getRepo(), filename));
			}

			getProject().addReference(removed, resources);
		}
	}

}
