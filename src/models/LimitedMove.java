package models;

import config.LevelLoader;
import config.WinCondition;

public class LimitedMove {
    
    private int[] maxMoves= {20,25,45,45,45,50,50,50,50,50,50,50,50,50,50};
    private final int move=maxMoves[LevelLoader.getSelectedLevel()-1]*(LevelLoader.getActualDifficulty().ordinal()+1);
    private int nbMove=0;

    public int[] getMaxMoves() {
        return maxMoves;
    }

    public int getNbMove() {
        return nbMove;
    }

    public int getMove() {
        return move;
    }

    public void addMove(){
        nbMove++;
    }

    public void setNbMove(int nbMove) {
        this.nbMove = nbMove;
    }

    public boolean isLost(){
        return nbMove==move && !WinCondition.isWin();
    }

    public void resetMoves(){
        nbMove=0;
    }

    public int getRemaining(){
        return move- nbMove;
    }

    public int getMaxRemaining(){
        return move- nbMove;
    }
}
