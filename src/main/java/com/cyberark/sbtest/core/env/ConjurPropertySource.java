package com.cyberark.sbtest.core.env;

import org.springframework.core.env.EnumerablePropertySource;

public class ConjurPropertySource extends EnumerablePropertySource {

	protected ConjurPropertySource(String name) {
		super(name);
	}

	@Override
	public String[] getPropertyNames() {
		return new String[0];
	}

	@Override
	public Object getProperty(String name) {
		
		if ("database.uid".equals(name)) {
			return "my id";
		}
		
		if ("database.password".equals(name)) {
			return "my password";
		}
		
		return null;
	}

}
