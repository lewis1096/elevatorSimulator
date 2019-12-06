package elevator;


import gui.ElevatorDisplay;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;





/*
    Elevator controller algorithm works the following way:
        1) Check if there is a new Floor request.
        2) if there's a new floor request, it will take the array of elevators and sort them.
            *Elevators implement Comparable. The compareTo function will sort them based on
            how far the elevator is to a floor. Closest elevator being at index 0, etc.
        3) then for every elevator starting from the closest one, it checks which one
        is more adequate to respond to the floor request.
 */



public class ElevatorController{
    private static ElevatorController instance;
    private static ArrayList<Integer> floorRequests = new ArrayList<>();
    private static boolean isThereNewFloorRequest;




    //Method to prevent the creation of more than one ElevatorController object.
    public static ElevatorController getInstance() {
        if (instance == null) {
            instance = new ElevatorController();
            System.out.println(Timer.getTimeStamp() + " Elevator controller created");
            Timer.addAtDestTime(0);
            Timer.addCreationTime(0);
            Timer.addOnElevatorTime(0);
            Timer.matchIndexRideTime();
        }
        return instance;
    }


    public static void setIsThereNewFloorRequest(boolean isThereNewFloorRequest) {
        ElevatorController.isThereNewFloorRequest = isThereNewFloorRequest;
    }

    //returns sorted array of elevators based on their distance to the FR
    private static Elevator[] closestElevatorsToFloor(int floor){
        Elevator[] elevators = new Elevator[Building.getNumElevators()];

        int i = 1;
        while (i<=Building.getNumElevators()) {
            Elevator elev = Building.getElevator(i);
            elevators[i-1] = elev;
            int diff = Math.abs(floor - elev.getElevatorLocation());
            elev.setDistToRequest(diff);
            i++;
        }
        Arrays.sort(elevators);
        return elevators;

    }


    public static void whichElevator()throws InterruptedException{
        int frLastIndex = floorRequests.size()-1;

        if(isThereNewFloorRequest){
            isThereNewFloorRequest = false;


            Elevator[] closestElevators = closestElevatorsToFloor(floorRequests.get(frLastIndex));

            for (Elevator elevator : closestElevators){
                if(elevator.getDirection() == 1){
                    if(elevator.highestButtonPressed()>floorRequests.get(frLastIndex) && elevator.getElevatorLocation() < floorRequests.get(frLastIndex)){
                        System.out.println(Timer.getTimeStamp() + " Elevator " + elevator.getElevatorNum() + " received the request from floor " + floorRequests.get(frLastIndex));

                        return;
                    }
                    else{

                    }
                }
                if(elevator.getDirection() == -1){
                    if (elevator.lowestButtonPressed()<floorRequests.get(frLastIndex) && elevator.getElevatorLocation()>floorRequests.get(frLastIndex)){
                        System.out.println(Timer.getTimeStamp() + " Elevator " + elevator.getElevatorNum() + " received the request from floor " + floorRequests.get(frLastIndex));



                        return;
                    }
                }


                if(elevator.getDirection() == 0){
                    elevator.personPressButton(floorRequests.get(frLastIndex));
                    System.out.println(Timer.getTimeStamp() + " Elevator " + elevator.getElevatorNum() + " received the request from floor " + floorRequests.get(frLastIndex));

                    return;
                }


            }



            }


        }



    public void addFloorRequest(int floor){
        floorRequests.add(floor);
    }



}
