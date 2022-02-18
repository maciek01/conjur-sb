package com.cyberark.conjur.springboot.core.env;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

public class ConjurPropertySource
//extends PropertySource<Object> {
//consider the following alternative if miss rates are excessive
extends EnumerablePropertySource<Object> {
	
	private String vaultInfo = "";
	
	private String vaultPath = "";
	
	static {
		
		// a hack to support seeding environment for the file based api token support in downstream java
		
		System.out.printf("%s\n", System.getenv("CONJUR_AUTHN_API_KEY"));
		System.out.printf("%s\n", System.getenv("CONJUR_AUTHN_TOKEN_FILE"));

		Map<String,String> newVars = new HashMap<String, String>();
		
		//TODO: resolve the value of api key from file content here CONJUR_AUTHN_TOKEN_FILE
		
		newVars.put("CONJUR_AUTHN_API_KEY", "my key from CONJUR_AUTHN_TOKEN_FILE specified locaion");
		
		try {
			set(newVars);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		System.out.printf("%s\n", System.getenv("CONJUR_AUTHN_API_KEY"));
	}
	
	
	public static void set(Map<String, String> newenv) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	    Class[] classes = Collections.class.getDeclaredClasses();
	    Map<String, String> env = System.getenv();
	    for(Class cl : classes) {
	        if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
	            Field field = cl.getDeclaredField("m");
	            field.setAccessible(true);
	            Object obj = field.get(env);
	            Map<String, String> map = (Map<String, String>) obj;
	            map.putAll(newenv);
	        }
	    }
	}

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
		
		name = ConjurConfig.getInstance().mapProperty(name);
		
		System.out.printf("Resolving %s in context of %s@%s\n", name, vaultPath, vaultInfo);
		
		
		//TODO: make the call to sdk and eliminate hard-coded "if"s
		
		
		
		if ("database.uid".equals(name)) {
			return "my id";
		}
		
		if ("database.password".equals(name)) {
			return "my password";
		}
		
		if ("MyConjurOracleId".equals(name)) {
			return "my conjur oracle id";
		}
		
		if ("MyConjurOraclePwd".equals(name)) {
			return "my conjur oracle password";
		}
		
		return null;
	}

}
