package com.cyberark.sbtest.core.env;

import org.springframework.core.env.EnumerablePropertySource;

public class ConjurPropertySource extends EnumerablePropertySource<Object> {
	
	private String vaultInfo = "";
	
	private String vaultPath = "";

	protected ConjurPropertySource(String vaultPath) {		
		super(vaultPath+"@");
		this.vaultPath = vaultPath;

	}
	
	protected ConjurPropertySource(String vaultPath, String vaultInfo) {		
		super(vaultPath+"@"+vaultInfo);
		this.vaultPath = vaultPath;
		this.vaultInfo = vaultInfo;
	}

	@Override
	public String[] getPropertyNames() {
		return new String[0];
	}

	@Override
	public Object getProperty(String name) {
		
		System.out.printf("Resolving %s in context of %s@%s\n", name, vaultPath, vaultInfo);
		
		if ("database.uid".equals(name)) {
			return "my id";
		}
		
		if ("database.password".equals(name)) {
			return "my password";
		}
		
		return null;
	}

}
