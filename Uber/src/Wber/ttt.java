package Wber;

import java.util.ArrayList;
import java.util.ListIterator;

public class ttt {
    public static void main(String[] args){
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.add(4);
        ListIterator<Integer> integerListIterator = arrayList.listIterator();
        System.out.println(integerListIterator.next());
        System.out.println(integerListIterator.next());
        System.out.println(integerListIterator.next());
        System.out.println(integerListIterator.previous());
        System.out.println(integerListIterator.previous());
        System.out.println(integerListIterator.next());

    }
}
