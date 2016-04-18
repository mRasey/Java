package demo2;

import java.util.Random;
import java.util.concurrent.*;

/**
 服务中心
 
 */
public class Center extends Thread {  
    private BlockingQueue<Waiter> waiters;
    private BlockingQueue<Customer> customers;
    private Random rand = new Random();
    private final static int PRODUCERSLEEPSEED = 3000;  
    private final static int CONSUMERSLEEPSEED = 100000;

    //创建提供服务的柜台队列和取得号的客户队列
    public Center(int num) {
        waiters = new LinkedBlockingDeque<>(num);/*构造柜台队列长度*/
        for(int i = 0; i < num; i++){
            waiters.add(new Waiter());
        }
        customers = new LinkedBlockingQueue<>();
    }  
    //取号机产生新号码
    public void produce() throws InterruptedException {
        Customer customer = new Customer();
        customers.add(customer);
        System.out.println(customer.toString() + "号顾客正在等待服务");
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(PRODUCERSLEEPSEED));
    }
    //客户获得服务
    public void consume() throws InterruptedException {
        Customer customer = customers.take();/*获取并移除顾客队列的队头元素*/
        Waiter waiter = waiters.take();
        System.out.println(waiter.toString() + "号窗口正在为" + customer.toString() + "号顾客办理业务");
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(CONSUMERSLEEPSEED));/*服务时间*/
        waiters.add(waiter);
    }  
} 