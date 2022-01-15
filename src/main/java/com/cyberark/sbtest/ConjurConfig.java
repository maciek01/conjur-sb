package com.cyberark.sbtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
//@PropertySource("classpath:/com/${my.placeholder:default/path}/app.properties")
public class ConjurConfig {

    @Autowired
    Environment env;

//    @Bean
//    public TestBean testBean() {
//        TestBean testBean = new TestBean();
//        testBean.setName(env.getProperty("testbean.name"));
//        return testBean;
//    }
}