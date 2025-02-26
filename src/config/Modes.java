package config;

import java.util.ArrayList;

import java.util.Stack;
import java.util.Timer;

import models.LimitedMove;
import models.PipeModel;
import models.TimerMode;

public class Modes {
    
    public enum Mode{
        NORMAL("NORMAL"),LIMITED_MOVE("LIMITED MOVE"),TIMER("TIMER");
        private final String nom;
        Mode(String nom){
            this.nom=nom;
        }
        @Override
        public String toString(){
            return nom;
        }
    };
    private static Mode actualMode = Mode.NORMAL;
    private static int score=100;

    private static LimitedMove limitedMove= new LimitedMove();
    private static TimerMode timer = new TimerMode();

    private static Stack<PipeModel> undoStack= PipeMap.getUndoStack(); // pour enregistrer les moves (pour annuler)
    private static Stack<PipeModel> redoStack= PipeMap.getRedoStack(); // pour enregistrer les moves (pour les refaire)

    public static ArrayList<Integer> getTimeArray() {
        return timer.getTimeArray();
    }

    public static int getRemainingTime() {
        return timer.getRemainingTime();
    }

    public static void setRemainingTime(int remainingTime) {
        timer.setRemainingTime(remainingTime);
    }

    public static Mode getActualMode() {
        return actualMode;
    }

    public static void setActualMode(Mode actualMode) {
        Modes.actualMode = actualMode;
    }
    
    public static void addMove(){
        limitedMove.addMove();
    }

    public static void startTimer() {
        timer.startTimer();
    }

    public static void stopTimer(){
        timer.stopTimer();
    }

    public static boolean isLost(){
       
        switch(actualMode) {
            case LIMITED_MOVE: 
                return limitedMove.isLost();
            case TIMER:
                return timer.isLost();
            default:
                return false;
        }
    }

    public static void resetMode(){
        score=100;
        stopTimer();
        if(actualMode==Mode.LIMITED_MOVE){
            limitedMove.resetMoves();
        }
        else if (actualMode==Mode.TIMER) {
            timer.resetTimer();
        }
    }

    public static void undoMove() {
        if (!undoStack.isEmpty()) {
            PipeModel lastPipe= undoStack.pop();
            if(lastPipe.isCanBeRotated()){
                PipeMap.getPipe(lastPipe.getX(), lastPipe.getY()).setRotate(lastPipe.getRotate()-90);
                limitedMove.setNbMove(limitedMove.getNbMove()-1);
                redoStack.add(lastPipe);
            }
        }
    }

    public static void redoMove() {
        if (!redoStack.isEmpty()) {
            PipeModel lastPipe= redoStack.pop();
            if(lastPipe.isCanBeRotated()){
                PipeMap.getPipe(lastPipe.getX(), lastPipe.getY()).setRotate(lastPipe.getRotate()+90);
                limitedMove.setNbMove(limitedMove.getNbMove()+1);
                undoStack.add(lastPipe);
            }
        }
    }

    public static void resetUndoRedo() {
        undoStack.clear();
        redoStack.clear();
    }

    public static void restart(){
        PipeMap.resetRotatePipesForRestart();
        
        while(!undoStack.isEmpty()){
            undoMove();
        }
        limitedMove.resetMoves();
        resetUndoRedo();
    }

    public static int getRemaining() {
        switch (actualMode) {
            case LIMITED_MOVE:
                return limitedMove.getRemaining();
            case TIMER:
                return getRemainingTime();
            default:
                break;
        }
        return -1;
    }

    public static int getMaxRemaining() { // pour l'affichage des barres sans qu'elles retrécissent
        switch (actualMode) {
            case LIMITED_MOVE:
                return limitedMove.getMaxRemaining();
            case TIMER:
                return getRemainingTime();
            default:
                break;
        }
        return -1;
    }

    public static int getStar() {
        if (score>100) {
            return 3;
        }
        else if (score>40) {
            return 2;
        }
        else {
            return 1;
        }
    }

    public static int getScore() {
        return score;
    }

    public static void setScore() { 
        if (score<=0 || isLost()) {
            score=0;
        }
        else {
            switch (actualMode) {
                case NORMAL:
                    score= 300-limitedMove.getNbMove()/3 -timer.getTime()/5;
                    break;
                case LIMITED_MOVE:
                    score= (limitedMove.getMove()-limitedMove.getNbMove())*10 -timer.getTime()/10;
                    break;
                case TIMER:
                    score= getRemainingTime()*6 -limitedMove.getNbMove()/10;
                    break;
                default:
                    break;
            }
            score-= Hints.getNbHintsUsed()*30;
        }
        
    } 

    public static int getTime() {
        return timer.getTime();
    }

    public static void setTime(int times) {
        timer.setTime(times);
    }

    public static LimitedMove getLimitedMove() {
        return limitedMove;
    }

    
}
