package MaxClique;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Operation {
	public static Point[] PointArray;
	
	public static boolean correctTest(HashSet<Integer> clique){
		//test if the clique is a CLIQUE
		ArrayList<Integer> list = new ArrayList<>();
		list.addAll(clique);
		for(int i=0;i<list.size();i++){
			for(int j=i+1;j<list.size();j++){
				Point p1 = PointArray[list.get(i)];
				Point p2 = PointArray[list.get(j)];
				if(!(p1.Neighbour.contains(p2.id))){
					return false;
				}
			}
		}
		return true;
	}
	
	public static HashSet<Integer> getNeighbour(HashSet<Integer> Clique){
		//return the common neighbor points of the given point set Clique
		LinkedList<Integer> pointList = new LinkedList<>();
		pointList.addAll(Clique);
		
		HashSet<Integer> neighbour = new HashSet<>();
		neighbour.addAll(PointArray[pointList.get(0)].Neighbour);
		
		for(int i=1;i<pointList.size();i++){
			neighbour.retainAll(PointArray[pointList.get(i)].Neighbour);
		}
		return neighbour;
    }
	
	public static HashSet<Integer> getRandomSubset(HashSet<Integer> set,int num){
		//return a randomly selected subset of the given set with size of num
		HashSet<Integer> subset = new HashSet<>();
		LinkedList<Integer> list = new LinkedList<>();
		list.addAll(set);
		Random random = new Random();
		while(subset.size()<num){
			int i =random.nextInt(list.size());
			if(subset.add(list.get(i))){
				list.remove(i);
			}
		}		
		return subset;
	}

	public static Integer getRandomPoint(HashSet<Integer> set){
		LinkedList<Integer> list = new LinkedList<>();
		list.addAll(set);
		Random random = new Random();
		return list.get(random.nextInt(list.size()));
	}
	
	public static void toMaxClique(HashSet<Integer> clique){
		HashSet<Integer> neighbour = Operation.getNeighbour(clique);
		while(neighbour.size()!=0){
			clique.addAll(Operation.getRandomSubset(neighbour,1));
			neighbour = Operation.getNeighbour(clique);
		}
	}
}
