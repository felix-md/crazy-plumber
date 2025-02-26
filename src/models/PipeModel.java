package models;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


import config.Modes;
import config.Modes.Mode;
import config.SoundLoader;
import controllers.SoundController;
import models.pipes.EmptyPipe;
import java.awt.image.LookupTable;


public abstract class PipeModel {
    public enum PipeType {
        STRAIGHT, DOUBLECORNER, T, CORNER,PLUS, START, END,EMPTY
    }

    private WaterColor waterColor;

    
    //private Direction[] directions;

    private PipeType type;
    private int rotate;
    private int x;
    private int y;
    private boolean isPassed = false;
    private int counter = 0;
    private boolean isInPath = false;
    private boolean endAnimation = false;
    private boolean isAnimating = false;
    private boolean canBeRotated = true;

    private PipeModel encapsulate = null;
    private ArrayList<PipeModel> pipeEncapsulated ;

    private Direction animationFrom = null;
    private boolean Mirror = false;
    private int animationRotation = 0;
    private boolean isRotationAnimating = false;
    private long rotateStartTime;
    private boolean[] isFrameRotatedAnimated = new boolean[10];

    private long startTime;
    private BufferedImage image;
    private ArrayList<BufferedImage> waterAnimation = new ArrayList<BufferedImage>();
    private ArrayList<BufferedImage> waterAnimation2 = new ArrayList<BufferedImage>();
    private static Direction[][][] table = {
        //{from North { diffuse ... } from EAST { diffuse ... } from SOUTH {diffuse... }{ from WEST {diffuse...}}
        {{Direction.SOUTH},{},{Direction.NORTH},{}},//STRAIGHT
        {{Direction.WEST},{Direction.SOUTH},{Direction.EAST},{Direction.NORTH}},//DOUBLECORNER
        {{Direction.EAST,Direction.WEST},{Direction.NORTH,Direction.WEST},{},{Direction.EAST,Direction.NORTH}},//T
        {{},{Direction.SOUTH},{Direction.EAST},{}},//CORNER
        {{Direction.SOUTH},{Direction.WEST},{Direction.NORTH},{Direction.EAST}},//PLUS
        {{},{Direction.EAST},{},{}},//START
        {{},{},{},{Direction.WEST}},//END
        {{},{},{},{}}//EMPTY

    };

    private static int[] nbComponent = {1,2,1,1,2,1,1,1};

    public PipeModel(PipeType type) {
        this.type = type;
        this.rotate = 0;
        this.waterColor = WaterColor.NONE;
        this.pipeEncapsulated = new ArrayList<PipeModel>();
    }
    
    public boolean isConnected(int direction){
        Direction dir  = Direction.values()[direction];
        Direction[] tab1 = this.getOutputTab()[dir.nextDirection(-this.getRotate() / 90).ordinal()];
        return tab1.length !=0;
    }

    public BufferedImage getImage(){
        return this.image;
    }
    public void setImage(BufferedImage image){
        this.image = image;
    }

    public ArrayList<BufferedImage> getWaterAnimation(){
        return this.waterAnimation;
    }

    public ArrayList<BufferedImage> getWaterAnimation2(){
        return this.waterAnimation2;
    }
    public void setWaterAnimation(ArrayList<BufferedImage> waterAnimation){
        this.waterAnimation = waterAnimation;
    }
    public void setWaterAnimation2(ArrayList<BufferedImage> waterAnimation2){
        this.waterAnimation2 = waterAnimation2;
    }

    public Direction[][] getOutputTab(){
        return table[this.getType().ordinal()];
    }

    public int nbOfOutput(){
        int nb = 0;
        for(Direction[] tab : table[this.getType().ordinal()]){
            nb += tab.length;

        }
        return nb;
    }


    public boolean isFrameRotatedAnimated(int i){
        return isFrameRotatedAnimated[i];
    }
    public void setFrameRotatedAnimated(int i, boolean b){
        isFrameRotatedAnimated[i] = b;
    }

    public boolean[] getFrameRotatedAnimated(){
        return isFrameRotatedAnimated;
    }

    public boolean isCanBeRotated() {
        return canBeRotated;
    }

    public void setCanBeRotated(boolean canBeRotated) {
        this.canBeRotated = canBeRotated;
    }

    public  int getNbComponent(){
        return nbComponent[this.getType().ordinal()];
    }

    public int getCounter() {
        return counter;
    }

    public void incrementCounter() {
        this.counter++;
    }

    public void resetCounter() {
        this.counter = 0;
    }

    public boolean isMaxPassed(){
        return this.counter == this.getNbComponent() && isPassed;
    }

    public Direction[][][] getTable() {
        return table;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean isPassed) {
        this.isPassed = isPassed;
    }

    public boolean isInPath() {
        return isInPath;
    }

    public void setInPath(boolean isInPath) {
        this.isInPath = isInPath;
    }


  

    public PipeType getType() {
        return type;
    }

    public String toString(){
        
        return this.type.toString();
    }


    public void rotate(){    
        if (isRotationAnimating || this instanceof EmptyPipe ) return;


    
        SoundController soundController = new SoundController(SoundLoader.getSoundClips());

        soundController.setVolume("rotation", -20.0f);
        soundController.playSound("rotation");

        this.isFrameRotatedAnimated = new boolean[10];
        this.isRotationAnimating = true;
        
        if(!(this instanceof EmptyPipe)){
            Modes.addMove();
        }
    }

    
    public void setFrameRotatedAnimated(boolean[] isFrameRotatedAnimated) {
        this.isFrameRotatedAnimated = isFrameRotatedAnimated;
    }
    


    public void setIsRotationAnimating(boolean isRotationAnimating) {
        this.isRotationAnimating = isRotationAnimating;
    }
    public boolean isRotationAnimating() {
        return isRotationAnimating;
    }
 

    


    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate % 360;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCounter(int i) {
        this.counter = i;
    }

    public boolean isEndAnimation() {
        return endAnimation;
    }

    public void setEndAnimation(boolean endAnimation) {
        this.endAnimation = endAnimation;
    }


    public void setRotateStartTime(long rotateStartTime) {
        this.rotateStartTime = rotateStartTime;
    }

    public long getRotateStartTime() {
        return rotateStartTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }



    public boolean isAnimating() {
        return isAnimating;
    }

    public void setAnimating(boolean isAnimating) {
        this.isAnimating = isAnimating;
    }

    public void setWaterColor(WaterColor waterColor) {
        this.waterColor = waterColor;
    }

    public WaterColor getWaterColor() {
        return waterColor;
    }

    public LookupTable getLookupTable(){
        return waterColor.getLookupTable();
    }

    public void setAnimationFrom(Direction animationFrom) {
        this.animationFrom = animationFrom;
    }

    public Direction getAnimationFrom() {
        return animationFrom;
    }

    public void setAnimationRotation(int animationRotation) {
        this.animationRotation = animationRotation;
    }

    public int getAnimationRotation() {
        return animationRotation;
    }


    public void setMirror(boolean Mirror) {
        this.Mirror = Mirror;
    }

    public boolean isMirror() {
        return Mirror;
    }

    public void setEncapsulate(PipeModel encapsulate) {
        this.encapsulate = encapsulate;
    }

    public PipeModel getEncapsulate() {
        return encapsulate;
    }

    public boolean isEncapsulated(){
        return encapsulate != null;
    }

    public void setPipeEncapsulated(ArrayList<PipeModel> pipeEncapsulated) {
        this.pipeEncapsulated = pipeEncapsulated;
    }

    public ArrayList<PipeModel> getPipeEncapsulated() {
        return pipeEncapsulated;
    }

    public void addPipeEncapsulated(PipeModel pipe){
        this.pipeEncapsulated.add(pipe);
    }


    


    


    




}
