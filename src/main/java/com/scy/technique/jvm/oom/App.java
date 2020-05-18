package com.scy.technique.jvm.oom;


import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        List list = new ArrayList();
        while (true)
        list.add(new App());
    }
}
