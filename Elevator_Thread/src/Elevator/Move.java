package Elevator;

/**
 * Created by Billy on 2016/3/14.
 */
public interface Move{
    void traverseFloors(AskQueue askQueue);
    void starToMove(AskQueue askQueue, int i);
    void moveStepByStep(AskQueue askQueue);
    void printElevatorState();
}
