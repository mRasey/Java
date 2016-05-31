package max;

import java.util.Currency;

class MaxClique implements Runnable{
    int size;
    int[][] map;
    int[][] alt;
    int answer;
    int[] max;
    boolean flag;

    public MaxClique(int size, int[][] map) {
        this.size = size;
        alt = new int[size][size];
        this.map = map;
        max = new int[size];
        answer = 0;
    }

    public boolean dfs(int current, int total){
        if(current == 0){
            if(total > answer){
                answer = total;
                return true;
            }
            return false;
        }
        for(int i = 0; i < current; i++){
            if(current - i + total <= answer)
                return false;
            int u = alt[total][i];
            if(max[u] + total <= answer)
                return false;
            int nxt = 0;
            for(int j = i + 1; j < current; j++){
                if(map[u][alt[total][j]] == 1){
                    alt[total + 1][nxt++] = alt[total][j];
                }
            }
            if(dfs(nxt, total + 1))
                return true;
        }
        return false;
    }

    public int maxClique(){
        for(int i = size - 1; i >= 0; i--){
            int current = 0;
            for(int j = i + 1; j < size; j++)
                if(map[i][j] == 1)
                    alt[1][current++] = j;
            dfs(current, 1);
            max[i] = answer;
        }
        flag = true;
        return answer;
    }

    @Override
    public void run(){
        maxClique();
    }

}
