package MaxClique;

import java.util.Random;

public class XJBS {

    public int ite=500000;
    private Set initialCluque;
    private Set antSet;

    public XJBS(Set initialCluque00){//传入作为初始解
        initialCluque=initialCluque00;
        antSet = new Set();
    }

    public XJBS(Set initialCluque00, Set antSet){//传入作为初始解
        initialCluque=initialCluque00;
        this.antSet =  antSet.clone();
    }

    public Set getans(){
        Set nowClique=initialCluque.clone();
        Random random=new Random();
        for(int t=1;t<=ite;t++){
            int a=0;
            do {
                a = random.nextInt(InputHandler.PointsNum) + 1;
            }
            while (nowClique.contains(a));

            //加入a
            Set maybenextClique =  nowClique.clone();
            maybenextClique = Set.Intersection(maybenextClique,InputHandler.PointArray[a].Neighbour);
            maybenextClique.add(a);

            int maybenextsize = maybenextClique.size();
            int nowsize = nowClique.size();
            if(maybenextsize>=nowsize) {
                nowClique =  maybenextClique.clone();
            }
        }
        System.out.println(nowClique.size());
        
        return  nowClique;
    }
    
    public Set getans2(int probability){
        Set nowClique=initialCluque.clone();
        Random random=new Random();
        for(int t=1;t<=ite;t++){
            int a=0;
            do {
                if(random.nextInt(100) < probability) {
                    a = Operation.getRandomPoint(antSet);
                }
                else{
                    a = random.nextInt(InputHandler.PointsNum) + 1;
                }
            }
            while (nowClique.contains(a));

            //加入a
            Set maybenextClique =  nowClique.clone();
            maybenextClique = Set.Intersection(maybenextClique,InputHandler.PointArray[a].Neighbour);
            maybenextClique.add(a);

            int maybenextsize = maybenextClique.size();
            int nowsize = nowClique.size();
            if(maybenextsize>=nowsize) {
                nowClique =  maybenextClique.clone();
            }
        }
        System.out.println(nowClique.size());
        
        return  nowClique;
    }
    
    public Set getans3(){
    	
    		Set clique = initialCluque.clone();
    		Set nowClique = initialCluque.clone();
    		Random random = new Random();
        int k=3;		//一次更换k个点
        int[] a = new int[k];
        for(int i=0;i<1000;i++){
        	
            for(int j=0;j<k;j++){
            		do{
                		a[j] = random.nextInt(InputHandler.PointsNum) + 1;
            		}while(!clique.contains(a[j]));
            }
        		
        		for(int j=0;j<k;j++){
            		clique.remove(a[j]);
            }
        		Set neighbour = Operation.getNeighbour(clique);
        		
        		System.out.println("neighbour size:"+neighbour.size());
        		
        		MaxClique m = new MaxClique(neighbour);
        		int size = m.maxClique();
        		if(size<(nowClique.size()-clique.size())){
        			System.out.println("wrong!!!!");
        		}
        		if(size>nowClique.size()-clique.size()){
        			System.out.println(clique.size()+"!!!"+size);
        			if(size==neighbour.size()){
        				nowClique.addAll(neighbour);
        				System.out.println(clique.size());
        				clique = nowClique.clone();
        				continue;
        			}
        		}
        		for(int j=0;j<k;j++){
            		clique.add(a[j]);
            }
        }
        
        return nowClique;
    }
}
