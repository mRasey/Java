package JunitDemo;

class pos {
    int x;
    int y;

    pos(int val_x, int val_y) {
        this.x = val_x;
        this.y = val_y;
    }

    pos(pos next) {
        this.x = next.x;
        this.y = next.y;
    }
}
