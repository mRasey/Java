package Elevator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ALS_ScheduleTest {

    private ALS_Schedule als_schedule = new ALS_Schedule();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void repOK(){
        assertEquals(true, als_schedule.repOK());
    }

    @Test
    public void getArriveTime() throws Exception {
        als_schedule.getArriveTime();
    }

    @Test
    public void setArriveTime() throws Exception {
        als_schedule.setArriveTime(new Elevator());
    }

    @Test
    public void ifCanCarry() throws Exception {
        als_schedule.ifCanCarry(new Elevator(), new Asking("(FR,1,UP,0)"));
        als_schedule.ifCanCarry(new Elevator(), new Asking("(FR,1,UP,2)"));
        als_schedule.ifCanCarry(new Elevator(), new Asking("(FR,0,UP,1)"));
    }

}