package edu.eci.arsw.primefinder;
import java.util.LinkedList;
import java.util.List;


public class PrimeFinderThread extends Thread {
    boolean suspend = false;
    int a, b;
    private static List<Integer> primes = new LinkedList<Integer>();
    public PrimeFinderThread(int a, int b) {
        super();
        this.a = a;
        this.b = b;
    }
    public void run() {
        for (int i = a; i <= b; i++) {
            if (isPrime(i)) {
                primes.add(i);
                System.out.println(i);
                enSuspension();
            }
        }
    }
    boolean isPrime(int n) {
        if (n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    public synchronized void suspender() {
        suspend = true;
    }
    public synchronized void reanudar() {

        suspend = false;

        notifyAll();
    }
    public synchronized void enSuspension() {
        while (suspend) {
            try {
                wait();
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }
    public static List<Integer> getPrimes() {
        return primes;
    }
}