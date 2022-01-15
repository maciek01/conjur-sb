package com.cyberark.sbtest;

import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import com.cyberark.sbtest.AppConfig.TestBean;
import com.cyberark.sbtest.annotations.ConjurPropertySource;

import javax.sql.DataSource;

@Configuration
@ConjurPropertySource("myorg/myapp")
public class AppConfig {
	
	
    public class TestBean {

		public void setPassword(String property) {
			System.out.printf("%s\n", property);
		}

	}


	@Autowired
    DataSourceProperties dataSourceProperties;
    
    @Autowired
    Environment env;

    @Bean
    @ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
    DataSource realDataSource() {
        DataSource dataSource = DataSourceBuilder
                .create(this.dataSourceProperties.getClassLoader())
                .url(this.dataSourceProperties.getUrl())
                .username(this.dataSourceProperties.getUsername())
                .password(this.dataSourceProperties.getPassword())
                .build();
        return dataSource;
    }

    @Bean
    @Primary
    DataSource dataSource() {
        return new DataSourceSpy(realDataSource());
    }

    
    @Value("${database.uid}")
    String userId;
    
    @Bean
    public TestBean testBean() {
        TestBean testBean = new TestBean();
        testBean.setPassword(env.getProperty("database.password"));
        return testBean;
    }
    
    
    
}
