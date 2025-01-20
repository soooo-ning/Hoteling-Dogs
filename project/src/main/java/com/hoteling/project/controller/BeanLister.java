package com.hoteling.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class BeanLister {
    private final ApplicationContext applicationContext;

    @Autowired
    public BeanLister(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        listBeans();
    }

    private void listBeans() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
}