package JunitDemo;

class node {
    int mx;
    int my;
    int bx;
    int by;
    int step;
    node prev;

    node(node next) {
        this.mx = next.mx;
        this.my = next.my;
        this.bx = next.bx;
        this.by = next.by;
        this.step = next.step;
        this.prev = next.prev;
    }

    node(int val_mx, int val_my, int val_bx, int val_by, int val_step, node val_prev) {
        this.mx = val_mx;
        this.my = val_my;
        this.bx = val_bx;
        this.by = val_by;
        this.step = val_step;
        this.prev = val_prev;
    }
}
