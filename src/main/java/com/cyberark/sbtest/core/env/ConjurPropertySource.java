package com.cyberark.sbtest.core.env;

import org.springframework.core.env.EnumerablePropertySource;

public class ConjurPropertySource extends EnumerablePropertySource<Object> {

	protected ConjurPropertySource(String name) {
		super(name);
	}

	@Override
	public String[] getPropertyNames() {
		return new String[0];
	}

	@Override
	public Object getProperty(String name) {
		
		System.out.printf("Resolving %s in context of %s\n", name, getName());
		
		if ("database.uid".equals(name)) {
			return "my id";
		}
		
		if ("database.password".equals(name)) {
			return "my password";
		}
		
		return null;
	}

}
