package org.oecd.ant.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.StoredConfig;

public class ConfigTask extends AbstractGitTask {

	public ConfigTask() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void checkProperties() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void executeCustom(Git git) throws Exception {
		StoredConfig conf = git.getRepository().getConfig();

	}

}
