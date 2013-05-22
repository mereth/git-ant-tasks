package org.oecd.ant.git;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import org.apache.tools.ant.BuildException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;

public class ExtractTask extends AbstractGitTask {

	private String rev = "HEAD";
	private String srcfile;

	private String dstfile;
	private String dstdir;

	public void setRev(String rev) {
		this.rev = rev;
	}

	public void setSrcfile(String srcfile) {
		this.srcfile = srcfile;
	}

	public void setDstfile(String dstfile) {
		this.dstfile = dstfile;
	}

	public void setDstdir(String dstdir) {
		this.dstdir = dstdir;
	}

	@Override
	protected void checkProperties() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void executeCustom(Git git) throws Exception {
		ObjectId objectId = git.getRepository().resolve(rev + ":" + srcfile);

		ObjectLoader ol = git.getRepository().open(objectId);

		if (ol.getType() == Constants.OBJ_BLOB) {

			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(dstfile));
			ol.copyTo(stream);
			stream.close();

		} else {
			throw new BuildException("Unsupported object type");
		}
	}

}
