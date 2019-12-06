package elevator;

import gui.ElevatorDisplay;

import java.util.ArrayList;


public class Elevator implements Comparable<Elevator>{
    Building building;
    private int elevatorNum = 0;
    private int capacity;
    private int elevatorLocation;
    private int direction;
    private ArrayList<Person> peopleInElevator = new ArrayList<>();
    private boolean isDoorOpen;
    private int elevatorDest;
    private boolean[] buttonsPressed;
    private int idleTimer;
    private int distToRequest;



    //Creates an elevator with a number to ID it and a capacity.
    public Elevator(int elevatorNum, int capacity, Building building){
        this.elevatorNum = elevatorNum + 1;
        this.capacity = capacity;
        this.elevatorLocation = 1;
        this.building = building;
        this.direction = 0;
        this.buttonsPressed = new boolean[building.getNumFloors()];
        this.idleTimer = 0;
        ElevatorDisplay.getInstance().addElevator(this.elevatorNum, 1);


    }


    private ArrayList<Integer> buttonsPressed(){
        ArrayList<Integer> buttonsOn = new ArrayList<>();
        for (int i = 0; i < buttonsPressed.length; i++){
            if (buttonsPressed[i] == true){
                buttonsOn.add(i+1);
            }
        }
        return buttonsOn;
    }

    public void printButtonsPressed(){
        System.out.print("Buttons Pressed: [");

        for (int i : buttonsPressed()){
            System.out.print(" " + i);
        }
        System.out.print(" ].");

    }


    public void printRiders(){
        System.out.print(" Riders: [");

        for (Person i : peopleInElevator){
            System.out.print(" P" + i.getPid());
        }
        System.out.print(" ]");
        System.out.println();

    }

    public int getIdleTimer() {
        return idleTimer;
    }


    public void incrementIdleTimer(){
        idleTimer++;
    }

    public void resetIdleTimer(){
        idleTimer = 0;
    }


    public boolean isThereDropOff(int floorNum){
        for (Person p : peopleInElevator){
            if (p.getDestFloor() == floorNum){
                return true;
            }
        }
        return false;
    }


    public void peopleOff(Floor floor){
        int size = peopleInElevator.size();
        for (int i = 0; i < size; i++){
            Person person = peopleInElevator.get(i);
            if (person.getDestFloor() == floor.getFloorNum()){
                floor.addPersonToDonePeople(person);
                this.peopleInElevator.remove(person);
                this.buttonDepress(floor.getFloorNum());
                Timer.addAtDestTime(System.currentTimeMillis());
                System.out.println(Timer.getTimeStamp() + " Person " + person.getPid() + " ARRIVED at floor " + floor.getFloorNum());
                size = peopleInElevator.size();
                i--;
            }
        }

    }

    public void personPressButton(int floor){
        int floorIdx = floor-1;
        buttonsPressed[floorIdx] = true;

    }




    public void buttonDepress(int floor){
        int floorIdx = floor-1;

        buttonsPressed[floorIdx] = false;

    }


    public int highestButtonPressed(){
        for (int i = buttonsPressed.length-1; i>0; i--){
            if (buttonsPressed[i] == true){
                return i+1;
            }
        }
        return 1;
    }


    public int lowestButtonPressed(){
        for (int i = 0; i < buttonsPressed.length; i++){
            if (buttonsPressed[i] == true){
                return i+1;
            }
        }
        return building.getNumFloors();
    }



    public int getElevatorLocation() {
        return elevatorLocation;
    }

    public void incrementElevatorLocation(){
        if(this.elevatorLocation <= building.getNumFloors()-1){
            this.elevatorLocation++;
        }
        else{
            throw new IllegalArgumentException("Cannot go higher than floor " + building.getNumFloors());
        }

    }

    public void decrementElevatorLocation(){
        if (this.elevatorLocation >= 2){
            this.elevatorLocation--;
        }
        else{
            throw new IllegalArgumentException("Cannot go lower than floor 1.");
        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        if(direction == 1 || direction == 0 || direction == -1){
            this.direction = direction;
        }
        else{
            throw new IllegalArgumentException("Invalid input for direction.");
        }
    }

    public int getElevatorNum() {
        return elevatorNum;
    }

    public int numRiders(){
       return this.peopleInElevator.size();
    }

    public void addPersonToElevator(Person person){
        this.peopleInElevator.add(person);

    }




    public void openDoors(){
        System.out.println(Timer.getTimeStamp() + " Elevator " + this.getElevatorNum() + " doors open");
        isDoorOpen = true;
        ElevatorDisplay.getInstance().openDoors(this.getElevatorNum());
    }

    public void closeDoors(){
        System.out.println(Timer.getTimeStamp() + " Elevator " + this.getElevatorNum() + " doors close");
        isDoorOpen = false;
        ElevatorDisplay.getInstance().closeDoors(this.getElevatorNum());
    }

    public boolean isDoorOpen() {
        return isDoorOpen;
    }

    public void depressAllButtons(){
        for (int i = 0; i<buttonsPressed.length; i++){
            if (buttonsPressed[i] == true){
                buttonsPressed[i] = false;
            }
        }


    }




    public int getDistToRequest() {
        return distToRequest;
    }

    public void setDistToRequest(int distToRequest) {
        this.distToRequest = distToRequest;
    }

    @Override
    public int compareTo(Elevator compareElevator) {

        int compareDiff = ((Elevator) compareElevator).getDistToRequest();

        return this.getDistToRequest() - compareDiff;
    }


}


