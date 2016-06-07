package Elevator;

public class Floor {

    public static void main(String[] args) {
        try {
            new Thread(new ALS_Schedule()).start();
        }catch (Throwable t){
            System.exit(0);
        }
    }

}
