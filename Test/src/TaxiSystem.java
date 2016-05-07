public class TaxiSystem {

	public static void main(String[] args){
        //REQUIRES:none
        //MODIFIES:none
        //EFFECTS:创建所有线程执行程序
        try {
            Map map = new Map();
            map.ReadMap("D:\\map.txt");
            RequestQueue reqList = new RequestQueue();
            TaxiList taxis = new TaxiList();
            Controller controller = new Controller(taxis, reqList, map);

            new Thread(map).start();
            for (int i = 0; i < 100; i++) {
                Taxi x= new Taxi(i, map);
                taxis.AddTaxi(x);
                Thread thread = new Thread(x);
                thread.start();
            }

            Test test = new Test(reqList, taxis,map);
            Thread thread = new Thread(test);
            thread.start();

            Thread thread2 = new Thread(controller);
            thread2.start();
        }catch(Exception e){System.out.println("");}
	}
}
