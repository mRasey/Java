package demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * 模拟银行叫号系统
 */
public class CallNum {
	public static void main(String[] args) throws InterruptedException {  
		//创建服务中心,如一个银行的营业厅
		Center center = new Center(10);
        ExecutorService exec = Executors.newCachedThreadPool();  
        //模拟产生服务人员
        Producer producer = new Producer(center);  
        //模拟产生客户
        Consumer consumer = new Consumer(center);  
        exec.execute(producer);  
        //模拟10名客户
        for (int i = 0; i < 10; i++) {  
            exec.execute(consumer);  
        }
        exec.shutdown();
    }
}
