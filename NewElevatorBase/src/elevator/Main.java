
package elevator;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Main {
    static ArrayList<Long> noOfFloors = new ArrayList<>();
    static ArrayList<Long> noOfElevators = new ArrayList<>();
    static ArrayList<Long> maxElevatorCapacity = new ArrayList<>();
    static ArrayList<Object> timePerFloor = new ArrayList<>();
    static ArrayList<Object> doorTime = new ArrayList<>();
    static ArrayList<Object> elevatorTimeOut = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {



        //variable to keep track of the values for each simulation.
        //it represents the index where each value is stored.
        int simulationNo = 0;




        FileReader reader;
        try {
            // Create a FileReader object using your filename
            reader = new FileReader("Simulation.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        JSONParser jsonParser = new JSONParser();
        JSONObject jObj;

        try {
            // Create a JSONParser using the FileReader
            jObj = (JSONObject) jsonParser.parse(reader);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return;
        }

        //import the json arrays.

        JSONArray floors = (JSONArray) jObj.get("NoOfFloors");
        for (Object floor : floors){
            noOfFloors.add((long)floor);
        }

        JSONArray elevators = (JSONArray) jObj.get("NoOfElevators");
        for (Object elevator : elevators){
            noOfElevators.add((long)elevator);
        }

        JSONArray capacities = (JSONArray) jObj.get("MaxElevatorCapacity");
        for (Object capacity : capacities){
            maxElevatorCapacity.add((long)capacity);
        }

        JSONArray timesPerFloor = (JSONArray) jObj.get("TimePerFloor");
        for (Object time : timesPerFloor){
            timePerFloor.add(time);
        }

        JSONArray doorTimes = (JSONArray) jObj.get("DoorTime");
        for (Object time : doorTimes){
            doorTime.add(time);
        }

        JSONArray timeOuts = (JSONArray) jObj.get("ElevatorTimeOut");
        for (Object timeOut : timeOuts){
            elevatorTimeOut.add(timeOut);
        }



        //---------------------------------TESTING OUTPUT HERE------------------

        test1();




    }


    private static int[] generateFloors(int highest){
        Random random = new Random();
        int n1 = random.nextInt(highest);
        n1++;
        int n2 = random.nextInt(highest);
        n2++;
        while(n1 == n2){
            n2 = random.nextInt(highest);
            n2++;
        }
        int[] floors = new int[2];
        floors[0] = n1;
        floors[1] = n2;
        return floors;
    }



    private static void test1() throws InterruptedException{

        int f = noOfFloors.get(0).intValue();
        int e = noOfElevators.get(0).intValue();
        int c = maxElevatorCapacity.get(0).intValue();




        Building building = Building.getInstance(f, e, c);



        int runTime = 65;

        for(int i = 0; i<runTime; i++){
            System.out.println("------------------------ Loop completion status: " + i + " of " + (runTime-1) + " iterations remaining ------------------------");



            if(i%2 == 0 && i<20){

                int[] floors = generateFloors(building.getNumFloors());
                building.addPerson(floors[0], floors[1]);
            }



            building.operateElevators(1000);
            Thread.sleep(1000);
        }

        System.out.println();


        //try getting the pid-1 otherwise the index doesnt match with the person!!
        //i'm not sure why the elevators dont wuite work all the time. sometimes they get these ghost requests.




        System.out.println("Person --------- " + "Start Floor  --------- " + "End Floor --------- " + "Direction --------- "
                + "Wait Time --------- " + "Ride Time --------- " + "Total Time");

        for (Person p : building.peopleCreatedInSimulation){
            if(p.getPersonDir() == 1){
                System.out.println("    " + p.getPid() + " --------- " + p.getCurrentFloor()+ " --------- " + p.getDestFloor()+ " --------- " + "UP --------- "
                        + Timer.convertTimeStamp(Timer.getWaitTime(p.getPid())) +  " --------- " + Timer.convertTimeStamp(Timer.getRideTime(p.getPid())) + " --------- "
                        + Timer.convertTimeStamp(Timer.getWaitTime(p.getPid())+ Timer.getRideTime(p.getPid())));
            }
            else{
                System.out.println("    " +  p.getPid() + " --------- " + p.getCurrentFloor()+ " --------- " + p.getDestFloor()+ " --------- " + "DOWN --------- "
                        + Timer.convertTimeStamp(Timer.getWaitTime(p.getPid())) +  " --------- " + Timer.convertTimeStamp(Timer.getRideTime(p.getPid())) + " --------- "
                        + Timer.convertTimeStamp(Timer.getWaitTime(p.getPid())+ Timer.getRideTime(p.getPid())));
            }

        }


        long avgWaitTime = Timer.getAvgWaitTime();
        long avgRideTime = Timer.getAvgRideTime();

        long minWaitTime = Timer.getMinWaitTime();
        long minRideTime = Timer.getMinRideTime();

        long maxWaitTime = Timer.getMaxWaitTime();
        long maxRideTime = Timer.getMaxRideTime();

        System.out.println("Average Wait Time: " + Timer.convertTimeStamp(avgWaitTime));
        System.out.println("Average Ride Time: " + Timer.convertTimeStamp(avgRideTime));
        System.out.println();
        System.out.println("Minimum Wait Time: " + Timer.convertTimeStamp(minWaitTime) + Timer.getMinWaitP());
        System.out.println("Minimum Ride Time: " + Timer.convertTimeStamp(minRideTime) + Timer.getMinRideP());
        System.out.println();
        System.out.println("Maximum Wait Time: " + Timer.convertTimeStamp(maxWaitTime) + Timer.getMaxWaitP());
        System.out.println("Maximum Ride Time: " + Timer.convertTimeStamp(maxRideTime)+ Timer.getMaxRideP());




    }





}
