package renders;

import java.util.ArrayList;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;


import models.Direction;
import models.PipeModel;
import models.pipes.EmptyPipe;

import config.Constant;
import config.PipeMap;
import config.WinCondition;
import gui.Game;

import java.awt.image.LookupOp;
import java.awt.image.LookupTable;


public class PipeView implements Graphic {

    private Game game;
    private BufferedImage asset, background, locked;// image de fonc des tuyaux

    private static ArrayList<PipeModel> pipeChain = new ArrayList<>();// liste des pipes
    private static boolean startAreAnimating = false;
    private static boolean[] rotationAreAnimating = { false, false };

    private static boolean animationEnded = false;

    public PipeView(Game game) {
        this.game=game;
       
        addAssets();
    }

    public void addAssets() {

        asset = getImage("puzzle_pipes/OGA_preview.png");
        background = asset.getSubimage(0, 0, 128, 128);
        locked = asset.getSubimage(2 * 128, 1 * 128, 128, 128);
    }

    public void drawPipe(Graphics g, PipeModel pipe, int x, int y, boolean drawBackground, boolean animation) {
        // background
        if (pipe instanceof EmptyPipe) {

            g.drawImage(background, x * Constant.PIPE_SIZE, y * Constant.PIPE_SIZE,
                    Constant.PIPE_SIZE, Constant.PIPE_SIZE, null);
            return;
        }
        if (pipe.getNbComponent() > 1) {
            for (PipeModel p : pipe.getPipeEncapsulated()) {
                drawPipe(g, p, x, y, drawBackground, animation);
            }
            return;
        }

        if (drawBackground) {
            g.drawImage(background, x * Constant.PIPE_SIZE, y * Constant.PIPE_SIZE,
                    Constant.PIPE_SIZE, Constant.PIPE_SIZE, null);
        }

        BufferedImage img = pipe.getImage();
        img = resizeImage(img, Constant.PIPE_SIZE, Constant.PIPE_SIZE);

        if (pipe instanceof models.pipes.StartPipe && !pipe.isAnimating() && !pipe.isEndAnimation()) {
            g.setColor(pipe.getWaterColor().getColor());
            g.fillRect(x * Constant.PIPE_SIZE, y * Constant.PIPE_SIZE,
                    Constant.PIPE_SIZE, Constant.PIPE_SIZE);
        }
        if (animation) {
            auxAnim(g, pipe, x, y);
        }
        if (pipe.isRotationAnimating() && !pipe.isEncapsulated()) {
            auxRotateAnimation(g, pipe, x, y);

        } else if (pipe.isRotationAnimating() && pipe.isEncapsulated()) {
            ArrayList<PipeModel> pipes = pipe.getEncapsulate().getPipeEncapsulated();

            auxRotateAnimation(g, pipes, x, y, pipe);

        }

        AffineTransform at = new AffineTransform();
        at.translate(x * Constant.PIPE_SIZE + Constant.PIPE_SIZE / 2, y * Constant.PIPE_SIZE + Constant.PIPE_SIZE / 2);
        at.rotate(Math.toRadians(pipe.getRotate()));

        at.translate(-img.getWidth() / 2, -img.getHeight() / 2);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(img, at, null);

        if (!pipe.isCanBeRotated() && !animation) {
            g.drawImage(locked, x * Constant.PIPE_SIZE, y * Constant.PIPE_SIZE,
                    Constant.PIPE_SIZE, Constant.PIPE_SIZE, null);
        }

    }

    private void auxRotateAnimation(Graphics g, ArrayList<PipeModel> pipes, int x, int y, PipeModel mainPipe) {

        for (int i = 0; i < pipes.size(); i++) {
            if (!rotationAreAnimating[i]) {
                pipes.get(i).setRotateStartTime(System.currentTimeMillis());

                rotationAreAnimating[i] = true;

            }
        }

        long time = System.currentTimeMillis() - pipes.get(0).getRotateStartTime();
        int speed = 15;


        int animationIndex = (int) ((time - speed) / speed);
        animationIndex = animationIndex < 0 ? 0 : animationIndex;
        animationIndex = animationIndex > 10 ? 10 : animationIndex;
        int j = 0;
        for (PipeModel pipe : pipes) {
            if (animationIndex < 10 && !pipe.isFrameRotatedAnimated(animationIndex)) {

                for (int i = 0; i < animationIndex; i++) {
                    if (!pipe.isFrameRotatedAnimated(i)) {

                        pipe.setFrameRotatedAnimated(i, true);
                        pipe.setRotate(pipe.getRotate() + (90 / 10));

                    }
                }
                pipe.setFrameRotatedAnimated(animationIndex, true);
                pipe.setRotate(pipe.getRotate() + (90 / 10));
                if (j % 2 == 0) {
                    animationIndex += 1;
                }

                j += 1;

            }

            if (animationIndex >= 10) {
                pipe.setRotateStartTime(0);

                pipe.setIsRotationAnimating(false);
                mainPipe.setIsRotationAnimating(false);
                mainPipe.setRotateStartTime(0);
                boolean[] rotationAreAnimating = { false, false };
                PipeView.setrotationAreAnimating(rotationAreAnimating);

            }
        }

    }

    private void auxRotateAnimation(Graphics g, PipeModel pipe, int x, int y) {
        if (!rotationAreAnimating[0]) {
            pipe.setRotateStartTime(System.currentTimeMillis());

            rotationAreAnimating[0] = true;
        }

        long time = System.currentTimeMillis() - pipe.getRotateStartTime();
        int speed = 15;


        int animationIndex = (int) ((time - speed) / speed);
        animationIndex = animationIndex < 0 ? 0 : animationIndex;
        animationIndex = animationIndex > 10 ? 10 : animationIndex;
        if (animationIndex < 10 && !pipe.isFrameRotatedAnimated(animationIndex)) {
            for (int i = 0; i < animationIndex; i++) {
                if (!pipe.isFrameRotatedAnimated(i)) {

                    pipe.setFrameRotatedAnimated(i, true);
                    pipe.setRotate(pipe.getRotate() + (90 / 10));

                }
            }
            pipe.setFrameRotatedAnimated(animationIndex, true);
            pipe.setRotate(pipe.getRotate() + (90 / 10));
            animationIndex += 1;

        }

        if (animationIndex >= 10) {
            
            pipe.setRotate(roundToNearest90(pipe.getRotate()));
        
            pipe.setRotate(roundToNearest90(pipe.getRotate()));
            pipe.setRotateStartTime(0);
            pipe.setIsRotationAnimating(false);
            boolean[] rotationAreAnimating = { false, false };
            PipeView.setrotationAreAnimating(rotationAreAnimating);
        }


    }
    private int roundToNearest90(int angle) {
        int remainder = angle % 360;
        if (remainder < 0) remainder += 360; 

        int nearestMultipleOf90 = (int) (Math.round(remainder / 90.0) * 90);

        return (nearestMultipleOf90 == 360) ? 0 : nearestMultipleOf90;
    }
    private void Animate(int frame, PipeModel pipe, int x, int y, Graphics g,
            ArrayList<BufferedImage> waterAnimationSelected) {
        AffineTransform at = new AffineTransform();
        at.translate(x * Constant.PIPE_SIZE + Constant.PIPE_SIZE / 2,
                y * Constant.PIPE_SIZE + Constant.PIPE_SIZE / 2);
        at.rotate(Math.toRadians(pipe.getAnimationRotation()));
        at.translate(-waterAnimationSelected.get(1).getWidth() / 2,
                -waterAnimationSelected.get(1).getHeight() / 2);
        if (pipe.isMirror()) {
            at.concatenate(AffineTransform.getScaleInstance(-1, 1));
            at.concatenate(
                    AffineTransform.getTranslateInstance(-waterAnimationSelected.get(frame).getWidth(), 0));

        }
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage img = waterAnimationSelected.get(frame);
        LookupTable lookupTable = pipe.getLookupTable();
        if (lookupTable != null) {
            LookupOp op = new LookupOp(lookupTable, null);
            img = op.filter(img, null);
        }
        g2d.drawImage(img, at, null);
    }

    private void auxAnim(Graphics g, PipeModel pipe, int x, int y) {
        if (!startAreAnimating) {
            for (PipeModel p : pipeChain) {
                if (p instanceof models.pipes.StartPipe) {
                    p.setAnimating(true);
                    p.setStartTime(System.currentTimeMillis());
                    p.setAnimationFrom(Direction.NORTH);
                }
            }
            startAreAnimating = true;
        }

        if (pipe.isEndAnimation()) {
            

            Animate(11, pipe, x, y, g, pipe.getWaterAnimation());

            if (endAnimationCondition() && animationEnded == false) {

                if (pipe instanceof models.pipes.EndPipe && animationEnded == false) {
                    animationEnded = true;
                }

            }
            

            
            

            diffuseAnimation(pipe);
            
        }

        if ((pipe.isAnimating()))

        {

            long time = System.currentTimeMillis() - pipe.getStartTime();

            var WaterAnimationSelected = pipe.getWaterAnimation();

            WaterAnimationSelected = waterAnimationSetupPipe(pipe);

            int speed = 25;

            if (time > speed * 12) {
                Animate(11, pipe, x, y, g, WaterAnimationSelected);
                pipe.setEndAnimation(true);
                pipe.setAnimating(false);
                

            } else {
                int animationIndex = (int) ((time - speed) / speed);
                if (animationIndex < 0) {
                    animationIndex = 0;
                } else if (animationIndex > 10) {
                    animationIndex = 11;
                }
                if (animationIndex != 0) {
                    Animate(animationIndex, pipe, x, y, g, WaterAnimationSelected);
                }

            }

        }
    }
    

    private void diffuseAnimation(PipeModel pipe) {
        int dir = 0;
        ArrayList<PipeModel> neighbours = PipeMap.getNeighbours(pipe);
        if (pipe.isEncapsulated()) {

            neighbours = PipeMap.getNeighbours(pipe.getEncapsulate());
        }

        for (PipeModel p : neighbours) {

            if (p != null) {
                

                if (p.getNbComponent() > 1) {

                    for (PipeModel pipeEncap : p.getPipeEncapsulated()) {
                        if (pipeChain.contains(p) && !pipeEncap.isAnimating() && pipeEncap.isConnected((dir + 2) % 4) && pipe.isConnected(dir)){
                                pipeEncap.setWaterColor(pipe.getWaterColor().addColor(pipeEncap.getWaterColor()));
                                if (!pipeEncap.isEndAnimation()) {
                                    pipeEncap.setAnimating(true);
                                    pipeEncap.setStartTime(System.currentTimeMillis());
                                    pipeEncap.setAnimationFrom(Direction.values()[dir].getOpposedDir());
                        
                        
                            }
                        }
                        
                    }

                }

                else {
                    p.setWaterColor(pipe.getWaterColor().addColor(p.getWaterColor()));

                    

                        

                    

                    if (pipeChain.contains(p) && !p.isAnimating() && !p.isEndAnimation()
                            && pipe.isConnected(dir)) {

                        p.setAnimating(true);
                        p.setStartTime(System.currentTimeMillis());

                        p.setAnimationFrom(Direction.values()[dir].getOpposedDir());
                    }

                }

            }

            dir++;

        }

    }

    private ArrayList<BufferedImage> waterAnimationSetupPipe(PipeModel pipe) {
        int extraRotation = 0;
        boolean mirror = false;
        var waterAnimationSelected = pipe.getWaterAnimation();
        switch (pipe.getType()) {
            case STRAIGHT:
                extraRotation = ((pipe.getAnimationFrom().ordinal() + 2) % 4) * 90;
                break;

            case CORNER:
                extraRotation = ((pipe.getAnimationFrom().ordinal() + 2) % 4) * 90;
                mirror = pipe.getRotate() == (extraRotation + 90) % 360;
                break;

            case T:
                extraRotation = pipe.getRotate();
                mirror = pipe.getAnimationFrom() == Direction.values()[pipe.getRotate() / 90].nextDirection();
                if (mirror || pipe.getAnimationFrom() == Direction.values()[pipe.getRotate() / 90].nextDirection(-1)) {
                    waterAnimationSelected = pipe.getWaterAnimation2();
                }
                break;
            default:
                extraRotation = pipe.getRotate();
                break;
        }
        pipe.setAnimationRotation(extraRotation);
        pipe.setMirror(mirror);

        return waterAnimationSelected;
    }

    public void drawAsset(PipeModel pipe, int x, int y, Graphics g) {
        BufferedImage img = pipe.getImage();
        img = resizeImage(img, Constant.PIPE_SIZE, Constant.PIPE_SIZE);
        AffineTransform at = new AffineTransform();
        at.translate(x * Constant.PIPE_SIZE + Constant.PIPE_SIZE / 2, y * Constant.PIPE_SIZE + Constant.PIPE_SIZE / 2);
        at.rotate(Math.toRadians(pipe.getRotate()));
        at.translate(-img.getWidth() / 2, -img.getHeight() / 2);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(img, at, null);

    }

    private boolean endAnimationCondition() {
        for (PipeModel p : pipeChain) {
            if (p.getNbComponent() > 1) {
                //int n = p.getCounter();
                ArrayList<PipeModel> pipes = p.getPipeEncapsulated();

                for (PipeModel pipe : pipes) {
                    if (pipe.isInPath() && !pipe.isEndAnimation()) {
                        return false;
                    }
                }
             
            }

            else if (!p.isEndAnimation()) {
                return false;
            }
        }
        return true;
    }

    public void importArray() {
        pipeChain = WinCondition.getPassedPipes();

    }

    public static void resetAll() {
        pipeChain = new ArrayList<>();
        startAreAnimating = false;
        animationEnded = false;
    }

    public static boolean isAnimationEnded() {
        return animationEnded;
    }

    public static void setrotationAreAnimating(boolean[] rotationAreAnimating) {
        PipeView.rotationAreAnimating = rotationAreAnimating;
    }
}
