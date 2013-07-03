package org.oecd.ant.git;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.IdentityMapper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;

public class ExtractTask extends AbstractGitTask {

	private String rev = "HEAD";

	private String srcfile;
	private final Vector<ResourceCollection> srcfiles = new Vector<ResourceCollection>();

	private File dstfile;
	private File dstdir;

	private Mapper mapperElement;

	public void setRev(String rev) {
		this.rev = rev;
	}

	public void setSrcfile(String srcfile) {
		this.srcfile = srcfile;
	}

	public void setDstfile(File dstfile) {
		this.dstfile = dstfile;
	}

	public void setDstdir(File dstdir) {
		this.dstdir = dstdir;
	}

	/**
	 * Define the mapper to map source to destination files.
	 * 
	 * @return a mapper to be configured.
	 * @exception BuildException
	 *                if more than one mapper is defined.
	 */
	public Mapper createMapper() throws BuildException {
		if (mapperElement != null) {
			throw new BuildException("Cannot define more than one mapper", getLocation());
		}
		mapperElement = new Mapper(getProject());
		return mapperElement;
	}

	/**
	 * Add a nested filenamemapper.
	 * 
	 * @param fileNameMapper
	 *            the mapper to add.
	 * @since Ant 1.6.3
	 */
	public void add(FileNameMapper fileNameMapper) {
		createMapper().add(fileNameMapper);
	}

	public void add(ResourceCollection res) {
		srcfiles.add(res);
	}

	/**
	 * returns the mapper to use based on nested elements or the flatten attribute.
	 */
	private FileNameMapper getMapper() {
		FileNameMapper mapper = null;
		if (mapperElement != null) {
			mapper = mapperElement.getImplementation();
		} else {
			mapper = new IdentityMapper();
		}
		return mapper;
	}

	@Override
	protected void checkProperties() throws Exception {
		if (srcfile == null && srcfiles.size() == 0) {
			throw new BuildException("Specify at least one source: srcfile or a nested resource collection.");
		}

		if (srcfile != null && srcfiles.size() > 0) {
			throw new BuildException("Specify only one source.");
		}

		if (dstfile != null && dstdir != null) {
			throw new BuildException("Only one of dstfile and dstdir may be set.");
		}

		if (srcfiles.size() > 0 && dstdir == null) {
			throw new BuildException("dstdir must be set when using resource collection.");
		}
	}

	@Override
	protected void executeCustom(Git git) throws Exception {
		if (srcfile != null) {
			extractFile(git, srcfile, dstfile);
		} else {
			for (ResourceCollection rc : srcfiles) {
				Iterator<?> it = rc.iterator();
				while (it.hasNext()) {
					Resource rs = (Resource) it.next();

					String[] dstfiles = getMapper().mapFileName(rs.getName());

					for (String dstfile : dstfiles) {
						extractFile(git, rs.getName(), FileUtils.getFileUtils().resolveFile(dstdir, dstfile));
					}
				}
			}
		}
	}

	private void extractFile(Git git, String src, File dst) throws Exception {
		ObjectId objectId = git.getRepository().resolve(rev + ":" + src.replace('\\', '/'));

		ObjectLoader ol = git.getRepository().open(objectId);

		if (ol.getType() == Constants.OBJ_BLOB) {
			log(dst.getAbsolutePath());
			FileUtils.getFileUtils().createNewFile(dst, true);

			OutputStream stream = null;
			try {
				stream = new BufferedOutputStream(new FileOutputStream(dst));
				ol.copyTo(stream);
			} finally {
				if (stream != null)
					stream.close();
			}
		} else {
			throw new BuildException("Unsupported object type");
		}
	}
}
