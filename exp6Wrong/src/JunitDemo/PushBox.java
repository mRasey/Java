package JunitDemo;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class PushBox {
    private int[][] map = new int[20][20];
    private boolean[][][][] vis = new boolean[20][20][20][20];
    private final int[] dx = new int[]{0, 0, 1, -1};
    static final int[] dy = new int[]{1, -1, 0, 0};
    int m;
    int n;
    Queue<node> q = new LinkedList();
    node start = new node(0, 0, 0, 0, 0, (node)null);

    public PushBox() {
    }

    public void pre(Queue<node> q) {
        if(!q.isEmpty()) {
            q.clear();
        }

        for(int i = 0; i < 20; ++i) {
            for(int j = 0; j < 20; ++j) {
                for(int k = 0; k < 20; ++k) {
                    for(int l = 0; l < 20; ++l) {
                        this.vis[i][j][k][l] = false;
                    }
                }
            }
        }

    }

    public boolean check(int x, int y) {
        return x > 0 && x < this.m && y > 0 && y < this.n && this.map[x][y] != 1;
    }

    public boolean bfs_man(int sx, int sy, int ex, int ey, int bx, int by) {
        LinkedList qq = new LinkedList();
        boolean[][] vis2 = new boolean[20][20];
        vis2[sx][sy] = vis2[bx][by] = true;
        qq.add(new pos(sx, sy));

        while(!qq.isEmpty()) {
            pos u = (pos)qq.poll();
            if(u.x == ex && u.y == ey) {
                return true;
            }

            pos next = new pos(0, 0);

            for(int i = 0; i < 4; ++i) {
                next.x = u.x + this.dx[i];
                next.y = u.y + dy[i];
                if(this.check(next.x, next.y) && !vis2[next.x][next.y]) {
                    vis2[next.x][next.y] = true;
                    qq.add(new pos(next));
                }
            }
        }

        return false;
    }

    public void bfs() {
        this.pre(this.q);
        this.q.add(this.start);
        this.vis[this.start.bx][this.start.by][this.start.mx][this.start.my] = true;

        while(!this.q.isEmpty()) {
            node u = (node)this.q.poll();
            node next;
            if(this.map[u.bx][u.by] == 3) {
                System.out.println("the total steps is:");
                System.out.println(u.step);
                next = new node(u);

                Stack var4;
                for(var4 = new Stack(); next != null; next = next.prev) {
                    var4.push(next);
                }

                System.out.println("the trace of box is:");

                while(!var4.isEmpty()) {
                    next = (node)var4.pop();
                    System.out.println("(" + next.bx + "," + next.by + ")");
                }

                System.out.println();
                return;
            }

            next = new node(0, 0, 0, 0, 0, (node)null);

            for(int i = 0; i < 4; ++i) {
                next.bx = u.bx + this.dx[i];
                next.by = u.by + dy[i];
                next.mx = u.bx;
                next.my = u.by;
                next.step = u.step + 1;
                next.prev = u;
                if(this.check(next.bx, next.by) && !this.vis[next.bx][next.by][next.mx][next.my] && this.bfs_man(u.mx, u.my, u.bx - this.dx[i], u.by - dy[i], u.bx, u.by)) {
                    this.vis[next.bx][next.by][next.mx][next.my] = true;
                    this.q.add(next);
                }
            }
        }

        System.out.println("No solutions!");
    }

    public boolean readmap() {
        Scanner input = new Scanner(System.in);
        this.m = input.nextInt();
        this.n = input.nextInt();

        for(int i = 0; i < this.m; ++i) {
            for(int j = 0; j < this.n; ++j) {
                this.map[i][j] = input.nextInt();
                if(this.map[i][j] == 2) {
                    this.map[i][j] = 0;
                    this.start.bx = i;
                    this.start.by = j;
                } else if(this.map[i][j] == 4) {
                    this.map[i][j] = 0;
                    this.start.mx = i;
                    this.start.my = j;
                }
            }
        }

        return true;
    }
}
