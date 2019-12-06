package elevator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class Timer {
    private static int index = 1;
    private static ArrayList<Long> creationTime = new ArrayList<>();
    private static ArrayList<Long> onElevatorTime = new ArrayList<>();
    private static ArrayList<Long> atDestTime = new ArrayList<>();
    private static ArrayList<Long> rideTime = new ArrayList<>();
    private static long initTime = System.currentTimeMillis();
    private static boolean executedCorrectly = true;
    private static ArrayList<Long> waitTimes = new ArrayList<>();
    private static ArrayList<Long> rideTimes = new ArrayList<>();




    public static String getTimeStamp(){
        long now = System.currentTimeMillis() - initTime;

        long hours = now/3600000;
        now -= (hours * 3600000);

        long minutes = now/60000;
        now -= (minutes * 60000);

        long seconds = now/1000;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);

    }

    public static String convertTimeStamp(long time){

        long hours = time/3600000;
        time -= (hours * 3600000);

        long minutes = time/60000;
        time -= (minutes * 60000);

        long seconds = time/1000;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);

    }




    public static void addCreationTime(long time){
        creationTime.add(time);
    }

    public static void addOnElevatorTime(long time){
        onElevatorTime.add(time);
    }

    public static void addAtDestTime(long time){
        atDestTime.add(time);
    }


    public static void matchIndexRideTime(){
        rideTime.add(0, (long) 0);
    }

    public static boolean isExecutedCorrectly() {
        return executedCorrectly;
    }

    //This try catch will allow the stats to show even if some poeple don't get to the floor.
    public static long getWaitTime(int index){
        try {
            long waitTime = Math.abs(creationTime.get(index) - onElevatorTime.get(index));
            waitTimes.add(waitTime);
            return waitTime;
        }catch (Exception NullPointerException){
            executedCorrectly = false;
            addAtDestTime(0);
            return 0;
        }
    }

    //This try catch will allow the stats to show even if some poeple don't get to the floor.
    public static long getRideTime(int index){
        try {
            long rideTime = Math.abs(atDestTime.get(index) - onElevatorTime.get(index));
            rideTimes.add(rideTime);
            return rideTime;
        } catch (Exception NullPointerException){
            return 0;
        }
    }


    public static long getMinWaitTime(){
        return Collections.min(waitTimes);
    }

    public static String getMinWaitP(){
        int idx = waitTimes.indexOf(getMinWaitTime());
        idx = (idx/2)+1;
        return " (P" + idx + ")";
    }


    public static long getMaxWaitTime(){
        return Collections.max(waitTimes);
    }

    public static String getMaxWaitP(){
        int idx = waitTimes.indexOf(getMaxWaitTime());
        idx = (idx/2)+1;
        return " (P" + idx + ")";
    }

    public static long getMinRideTime(){
        return Collections.min(rideTimes);
    }

    public static String getMinRideP(){
        int idx = rideTimes.indexOf(getMinRideTime());
        idx = (idx/2)+1;
        return " (P" + idx + ")";
    }

    public static long getMaxRideTime(){
        return Collections.max(rideTimes);
    }

    public static String getMaxRideP(){
        int idx = rideTimes.indexOf(getMaxRideTime());
        idx = (idx/2)+1;
        return " (P" + idx + ")";
    }

    public static long getAvgWaitTime(){

        int sum = 0;
        for(long i : waitTimes){
            sum+=i;
        }
        return sum/waitTimes.size();

    }

    public static long getAvgRideTime(){
        int sum = 0;
        for(long i : rideTimes){
            sum+=i;
        }
        return sum/rideTimes.size();
    }




}
