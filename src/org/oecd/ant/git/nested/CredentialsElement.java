package org.oecd.ant.git.nested;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class CredentialsElement {

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public CredentialsProvider toCredentialsProvider() {
		return new UsernamePasswordCredentialsProvider(username, password);
	}
}
