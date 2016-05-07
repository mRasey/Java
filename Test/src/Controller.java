import java.util.ArrayList;

class Controller extends GlobalConstant implements Runnable{
	private TaxiList taxis;
	private RequestQueue reqList;
	private Map map;
	
	Controller(TaxiList taxis, RequestQueue reqList, Map map){
        //REQUIRES:none
        //MODIFIES:taxis,reqList,map
        //EFFECTS:创建控制器实例
		this.taxis = taxis;
		this.reqList = reqList;
		this.map = map;
	}
	
	public void run(){
        try {
            while (true) {
                for (int i = 0; i < reqList.getLength(); i++) {
                    Request request = reqList.getRequest(i);
                    request = Compete(request);
                    if (System.currentTimeMillis() - request.getStartTime() >= 3000) {
                        ArrayList<Taxi> tArrayList = request.getTaxis();
                        AllocateRequest(request,tArrayList);
                        reqList.RemoveRequest(i);
                        i--;
                    }
                }
                sleep(10);
            }
        }catch(Exception e){System.out.println("");}
	}
	
	private boolean InRange(int[] requestPosition, int[] taxiPosition){
        //REQUIRES:none
        //MODIFIES:none
        //EFFECTS:判断出租车是否在请求4x4范围内并返回布尔值
		return ((taxiPosition[0] >= requestPosition[0] - 2
                && taxiPosition[0] <= requestPosition[0] +2)
                && (taxiPosition[1] >= requestPosition[1] - 2
                && taxiPosition[1] <= requestPosition[1] + 2));
	}
	
	private synchronized int BFSPath(int[] RequestPosition, int[] TaxiPosition){
        //REQUIRES:地图是连通图，存在两点间最短路径
        //MODIFIES:none
        //EFFECTS:用广度优先搜索计算两点之间最短路径，返回路径长度
		int[][][] PathMap = InitPathMap();
        PathMap[TaxiPosition[0]][TaxiPosition[1]][0] = 0;
        PathMap[TaxiPosition[0]][TaxiPosition[1]][1] = 1;
		ArrayList<int[]> PointList = new ArrayList<>();
        PointList.add(TaxiPosition);
 		while(!PointList.isEmpty()){
 			int[] point = PointList.remove(0);
 			for(int i = 0; i < 4; i++){
 				if(map.getPoint(point[0], point[1], i) != Disconnect){
                    switch (i){
                        case Left:
                            if(PathMap[point[0]][point[1] - 1][1] == Unmarked){
                                PathMap[point[0]][point[1] - 1][0] = PathMap[point[0]][point[1]][0] + 1;
                                PathMap[point[0]][point[1] - 1][1] = Marked;
                                int[] t = {point[0], point[1] - 1};
                                PointList.add(t);
                            }
                            break;
                        case Up:
                            if(PathMap[point[0] - 1][point[1]][1] == Unmarked){
                                PathMap[point[0] - 1][point[1]][0] = PathMap[point[0]][point[1]][0] + 1;
                                PathMap[point[0] - 1][point[1]][1] = Marked;
                                int[] t = {point[0] - 1, point[1]};
                                PointList.add(t);
                            }
                            break;
                        case Right:
                            if(PathMap[point[0]][point[1] + 1][1] == Unmarked){
                                PathMap[point[0]][point[1] + 1][0] = PathMap[point[0]][point[1]][0] + 1;
                                PathMap[point[0]][point[1] + 1][1] = Marked;
                                int[] t = {point[0], point[1] + 1};
                                PointList.add(t);
                            }
                            break;
                        default:
                            if(PathMap[point[0] + 1][point[1]][1] == Unmarked){
                                PathMap[point[0] + 1][point[1]][0] = PathMap[point[0]][point[1]][0] + 1;
                                PathMap[point[0] + 1][point[1]][1] = Marked;
                                int[] t = {point[0] + 1, point[1]};
                                PointList.add(t);
                            }
                    }
 				}
 			}
 		}
 		return PathMap[RequestPosition[0]][RequestPosition[1]][0];
	}
	
	private int[][][] InitPathMap(){
        //REQUIRES:none
        //MODIFIES:PathMap
        //EFFECTS:创建存储路径信息的数组
        int[][][] PathMap = new int[80][80][2];
		for(int i = 0; i < 80; i++)
			for(int j = 0; j < 80; j++){
                PathMap[i][j][0] = -1;
                PathMap[i][j][1] = 0;
			}
        return PathMap;
	}

    private Request Compete(Request request){
        //REQUIRES:请求合法
        //MODIFIES:request，taxis
        //EFFECTS:设定请求的开始时间并为抢单范围内的出租车增加信用度
        if (request.getStartTime() == 0)
            request.setStartTime(System.currentTimeMillis());
        for (int j = 0; j < 100; j++) {
            if (taxis.getTaxi(j).getStatus() == 0 && !request.Available(j)) {
                if (InRange(request.getPosition(), taxis.getTaxi(j).getPosition())) {
                    taxis.getTaxi(j).addCredit(1);
                    request.addTaxi(taxis.getTaxi(j), j);
                }
            }
        }
        return request;
    }

    private void AllocateRequest(Request request,ArrayList<Taxi> taxiList){
        //REQUIRES:请求合法，taxiList不为空
        //MODIFIES:taxis
        //EFFECTS:寻找路程最近的出租车分配请求，修改该出租车请求及状态
        if (taxiList.size() > 0) {
            int MaxCredit = 0;
            ArrayList<Taxi> MaxCreditTaxiList = new ArrayList<>();
            int MinPathTaxi = 0;
            for (Taxi x : taxiList)
                if (x.getCredit() > MaxCredit && x.getStatus() == inWait)
                    MaxCredit = x.getCredit();
            for (Taxi x : taxiList) {
                if (x.getCredit() == MaxCredit && x.getStatus() == inWait)
                    MaxCreditTaxiList.add(x);
            }
            if (MaxCreditTaxiList.size() >= 1) {
                int[] pathLength = new int[MaxCreditTaxiList.size()];
                for (int k = 0; k < MaxCreditTaxiList.size(); k++)
                    pathLength[k] = BFSPath(request.getPosition(), MaxCreditTaxiList.get(k).getPosition());
                for (int l = 0; l < pathLength.length; l++) {
                    if (pathLength[l] < pathLength[MinPathTaxi]) MinPathTaxi = l;
                }
                Taxi taxi = MaxCreditTaxiList.get(MinPathTaxi);
                System.out.println("taxi num " + taxi.getNumber());
                taxi.setRequest(request);
                taxi.setStatus(inStart);
            }
        }
        else
            System.out.println("no car");
    }

}
