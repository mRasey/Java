package oo10_taxi;
import java.util.*;
public class Person extends Thread{
	private ReqQueue RQ;
	private Taxi[] taxi;
	private Map map;
	public Person(ReqQueue RQ,Taxi[] taxi,Map map){
		this.RQ = RQ;
		this.taxi = taxi;
		this.map = map;
	}
	public void run() {
//  you can write the test code here
//	I provide you the test function named installReqRondom and isntallReq in ReqQueue class
//	also you can watch the state(location,time etc) of taxi by function named getLocation(return String) and getTime(at 100ms level)
//	and there are some else functions to watch the state of taxi,you can get them by reading the code
//	thanks for your test
//        Random random = new Random();
//        for (int j = 0; j < 80; j++){
//            for (int i = 0; i < 79; i++) {
//                map.closeRoad(j, i, j, i + 1);
//            }
//        }
//        for (int j = 0; j < 80; j++){
//            for (int i = 0; i < 79; i++) {
//                map.closeRoad(i, j, i+1, j);
//            }
//        }
//        while(true) {
//            System.out.println(taxi[0].getLocation());
//            System.out.println(taxi[1].getLocation());
//            System.out.println(taxi[2].getLocation());
//            System.out.println(taxi[3].getLocation());
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            taxi[10].improveCredit(10000);
//        }
//        taxi[11].improveCredit(10000);
		for(int i = 0; i < 1;i++){
//			RQ.installReq(random.nextInt(79), random.nextInt(79),
//                    random.nextInt(79), random.nextInt(79));
			RQ.installReq(0,0,0,79);
		}
//        try {
//            Thread.sleep(100000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        while(true){
//            System.out.println();
//        }
//        for(int i = 0; i < 100; i++){
//            System.out.println(taxi[i].getCredit());
//        }
//        try{
//			sleep(1000);
//			map.closeRoad(60,60,60,61);
////			map.closeRoad(59,60,60,60);
//			sleep(10000);
//			int j = 0;
//			while(j++ <3){
//				sleep(10000);
//                for(int i = 0;i < 100;i++){
//    //				System.out.println(taxi[i].getNumOfSer());
//                    ListIterator<ArrayList<String>> liter = taxi[i].getIterator();
//                    while(liter.hasNext()){
//                        System.out.println((String)liter.next().toString());
//                    }
//                    taxi[i].IterToString(0);
//                    taxi[i].IterToString(1);
//                    taxi[i].IterToString(2);
//                }
//			}

			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
}
