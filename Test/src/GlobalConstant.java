
class GlobalConstant {

    static final int inWait = 0;
    static final int inStart = 1;
    static final int inServe = 2;
    static final int inRest = 3;

    static final int Disconnect = -1;
    static final int Initconnect = 0;

    static final int Left = 0;
    static final int Up = 1;
    static final int Right = 2;
    static final int Down = 3;

    static final int Unmarked = 0;
    static final int Marked = 1;

    void sleep(int time){
        //REQUIRES:none
        //MODIFIES:none
        //EFFECTS:线程睡眠指定时间
        try {
            Thread.sleep(time);
        } catch (Exception exception) {
            System.out.println("");
        }
    }
}
