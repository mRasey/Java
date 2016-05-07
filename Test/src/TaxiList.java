import java.util.ArrayList;

class TaxiList {
    private ArrayList<Taxi> taxiList;

    TaxiList(){
        //REQUIRES:none
        //MODIFIES:taxiList
        //EFFECTS:创建出租车列表实例
        this.taxiList = new ArrayList<>();
    }

    synchronized void AddTaxi(Taxi x){
        //REQUIRES:线程锁控制
        //MODIFIES:taxiList
        //EFFECTS:向列表中加入出租车
        this.taxiList.add(x);
    }

    synchronized Taxi getTaxi(int num){
        //REQUIRES:线程锁控制
        //MODIFIES:none
        //EFFECTS:返回指定编号的出租车
        return this.taxiList.get(num);
    }
}
