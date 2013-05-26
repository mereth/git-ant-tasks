package org.oecd.ant.git;

import org.apache.tools.ant.Task;
import org.eclipse.jgit.lib.ProgressMonitor;

public class SimpleProgressMonitor implements ProgressMonitor {

	Task task;

	public SimpleProgressMonitor(Task task) {
		this.task = task;
	}

	public void start(int totalTasks) {
	}

	public void beginTask(String title, int totalWork) {
		String message;
		if (totalWork == ProgressMonitor.UNKNOWN) {
			message = title;
		} else {
			message = title + " (" + totalWork + ")";
		}
		task.log(message);
	}

	public void update(int completed) {
	}

	public void endTask() {
	}

	public boolean isCancelled() {
		return false;
	}

}
