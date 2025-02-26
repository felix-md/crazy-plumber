package models;

public class HintModel {
    
    private PipeModel pipe;
    private int rotation;

    public HintModel(PipeModel pipe, int rotation){
        this.pipe=pipe;
        this.rotation=rotation*90;
    }

    public PipeModel getPipe() {
        return pipe;
    }
    
    public int getRotation() {
        return rotation;
    }

    public void activateHint(){
        this.pipe.setRotate(rotation);
        this.pipe.setCanBeRotated(false);
    }
}
