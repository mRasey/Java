package oo10_taxi;

import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class ttt {
    public static void main(String[] args) throws InterruptedException {
        Map map = new Map();
        Random rd = new Random();
        ReqQueue RQ = new ReqQueue();
//        Scanner keyboard = new Scanner(System.in);
        System.out.println("Please input the Absolute Path of the map(should be .txt file):");
        try{
//            String file = keyboard.nextLine();
            String file = "D:\\123\\test.txt";
            map.inStallMap(file);
            System.out.println("Please input the Absolute Path of the map(should be .txt file):");
//            String files = keyboard.nextLine();
            String files = "D:\\123\\789.txt";
            map.installLight(files);
        }catch(Exception re){
            re.printStackTrace();
        }
        Light light = new Light(map.getMap(),map.getLight_NS(),map.getLight_EW());
        light.start();
        //start initialize taxi array;
        Buffer buf = new Buffer(map.getXroad(),map.getYroad());
        buf.start();
        Taxi[] taxi = new Taxi[100];
        int i = 0;
//        for(;i < 70;i++){
//            taxi[i] = new Taxi(i,0,RQ,map.getMap(),map.getWay(),buf,map,map.getLight_NS(),map.getLight_EW());
//            taxi[i].start();
//        }
        Buffer Nbuf = new Buffer(map.getXroadC(),map.getYroadC());
        Nbuf.start();
        for(;i < 100;i++){
            taxi[i] = new VipTaxi(i,0,RQ,map.getMapC(),map.getWay(),Nbuf,map,map.getLight_NS(),map.getLight_EW());
            taxi[i].start();
        }
        //Control thread and person thread must be after initialization of taxi array
        Control ctr = new Control(taxi,	RQ,map.getWay());
        Person per = new Person(RQ,taxi,map);
        ctr.start();
        System.out.println("System has started");
        per.start();//test Thread started here

        Thread.sleep(20000);
        Iterator iterators = taxi[10].getIteratorByNum(1);
        while(iterators.hasNext()){
            System.out.println(iterators.next().toString());
        }

//        while(true){
//            System.out.println(Nbuf.getYroad()[0][1]);
//            Thread.sleep(30);
//        }
    }
}
