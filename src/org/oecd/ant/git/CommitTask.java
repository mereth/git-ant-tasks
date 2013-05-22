package org.oecd.ant.git;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.oecd.ant.git.nested.PersonIdentElement;

public class CommitTask extends AbstractGitTask {

	private boolean all;
	private boolean amend;
	private String message;
	private PersonIdentElement author;
	private PersonIdentElement committer;

	private String revproperty;
	private int revlength;

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

	public boolean isAmend() {
		return amend;
	}

	public void setAmend(boolean amend) {
		this.amend = amend;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PersonIdentElement getAuthor() {
		return author;
	}

	public void addAuthor(PersonIdentElement author) {
		if (this.author != null)
			throw new BuildException(":only_one");
		this.author = author;
	}

	public PersonIdentElement getCommitter() {
		return committer;
	}

	public void addCommitter(PersonIdentElement commiter) {
		if (this.committer != null)
			throw new BuildException(":only_one");
		this.committer = commiter;
	}

	public String getRevproperty() {
		return revproperty;
	}

	public void setRevproperty(String revproperty) {
		this.revproperty = revproperty;
	}

	public int getRevlength() {
		return revlength;
	}

	public void setRevlength(int revlength) {
		this.revlength = revlength;
	}

	@Override
	protected void checkProperties() throws Exception {

	}

	@Override
	protected void executeCustom(Git git) throws Exception {
		CommitCommand cc = git.commit();

		cc.setAll(isAll());
		cc.setAmend(isAmend());

		if (getAuthor() != null) {
			cc.setAuthor(getAuthor().toPersonIdent());
		}

		if (getCommitter() != null) {
			cc.setCommitter(getCommitter().toPersonIdent());
		}

		cc.setMessage(getMessage());

		RevCommit commit = cc.call();

		Ref head = git.getRepository().getRef(Constants.HEAD);

		String branchName;
		if (!head.isSymbolic())
			branchName = "detached HEAD";
		else {
			branchName = head.getTarget().getName();
			if (branchName.startsWith(Constants.R_HEADS))
				branchName = branchName.substring(Constants.R_HEADS.length());
		}

		if (getRevproperty() != null) {
			String sha1 = commit.name();
			if (getRevlength() > 0) {
				sha1 = sha1.substring(0, getRevlength());
			}
			PropertyHelper.getPropertyHelper(getProject()).setNewProperty(getRevproperty(), sha1);
		}

		log("[" + branchName + " " + commit.name().substring(0, 7) + "] " + commit.getShortMessage());

		//TODO missing commit summary
	}

}
