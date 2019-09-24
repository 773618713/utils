package com.scy.technique.jvm;

public class SubClass extends SuperClass {
    static {
        System.out.println("SubClass init!" + SuperStaticAttribute);
    }
    {
        System.out.println("SubBlock");
    }

    SubClass(){
        System.out.println("SubConstructor");
    }
}
