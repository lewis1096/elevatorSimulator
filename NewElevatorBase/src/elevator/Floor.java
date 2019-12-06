package elevator;

import java.sql.Time;
import java.util.ArrayList;

public class Floor {
    private int floorNum = 0;
    private boolean isFirstFloor;
    private boolean isTopFloor;
    private boolean upButtonPressed = false;
    private boolean downButtonPressed = false;
    private ArrayList<Person> waitingOnFloor = new ArrayList<>();
    private ArrayList<Person> donePeople = new ArrayList<>();



    public Floor(int floorNum, boolean isFirstFloor, boolean isTopFloor){
        this.floorNum = floorNum + 1;
        this.isFirstFloor = isFirstFloor;
        this.isTopFloor = isTopFloor;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void addPersonWaitingOnFloor(Person person){
        this.waitingOnFloor.add(person);
    }

    public void removePersonWaitingOnFloor(Person person){
        this.waitingOnFloor.remove(person);
    }

    public boolean isTherePickup(Elevator elevator) {
        for (Person person : waitingOnFloor) {
            if (person.getPersonDir() == elevator.getDirection() || elevator.getDirection()==0) {
                return true;
            }
        }
        return false;
    }


    public void addPersonToDonePeople(Person person){
        donePeople.add(person);
    }




    public void personPickUp(Elevator elevator){
        int size = waitingOnFloor.size();
        for (int i = 0; i < size; i++){
            Person person = waitingOnFloor.get(i);
            if (person.getPersonDir() == elevator.getDirection() || elevator.getDirection()==0){
                elevator.addPersonToElevator(person);
                removePersonWaitingOnFloor(person);
                elevator.personPressButton(person.getDestFloor());
                Timer.addOnElevatorTime(System.currentTimeMillis());
                System.out.println(Timer.getTimeStamp() + " Person " + person.getPid() + " boarded elevator " + elevator.getElevatorNum() + " and pressed floor " + person.getDestFloor());
                size = waitingOnFloor.size();
                i--;
            }

        }
    }

}

