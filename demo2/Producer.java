package demo2;
/*生产者线程,模拟银行工作人员服务完成一位客户后，服务资源释放，开始准备服务下一位客户*/

public class Producer implements Runnable {  
    private Center center;  
  
    public Producer(Center center) {  
        this.center = center;  
    }  
  
    @Override  
    public void run() {  
        while (!Thread.interrupted()) {
            try {
                center.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }  
    }  
}  


