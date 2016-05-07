import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
class Taxi extends GlobalConstant implements Runnable{
	private int num;
	private int credit;
	private int status;
	private int waitingTime;
	private Map map;
	private int[] position = new int[2];
	private Random rand = new Random();
	private Request request;
	
	Taxi(int num, Map map){
        //REQUIRES:none
        //MODIFIES:num，credit，status，waitingTime，map，position,request
        //EFFECTS:创建出租车实例
		this.num = num;
		credit = 0;
		status = inWait;
		waitingTime = 0;
		this.map = map;
		position[0] = rand.nextInt(80);
		position[1] = rand.nextInt(80);
        request = null;
	}
	
	synchronized void setRequest(Request request){
        //REQUIRES:出租车处于等待服务状态，线程锁控制
        //MODIFIES:request
        //EFFECTS:为出租车分配请求
		this.request = request;
	}
	
	synchronized int getStatus(){
        //REQUIRES:线程锁控制
        //MODIFIES:none
        //EFFECTS:返回出租车的状态
        return this.status;
    }

    synchronized void setStatus(int status){
        //REQUIRES:线程锁控制
        //MODIFIES:status
        //EFFECTS:修改出租车的状态
        this.status = status;
    }

    synchronized int[] getPosition(){
        //REQUIRES:线程锁控制
        //MODIFIES:none
        //EFFECTS:返回出租车的坐标
        return this.position;
    }

    synchronized int getCredit(){
        //REQUIRES:线程锁控制
        //MODIFIES:none
        //EFFECTS:返回出租车的信用度
        return this.credit;
    }

    synchronized void addCredit(int newCredit){
        //REQUIRES:线程锁控制
        //MODIFIES:credit，程序终止
        //EFFECTS:增加出租车的信用度，当信用度达到上限时令程序终止
		this.credit += newCredit;
        if(this.credit>=99999999)
            System.exit(1);
    }

    synchronized int getNumber(){
        //REQUIRES:线程锁控制
        //MODIFIES:none
        //EFFECTS:返回出租车编号
        return this.num;
    }
	
	public void run(){
        try {
            while (true) {
                switch (this.status) {
                    case inWait:
                        WaitStep();
                        break;
                    case inStart:
                        StartStep();
                        break;
                    case inServe:
                        ServeStep();
                        break;
                    default:
                }
            }
        }catch(Exception e){System.out.print("");}
	}

	private void RandomStep(){
        //REQUIRES:出租车处于等待服务状态
        //MODIFIES:position
        //EFFECTS:随机选择一条车流量最小的边走一步
		sleep(100);
        int MiniFlow = 9999;
        for(int i = 0; i < 4; i++)
            if(map.getPoint(position[0], position[1], i) != Disconnect && map.getPoint(position[0], position[1], i) < MiniFlow)
                MiniFlow = map.getPoint(position[0], position[1], i);
		int MiniFlowChoice = 0;
        for(int i=0; i < 4; i++){
            if(map.getPoint(position[0], position[1], i) != Disconnect && map.getPoint(position[0], position[1], i) == MiniFlow){
                MiniFlowChoice+=1;
            }
        }
        int randomChoice = rand.nextInt(MiniFlowChoice);
        int targetChoice = 0;
        int finalChoice = 0;
        for(; targetChoice < 4; targetChoice++){
            if(map.getPoint(position[0], position[1], targetChoice) != Disconnect && map.getPoint(position[0], position[1], targetChoice) == MiniFlow){
                if(finalChoice == randomChoice)
                    break;
                finalChoice+=1;
            }
        }
        MoveOneStep(targetChoice);

    }

    private void MoveOneStep(int choice){
        //REQUIRES:出租车处于非停止服务状态
        //MODIFIES:position
        //EFFECTS:根据选择的行驶方向更新车流量，然后更新车的坐标
        switch(choice){
            case Left:
                map.addFlow(position[0],position[1],Left);
                map.addFlow(position[0],position[1]-1,Right);
                position[1] -= 1;
                break;
            case Up:
                map.addFlow(position[0],position[1],Up);
                map.addFlow(position[0]-1,position[1],Down);
                position[0] -= 1;
                break;
            case Right:
                map.addFlow(position[0],position[1],Right);
                map.addFlow(position[0],position[1]+1,Left);
                position[1] += 1;
                break;
            default:
                map.addFlow(position[0],position[1],Down);
                map.addFlow(position[0]+1,position[1],Up);
                position[0] += 1;
        }
    }

    private synchronized int BFS(int[] target){
        //REQUIRES:必须保证地图是连通图
        //MODIFIES:none
        //EFFECTS:用广度优先算法确定下一步的方向
        boolean[][] visit = new boolean[80][80];
        ArrayList<int[]> MainLayer = new ArrayList<>();
        ArrayList<int[]> TmpLayer = new ArrayList<>();
        ArrayList<int[]> FinalChoices = new ArrayList<>();
        visit[target[0]][target[1]]=true;
        MainLayer.add(target);
        while(MainLayer.size()!=0){
            for(int[] x:MainLayer){
                for(int i=0;i<4;i++){
                    if(map.getPoint(x[0],x[1], i) != Disconnect) {
                        switch (i) {
                            case Left:
                                int[] b={x[0],x[1] - 1};
                                if(b[1]>=0 && !visit[b[0]][b[1]]) {
                                    TmpLayer.add(b);
                                    if(b[0]==position[0] && b[1]==position[1])FinalChoices.add(x);
                                    visit[b[0]][b[1]] = true;
                                }
                                break;
                            case Up:
                                int[] c={x[0]-1,x[1]};
                                if(c[0]>=0 && !visit[c[0]][c[1]]) {
                                    TmpLayer.add(c);
                                    if(c[0]==position[0] && c[1]==position[1])FinalChoices.add(x);
                                    visit[c[0]][c[1]] = true;
                                }
                                break;
                            case Right:
                                int[] d={x[0],x[1] + 1};
                                if(d[1]<=79 && !visit[d[0]][d[1]]) {
                                    TmpLayer.add(d);
                                    if(d[0]==position[0] && d[1]==position[1])FinalChoices.add(x);
                                    visit[d[0]][d[1]] = true;
                                }
                                break;
                            default:
                                int[] e={x[0]+1,x[1]};
                                if(e[0]<=79 && !visit[e[0]][e[1]]) {
                                    TmpLayer.add(e);
                                    if(e[0]==position[0] && e[1]==position[1])FinalChoices.add(x);
                                    visit[e[0]][e[1]] = true;
                                }
                        }
                    }
                }
            }
            if(contain(TmpLayer,position[0],position[1]))break;
            else{
                MainLayer=new ArrayList<>();
                MainLayer.addAll(TmpLayer);
                TmpLayer=new ArrayList<>();
            }

        }
        int choice = MinFlowBFS(FinalChoices);
        return choice;

    }

    private int MinFlowBFS(ArrayList<int[]> FinalChoices){
        //REQUIRES:FinalChoices中包含正确的广搜结果
        //MODIFIES:none
        //EFFECTS:确定最短路径后选择一条流量最小的边行驶
        int MiniFlow = 9999;
        int choice = -1;
        if(contain(FinalChoices,position[0],position[1]-1) && map.getPoint(position[0],position[1],0)<MiniFlow){
            MiniFlow = map.getPoint(position[0],position[1],0);
            choice = Left;
        }
        if(contain(FinalChoices,position[0]-1,position[1]) && map.getPoint(position[0],position[1],1)<MiniFlow){
            MiniFlow = map.getPoint(position[0],position[1],1);
            choice = Up;
        }
        if(contain(FinalChoices,position[0],position[1]+1) && map.getPoint(position[0],position[1],2)<MiniFlow){
            MiniFlow = map.getPoint(position[0],position[1],2);
            choice = Right;
        }
        if(contain(FinalChoices,position[0]+1,position[1]) && map.getPoint(position[0],position[1],3)<MiniFlow)
            choice = Down;
        return choice;
    }

    private boolean contain(ArrayList<int[]> tmp,int posX,int posY){
        //REQUIRES:none
        //MODIFIES:none
        //EFFECTS:判断ArrayList中是否包含指定坐标并返回布尔值
        for(int[] x:tmp)
            if(x[0]==posX && x[1]==posY)
                return true;
        return false;
    }

    private void WaitStep(){
        //REQUIRES:出租车处于等待服务状态
        //MODIFIES:position，waitingTime，status
        //EFFECTS:在等待服务状态下的随机行驶并增加连续行驶的时间，当连续行驶20s后修改出租车的状态并将连续行驶时间归零
        this.waitingTime += 1;
        RandomStep();
        if (waitingTime == 20) {
            waitingTime = 0;
            status = inRest;
            sleep(1000);
            status = inWait;
        }
    }

    private void StartStep(){
        //REQUIRES:出租车处于即将服务状态
        //MODIFIES:request
        //EFFECTS:为出租车分配请求
        long StartTime = new Date().getTime();
        int direction=BFS(request.getPosition());
        System.out.println("start end bfs");
        int sleepTime = (int)(new Date().getTime()-StartTime);
        if(sleepTime>100)
            sleep(1);
        else
            sleep(100-sleepTime);
        MoveOneStep(direction);
        if (position[0] == request.getPosition()[0] && position[1] == request.getPosition()[1]) {
            status = inServe;
            sleep(1000);
            System.out.println("serve");
        }
    }

    private void ServeStep(){
        //REQUIRES:出租车处于正在服务状态
        //MODIFIES:request
        //EFFECTS:为出租车分配请求
        long StartTime = new Date().getTime();
        int direction=BFS(request.getTarget());
        System.out.println("server end bfs");
        int sleepTime = (int)(new Date().getTime()-StartTime);
        if(sleepTime>100)
            sleep(1);
        else
            sleep(100-sleepTime);
        MoveOneStep(direction);
        System.out.println(sleepTime+"   "+this.num);
        if (position[0] == request.getTarget()[0] && position[1] == request.getTarget()[1]) {
            credit += 3;
            System.out.println(num + "arrive");
            status = inRest;
            sleep(1000);
            status = inWait;
        }
    }

}
