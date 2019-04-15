package com.myapp.concurrent;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConHashMap {


    public static void main(String[] args) throws InterruptedException {
        final Map<Integer, String> hashMap = new ConcurrentHashMap<>();

        Thread write1 = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                hashMap.putIfAbsent(i, "A" + i);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread write2 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                hashMap.putIfAbsent(i, "B" + i);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread read = new Thread(() -> {
            Iterator<Integer> ite = hashMap.keySet().iterator();
            while (ite.hasNext()) {
                Integer key = ite.next();
                System.out.println(key + ": " + hashMap.get(key));
            }
        });

        write1.start();
        read.start();
        write2.start();

        write1.join();
        write2.join();
        read.join();
    }
}
