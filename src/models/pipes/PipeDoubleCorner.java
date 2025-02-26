package models.pipes;


import models.PipeModel;
import renders.Graphic;

public class PipeDoubleCorner extends PipeModel implements Graphic{

    
    PipeModel p1;
    PipeModel p2;


    public PipeDoubleCorner(){
        super(PipeType.DOUBLECORNER);
        p1 = new PipeCorner();
        p2 = new PipeCorner();
        
        p1.setRotate(getRotate() );
        p2.setRotate(getRotate() );
        p1.setEncapsulate(this);
        p2.setEncapsulate(this);
        this.addPipeEncapsulated(p1);
        this.addPipeEncapsulated(p2);



    }

  

    @Override
    public void rotate() {
        
        //super.rotate();
        p1.rotate();
        p2.rotate();
        this.setRotate(getRotate()+90);
        p1.setRotate(p1.getRotate() - 90);
        p2.setRotate(p2.getRotate() - 90);
        
        

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
    public void setRotate(int rotate) {
        super.setRotate(rotate);
        p1.setRotate(this.getRotate()+ 180);
        p2.setRotate(this.getRotate() );
        

    }

    @Override
    public boolean isCanBeRotated() {
        return p2.isCanBeRotated();
    }
    
    @Override
    public void setCanBeRotated(boolean canBeRotated) {
        p2.setCanBeRotated(canBeRotated);
    }
  
    public PipeModel getEncapsulated(int i) {
        if (i == 0) {
            return p1;
        } else if (i == 1) {
            return p2;
        }
        return p1;
    }

}
