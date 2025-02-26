package models;

import java.util.ArrayList;
import java.util.Timer;

import config.LevelLoader;
import config.Modes;
import config.TaskTimer;
import config.WinCondition;

public class TimerMode {

    private  ArrayList<Integer> timeArray = setTimeArray();
    private  Timer timer=new Timer();
    private  int remainingTime= timeArray.get(LevelLoader.getSelectedLevel()-1);
    private  int time;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ArrayList<Integer> getTimeArray() {
        return timeArray;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remaining) {
        remainingTime = remaining;
    }

    private ArrayList<Integer> setTimeArray() {
        ArrayList<Integer> timeArray= new ArrayList<>();
        final int n= 30;
        for (int i=1; i<16; i++) {
            timeArray.add((i/5 +1)*n);
        }
        return timeArray;
    }

    public void startTimer() {
        time=0;
        timer= new Timer();
        timer.scheduleAtFixedRate(new TaskTimer(), 0, 1000);
    }

    public void stopTimer(){
        timer.cancel();
    }

    public boolean isLost(){
        return remainingTime==0.0 && !WinCondition.isWin();
    }

    public void resetTimer(){
        remainingTime= timeArray.get(LevelLoader.getSelectedLevel()-1);
    }
}
