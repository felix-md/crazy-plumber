package models;

public enum Direction {
    NORTH, EAST, SOUTH, WEST;
    
    public Direction nextDirection(){
        return values()[(this.ordinal() + 1)%4];
    }

    public Direction getOpposedDir(){
        return values()[(this.ordinal() + 2)%4];
        
    }

    public Direction nextDirection(int n){
        if(n < 0){
            n = 4 + n;
        }
        return values()[(this.ordinal() + n)%4];
    }

    
}
