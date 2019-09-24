package com.scy.technique.jvm;

public class SuperClass {
    final static String SuperFinalStaticAttribute = "SuperFinalStaticAttribute";
    static {
        System.out.println("SuperClass init! " + SuperFinalStaticAttribute);
    }

    public static String SuperStaticAttribute = "SuperStaticAttribute";

    {
        System.out.println("SuperBlock");
    }

    public String SuperAttribute = "SuperAttribute";

    SuperClass(){
        System.out.println("SuperConstructor" + SuperAttribute);
    }
}
