package org.oecd.ant.git;

import java.util.Set;

import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.types.ResourceCollection;
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

	}

	@Override
	protected void executeCustom(Git git) throws Exception {
		StatusCommand sc = git.status();

		Status status = sc.call();

		if (status.isClean() && isclean != null) {
			PropertyHelper.getPropertyHelper(getProject()).setNewProperty(isclean, "true");
		}

		if (untracked != null) {
			ResourceCollection resources = buildResourcesCollection(status.getUntracked());
			getProject().addReference(untracked, resources);
		}

		if (added != null) {
			ResourceCollection resources = buildResourcesCollection(status.getAdded());
			getProject().addReference(added, resources);
		}

		if (modified != null) {
			ResourceCollection resources = buildResourcesCollection(status.getModified());
			getProject().addReference(modified, resources);
		}

		if (changed != null) {
			ResourceCollection resources = buildResourcesCollection(status.getChanged());
			getProject().addReference(changed, resources);
		}

		if (missing != null) {
			ResourceCollection resources = buildResourcesCollection(status.getMissing());
			getProject().addReference(missing, resources);
		}

		if (removed != null) {
			ResourceCollection resources = buildResourcesCollection(status.getRemoved());
			getProject().addReference(removed, resources);
		}
	}

	private ResourceCollection buildResourcesCollection(Set<String> source) {
		GitFiles destination = new GitFiles();

		for (String filename : source) {
			destination.add(new FileResource(getRepo(), filename));
		}

		return destination;
	}
}
