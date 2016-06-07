package MaxClique;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Operation {
	public static Point[] PointArray;
	
	public static boolean correctTest(Set clique){
		//test if the clique is a CLIQUE
		ArrayList<Integer> list = new ArrayList<>();
		list.addAll(clique.getAllElements());
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
	
	public static int totalEdge(Set set){
		int count = 0;

		ArrayList<Integer> list = new ArrayList<>();
		list.addAll(set.getAllElements());
		for(int i=0;i<list.size();i++){
			Point p1 = PointArray[list.get(i)];
			count+=Set.Intersection(p1.Neighbour,set).size();
		}
		count/=2;

		return count;
	}
	
	public static Set getNeighbour(Set Clique){
		//return the common neighbor points of the given point set Clique
		LinkedList<Integer> pointList = new LinkedList<>();
		pointList = Clique.getAllElements();
		
		Set neighbour = new Set();
		neighbour = PointArray[pointList.get(0)].Neighbour.clone();
		
		for(int i=1;i<pointList.size();i++){
			neighbour = Set.Intersection(neighbour,PointArray[pointList.get(i)].Neighbour);
		}
		return neighbour;
    }
	
	public static Set getRandomSubset(Set set,int num){
		//return a randomly selected subset of the given set with size of num
		Set subset = new Set();
		LinkedList<Integer> list = new LinkedList<>();
		list.addAll(set.getAllElements());
		Random random = new Random();
		while(subset.size()<num){
			int i = random.nextInt(list.size());
			if(subset.add(list.get(i))){
				list.remove(i);
			}
		}		
		return subset;
	}

	public static Integer getRandomPoint(Set set){
		LinkedList<Integer> list = new LinkedList<>();
		list.addAll(set.getAllElements());
		Random random = new Random();
		return list.get(random.nextInt(list.size()));
	}
	
	public static void toMaxClique(Set clique){
		Set neighbour = Operation.getNeighbour(clique);
		while(neighbour.size()!=0){
			clique.add(Operation.getRandomPoint(neighbour));
			neighbour = Operation.getNeighbour(clique);
		}
	}
}
