package org.fib.bip;

import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App {
    static void main() {
        System.out.println("Hello World!");
        Thread t = Thread.startVirtualThread(() -> {
            System.out.println(System.currentTimeMillis());
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        IO.println(t.getName());
    }
}
