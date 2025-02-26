package config;

import java.util.TimerTask;

import config.Modes.Mode;

public class TaskTimer extends TimerTask {

    @Override
    public void run() {
        if (Modes.getActualMode()==Mode.TIMER) {
            if (Modes.getRemainingTime()>0 ) {
                Modes.setRemainingTime(Modes.getRemainingTime()-1);;
            }
        }
        Modes.setTime(Modes.getTime()+1);
    }
    
}
