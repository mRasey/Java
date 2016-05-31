package Elevator;

enum EntryState{
    FR, ER
}

public class Asking {
    private EntryState entryState = EntryState.FR;
    private ElevatorState elevatorState = ElevatorState.UP;
    private int askingFloorNumber;
    private int askingTime;

    public Asking(String input){
        String[] strings = input.split("[(,)]");
        if(strings[1].equals("FR")) {
            entryState = EntryState.FR;
            askingFloorNumber = Integer.parseInt(strings[2]);
            if (strings[3].equals("UP"))
                elevatorState = ElevatorState.UP;
            else
                elevatorState = ElevatorState.DOWN;
            askingTime = Integer.parseInt(strings[4]);
        }
        else {
            entryState = EntryState.ER;
            askingFloorNumber = Integer.parseInt(strings[2]);
            askingTime = Integer.parseInt(strings[3]);
        }
    }

    public EntryState getEntryState(){
        return entryState;
    }

    public int getAskingFloorNumber(){
        return  askingFloorNumber;
    }

    public ElevatorState getElevatorState(){
        return elevatorState;
    }

    public int getAskingTime(){
        return askingTime;
    }

}

