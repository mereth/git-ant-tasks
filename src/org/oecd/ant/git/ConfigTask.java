package org.oecd.ant.git;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.PropertyHelper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.StoredConfig;
import org.oecd.ant.git.nested.ConfigOptionElement;

public class ConfigTask extends AbstractGitTask {

	private String section;
	private String subsection;
	private String name;
	private String value;
	private String property;

	private final List<ConfigOptionElement> options = new ArrayList<ConfigOptionElement>();

	public void setSection(String section) {
		this.section = section;
	}

	public void setSubsection(String subsection) {
		this.subsection = subsection;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void addOption(ConfigOptionElement option) {
		options.add(option);
	}

	@Override
	protected void checkProperties() throws Exception {

	}

	@Override
	protected void executeCustom(Git git) throws Exception {
		StoredConfig conf = git.getRepository().getConfig();

		if (section != null || subsection != null || name != null || value != null || property != null) {
			ConfigOptionElement option = new ConfigOptionElement(section, subsection, name, value, property);
			options.add(0, option);
		}

		boolean updated = false;

		for (ConfigOptionElement option : options) {
			String property = option.getProperty();
			if (property != null) {
				String oldValue = conf.getString(option.getSection(), option.getSubsection(), option.getName());
				if (oldValue != null) {
					PropertyHelper.getPropertyHelper(getProject()).setNewProperty(property, oldValue);
				}
			}

			String value = option.getValue();
			if (value != null) {
				if (value.length() == 0)
					conf.unset(option.getSection(), option.getSubsection(), option.getName());
				else
					conf.setString(option.getSection(), option.getSubsection(), option.getName(), value);
				updated = true;
			}
		}

		if (updated)
			conf.save();
	}

}
