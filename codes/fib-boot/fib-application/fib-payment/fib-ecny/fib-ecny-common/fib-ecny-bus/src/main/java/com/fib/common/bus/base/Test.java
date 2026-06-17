package com.fib.common.bus.base;

public class Test {
    static void main() {

    }

    public String desc(Shape shape) {
        return switch (shape) {
            case Circle c -> "";
            case Rectangle r -> "";
        };
    }
}
