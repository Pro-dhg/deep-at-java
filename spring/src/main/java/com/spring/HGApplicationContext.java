package com.spring;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    // 存储的是 beanPostProcess 给后续创建bean的时候使用
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>() ;

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
                Object bean = createBean(beanName,beanDefinition);
                singletonObjects.put(beanName,bean);
            }
        }

    }

    public Object createBean(String beanName,BeanDefinition beanDefinition){
        // 获取要创建的bean的类型
        Class clazz = beanDefinition.getClazz();
        try {
            // 通过反射获取实例对象
            Object instance = clazz.getDeclaredConstructor().newInstance();

            // 对属性进行赋值，也就是依赖注入

            for (Field declaredField : clazz.getDeclaredFields()) {
                // 判断所有属性是否加上了Autowired
                if (declaredField.isAnnotationPresent(Autowired.class)){

                    // 给属性赋值 ！
                    Object bean = getBean(declaredField.getName());

                    if (bean == null){
                        throw new NullPointerException() ;
                    }
                    declaredField.setAccessible(true);
                    declaredField.set(instance,bean);
                }

            }

            // Aware回调
            // 当前实例是不是实现了BeanNameAware接口
            if (instance instanceof BeanNameAware){
                // 因为实现了，所以可以强制转换
                ((BeanNameAware)instance).setBeanName(beanName);
            }

            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                // 外部加工后的instance
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            // 初始化
            if (instance instanceof InitializingBean){
                try {
                    ((InitializingBean)instance).afterPropertiesSett();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                // 外部加工后的instance
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }

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
        System.out.println("componentscan路径："+path);
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
        System.out.println("对应路径："+resource.getFile());
        if (file.isDirectory()) {

            File[] files = file.listFiles();
            for (File f : files) {
                System.out.println(f.getAbsolutePath());
                String fileName = f.getAbsolutePath();

                if (fileName.endsWith(".class")){
                    String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
//                    className = className.replace("/", "."); // mac
                    className = className.replace("\\", "."); //windows
                    System.out.println(className);

                    try {
                        Class<?> clazz = classLoader.loadClass(className);

                        if (clazz.isAnnotationPresent(Component.class)){
                            //表示当前这个类是一个Bean
                            //解析类，判断当前bean是单例bean，还是prototype的bean --> BeanDefinition
                            // BeanDefinition

                            /**
                             * 判断clazz是否实现了BeanPostProcessor类
                             * 为什么不用instanceof ？ 因为instanceof是判断实例对象是不是实现了，
                             *            而我们要判断的是某一个类是不是实现了BeanPostProcessor
                             */
                            //当扫描到有beanPostProcessor的类时，需要特殊处理
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                BeanPostProcessor instance = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();

                                // 然后就需要把它存起来，实例化的时候用
                                beanPostProcessorList.add(instance) ;
                            }

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
                    } catch (ClassNotFoundException | NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
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
                Object bean = createBean(beanName,beanDefinition);
                return bean ;
            }
        }else {
            // 不存在对应的bean
            throw new NullPointerException() ;
        }
    }
}
