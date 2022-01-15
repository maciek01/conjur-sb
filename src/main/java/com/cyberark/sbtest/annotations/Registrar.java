package com.cyberark.sbtest.annotations;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.MultiValueMap;


public class Registrar implements ImportBeanDefinitionRegistrar, BeanFactoryPostProcessor, EnvironmentAware{

	private Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	public Environment getEnvironment() {
		return environment;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
		ConfigurableEnvironment env = beanFactory.getBean(ConfigurableEnvironment.class);
		MutablePropertySources propertySources = env.getPropertySources();
		
		Collection<com.cyberark.sbtest.core.env.ConjurPropertySource> beans = beanFactory.getBeansOfType(com.cyberark.sbtest.core.env.ConjurPropertySource.class).values();
		
		for (PropertySource<?> ps : beans) {

			if (propertySources.contains(ps.getName())) {
				continue;
			}

			propertySources.addLast(ps);
		}
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		
		if (!registry.isBeanNameInUse(this.getClass().getName())) {
			registry.registerBeanDefinition(this.getClass().getName(),
					BeanDefinitionBuilder.genericBeanDefinition(this.getClass())
						.setRole(BeanDefinition.ROLE_INFRASTRUCTURE).getBeanDefinition());
		}
		
		//find container annotations
		MultiValueMap<String, Object> attributesCont = importingClassMetadata
				.getAllAnnotationAttributes(
						ConjurPropertySources.class.getName(), false);
		
		//find simple annotations
		MultiValueMap<String, Object> attributes = importingClassMetadata
				.getAllAnnotationAttributes(
						ConjurPropertySource.class.getName(), false);
		
		//resolve repeatable / container annotations
		if (attributesCont != null)
			for (Object attribs : attributesCont.get("value")) {
				for (AnnotationAttributes a : ((AnnotationAttributes[])attribs)) {
					makeAndRegisterBean(registry, (String[])a.get("value"));
				}
			}
		
		//resolve single annotations
		if (attributes != null)
			for (Object valuesObj : attributes.get("value")) {
				makeAndRegisterBean(registry, (String[])valuesObj);
			}		
	}

	private void makeAndRegisterBean(BeanDefinitionRegistry registry, String[] values) {
		for (String value : values) {
			if (!registry.containsBeanDefinition(com.cyberark.sbtest.core.env.ConjurPropertySource.class.getName()+"-"+value)) {
				registerBeanDefinition(registry, com.cyberark.sbtest.core.env.ConjurPropertySource.class,
						com.cyberark.sbtest.core.env.ConjurPropertySource.class.getName()+"-"+value, value);
			}
		}
	}
	
	private void registerBeanDefinition(BeanDefinitionRegistry registry,
			Class<?> type, String name, String value) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.genericBeanDefinition(type);
		builder.addConstructorArgValue(value);
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		registry.registerBeanDefinition(name, beanDefinition);

	}
	

}
