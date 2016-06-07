package Elevator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchedulerTest {

    private Scheduler scheduler = new Scheduler();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void repOK(){
        scheduler.setNextFloor(0);
        assertEquals(false, scheduler.repOK());
        scheduler.setNextFloor(1);

        scheduler.setNextFloor(11);
        assertEquals(false, scheduler.repOK());
        scheduler.setNextFloor(1);
        
        scheduler.setNextTime(-1);
        assertEquals(false, scheduler.repOK());
        scheduler.setNextTime(0);

        assertEquals(true, scheduler.repOK());
    }
    @Test
    public void getNextFloor() throws Exception {
        System.out.println(scheduler.getNextFloor());
    }

    @Test
    public void setNextFloor() throws Exception {
        scheduler.setNextFloor(5);
    }

    @Test
    public void setNextTime() throws Exception {
        scheduler.setNextTime(1);
    }

    @Test
    public void getNextTime() throws Exception {
        System.out.println(scheduler.getNextTime());
    }

}