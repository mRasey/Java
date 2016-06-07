package MaxClique;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Trygetmore {
	
	public static Set getMore1(){
		//从不同基础团往外扩
		Random random = new Random();
		
		Set nowClique = new Set();
		int initPoint = random.nextInt(InputHandler.PointsNum);
		Algorithm ag  = new Algorithm(InputHandler.PointArray[initPoint].id);
		ag.run();
		
		for(int i=0;i<25;i++){
			//Union 25 clique
			XJBS xjbs=new XJBS(ag.Clique);
			nowClique = Set.Union(nowClique,xjbs.getans());
		}
		System.out.println("nowClique:"+nowClique.size());
		
		//randomly get a clique from the Union point set
		Set bestClique = new Set();
		int bestNum = 0;
		Set clique = new Set();
		ArrayList<Integer> list = new ArrayList<>(nowClique.size());
		list.addAll(nowClique.getAllElements());
		for(int i=0;i<1000;i++){
			clique = new Set();
			
			//get a random point
			initPoint = list.get(random.nextInt(list.size()));
			clique.add(initPoint);
			
			Set neighbour = (Set)nowClique.clone();
			neighbour = Set.Intersection(neighbour,InputHandler.PointArray[initPoint].Neighbour);
			LinkedList<Integer> neighbourList = new LinkedList<>();
			while(neighbour.size()!=0){
				neighbourList.clear();
				neighbourList.addAll(neighbour.getAllElements());
				clique.add(neighbourList.get(random.nextInt(neighbourList.size())));
				neighbour = Operation.getNeighbour(clique);
				neighbour = Set.Intersection(nowClique,neighbour);
			}
			
			if(clique.size()>bestNum){
				bestClique = (Set) clique.clone();
				bestNum = bestClique.size();
			}
		
		}
		
		Set tempClique;
		XJBS xjbs=new XJBS(clique);
		xjbs.ite = 1000000;

        //Set searchSet = new Set();
        bestClique = xjbs.getans();
        bestNum = bestClique.size();
        //searchSet.addAll(bestClique);
		for(int i=0;i<30;i++){

            //xjbs.antSet = searchSet;
			tempClique = xjbs.getans();
            //searchSet.addAll(tempClique);
			if(tempClique.size()>bestNum){
				bestClique = (Set) tempClique.clone();
				bestNum = bestClique.size();
			}
		}

		return  bestClique;
	}
	
	public static Set getMore2(){
		//用两个团，从一个团删除若干点，然后用其邻居交另一个点
		
		Set nowClique = new Set();
		
		Algorithm ag=new Algorithm(InputHandler.SortedArray[0].id);
		ag.run();
		XJBS xjbs=new XJBS(ag.Clique);
		nowClique = xjbs.getans();
		int bestNum = nowClique.size();
		
		for(int i=0;i<10;i++){
			Set crossset = xjbs.getans().clone();
			for(int j=2;j<4;j++){
				boolean flag = false;
				for(int k=0;k<1000;k++){
					Set subset = Operation.getRandomSubset(nowClique, bestNum-j);	;
					
					Set subnb=Operation.getNeighbour(subset);
					subnb = Set.Intersection(crossset,subnb);
					
					//System.out.println("j: "+j+", subnb size: "+subnb.size());
					if(subnb.size()>=j){
						nowClique = new Set();
						nowClique.addAll(subset);
						nowClique.addAll(subnb);
						Operation.toMaxClique(nowClique);
						if(nowClique.size()>bestNum){
							flag = true;
							bestNum = nowClique.size();
						}
					}
				}
				if(flag){
					break;
				}
			}
		}
		
		return nowClique;	
	}
	
	public static Set getMore3(){
		//一次替换多个点
		
		Algorithm ag=new Algorithm(InputHandler.SortedArray[0].id);
		ag.run();
		XJBS xjbs=new XJBS(ag.Clique);
		Set clique = xjbs.getans();
		xjbs=new XJBS(clique);
		clique = xjbs.getans3();
		return clique;
	}
	
	public static void getMore4(){
		//蚁群尝试1
		Set serchClique = new Set();
		
		Algorithm ag=new Algorithm(InputHandler.SortedArray[0].id);
		ag.run();
		XJBS xjbs=new XJBS(ag.Clique);
		
		//并三个团
		for(int i=0;i<3;i++){
            Set clique = xjbs.getans();
            serchClique.addAll(clique);
			System.out.println(i+" set size: "+clique.size());
		}
		
		//将并的团传入精确算法计算
		System.out.println(serchClique.size());
		MaxClique max = new MaxClique(serchClique);
		System.out.println("serch answer:"+max.maxClique());
		
		return;
	}

	public static Set getMore5(){
		//蚁群2
		Set bestClique = new Set();

		Algorithm ag=new Algorithm(InputHandler.SortedArray[0].id);
		ag.run();
		XJBS xjbs=new XJBS(ag.Clique);

		//first time
		Set serchSet = new Set();
		bestClique = xjbs.getans();
		int bestSize = bestClique.size();
		serchSet = (Set) bestClique.clone();		//已搜到的团的并集

		for(int i=0;i<10;i++){

			ag=new Algorithm(InputHandler.SortedArray[new Random().nextInt(4000)+1].id);
			ag.run();
			xjbs=new XJBS(ag.Clique, serchSet);

			Set clique = xjbs.getans2(20);
			serchSet.addAll(clique);
			//System.out.println(i+" set size: "+serchSet.size());

			if(clique.size()>bestSize){
				bestClique = clique;
				bestSize=clique.size();
			}
		}
		return bestClique;
	}
	
}
