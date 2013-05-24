package org.oecd.ant.git;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.property.LocalProperties;
import org.eclipse.jgit.api.Git;

public class GitTask extends AbstractGitTask implements TaskContainer {

	private final List<Task> nestedTasks = new ArrayList<Task>();

	@Override
	public void addTask(Task task) {
		nestedTasks.add(task);
	}

	@Override
	protected void checkProperties() throws Exception {

	}

	@Override
	protected void executeCustom(Git git) throws Exception {
		String repo = getRepo().getCanonicalPath();

		LocalProperties localProperties = LocalProperties.get(getProject());
		localProperties.enterScope();
		try {
			for (Task nestedTask : nestedTasks) {
				// Most likely an UnknownElement, so we test the tasktype
				if (nestedTask.getTaskType().startsWith("git")) {
					RuntimeConfigurable nestedWrapper = nestedTask.getRuntimeConfigurableWrapper();
					// the goal is to propagate the "repo" attribute, not to override it
					if (nestedWrapper.getAttributeMap().get(REPO_ATTRIBUTE) == null) {
						nestedWrapper.setAttribute(REPO_ATTRIBUTE, repo);
					}
				}
				nestedTask.perform();
			}
		} finally {
			localProperties.exitScope();
		}
	}

}
