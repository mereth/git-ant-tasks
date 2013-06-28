package org.oecd.ant.git;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileUtils;
import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.oecd.ant.git.types.GitFiles;

public class DiffTask extends AbstractGitTask {

	private String filter;

	private File output;

	private boolean cached;
	private String oldrev;
	private String newrev;

	private String added;
	private String changed;
	private String removed;

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void setOutput(File output) {
		this.output = output;
	}

	public void setCached(boolean cached) {
		this.cached = cached;
	}

	public void setOldrev(String oldrev) {
		this.oldrev = oldrev;
	}

	public void setNewrev(String newrev) {
		this.newrev = newrev;
	}

	public void setAdded(String added) {
		this.added = added;
	}

	public void setChanged(String changed) {
		this.changed = changed;
	}

	public void setRemoved(String removed) {
		this.removed = removed;
	}

	@Override
	protected void checkProperties() throws Exception {
		if (cached && newrev != null) {
			throw new BuildException("You can't use 'cached' and 'newrev' attribute at the same time.");
		}
	}

	@Override
	protected void executeCustom(Git git) throws Exception {
		DiffCommand dc = git.diff();

		if (output != null) {
			FileUtils.getFileUtils().createNewFile(output, true);
			FileOutputStream stream = new FileOutputStream(output);
			dc.setOutputStream(stream);

		} else {
			dc.setShowNameAndStatusOnly(true);
		}

		if (oldrev != null) {
			dc.setOldTree(getTreeIterator(git, oldrev));
		}

		if (cached) {
			dc.setCached(cached);
		} else if (newrev != null) {
			dc.setNewTree(getTreeIterator(git, newrev));
		}

		if (filter != null) {
			dc.setPathFilter(PathFilter.create(filter));
		}

		List<DiffEntry> diffs = dc.call();

		GitFiles added = new GitFiles();
		GitFiles modified = new GitFiles();
		GitFiles deleted = new GitFiles();

		for (DiffEntry entry : diffs) {
			switch (entry.getChangeType()) {
			case ADD:
				added.add(new FileResource(getRepo(), entry.getNewPath()));
				break;
			case MODIFY:
				modified.add(new FileResource(getRepo(), entry.getNewPath()));
				break;
			case DELETE:
				deleted.add(new FileResource(getRepo(), entry.getOldPath()));
				break;
			default:
				throw new Exception("Unsupported: " + entry.getChangeType());
			}
		}

		if (this.added != null) {
			getProject().addReference(this.added, added);
		}

		if (this.changed != null) {
			getProject().addReference(this.changed, modified);
		}

		if (this.removed != null) {
			getProject().addReference(this.removed, deleted);
		}
	}

	private AbstractTreeIterator getTreeIterator(Git git, String revstr) throws Exception {
		Repository repo = git.getRepository();
		ObjectId head = repo.resolve(revstr + "^{tree}"); //$NON-NLS-1$
		if (head == null)
			throw new NoHeadException(JGitText.get().cannotReadTree);
		CanonicalTreeParser p = new CanonicalTreeParser();
		ObjectReader reader = repo.newObjectReader();
		try {
			p.reset(reader, head);
		} finally {
			reader.release();
		}
		return p;
	}

}
