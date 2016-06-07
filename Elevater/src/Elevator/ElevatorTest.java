package Elevator;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ElevatorTest {

    private Elevator elevator = new Elevator();
    private AskQueue askQueue = new AskQueue();

    @org.junit.Before
    public void setUp() throws Exception {
        askQueue.getAskingQueue().add(new Asking("(FR,1,UP,0)"));
        askQueue.getAskingQueue().add(new Asking("(FR,3,UP,1)"));
        askQueue.getAskingQueue().add(new Asking("(FR,6,UP,1)"));
        askQueue.getAskingQueue().add(new Asking("(ER,5,2)"));
        askQueue.getAskingQueue().add(new Asking("(FR,3,UP,3)"));
        askQueue.getAskingQueue().add(new Asking("(ER,7,4)"));
        askQueue.getAskingQueue().add(new Asking("(FR,8,UP,5)"));
        askQueue.getAskingQueue().add(new Asking("(FR,5,DOWN,5)"));
        askQueue.getAskingQueue().add(new Asking("(ER,4,6)"));

    }

    @org.junit.After
    public void tearDown() throws Exception {
        askQueue.getAskingQueue().clear();
    }

    @org.junit.Test
    public void repOK(){
        elevator.time = -1;
        assertEquals(false, elevator.repOK());
        elevator.time = 0;

        elevator.currentFloor = 0;
        assertEquals(false, elevator.repOK());
        elevator.currentFloor = 1;
        
        elevator.currentFloor = 11;
        assertEquals(false, elevator.repOK());
        elevator.currentFloor = 1;

        elevator.primaryFloor = 0;
        assertEquals(false, elevator.repOK());
        elevator.primaryFloor = 1;

        assertEquals(true, elevator.repOK());
    }

    @org.junit.Test
    public void getTime() throws Exception {
        System.out.println(elevator.getTime());
    }

    @org.junit.Test
    public void getCurrentFloor() throws Exception {
        System.out.println(elevator.getCurrentFloor());
    }

    @org.junit.Test
    public void getCurrentTime() throws Exception {
        System.out.println(elevator.getCurrentTime());
    }

    @org.junit.Test
    public void getElevatorState(){
        System.out.println(elevator.getElevatorState());
    }

    @org.junit.Test
    public void getIfStay(){
        System.out.println(Arrays.toString(elevator.getIfStay()));
    }

    @org.junit.Test
    public void getPrimaryFloor() throws Exception {
        System.out.println(elevator.getPrimaryFloor());
    }

    @org.junit.Test
    public void getCarryRequests() throws Exception {
        elevator.getCarryRequests().add(0, askQueue.getAskingQueue().get(0));
        System.out.println(elevator.getCarryRequests().get(0).toString());
    }

    @org.junit.Test
    public void setElevatorState() throws Exception {
        elevator.currentFloor = 2;
        elevator.setElevatorState(1);
        elevator.setElevatorState(2);
        elevator.setElevatorState(3);
    }

    @org.junit.Test
    public void traverseFloors() throws Exception {
    	
    	askQueue.getAskingQueue().set(0, null);
        elevator.traverseFloors(askQueue);
        
        elevator.setElevatorState(5);
        elevator.currentFloor = 5;
        elevator.time = 10;
        elevator.primaryFloor = 9;
        elevator.traverseFloors(askQueue);

        elevator.setElevatorState(2);
        elevator.currentFloor = 9;
        elevator.primaryFloor = 1;
        elevator.traverseFloors(askQueue);
        
    	askQueue.getAskingQueue().clear();
        setUp();
        elevator.time = 10;
        elevator.setElevatorState(2);
        elevator.currentFloor = 1;
        elevator.primaryFloor = 10;
        elevator.traverseFloors(askQueue);
        
        askQueue.getAskingQueue().clear();
        setUp();
        elevator.time = 10;
        elevator.setElevatorState(3);
        elevator.currentFloor = 2;
        elevator.primaryFloor = 1;
        elevator.traverseFloors(askQueue);
        
        askQueue.getAskingQueue().clear();
        setUp();
        elevator.time = 10;
        elevator.currentFloor = 6;
        elevator.setElevatorState(4);
        elevator.primaryFloor = 10;
        elevator.traverseFloors(askQueue);
    }

    @org.junit.Test
    public void addCarryRequests() throws Exception {
        Asking asking = askQueue.getAskingQueue().get(0);
        assertEquals(true, elevator.addCarryRequests(asking));
        assertEquals(false, elevator.addCarryRequests(asking));
    }

    @org.junit.Test
    public void ifStillHaveTrueFloor() throws Exception {
        assertEquals(false, elevator.ifStillHaveTrueFloor());
        elevator.ifStay[1] = true;
        assertEquals(true, elevator.ifStillHaveTrueFloor());
    }

    @org.junit.Test
    public void starToMove() throws Exception {
        elevator.time = -1;
        elevator.starToMove(askQueue, 0);
    	
    	askQueue.getAskingQueue().clear();
    	setUp();
        elevator.time = 10;
        elevator.currentFloor = 1;
        elevator.starToMove(askQueue, 1);
        
        askQueue.getAskingQueue().clear();
    	
    	elevator.currentFloor = 5;
    	askQueue.getAskingQueue().add(new Asking("(ER,5,2)"));
        askQueue.getAskingQueue().add(new Asking("(ER,7,4)"));
        askQueue.getAskingQueue().add(new Asking("(FR,8,UP,5)"));
        elevator.starToMove(askQueue, 0);
    }

    @org.junit.Test
    public void moveStepByStep() throws Exception {
        elevator.setElevatorState(5);
        elevator.currentFloor = 4;
        elevator.ifStay[4] = false;
        elevator.moveStepByStep(askQueue);

        elevator.setElevatorState(1);
        elevator.currentFloor = 2;
        elevator.ifStay[0] = true;
        elevator.moveStepByStep(askQueue);
        
        elevator.currentFloor = 2;
        elevator.setElevatorState(1);
        elevator.ifStay[0] = false;
        elevator.moveStepByStep(askQueue);
        
        elevator.currentFloor = 4;
        elevator.setElevatorState(5);
        elevator.ifStay[4] = false;
        elevator.moveStepByStep(askQueue);
        
        elevator.currentFloor = 4;
        elevator.setElevatorState(4);
        elevator.ifStay[4] = false;
        elevator.moveStepByStep(askQueue);
    }

    @org.junit.Test
    public void printElevatorState() throws Exception {
        elevator.printElevatorState();
    }

    @org.junit.Test
    public void ToString() throws Exception {
        System.out.println(elevator.toString());
    }

    @org.junit.Test
    public void printCarryRequests() throws Exception {
    	elevator.printCarryRequests();
        elevator.carryRequests.add(askQueue.getAskingQueue().get(3));
        elevator.printCarryRequests();
        elevator.carryRequests.add(askQueue.getAskingQueue().get(0));
        elevator.carryRequests.add(askQueue.getAskingQueue().get(1));
        elevator.carryRequests.add(askQueue.getAskingQueue().get(2));
        elevator.carryRequests.add(askQueue.getAskingQueue().get(3));
        elevator.carryRequests.add(askQueue.getAskingQueue().get(4));
        elevator.carryRequests.add(askQueue.getAskingQueue().get(5));
        elevator.printCarryRequests();
    }

    @org.junit.Test
    public void rebuildCarryRequests() throws Exception {
        assertEquals(false, elevator.rebuildCarryRequests());
        elevator.carryRequests.add(askQueue.getAskingQueue().get(0));
        elevator.carryRequests.set(0, null);
        elevator.carryRequests.add(askQueue.getAskingQueue().get(1));
        elevator.carryRequests.add(askQueue.getAskingQueue().get(2));
        elevator.carryRequests.add(askQueue.getAskingQueue().get(3));
        elevator.carryRequests.add(askQueue.getAskingQueue().get(4));
        elevator.carryRequests.add(askQueue.getAskingQueue().get(5));
        elevator.primaryFloor = 5;
        assertEquals(true, elevator.rebuildCarryRequests());
    }

}