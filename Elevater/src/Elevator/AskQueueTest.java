package Elevator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AskQueueTest {

    AskQueue askQueue = new AskQueue();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void addAskingQueue() throws Exception {
        askQueue.addAskingQueue(new Asking("(FR,1,UP,0)"), 0);
        askQueue.addAskingQueue(new Asking("(FR,1,UP,5)"), 6);
    }

    @Test
    public void getAskingQueue() throws Exception {
        askQueue.getAskingQueue();
    }

}