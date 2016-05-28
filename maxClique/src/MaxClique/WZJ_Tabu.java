package MaxClique;

import java.util.HashSet;
import java.util.Random;

public class WZJ_Tabu {

    int n_diedai=500000;
    HashSet<Integer> Clique0;
    HashSet<Integer> antSet;

    class Tabu_Table{
        int sizemax=0;
        int size=0;
        int data[];
        Tabu_Table(int sizeeeeeemax){
            sizemax=sizeeeeeemax;
            data=new int[sizemax+1];
        }
        boolean isin(int x){
            for(int i=1;i<=size;i++)
            {
                if(data[i]==x) return  true;
            }
            return  false;
        }
        void deletehead(){
            size--;
            for(int i=1;i<=size;i++){
                data[i]=data[i+1];
            }
        }
        void add(int x){
            if(size==sizemax) deletehead();
            size++;
            data[size]=x;
        }
    }

    public WZJ_Tabu(HashSet<Integer> Clique000){//传入作为初始解
        Clique0=Clique000;
        antSet = new HashSet<>();
    }

    public WZJ_Tabu(HashSet<Integer> Clique000,
                    HashSet<Integer> antSet){//传入作为初始解
        Clique0=Clique000;
        this.antSet = (HashSet<Integer>) antSet.clone();
    }

    HashSet<Integer> getans(int gaiLv){
        HashSet<Integer> nowClique=(HashSet<Integer>)Clique0.clone();
        HashSet<Integer> bestClique=(HashSet<Integer>)Clique0.clone();
        //Tabu_Table tb_remove=new Tabu_Table(4);
        //Tabu_Table tb_add=new Tabu_Table(4);
        Random random=new Random();
        for(int t=1;t<=n_diedai;t++){
            int a;
            do {
                if(random.nextInt(100) < gaiLv) {
                    a = Operation.getRandomPoint(antSet) + 1;
                }
                else{
                    a = random.nextInt(InputHandler.PointsNum) + 1;
                }
            }
            while (nowClique.contains(a));
            //加入a
            HashSet<Integer> jyClique = (HashSet<Integer>) nowClique.clone();

            HashSet<Integer> maybenextClique = (HashSet<Integer>) nowClique.clone();
            maybenextClique.retainAll(InputHandler.PointArray[a].Neighbour);
            maybenextClique.add(a);

            int maybenextsize = maybenextClique.size();
            int nowsize = nowClique.size();
            //int bestsize=bestClique.size();
           // boolean bintbadd=false,bintbremove=false;
            //bintbadd=tb_add.isin(a);

            //bintbremove=tb_remove.isin(a);

            //System.out.println(t+" "+nowsize);//输出团结点

            jyClique.removeAll(maybenextClique);//此乃剩下部分
            //for(Integer i:jyClique) {
            //    if(tb_remove.isin(i)){ bintbremove=true;break;}
            //    if(tb_add.isin(i)){ bintbadd=true;break;}
            //}

            /*if(maybenextsize>bestsize){
                if(bintbadd||bintbremove){//特赦，允许加入nowClique

                }
                else//否则加入禁忌表,也允许加入nowClique
                {
                    tb_add.add(a);
                    for(Integer i:jyClique){
                        tb_remove.add(i);
                    }
                }
                nowClique=(HashSet<Integer>)maybenextClique.clone();
                bestClique=(HashSet<Integer>)maybenextClique.clone();
            }
            else {
                if(bintbadd||bintbremove){
                    //在表中，不行
                }
                else {
                    if(maybenextsize>=nowsize) {
                        tb_add.add(a);
                        for(Integer i:jyClique){
                            tb_remove.add(i);
                        }
                        nowClique = (HashSet<Integer>) maybenextClique.clone();
                    }
                }
            }*/
            if(maybenextsize>=nowsize) {

                nowClique = (HashSet<Integer>) maybenextClique.clone();
                bestClique=nowClique;
            }
        }
        System.out.println(bestClique.size());
        return  bestClique;
    }
}
