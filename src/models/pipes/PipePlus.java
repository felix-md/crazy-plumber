package models.pipes;


import models.PipeModel;
import renders.Graphic;

public class PipePlus extends PipeModel implements Graphic {


    
    PipeModel p1;
    PipeModel p2;


    public PipePlus() {
        super(PipeType.PLUS);
        p1 = new PipeStraight();
        p2 = new PipeStraight();

     
        p1.setRotate(getRotate());
        p2.setRotate(getRotate() + 90);
        p1.setEncapsulate(this);
        p2.setEncapsulate(this);
        this.addPipeEncapsulated(p1);
        this.addPipeEncapsulated(p2);
        


        
    }

    @Override
    public void rotate() {
        super.rotate();
        p1.rotate();
        p2.rotate();
    }

    @Override 
    public boolean isAnimating() {
        return p1.isAnimating() || p2.isAnimating();
    }

    @Override
    public boolean isEndAnimation() {
        return p1.isEndAnimation() && p2.isEndAnimation();
    }

    @Override
    public boolean isCanBeRotated() {
        return p2.isCanBeRotated();
    }
    
    @Override
    public void setCanBeRotated(boolean canBeRotated) {
        p2.setCanBeRotated(canBeRotated);
    }
    

    



}
