package com.spring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Objects;

/**
 * @ClassName HGApplicationContext
 * @Author dhg
 * @Version 1.0
 * @Date 2024/4/26 10:18
 * @Description:
 */
public class HGApplicationContext {

    private Class configClass;

    public HGApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 解析配置类
        // ComponentScan注解解析出来 --> 扫描路径 --> 扫描

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
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    public Object getBean(String beanName) {
        return null;
    }
}
