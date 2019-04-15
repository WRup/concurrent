package com.myapp.concurrent;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ConLinkedDeque {

    public static void main(String[] args) {
        Deque<String> kolejka = new ConcurrentLinkedDeque<>();
        Thread[] pulaWątków = new Thread[100];

        for (int i = 0; i < pulaWątków.length; i++) {
            Wypełniacz wypełniacz = new Wypełniacz(kolejka);
            pulaWątków[i] = new Thread(wypełniacz, "Wypełniacz" + i);
            pulaWątków[i].start();
        }
        granieNaCzekanie(pulaWątków);
        System.out.println("Wielkość kolejki to : " + kolejka.size());

        for (int i = 0; i < pulaWątków.length; i++) {
            Usuwacz usuwacz = new Usuwacz(kolejka);
            pulaWątków[i] = new Thread(usuwacz, "Usuwacz" + i);
            pulaWątków[i].start();
        }
        System.out.println("Start " + pulaWątków.length + " watkow usuwajacych dane z kolejki");

        granieNaCzekanie(pulaWątków);
        System.out.println("Wielkosc koncowa kolejki : " + kolejka.size());
    }

    private static void granieNaCzekanie(Thread[] threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}

class Wypełniacz implements Runnable {

    private Deque<String> kolejka;

    Wypełniacz(Deque<String> queue) {
        kolejka = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            kolejka.add("Element" + i);
        }
    }
}

class Usuwacz implements Runnable {

    private Deque<String> kolejka;

    Usuwacz(Deque<String> queue) {
        kolejka = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5000; i++) {
            kolejka.pollFirst();
            kolejka.pollLast();
        }
    }
}



