package demo2;
//模拟柜台，柜台编号若小于10则前面加0，如01,02
public class Waiter {
	private final int id = counter++;  
    private static int counter = 1;  
    public String toString() {  
        if (id > 9)  
            return "Waiter [id=" + id + "]";  
        return "Waiter [id=0" + id + "]";  
     }  
}
