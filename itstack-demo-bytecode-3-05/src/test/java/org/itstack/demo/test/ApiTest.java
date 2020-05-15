package org.itstack.demo.test;


public class ApiTest {

    public int sum(int i, int m) {
        Monitor.info("sum", i, m);
        return i + m;
    }

    static class Monitor {

        public static void info(String name, int... parameters) {
            System.out.println(name);
        }

    }
}


