package org.oecd.ant.git.types;

import java.util.Iterator;
import java.util.Vector;

import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;

public class GitFiles implements ResourceCollection {

	Vector<FileResource> files = new Vector<FileResource>();

	public void add(FileResource file) {
		files.add(file);
	}

	@Override
	public Iterator<FileResource> iterator() {
		return files.iterator();
	}

	@Override
	public int size() {
		return files.size();
	}

	@Override
	public boolean isFilesystemOnly() {
		return true;
	}

}
