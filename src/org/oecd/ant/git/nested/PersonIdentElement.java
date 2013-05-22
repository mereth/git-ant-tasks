package org.oecd.ant.git.nested;

import org.eclipse.jgit.lib.PersonIdent;

public class PersonIdentElement {
	private String name;
	private String email;

	public PersonIdentElement() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public PersonIdent toPersonIdent() {
		return new PersonIdent(name, email);
	}
}
