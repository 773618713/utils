package com.scy.technique.jvm.classloader;

import java.io.IOException;
import java.io.InputStream;

/**
 * 类加载器 相关
 * 使用自定义的类加载器加载类
 *
 */
public class ClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf(".")+1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if(is == null){
                        return super.loadClass(name);
                    }
                    byte[] b = new byte[0];
                    b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name, b, 0 ,b.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException(name);
                }
            }
        };

        //使用自定义的类加载器加载
        Object obj = myLoader.loadClass("com.scy.technique.jvm.classloader.ClassLoaderTest").newInstance();
        System.out.println(obj.getClass());

        //使用应用程序类加载器加载
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        obj = classLoader.loadClass("com.scy.technique.jvm.classloader.ClassLoaderTest").newInstance();
        //obj = Class.forName("com.scy.technique.jvm.classloader.ClassLoaderTest").newInstance();

        System.out.println(obj instanceof com.scy.technique.jvm.classloader.ClassLoaderTest);
    }
}
