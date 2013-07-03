package org.oecd.ant.git;

import java.text.MessageFormat;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;

public class RefTask extends AbstractGitTask {

	private String name;
	private String property;
	private int length;

	public void setName(String name) {
		this.name = name;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	protected void checkProperties() throws Exception {
		if (name == null) {
			throw new BuildException("The reference name attribute must be set.");
		}
	}

	@Override
	protected void executeCustom(Git git) throws Exception {
		Ref ref = git.getRepository().getRef(name);
		if (ref == null) {
			throw new BuildException(MessageFormat.format("Ref ''{1}'' not found in ''{0}''", getRepo().getAbsolutePath(), name));
		}

		String sha1 = ref.getObjectId().getName();

		log(getRepo().getAbsolutePath() + " [" + name + "] " + sha1);

		if (property != null) {
			if (length > 0) {
				sha1 = sha1.substring(0, length);
			}
			PropertyHelper.getPropertyHelper(getProject()).setNewProperty(property, sha1);
		}
	}

}
