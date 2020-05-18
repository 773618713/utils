package com.scy.technique.thread;

public class App {

    public synchronized void printEven() {
        try {
            for (int i = 0; i <= 10; i++) {
                if (i % 2 == 0) {
                    System.out.println(i);
                    notify();
                    wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void printOdd() {
        try {
            for (int i = 0; i <= 10; i++) {
                if (i % 2 == 1) {
                    wait();
                    System.out.println(i);
                    notify();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        App app = new App();
        new Thread(new Runnable() {
            @Override
            public void run() {
                app.printEven();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                app.printOdd();
            }
        }).start();
    }
}
