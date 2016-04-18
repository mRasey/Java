package demo2;

public class Customer {
	//模拟取号机，号码每次自动递增，得到客户编号，编号若小于10则前面加0，如01,02
	private final int id = counter++;  
    private static int counter = 1;  
    public String toString() {  
        if (id > 9) {  
            return "Customer [id=" + id + "]";  
        }  
        return "Customer [id=0" + id + "]";  
    }  
}
