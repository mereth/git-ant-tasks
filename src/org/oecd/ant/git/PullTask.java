package org.oecd.ant.git;

import org.apache.tools.ant.BuildException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.RebaseCommand;
import org.eclipse.jgit.api.RebaseCommand.Operation;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.api.RebaseResult.Status;

public class PullTask extends AbstractGitTask {

	@Override
	protected void checkProperties() throws Exception {

	}

	@Override
	protected void executeCustom(Git git) throws Exception {
		PullCommand command = git.pull();
		command.setRebase(true);

		PullResult pullResult = command.call();

		Status status = pullResult.getRebaseResult().getStatus();
		log("Rebase Status: " + status.name());

		if (status == Status.OK || status == Status.FAST_FORWARD || status == Status.UP_TO_DATE) {
			// everything is fine
			return;
		}

		if (status == Status.STOPPED) {
			// Stopped due to a conflict; must either abort or resolve or skip
			// let's aboooooort !
			abortRebase(git);
			throw new BuildException("Rebase stopped and aborted due to a conflict.");
		}

		throw new BuildException("Unexpected rebase status: " + status.name());
	}

	private void abortRebase(Git git) throws Exception {
		RebaseCommand rebase = git.rebase();
		rebase.setOperation(Operation.ABORT);

		RebaseResult rebaseResult = rebase.call();
		Status status = rebaseResult.getStatus();
		log("Abort Rebase Status: " + status.name());

		if (status != Status.ABORTED) {
			throw new BuildException("Unexpected rebase abort status: " + status.name());
		}
	}

}
