import java.util.Date;
import java.util.Random;

class Test extends GlobalConstant implements Runnable{
    private RequestQueue reqlist;
    private Random random = new Random();
    private TaxiList taxilist;
    private long starttime = new Date().getTime();
    private Map map;

    Test(RequestQueue reqlist, TaxiList taxilist,Map map){
        //REQUIRES:none
        //MODIFIES:reqlist,taxilist,map
        //EFFECTS:创建乘客实例
        this.reqlist = reqlist;
        this.taxilist = taxilist;
        this.map = map;
    }

    public void run(){
        try {
            ChangeConnect(55, 55, Left);
            ChangeConnect(55, 55, Right);
            AddRequest(55, 54, 55, 56, 0);
            while(true){
                int i = map.getPoint(55, 55, Left);
                if(i != -1)
                    System.out.println(i);
            }
            /**********************
             *  example 1 : Random  request
             ********************/
//            while (true) {
//            AddRequest(0,0,79,79,50);
//                RandomRequest();
//            }

            /**********************
             *  example 2 : Specific  request
             ********************/
//            AddRequest(0,0,79,79,1000);

            /**********************
             *  example 3 : output taxi status
             ********************/
//            getTaxiStatus(23);

            /**********************
             *  example 3 : output taxi status
             ********************/
//            ChangeConnect(79,0,2);
        }catch(Exception e){System.out.println("");}
    }

    private void AddRequest(int startx, int starty, int desx, int desy,int time){
        //REQUIRES:none
        //MODIFIES:reqlist
        //EFFECTS:向请求队列中加入指定起点与终点的新请求
        int sleeptime;
        if(time<50)sleeptime=50;
        else sleeptime = time;
        Request newrequest = new Request(startx,starty,desx,desy);
        System.out.println("NEW REQUEST : ("+startx+","+starty+")  to  ("+desx+","+desy+")");
        this.reqlist.AddRequest(newrequest);
        sleep(sleeptime);
    }

    private void RandomRequest(){
        //REQUIRES:none
        //MODIFIES:reqlist
        //EFFECTS:向请求队列中加入随机起点与终点的新请求
        int startx = random.nextInt(80);
        int starty = random.nextInt(80);
        int desx = random.nextInt(80);
        int desy = random.nextInt(80);
        Request newrequest = new Request(startx,starty,desx,desy);
        System.out.println("NEW REQUEST : ("+startx+","+starty+")  to  ("+desx+","+desy+")");
        this.reqlist.AddRequest(newrequest);
        sleep(50);
    }

    private void getTaxiStatus(int num){
        //REQUIRES:none
        //MODIFIES:none
        //EFFECTS:控制台输出指定编号出租车的状态信息
        if(num>=0 && num<=99) {
            Taxi t = this.taxilist.getTaxi(num);
            long time = new Date().getTime()-starttime;
            System.out.println("Time:"+time+" Taxi No." +t.getNumber()+" Credit:"+t.getCredit()+" Position:("+t.getPosition()[0]+","+t.getPosition()[1]+")");
        }else
            System.out.println("invalid taxi number!");
    }

    private void ChangeConnect(int posX,int posY,int direction){
        //REQUIRES:none
        //MODIFIES:map
        //EFFECTS:增删改变地图上指定边
        map.ChangeConnect(posX,posY,direction);
    }
}
