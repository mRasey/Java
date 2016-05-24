package oo10_taxi;
import java.util.Queue;
import java.util.LinkedList; 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
//Overview
//install the message such as map road and light and provide the function to open or close one road;
public class Map {
	private int[][] map;
	private int[][] map_copy;
	private int[][] road_X;
	private int[][] road_Y;
	private int[][] way;
	private int size = 80;
	private int[][] road_X_copy;
	private int[][] road_Y_copy;
	private short[][] flag;
	private short[][] light_NS;
	private short[][] light_EW;
 	public Map(){
		this.map = new int[size][size];
		this.way = new int[size*size][size*size];
		this.road_X = new int[size][size];
		this.road_Y = new int[size][size];
		this.road_X_copy = new int[size][size];
		this.road_Y_copy = new int[size][size];
		this.flag = new short[size][size];
		this.light_EW = new short[size][size];
		this.light_NS = new short[size][size];
		this.map_copy = new int[size][size];
	}
 	//MODIFIED:store the map from file and install the road message in system
 	//EFFECTS:if the file is illegal,print error message and exit system
 	//		  else install the message in map then install road message and copy it
	public void inStallMap(String file){
		String str;
		BufferedReader bre = null;
		File f = new File(file);
		if(!f.exists() || !f.isFile()){
			System.out.println("the file is not existed!");
			System.exit(0);
		}
		System.out.println("The Map is loading... ");
		try{
			bre = new BufferedReader(new FileReader(file));
			int i=0;
			while ((str = bre.readLine())!= null) {
				str = str.replaceAll(" ", "");
				if(str.length()==0)
					continue;
				char[] c=str.toCharArray();
				for(int j=0;j<80;j++){
					if(c[j] > '3'&& c[j]<'0'){
						System.out.println("the map is illegal!");
						System.exit(0);
					}
					if(79 == j && (c[j] == '1' || c[j] == '3')){
						System.out.println("the map is illegal!");
						System.exit(0);
					}
					if(79 == i && (c[j] == '2' || c[j] == '3')){
						System.out.println("the map is illegal!");
						System.exit(0);
					}
					map[i][j]=(int)(c[j]-'0');
				}
				i++;
				if(i==80)
					break;
				}
			if(0 == i){
				System.out.println("the file is empty!");
				System.exit(0);
			}else if(80 != i){
				System.out.println("it looks like that the context of file is wrong!");
				System.exit(0);
			}
			}catch(Throwable e){
				System.out.println("it looks like that the context of file is wrong!");
				System.exit(0);
			}
		inStallWay();
		installRoad();
		copy(this.road_X_copy,this.road_X);
		copy(this.road_Y_copy,this.road_Y);
		copy(this.map_copy,this.map);
	}
	
	//MODIFIED:store the across message from file and install the road message in system
	//EFFECTS:if the file is illegal,print error message and exit system
	//		  else install the message in flag then install it
	public void installLight(String file){
		String str;
		BufferedReader bre = null;
		File f = new File(file);
		if(!f.exists() || !f.isFile()){
			System.out.println("the file is not existed!");
			System.exit(0);
		}
		System.out.println("The across file is loading... ");
		try{
			bre = new BufferedReader(new FileReader(file));
			int i=0;
			while ((str = bre.readLine())!= null) {
				str = str.replaceAll(" ", "");
				if(str.length()==0)
					continue;
				char[] c=str.toCharArray();
				for(int j=0;j<80;j++){
					if(c[j] > '1'&& c[j]<'0'){
						System.out.println("the map is illegal!");
						System.exit(0);
					}
					flag[i][j]=(short)(c[j]-'0');
				}
				i++;
				if(i==80)
					break;
				}
			if(0 == i){
				System.out.println("the file is empty!");
				System.exit(0);
			}else if(80 != i){
				System.out.println("it looks like that the context of file is wrong!");
				System.exit(0);
			}
			}catch(Throwable e){
				System.out.println("it looks like that the context of file is wrong!");
				System.exit(0);
			}
		installLight();
	}
	//EFFECTS:install message of traffic light(0 stands for unused;1 to green;2 to red)
	private void installLight(){
		for(int i = 0;i < size;i++){
			for(int j = 0;j < size;j++){
				if(ifgetLight(i,j)){
					this.light_EW[i][j] = 1;
					this.light_NS[i][j] = 2;
				}
			}
		}
	}
	//REQUIRES:point (x,y) should in the map
	//EFFECTS:judge legality of traffic light in the point(x,y) 
	private boolean ifgetLight(int x,int y){
		int num = 0;
		if(this.flag[x][y] == 0)
			return false;
		if(0 == x && 0 == y)
			return false;
		else if(0 == x){
			if(map[x][y] == 3){
				num+=2;
			}else if(map[x][y] == 1 || map[x][y] == 2){
				num+=1;
			}
			if(map[x][y-1] == 1 || map[x][y-1] == 3){
				num+=1;
			}
		}else if(0 == y){
			if(map[x][y] == 3){
				num+=2;
			}else if(map[x][y] == 1 || map[x][y] == 2){
				num+=1;
			}
			if(map[x-1][y] == 2 || map[x-1][y] == 3){
				num+=1;
			}
		}else{
			if(map[x][y] == 3){
				num+=2;
			}else if(map[x][y] == 1 || map[x][y] == 2){
				num+=1;
			}
			if(map[x-1][y] == 2 || map[x-1][y] == 3){
				num+=1;
			}
			if(map[x][y-1] == 1 || map[x][y-1] == 3){
				num+=1;
			}
		}
		if(num > 2)
			return true;
		else 
			return false;
	}
	//REQUIRES:the size of a and b must be size*size
	//MODIFIED:the traffic of b was copy to a
	//EFFECTS:a was update

	private void copy(int[][] a,int[][] b){
		for(int i = 0;i < size;i++){
			for(int j = 0;j < size;j++){
				a[i][j] = b[i][j];
			}
		}
	}
	//REQUIRES:the map must be installed before
	//MODIFIED:store the road message in road_X and road_Y
	public void installRoad(){
		for(int i = 0;i < 80;i++){
			for(int j = 0;j < 80;j++){
				if(map[i][j] == 3){
					road_X[i][j] = 1;
					road_Y[i][j] = 1;
				}else if(map[i][j] == 2){
					road_Y[i][j] = 0;
					road_X[i][j] = 1;
				}else if(map[i][j] == 1){
					road_Y[i][j] = 1;
					road_X[i][j] = 0;
				}else{
					road_X[i][j] = 0;
					road_Y[i][j] = 0;
				}
			}
		}
	}
	//REQUIRES:system has got one map and two road message array
	//MODIFIED:open one road in the map
	//EFFECTS:if the road is out of the map or it is not existed in the old map,print error message and return
    //		  else update the way and road array 
	public void openRoad(int x,int y,int x1,int y1){
		int p = x*80+y;
		int q = x1*80+y1;
		int l = (p>q)?q:p;
		if(p>6400||p<0||q>6400||q<0){
			System.out.println("this position has been out of the map!");
			return;
		}else if(Math.abs((p-q)) != 80 && Math.abs((p-q)) != 1 ){
			System.out.println("this function only could open one road by one times");
			return;
		}else if(Math.abs((p-q)) == 80){
			if(this.road_X_copy[l/80][l%80] == 0){
				System.out.println("this map has not this road,we cannot open this road");
				return;
			}
			if(map[l/80][l%80] == 0){
				map[l/80][l%80] = 2;
			}else if(map[l/80][l%80] == 1){
				map[l/80][l%80] = 3;
			}else{
				System.out.println("the road has already existed!");
				return;
			}
			road_X[l/80][l%80] = 1;
		}else{
			if(this.road_Y_copy[l/80][l%80] == 0){
				System.out.println("this map has not this road,we cannot open this road");
				return;
			}
			if(map[l/80][l%80] == 0){
				map[l/80][l%80] = 1;
			}else if(map[l/80][l%80] == 2){
				map[l/80][l%80] = 3;
			}else{
				System.out.println("the road has already existed!");
				return;
			}
			road_Y[l/80][l%80] = 1;
		}
		synchronized(this.map){
			if(this.light_EW[x][y]==0 &&ifgetLight(x,y)){
				this.light_EW[x][y] = 1;
				this.light_NS[x][y] = 2;
			}
			if(this.light_EW[x1][y1]==0 &&ifgetLight(x1,y1)){
				this.light_EW[x1][y1] = 1;
				this.light_NS[x1][y1] = 2;
			}
		}
	}
	//REQUIRES:system has got one map and two road message array
	//MODIFIED:close one road in the map
	//EFFECTS:if the road is out of the map or it is not existed in the old map,print error message and return
	//		  else update the way and road array 
	public void closeRoad(int x,int y,int x1,int y1){
		int p = x*80+y;
		int q = x1*80+y1;
		int l = (p>q)?q:p;
		if(p>6400||p<0||q>6400||q<0){
			System.out.println("this position has been out of the map!");
			return;
		}else if(Math.abs((p-q)) != 80 && Math.abs((p-q)) != 1 ){
			System.out.println("this function only could open one road by one times");
			return;
		}else if(Math.abs((p-q)) == 80){
			if(this.road_X_copy[l/80][l%80] == 0){
				System.out.println("this map has not this road,we cannot close this road");
				return;
			}
			if(map[l/80][l%80] == 2){
				map[l/80][l%80] = 0;
			}else if(map[l/80][l%80] == 3){
				map[l/80][l%80] = 1;
			}else{
				System.out.println("the road has already been closed!");
				return;
			}
			road_X[l/80][l%80] = 0;
		}else{
			if(this.road_Y_copy[l/80][l%80] == 0){
				System.out.println("this map has not this road,we cannot close this road");
				return;
			}
			if(map[l/80][l%80] == 1){
				map[l/80][l%80] = 0;
			}else if(map[l/80][l%80] == 3){
				map[l/80][l%80] = 2;
			}else{
				System.out.println("the road has already been closed!");
				return;
			}
			road_Y[l/80][l%80] = 0;
		}
		synchronized(this.map){
			if(this.light_EW[x][y]!=0 &&!ifgetLight(x,y)){
				this.light_EW[x][y] = 0;
				this.light_NS[x][y] = 0;
			}
			if(this.light_EW[x1][y1]!=0 &&!ifgetLight(x1,y1)){
				this.light_EW[x1][y1] = 0;
				this.light_NS[x1][y1] = 0;
			}
		}
	}
	//MODIFIED:install shortest message by bfs function
	//EFFECTS:if the map was not fully connected,print error message and exit system
	//		  else store shortest message
	public void inStallWay(){
		for(int i = 0;i<6400;i++){
			try{
				bfs((int)i);
			}catch(Exception e){
				System.out.println("Please make sure the map is fully connected");
				System.exit(0);
			}
		}
		System.out.println("load completed");
	}
	//REQUIRES:the point p should be in the map and the map should be fully connected
	//MODIFIED:find the shortest length way and first step of the p to all points of map
	//EFFECTS: if the map is not fully connected,will crash(will be tried catch in Upper layer function)
	//		   else store the shortest length way and first step in array
	private void bfs(int p){
		int size = 0;
		Queue <Integer> Q = new LinkedList<Integer>();
		Queue <Integer> len = new LinkedList<Integer>();
		int l;
		int xa,ya;
		int[][] mark = new int[80][80];
		Q.offer(p);len.offer(0);
		size++;
		int x = p/80;
		int y = p%80;
		mark[x][y] = 1;
		int xx,yy,ll;
		xx = Q.peek()/80;
		yy = Q.peek()%80;
		ll = len.peek();
		Q.poll();len.poll();
		//向上方向
		if(!(xx==0 || map[xx-1][yy] == 1 || map[xx-1][yy] == 0)){
			xa = xx -1;ya = yy;l = (int)ll;
			if(mark[xa][ya] == 0){
				Q.offer((int)(xa*80+ya));
				l+=1;
				len.offer(l);
				mark[xa][ya] = 2;
				way[p][xa*80+ya] = l*10+mark[xa][ya];
				size++;
			}
		}
		//向右方向
		if(!(yy == 79 || map[xx][yy] == 0 || map[xx][yy] == 2)){
			xa = xx;ya = yy+1;l = (int)ll;
			if(mark[xa][ya] == 0){
				Q.offer((int)(xa*80+ya));
				l+=1;
				len.offer(l);
				mark[xa][ya] = 3;
				way[p][xa*80+ya] = l*10+mark[xa][ya];
				size++;
			}
		}
		//向下方向
		if(!(xx == 79 || map[xx][yy] == 0 || map[xx][yy] == 1)){
			xa = xx+1;ya = yy;l = (int)ll;
			if(mark[xa][ya] == 0){
				Q.offer((int)(xa*80+ya));
				l+=1;
				len.offer(l);
				mark[xa][ya] = 4;
				way[p][xa*80+ya] = l*10+mark[xa][ya];
				size++;
			}
		}
		//向左方向
		if(!(yy == 0 || map[xx][yy-1] == 2 || map[xx][yy-1] == 0)){
			xa = xx;ya=yy-1;l = (int)ll;
			if(mark[xa][ya] == 0){
				Q.offer((int)(xa*80+ya));
				l+=1;
				len.offer(l);
				mark[xa][ya] = 5;
				way[p][xa*80+ya] = l*10+mark[xa][ya];
				size++;
			}
		}
		while(size != 6400){
//			int xx,yy,ll;
			xx = Q.peek()/80;
			yy = Q.peek()%80;
			ll = len.peek();
			Q.poll();len.poll();
			//向上方向
			if(!(xx==0 || map[xx-1][yy] == 1 || map[xx-1][yy] == 0)){
				xa = xx -1;ya = yy;l = (int)ll;
				if(mark[xa][ya] == 0){
					Q.offer((int)(xa*80+ya));
					l+=1;
					len.offer(l);
					way[p][xa*80+ya] = l*10+mark[xx][yy];
					mark[xa][ya] = mark[xx][yy];
					size++;
				}
			}
			//向右方向
			if(!(yy == 79 || map[xx][yy] == 0 || map[xx][yy] == 2)){
				xa = xx;ya = yy+1;l = (int)ll;
				if(mark[xa][ya] == 0){
					Q.offer((int)(xa*80+ya));
					l+=1;
					len.offer(l);
					way[p][xa*80+ya] = l*10+mark[xx][yy];
					mark[xa][ya] = mark[xx][yy];
					size++;
				}
			}
			//向下方向
			if(!(xx == 79 || map[xx][yy] == 0 || map[xx][yy] == 1)){
				xa = xx+1;ya = yy;l = (int)ll;
				if(mark[xa][ya] == 0){
					Q.offer((int)(xa*80+ya));
					l+=1;
					len.offer(l);
					way[p][xa*80+ya] = l*10+mark[xx][yy];
					mark[xa][ya] = mark[xx][yy];
					size++;
				}
			}
			//向左方向
			if(!(yy == 0 || map[xx][yy-1] == 2 || map[xx][yy-1] == 0)){
				xa = xx;ya=yy-1;l = (int)ll;
				if(mark[xa][ya] == 0){
					Q.offer((int)(xa*80+ya));
					l+=1;
					len.offer(l);
					mark[xa][ya] = mark[xx][yy];
					way[p][xa*80+ya] = l*10+mark[xa][ya];
					size++;
				}
			}
		}
		
	}
	//REQUIRES:the point p and q should be in the map and there exists one way connected point p and point q
	//MODIFIED:find the shortest length way and first step of the p to q
	//EFFECTS: there was not existed one way connected point p and point q,will crash(will be tried catch in Upper layer function)
	//		   else return the shortest length way and first step in array
	public int bfs(int p,int q,int[][] map){
		Queue <Integer> Q = new LinkedList<Integer>();
		Queue <Integer> len = new LinkedList<Integer>();
		int l;
		int xa,ya;
		int[][] mark = new int[80][80];
		Q.offer(p);len.offer(0);
		int x = p/80;
		int y = p%80;
		mark[x][y] = 1;
		int xx,yy,ll=0;
		while(true){
			xx = Q.peek()/80;
			yy = Q.peek()%80;
			ll = len.peek();
			Q.poll();len.poll();
			if(xx == q/80 && yy == q%80){
				return ll;
			}
			//向上方向
			if(!(xx==0 || map[xx-1][yy] == 1 || map[xx-1][yy] == 0)){
				xa = xx -1;ya = yy;l = (int)ll;
				if(mark[xa][ya] == 0){
					Q.offer((int)(xa*80+ya));
					l+=1;
					len.offer(l);
					mark[xa][ya] =1;
				}
			}
			//向右方向
			if(!(yy == 79 || map[xx][yy] == 0 || map[xx][yy] == 2)){
				xa = xx;ya = yy+1;l = (int)ll;
				if(mark[xa][ya] == 0){
					Q.offer((int)(xa*80+ya));
					l+=1;
					len.offer(l);
					mark[xa][ya] =1;
				}
			}
			//向下方向
			if(!(xx == 79 || map[xx][yy] == 0 || map[xx][yy] == 1)){
				xa = xx+1;ya = yy;l = (int)ll;
				if(mark[xa][ya] == 0){
					Q.offer((int)(xa*80+ya));
					l+=1;
					len.offer(l);
					mark[xa][ya] = 1;
				}
			}
			//向左方向
			if(!(yy == 0 || map[xx][yy-1] == 2 || map[xx][yy-1] == 0)){
				xa = xx;ya=yy-1;l = (int)ll;
				if(mark[xa][ya] == 0){
					Q.offer((int)(xa*80+ya));
					l+=1;
					len.offer(l);
					mark[xa][ya] = 1;
				}
			}
		}
	}
	
	public short[][] getLight_EW(){
		return this.light_EW;
	}
	public short[][] getLight_NS(){
		return this.light_NS;
	}
	public int[][] getMap(){
		return this.map;
	}
	public int[][] getMapC(){
		return this.map_copy;
	}
	public int[][] getWay(){
		return this.way;
	}
	public int[][] getXroad(){
		return this.road_X;
	}
	public int[][] getXroadC(){
		return this.road_X_copy;
	}
	public int[][] getYroad(){
		return this.road_Y;
	}
	public int[][] getYroadC(){
		return this.road_Y_copy;
	}
	//EFFECTS:judge the legality of the map class
	public boolean repOK(){
		if(this.size != 80)
			return false;
		for(int i = 0;i < size;i++){
			for(int j = 0;j < size;j++){
				if(map[i][j] < 0 && map[i][j] > 3)
					return false;
				if(this.road_X[i][j]%10 != 0 && this.road_X[i][j] != 1)
					return false;
				if(this.road_Y[i][j]%10 != 0 && this.road_Y[i][j] != 1)
					return false;
				if((this.light_EW[i][j] > 2 || this.light_EW[i][j] < 0 )||(this.light_NS[i][j] > 2 || this.light_NS[i][j] < 0)){
					return false;
				}
			}
		}
		return true;
	}
}
