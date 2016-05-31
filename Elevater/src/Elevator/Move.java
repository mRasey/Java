package Elevator;

public interface Move{
    void traverseFloors(AskQueue askQueue);
    void starToMove(AskQueue askQueue, int i);
    void moveStepByStep(AskQueue askQueue);
    void printElevatorState();
}
