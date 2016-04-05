package Elevator;

public interface Move{
    void traverseFloors(AskQueue askQueue);
    boolean starToMove(AskQueue askQueue, int i);
    boolean moveStepByStep(AskQueue askQueue);
    void printElevatorState();
}
