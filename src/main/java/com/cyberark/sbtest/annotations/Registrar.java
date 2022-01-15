package com.cyberark.sbtest.annotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
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
		
		
		MultiValueMap<String, Object> attributesCont = importingClassMetadata
				.getAllAnnotationAttributes(
						ConjurPropertySources.class.getName(), false);
		

		MultiValueMap<String, Object> attributes = importingClassMetadata
				.getAllAnnotationAttributes(
						ConjurPropertySource.class.getName(), false);
		
		
		//TODO: make this work
		if (attributesCont != null)
			for (Class<?> type : collectClasses(attributesCont.get("value"))) {
				if (!registry.containsBeanDefinition(type.getName())) {
					registerBeanDefinition(registry, type, type.getName(), "");
				}
			}
		
		if (attributes != null)
			
			for (Object valuesObj : attributes.get("value")) {
				
				String[] values = (String[])valuesObj;
				
				for (String value : values) {
					if (!registry.containsBeanDefinition(com.cyberark.sbtest.core.env.ConjurPropertySource.class.getName()+"-"+value)) {
						registerBeanDefinition(registry, com.cyberark.sbtest.core.env.ConjurPropertySource.class,
								com.cyberark.sbtest.core.env.ConjurPropertySource.class.getName()+"-"+value, value);
					}
				}
			}		
		
		
	}
	
	
	
	private List<Class<?>> collectClasses(List<Object> list) {
		ArrayList<Class<?>> result = new ArrayList<Class<?>>();
		for (Object object : list) {
			for (Object value : (Object[]) object) {
				if (value instanceof Class && value != void.class) {
					result.add((Class<?>) value);
				}
			}
		}
		return result;
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
