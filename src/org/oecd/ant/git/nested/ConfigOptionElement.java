package org.oecd.ant.git.nested;

public class ConfigOptionElement {

	private String section;
	private String subsection;
	private String name;
	private String value;
	private String property;

	public ConfigOptionElement() {
		// empty constructor for ant
	}

	public ConfigOptionElement(String section, String subsection, String name, String value, String property) {
		this.section = section;
		this.subsection = subsection;
		this.name = name;
		this.value = value;
		this.property = property;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getSubsection() {
		return subsection;
	}

	public void setSubsection(String subsection) {
		this.subsection = subsection;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}
