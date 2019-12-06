package elevator;

/*
Person object will be created and make a request to go to a floor.
 */

public class Person {
    private static int personID = 1;
    private int pid;
    private int currentFloor;
    private int destFloor;
    private int personDir;



    public Person(int currentFloor, int destFloor){
        if(personDir == 1 || personDir == 0){
            pid = personID++;
            this.currentFloor = currentFloor;
            this.destFloor = destFloor;
            if(currentFloor < destFloor){
                personDir = 1;
                Timer.addCreationTime(System.currentTimeMillis());
                System.out.println(Timer.getTimeStamp() + " Person " + pid + " pressed UP button on floor " + currentFloor);

            }
            else{
                personDir = -1;
                Timer.addCreationTime(System.currentTimeMillis());
                System.out.println(Timer.getTimeStamp() + " Person " + pid + " pressed DOWN button on floor " + currentFloor);

            }



        }
        else{
            throw new IllegalArgumentException("Input for personDir can be 1 for UP or -1 for DOWN.");
        }

    }


    public int getPid() {
        return pid;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getPersonDir() {
        return personDir;
    }

    public int getDestFloor() {
        return destFloor;
    }
}
