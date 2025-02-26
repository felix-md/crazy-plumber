package config;

import java.util.ArrayList;

import models.Direction;
import models.PipeModel;

public class WinCondition {

    private static boolean isWin;
    private static boolean tempWin ;
    private static boolean fuite;
    private static ArrayList < PipeModel > passedPipes = new ArrayList < PipeModel > ();

    public static boolean isWin() {
        setWin();

        return isWin;
    }

    public static void setWin() {
        //On remet à zéro les attributs de la classe
        tempWin = false;
        fuite = false;
        passedPipes.clear();
        for(PipeModel start : PipeMap.getStartedPipes()){

            updatePipe(start);
            
            //tempWin contient true si il y a un chemin de Start à End	
            tempWin = check(start, Direction.EAST.nextDirection(start.getRotate() / 90)) && tempWin ;
          
            //isWin contient true si il y a un chemin de Start à End et qu'il n'y a pas de fuite
            PipeMap.resetAll();

            
        }
        isWin = (!fuite) && tempWin;
        

    }
    
    private static boolean check(PipeModel pipe, Direction to) {

        if (pipe instanceof models.pipes.EndPipe) {
     
            updatePipe(pipe);
            
            tempWin = true;
            return tempWin;
        }




        PipeModel neigh = PipeMap.getNeighbour(pipe, to);

        
        Direction[] tabDir = getTabDir(neigh, to);

        if (neigh != null && !neigh.isMaxPassed() && !(neigh instanceof models.pipes.StartPipe && pipe instanceof models.pipes.StartPipe)) {
            


            if (tabDir.length != 0) {
                updatePipe(neigh);
                
                for (Direction d : tabDir) {
                    check(neigh, d.nextDirection(neigh.getRotate() / 90));
                }
            }

        } if (tabDir.length==0|| neigh == null) {
            //Si le voisin est null (en dehors de la map) ou que le tuyau n'est pas connecté
            //On a une fuite
            fuite = true;
        }

        return tempWin;
    }

    private static void updatePipe(PipeModel pipe) {
        pipe.setInPath(true);
        pipe.setPassed(true);
        pipe.incrementCounter();
        
        if(!passedPipes.contains(pipe))passedPipes.add(pipe);

    }

    private static Direction[] getTabDir(PipeModel pipe, Direction to) {
        try {
            return pipe.getOutputTab()[to.nextDirection(-pipe.getRotate() / 90).getOpposedDir().ordinal()];
        } catch (Exception e) {
            return new Direction[] {};
        }

    }

    public static ArrayList < PipeModel > getPassedPipes() {
        return passedPipes;
    }

    

}
