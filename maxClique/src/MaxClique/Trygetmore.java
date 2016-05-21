package MaxClique;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

class Trygetmore {
	
	public static HashSet<Integer> getMore(){
		
		HashSet<Integer> nowClique = new HashSet<>();
			
		Algorithm ag=new Algorithm(InputHandler.SortedArray[0].id);
		ag.run();		
		for(int i=0;i<25;i++){		
			WZJ_Tabu wzj_tabu=new WZJ_Tabu(ag.Clique);
			nowClique.addAll(wzj_tabu.getans());
		}
		System.out.println("nowClique:"+nowClique.size());
		
		HashSet<Integer> bestClique = new HashSet<>();
		int bestNum = 0;
		HashSet<Integer> clique = new HashSet<>();
		ArrayList<Integer> list = new ArrayList<>(nowClique.size());
		list.addAll(nowClique);
		for(int i=0;i<1000;i++){
			clique.clear();
			
			//get a random point
			
			Random random = new Random();
			int initPoint = list.get(random.nextInt(list.size()));
			clique.add(initPoint);
			
			HashSet<Integer> neighbour = (HashSet<Integer>)nowClique.clone();
			neighbour.retainAll(InputHandler.PointArray[initPoint].Neighbour);
			LinkedList<Integer> neighbourList = new LinkedList<>();
			while(neighbour.size()!=0){
				neighbourList.clear();
				neighbourList.addAll(neighbour);
				clique.add(neighbourList.get(random.nextInt(neighbourList.size())));
				neighbour = Operation.getNeighbour(clique);
				neighbour.retainAll(nowClique);
			}
			
			if(clique.size()>bestNum){
				bestClique = (HashSet<Integer>) clique.clone();
				bestNum = bestClique.size();
			}
		}
		
		HashSet<Integer> tempClique = new HashSet<>(); 
		WZJ_Tabu wzj_tabu=new WZJ_Tabu(clique);
		wzj_tabu.n_diedai = 1000000;
		for(int i=0;i<30;i++){
			tempClique = wzj_tabu.getans();
			if(tempClique.size()>bestNum){
				bestClique = (HashSet<Integer>) tempClique.clone();
				bestNum = bestClique.size();
			}
		}
		
		System.out.println(Operation.correctTest(bestClique));
		return  bestClique;
		 
	}
	
	public static HashSet<Integer> getMore2(){
		HashSet<Integer> nowClique = new HashSet<>();
		
		Algorithm ag=new Algorithm(InputHandler.SortedArray[0].id);
		ag.run();
		WZJ_Tabu wzj_tabu=new WZJ_Tabu(ag.Clique);
		nowClique.addAll(wzj_tabu.getans());
		int bestNum = nowClique.size();
		
		for(int i=0;i<10;i++){
			HashSet<Integer> crossset = wzj_tabu.getans();
			for(int j=2;j<4;j++){
				boolean flag = false;
				for(int k=0;k<1000;k++){
					HashSet<Integer> subset = Operation.getRandomSubset(nowClique, bestNum-j);	
					
					HashSet<Integer>subnb=Operation.getNeighbour(subset);
					subnb.retainAll(crossset);
					
					//System.out.println("j: "+j+", subnb size: "+subnb.size());
					if(subnb.size()>=j){
						nowClique.clear();
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
		
		System.out.println(Operation.correctTest(nowClique));
		return nowClique;
		
	}
	
	public static HashSet<Integer> getMore3(){
		HashSet<Integer> bestClique = new HashSet<>();
		
		Algorithm ag=new Algorithm(InputHandler.SortedArray[0].id);
		ag.run();
		WZJ_Tabu wzj_tabu=new WZJ_Tabu(ag.Clique);
		
		HashSet<Integer> serchSet = new HashSet<>();
		
		for(int i=0;i<2;i++){
			bestClique.addAll(wzj_tabu.getans());
			System.out.println(i+" set size: "+bestClique.size());
		}
		
		MaxClique max = new MaxClique(bestClique);
		System.out.println("serch answer:"+max.maxClique());
		
		return bestClique;
	}
	
}
