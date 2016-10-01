package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Test {
    public static void main(String[] args) {
//        Foo f = new Foo();
//        f.foo(System.out::println);

//        Predicate<Integer> predicate1 = integer -> integer <= 0;
//        Predicate<Integer> predicate2 = integer -> integer > 0;
//        System.out.println("and: " + predicate1.and(predicate2).test(1));
//        System.out.println("or: " + predicate1.or(predicate2).test(1));
//        System.out.println("negate: " + predicate1.negate().test(1));

        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return new Random().nextInt(100);
            }
        };

        int[] ints = new int[10];
        for(int i = 0; i < 10; i++) {
            ints[i] = supplier.get();
        }
        Arrays.stream(ints).forEach(System.out::println);
    }
}

class Foo {
    private int[] data = new int[10];

    public Foo() {
        for(int i = 0; i < 10; i++) {
            data[i] = i;
        }
    }

    public void foo(Consumer<Integer> consumer) {
        for(int i : data)
            consumer.accept(i);
    }
}