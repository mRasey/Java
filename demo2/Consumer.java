package demo2;
/**消费者线程，模拟客户完成业务需求后,服务人员空闲就绪，下一位客户可请求服务
 */
public class Consumer implements Runnable {  
    private Center center;  
  
    public Consumer(Center center) {  
        this.center = center;  
    }  
  
    @Override  
    public void run() {  
        while (!Thread.interrupted()) {

            try {
                center.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }  
    }  
  
}  

