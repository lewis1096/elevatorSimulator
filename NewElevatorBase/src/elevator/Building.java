package elevator;

import gui.ElevatorDisplay;

import java.sql.Time;
import java.util.ArrayList;


public class Building {
    private static Building instance;
    private ElevatorController elevatorController = ElevatorController.getInstance();
    private int numFloors;
    private int numElevators;
    private int elevatorCapacity;
    private static Floor[] floors;
    private static Elevator[] elevators;
    ArrayList<Person> peopleCreatedInSimulation = new ArrayList<>();






    //Method to prevent the creation of more than one Building object.
    public static Building getInstance(int numFloors, int numElevators, int elevatorCapacity) {
        if (instance == null) {
            instance = new Building(numFloors, numElevators, elevatorCapacity);

            System.out.println(Timer.getTimeStamp() + " Building with " + numFloors + " floors and " + numElevators + " elevators created.");
        }
        return instance;
    }



    public void addPerson(int currFloor, int destFloor){
        if(currFloor != destFloor && destFloor <= this.getNumFloors() && currFloor>=1){
            Person newPerson = new Person(currFloor, destFloor);
            floors[currFloor-1].addPersonWaitingOnFloor(newPerson);
            elevatorController.addFloorRequest(currFloor);
            ElevatorController.setIsThereNewFloorRequest(true);
            peopleCreatedInSimulation.add(newPerson);
        }
        else throw new IllegalArgumentException("Invalid floor input.");



    }




    //Creates a building object by setting the number of floors, number of elevators, and maximum capacity for each elevator.
    public Building(int numFloors, int numElevators, int elevatorCapacity){
        this.numFloors = numFloors;
        this.numElevators = numElevators;
        this.elevatorCapacity = elevatorCapacity;
        floors = new Floor[numFloors];
        elevators = new Elevator[numElevators];
        ElevatorDisplay.getInstance().initialize(this.numFloors);
        createFloors();
        createElevators();

    }



    public static Elevator getElevator(int elevatorNum){
        return elevators[elevatorNum-1];
    }



    public void operateElevators(int timePerFloor) throws InterruptedException {

        ElevatorController.whichElevator();



        for (Elevator elevator : elevators){
            int currFloor = elevator.getElevatorLocation();

            if (elevator.isThereDropOff(currFloor) || floors[currFloor-1].isTherePickup(elevator)){
                elevator.openDoors();
                Thread.sleep(2000);
                elevator.closeDoors();
            }


            if (elevator.isThereDropOff(currFloor)){
                elevator.peopleOff(floors[currFloor-1]);
                elevator.buttonDepress(currFloor);

            }

            if(floors[currFloor-1].isTherePickup(elevator)){
                floors[currFloor-1].personPickUp(elevator);
                elevator.buttonDepress(currFloor);

            }

            if(elevator.getDirection()==0){

                elevator.incrementIdleTimer();
            }

            if (elevator.getDirection() == 0){
                if (currFloor < elevator.highestButtonPressed()){
                    elevator.setDirection(1);
                }
                else if(currFloor> elevator.lowestButtonPressed()){
                    elevator.setDirection(-1);
                }
            }



            //check what button is pressed on elevator
            //compare it to the current elevator location
            if(elevator.getDirection() == 1){
                int dest = elevator.highestButtonPressed();
                if (currFloor< dest){
                    elevator.setDirection(1);
                }
                else{
                    elevator.setDirection(0);
                    elevator.resetIdleTimer();
                }

            }

            if(elevator.getDirection() == -1){
                int dest = elevator.lowestButtonPressed();
                if (currFloor > dest){
                    elevator.setDirection(-1);
                }
                else{
                    elevator.setDirection(0);
                    elevator.resetIdleTimer();
                }

            }

            if (elevator.getIdleTimer() == 10 && elevator.getElevatorLocation() != 1){
                elevator.resetIdleTimer();
                System.out.println(Timer.getTimeStamp() + " Elevator " + elevator.getElevatorNum() + " has been idle for 10 seconds. Now returning to ground floor.");
                elevator.depressAllButtons();
                elevator.personPressButton(1);

            }

            if(elevator.getDirection() == 0 && elevator.getElevatorLocation()==1){
                elevator.buttonDepress(1);

            }



        }

        updateElevators();


    }



    private void updateElevators(){
        for (Elevator elevator : elevators){
            int elevDir = elevator.getDirection();
            if(elevDir == 1){

                moveElevatorUp(elevator);
                ElevatorDisplay.getInstance().updateElevator(elevator.getElevatorNum(), elevator.getElevatorLocation(), elevator.numRiders(), ElevatorDisplay.Direction.UP);
            }
            else if(elevDir == -1){
                moveElevatorDown(elevator);
                ElevatorDisplay.getInstance().updateElevator(elevator.getElevatorNum(), elevator.getElevatorLocation(), elevator.numRiders(), ElevatorDisplay.Direction.DOWN);
            }
            else if(elevDir == 0){
                //Do nothing - Elevator is idle
                ElevatorDisplay.getInstance().updateElevator(elevator.getElevatorNum(), elevator.getElevatorLocation(), elevator.numRiders(), ElevatorDisplay.Direction.IDLE);
                ElevatorDisplay.getInstance().setIdle(elevator.getElevatorNum());
            }
        }
    }




    private void moveElevatorUp(Elevator elevator){
        System.out.print(Timer.getTimeStamp() + " Elevator " + elevator.getElevatorNum() + " moved from floor "
                + elevator.getElevatorLocation() + " to floor " + (elevator.getElevatorLocation()+1) + ". ");
        elevator.printButtonsPressed();
        elevator.printRiders();


        elevator.incrementElevatorLocation();
    }

    private void moveElevatorDown(Elevator elevator){
        System.out.print(Timer.getTimeStamp() + " Elevator " + elevator.getElevatorNum() + " moved from floor "
                + elevator.getElevatorLocation() + " to floor " + (elevator.getElevatorLocation()-1) + ". ");
        elevator.printButtonsPressed();
        elevator.printRiders();

        elevator.decrementElevatorLocation();
    }


    public int getNumFloors() {
        return numFloors;
    }

    public static int getNumElevators() {
        return elevators.length;
    }

    //Creates floor objects and stores every floor in the array.
    //Check if floor is the first or last and sets boolean variables in constructor accordingly.
    private void createFloors(){
        for (int i = 0; i < numFloors; i++){
            if(i == 0) floors[i] = new Floor(i, true, false);
            else if (i == numFloors - 1) floors[i] = new Floor(i, false, true);
            else floors[i] = new Floor(i, false, false);
        }
    }



    //Creates elevator object with number to ID it, and maximum elevator capacity.
    private void createElevators(){
        for (int i = 0; i < numElevators; i++){
            elevators[i] = new Elevator(i, elevatorCapacity, this);

        }


    }



}
