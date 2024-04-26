package com.spring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName HGApplicationContext
 * @Author dhg
 * @Version 1.0
 * @Date 2024/4/26 10:18
 * @Description:
 */
public class HGApplicationContext {

    private Class configClass;

    // 单例池
    private ConcurrentHashMap<String,Object> singletonObjects = new ConcurrentHashMap<>() ;

    //所有扫描出来bean的定义
    private ConcurrentHashMap<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>() ;

    public HGApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 解析配置类
        // ComponentScan注解解析出来 --> 扫描路径 --> 扫描 --> Beandefinition --> BeanDefinitionMap
        scan(configClass);

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey() ;
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.getScope().equals("singleton")) {
                // 单例Bean
                Object bean = createBean(beanDefinition);
                singletonObjects.put(beanName,bean);
            }
        }

    }

    public Object createBean(BeanDefinition beanDefinition){
        // 获取要创建的bean的类型
        Class clazz = beanDefinition.getClazz();
        try {
            // 通过反射获取实例对象
            Object instance = clazz.getDeclaredConstructor().newInstance();
            return instance ;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void scan(Class configClass) {
        //判断注入类有没有ComponentScan注解
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        //扫描路径
        String path = componentScanAnnotation.value();
        System.out.println(path);
        path = path.replaceAll("\\.","/");

        // 扫描
        // 获取注解下面的类
        // 类加载器
        // Bootstrap--->jre/lib
        // Ext --->jre/ext/lib
        // App ---> classpath --->对应的路径
        ClassLoader classLoader = HGApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());
        if (file.isDirectory()) {

            File[] files = file.listFiles();
            for (File f : files) {
                System.out.println(f.getAbsolutePath());
                String fileName = f.getAbsolutePath();

                if (fileName.endsWith(".class")){
                    String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                    className = className.replace("/", ".");
                    System.out.println(className);

                    try {
                        Class<?> clazz = classLoader.loadClass(className);

                        if (clazz.isAnnotationPresent(Component.class)){
                            //表示当前这个类是一个Bean
                            //解析类，判断当前bean是单例bean，还是prototype的bean --> BeanDefinition
                            // BeanDefinition

                            Component componentAnnotation = clazz.getDeclaredAnnotation(Component.class);
                            String beanName = componentAnnotation.value();

                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClazz(clazz);
                            if (clazz.isAnnotationPresent(Scope.class)){
                                Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                                beanDefinition.setScope(scopeAnnotation.value());
                            } else {
                                beanDefinition.setScope("singleton");
                            }

                            beanDefinitionMap.put(beanName,beanDefinition);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public Object getBean(String beanName) {

        if (beanDefinitionMap.containsKey(beanName)){
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")){
                Object o = singletonObjects.get(beanName);
                return o;
            }else {
                // 如果不是单例的，是原型的，那就创建Bean对象 ？
                Object bean = createBean(beanDefinition);
                return bean ;
            }
        }else {
            // 不存在对应的bean
            throw new NullPointerException() ;
        }
    }
}
