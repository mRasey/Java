package util;

import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import jdk.nashorn.internal.runtime.Timing;
import jdk.nashorn.internal.runtime.regexp.JoniRegExp;
import jdk.nashorn.internal.runtime.regexp.joni.Matcher;
import org.omg.CORBA.INTERNAL;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.*;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static util.e.JISHU;
import static util.e.OUSHU;

public class Main {
    public static void main(String[] args) {
//        Integer[] ints = new Integer[10];
//        Arrays.fill(ints, 10);
//        Arrays.sort(ints, (o1, o2) -> o1 - o2);
//        Arrays.stream(ints).filter(i -> i > 0).distinct().forEach(i -> System.out.println(i));

//        A[] as = new A[10];
//        for(int i = 0; i < 10; i++) {
//            as[i] = new A(i);
//        }
//        Arrays.sort(as, (a, b) -> b.a - a.a);
//        Arrays.stream(as).forEach(i -> System.out.println(i.a));

//        ArrayList<Integer> arrayList = new ArrayList<>();
//        for(int i = 0; i < 10; i++)
//            arrayList.add(i);
//        arrayList.sort((a, b) -> b - a);
//        arrayList.forEach(System.out::println);

//        Collection<Object> collection = new ArrayList<>();
//        collection.add(new A(0));
//        collection.add(new B(0));
//        System.out.println(Collections.frequency(collection, new A(0)));
//        Arrays.spliterator(as).forEachRemaining(input -> System.out.println(input.a));
//        Arrays.spliterator(as).trySplit().trySplit().forEachRemaining(input -> System.out.println(input.a));
//        A[] bs = (A[]) Arrays.stream(as).toArray();
//        Arrays.stream(as).map((a -> {
//            a.a = 10;
//            return a;
//        })).forEach(a -> System.out.println(a.a));

//        ArrayList<A> as = new ArrayList<>();
//        ArrayList<A> as2 = new ArrayList<>();
//        as.add(new A(1));

//        ArrayList<Integer> arrayList = new ArrayList<>();
//        for(int i = 0; i < 10; i++) {
//            arrayList.add(i);
//        }
//        Collections.shuffle(arrayList);
//        Arrays.stream(arrayList.toArray()).forEach(System.out::println);

//        Comparator<Integer> comparator = new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                return o1 - o2;
//            }
//        };

        //todo test Consumer
//        Predicate<Integer> predicate = new Predicate<Integer>() {
//            @Override
//            public boolean test(Integer integer) {
//                return false;
//            }
//        };
//        predicate.negate();
//        predicate.or(predicate);
//
//        Consumer<Integer> consumer = i -> System.out.println("hello");
//        Consumer<Integer> consumer2 = i -> System.out.println("world");
//
//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(1);
//        list.add(1);
//        list.add(1);
//        list.forEach(consumer.andThen(consumer2));

//        new C().foo(System.out::println);

        //todo test BinaryOperator
        /*BinaryOperator.maxBy(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        System.out.println(BinaryOperator.maxBy((Comparator<Integer>) (o1, o2) -> o1 - o2).apply(100, 2));*/

        //todo test Predicate
//        Predicate<Integer> predicate = integer -> integer < 0;
//        Predicate<Integer> predicate2 = integer -> integer > 0;
//        System.out.println(predicate.and(predicate2).test(1));
//        System.out.println(predicate.or(predicate2).test(1));

        //todo test Supplier
//        Supplier<Integer> supplier = () -> new Random().nextInt(100);
//        for(int i = 0; i < 5; i++)
//            System.out.println(supplier.get());

        //todo test Function
//        Function<Integer, Integer> function = new Function<Integer, Integer>() {
//            @Override
//            public Integer apply(Integer integer) {
//                System.out.println(integer-1);
//                return integer;
//            }
//        };
//        function.andThen(function).apply(1);
//        function.compose(function).apply(1);

//        Function function1 = Function.identity();
//        System.out.println(function1.andThen(function).apply(100));

        //todo test toXXXFunction
//        ToDoubleFunction<Double> toDoubleFunction = value -> {
//            System.out.println(value);
//            return value;
//        };
//        toDoubleFunction.applyAsDouble(1.0);

        //todo test DoubleSummaryStatistics
//        DoubleSummaryStatistics doubleSummaryStatistics = new DoubleSummaryStatistics();
//        doubleSummaryStatistics.accept(1);
//        doubleSummaryStatistics.accept(2);
//        doubleSummaryStatistics.accept(10);
//        System.out.println(doubleSummaryStatistics.getAverage());

        //todo test hashMap
//        HashMap<Integer, String> hashMap = new HashMap<>();
//        hashMap.put(1, "hello");
//        hashMap.put(2, "world");
//        hashMap.compute(2, (key, value) -> {
//            System.out.println(key + " " + value);
//            return value;
//        });
//        hashMap.merge(2, "java", (oldValue, newValue) -> {
//            System.out.println(oldValue + " " + newValue);
//            return newValue;
//        });
//        hashMap.merge(3, "hadoop", (oldValue, newValue) -> { //oldValue != null 才调用
//            System.out.println(oldValue + " " + newValue);
//            return newValue;
//        });
//
//        HashMap<Integer, String> hashMap1 = new HashMap<>();
//        hashMap1.put(1, "world");
//        hashMap.putAll(hashMap1);
//        System.out.println("1:" + hashMap.get(1));
//
//        hashMap.replaceAll((key, value) -> {
//            if(key == 1)
//                value = "spring";
//            return value;
//        });
//        System.out.println(hashMap.get(1));

        //todo test iterator
//        ArrayList<Integer> arrayList = new ArrayList<>();
//        arrayList.add(1);
//        arrayList.add(1);
//        arrayList.add(1);
//        arrayList.iterator().forEachRemaining(i -> System.out.println(i));

        //todo test observable
//        MyObservable m = new MyObservable();
//        m.addObserver((o, arg) -> {
//            MyObservable myObservable = (MyObservable) o;
//            if(myObservable.getE() == e.JISHU)
//                System.out.println("JISHU");
//            else
//                System.out.println("OUSHU");
//        });
//        m.setA(1);
//        m.setA(2);
//        m.setA(3);
//        m.setA(4);

        //todo test optional
//        Optional<Integer> optional = Optional.of(1);
//        System.out.println(optional.isPresent());

        //todo test primitiveIterator
//        PrimitiveIterator.OfInt ofInt = new PrimitiveIterator.OfInt() {
//            @Override
//            public int nextInt() {
//                return 0;
//            }
//
//            @Override
//            public boolean hasNext() {
//                return false;
//            }
//        };

        //todo test priorityQueue
//        PriorityQueue<A> priorityQueue = new PriorityQueue<>((a, b) -> a.a - b.a);
//        priorityQueue.add(new A(10));
//        priorityQueue.add(new A(1));
//        priorityQueue.forEach(a -> System.out.println(a.a));

        //todo test queue
//        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
//        queue.offer(1);
//        queue.add(2);
//        queue.forEach(System.out::println);

        //todo test pattern & matcher
//        Pattern p = Pattern.compile("\\*");
//        java.util.regex.Matcher matcher = p.matcher("123");

        //todo test stream
        int[] a = new int[10];
        for(int i = 0; i < 10; i++)
            a[i] = i;
        Arrays.stream(a).onClose(() -> {
            for(int i = 0; i < 10; i++)
                System.out.println(a[i]);
        }).close();
        Arrays.stream(a).flatMap(new IntFunction<IntStream>() {
            @Override
            public IntStream apply(int value) {
                return null;
            }
        });


        //todo test collector

    }
}

class A {
    int a;
    public A(int a) {
        this.a = a;
    }

    @Override
    public boolean equals(Object obj) {
        return true;
    }
}

class B extends A {

    public B(int a) {
        super(a);
    }
}

class C {
    void foo(Consumer<Integer> c) {
        c.accept(1);
    }
}

class MyObservable extends Observable {
    int a = 0;
    e e = OUSHU;

    public void setA(int a) {
        this.a = a;
        e = JISHU;
        if(a % 2 == 0) {
            e = OUSHU;
        }
        setChanged();
        notifyObservers();
    }

    public e getE() {
        return e;
    }

}

enum e {
    OUSHU,
    JISHU
}

class MyObserver implements Observer {

    @Override
    public void update(Observable o, Object arg) {

    }
}

