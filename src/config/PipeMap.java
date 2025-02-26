package config;

import models.Direction;
import models.PipeModel;

import renders.GameView;
import renders.PipeView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import models.WaterColor;

public class PipeMap {
    private static PipeModel[][] pipeMap = new PipeModel[Constant.BOARD_SIZE][Constant.BOARD_SIZE];
    private static int[] start = { 0, 0 };
    private static int[] end = { Constant.BOARD_SIZE - 1, Constant.BOARD_SIZE - 1 };
    private static PipeModel startPipe;
    private static Stack<PipeModel> undoStack= new Stack<>(); // pour enregistrer les moves (pour annuler)
    private static Stack<PipeModel> redoStack= new Stack<>(); // pour enregistrer les moves (pour les refaire)

    public static PipeModel getPipe(int x, int y) {
        return pipeMap[x][y];
    }

    public static void resetMap() {
        for (int i = 0; i < Constant.BOARD_SIZE; i++) {
            for (int j = 0; j < Constant.BOARD_SIZE; j++) {
                pipeMap[i][j] = null;
            }
        }

    }

    public static void createMapFromLevel(int levelNumber) {

        // file is at ./levels/level_1.txt



        var levels = LevelLoader.getLevels();
        String[] lines = levels.get(levelNumber - 1).split("\n");

        int colorValue = 0; 
   

        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                
                switch (lines[i].charAt(j)) {
                    case 's':
                        pipeMap[i][j] = new models.pipes.StartPipe();
                        pipeMap[i][j].setWaterColor(WaterColor.values()[colorValue]);
                        colorValue = (colorValue + 1) % 2;

                        break;

                    case 'e':
                        pipeMap[i][j] = new models.pipes.EndPipe();

                        break;
                    case 'S':
                        pipeMap[i][j] = new models.pipes.PipeStraight();

                        break;
                    case 'C':
                        pipeMap[i][j] = new models.pipes.PipeCorner();

                        break;
                    case 'D':
                        pipeMap[i][j] = new models.pipes.PipeDoubleCorner();

                        break;
                    case 'T':
                        pipeMap[i][j] = new models.pipes.PipeT();

                        break;
                    case 'n':
                        pipeMap[i][j] = new models.pipes.EmptyPipe();
                        break;
                    case 'P':
                        pipeMap[i][j] = new models.pipes.PipePlus();

                        break;
                    case 'R':
                        pipeMap[i][j] = new models.pipes.StartPipe();
                        pipeMap[i][j].setWaterColor(WaterColor.RED);
                        break;
                    case 'B':
                        pipeMap[i][j] = new models.pipes.StartPipe();
                        pipeMap[i][j].setWaterColor(WaterColor.BLUE);
                        break;

                }

                pipeMap[i][j].setX(i);
                pipeMap[i][j].setY(j);
                Random rand = new Random();

                int n = rand.nextInt(4);
                pipeMap[i][j].setRotate(n * 90);

            }
        }
        Hints.initHints();
    }

    public static ArrayList<PipeModel> getStartedPipes() {
        ArrayList<PipeModel> startedPipes = new ArrayList<PipeModel>();
        for (int i = 0; i < Constant.BOARD_SIZE; i++) {
            for (int j = 0; j < Constant.BOARD_SIZE; j++) {
                if (pipeMap[i][j] instanceof models.pipes.StartPipe) {
                    startedPipes.add(pipeMap[i][j]);
                }
            }
        }
        return startedPipes;
    }

    public static PipeModel getStartPipe() {
        return startPipe;
    }

    public static void createMap() {
        pipeMap[0][0] = new models.pipes.StartPipe();
        pipeMap[0][0].setX(0);
        pipeMap[0][0].setY(0);
        startPipe = pipeMap[0][0];
        pipeMap[Constant.BOARD_SIZE - 1][Constant.BOARD_SIZE - 1] = new models.pipes.EndPipe();
        pipeMap[Constant.BOARD_SIZE - 1][Constant.BOARD_SIZE - 1].setX(Constant.BOARD_SIZE - 1);
        pipeMap[Constant.BOARD_SIZE - 1][Constant.BOARD_SIZE - 1].setY(Constant.BOARD_SIZE - 1);

        for (int i = 0; i < Constant.BOARD_SIZE; i++) {
            for (int j = 0; j < Constant.BOARD_SIZE; j++) {
                if (pipeMap[i][j] == null) {
                    Random rand = new Random();
                    int n = rand.nextInt(5);
                    switch (n) {
                        case 0:
                            pipeMap[i][j] = new models.pipes.PipeStraight();
                            pipeMap[i][j].setX(i);
                            pipeMap[i][j].setY(j);
                            break;
                        case 1:
                            pipeMap[i][j] = new models.pipes.PipeCorner();
                            pipeMap[i][j].setX(i);
                            pipeMap[i][j].setY(j);
                            break;
                        case 2:
                            pipeMap[i][j] = new models.pipes.PipeDoubleCorner();
                            pipeMap[i][j].setX(i);
                            pipeMap[i][j].setY(j);
                            break;
                        case 3:
                            pipeMap[i][j] = new models.pipes.PipeT();
                            pipeMap[i][j].setX(i);
                            pipeMap[i][j].setY(j);
                            break;
                        case 4:
                            pipeMap[i][j] = new models.pipes.PipePlus();
                            pipeMap[i][j].setX(i);
                            pipeMap[i][j].setY(j);
                            break;

                    }
                }
            }
        }
    }

   

    public static boolean isPlayerWon() {
        
        resetAll();

        return WinCondition.isWin();

    }

    public static void resetAll() {
        for (int i = 0; i < Constant.BOARD_SIZE; i++) {
            for (int j = 0; j < Constant.BOARD_SIZE; j++) {

                if (pipeMap[i][j] instanceof models.pipes.StartPipe) {
                    pipeMap[i][j].setInPath(true);
                    pipeMap[i][j].setPassed(true);
                    pipeMap[i][j].setCounter(1);
                } else {
                    pipeMap[i][j].setInPath(false);
                    pipeMap[i][j].setPassed(false);
                    pipeMap[i][j].setCounter(0);
                    pipeMap[i][j].setWaterColor(WaterColor.NONE);
                }
                
            }
        }
    }

    public static void rotateOnClick(int x, int y) {

        if (onMatrix(x, y) && !GameView.isAnimating() && pipeMap[x - Constant.GAP_FROM_SIDE_Y][y - Constant.GAP_FROM_SIDE_X].isCanBeRotated()) {
            resetAll();
            undoStack.add(PipeMap.getPipe(x-Constant.GAP_FROM_SIDE_Y, y-Constant.GAP_FROM_SIDE_X));
            redoStack.clear();
            pipeMap[x - Constant.GAP_FROM_SIDE_Y][y - Constant.GAP_FROM_SIDE_X].rotate();
            
        }
    }

    public static boolean isEmpty(int x, int y){
        return (pipeMap[x - Constant.GAP_FROM_SIDE_Y][y - Constant.GAP_FROM_SIDE_X] instanceof models.pipes.EmptyPipe);
    }

    public static boolean onMatrix(int x, int y) {
        return x >= Constant.GAP_FROM_SIDE_Y && x < Constant.GAP_FROM_SIDE_Y + Constant.BOARD_SIZE &&
                y >= Constant.GAP_FROM_SIDE_X && y < Constant.GAP_FROM_SIDE_X + Constant.BOARD_SIZE;
    }

    public void afficherMap() {
        for (int i = 0; i < Constant.BOARD_SIZE; i++) {
            for (int j = 0; j < Constant.BOARD_SIZE; j++) {
                System.out.print(pipeMap[i][j].getType().toString().substring(0, 1));

            }
            System.out.println();
        }
    }

    public static ArrayList<PipeModel> getNeighbours(PipeModel pipe) {

        ArrayList<PipeModel> list = new ArrayList<PipeModel>();
        for (int i = 0; i < 4; i++) {
            list.add(getNeighbour(pipe, Direction.values()[i]));
        }

        return list;
    }

    public static PipeModel getNeighbour(PipeModel pipe, Direction dir) {

        try {
            switch (dir) {
                case NORTH:
                    return pipeMap[pipe.getX() - 1][pipe.getY()];
                case SOUTH:
                    return pipeMap[pipe.getX() + 1][pipe.getY()];
                case EAST:
                    return pipeMap[pipe.getX()][pipe.getY() + 1];
                case WEST:
                    return pipeMap[pipe.getX()][pipe.getY() - 1];
                default:
                    return null;
            }

        } catch (Exception outOfBoundsException) {
            return null;
        }

    }

    public static PipeModel[][] getPipeMap() {
        return pipeMap;
    }

    public static int[] getStart() {
        return start;
    }

    public static int[] getEnd() {
        return end;
    }

    public static void resetAnimationAttributes() {
        for (int i = 0; i < Constant.BOARD_SIZE; i++)
            for (int j = 0; j < Constant.BOARD_SIZE; j++) {
                pipeMap[i][j].setEndAnimation(false);
                pipeMap[i][j].setAnimating(false);

            }
    }

    public static void resetRotatePipes() {
        for (int i = 0; i < Constant.BOARD_SIZE; i++)
            for (int j = 0; j < Constant.BOARD_SIZE; j++) {
                
                
                Random rand = new Random();
                int n = rand.nextInt(4);
                pipeMap[i][j].setRotate(n * 90);
                pipeMap[i][j].setRotateStartTime(0);
                pipeMap[i][j].setIsRotationAnimating(false);

            }
    }

    public static void resetRotatePipesForRestart(){
        boolean[] rotationAreAnimating = { false, false };
        PipeView.setrotationAreAnimating(rotationAreAnimating);
   
   
        for (int i = 0; i < Constant.BOARD_SIZE; i++) {
            for (int j = 0; j < Constant.BOARD_SIZE; j++) {

                if (pipeMap[i][j] instanceof models.pipes.StartPipe) {
                    pipeMap[i][j].setInPath(true);
                    pipeMap[i][j].setPassed(true);
                    pipeMap[i][j].setCounter(1);
                } else {
                    pipeMap[i][j].setInPath(false);
                    pipeMap[i][j].setPassed(false);
                    pipeMap[i][j].setCounter(0);
                    pipeMap[i][j].setWaterColor(WaterColor.NONE);
                    pipeMap[i][j].setFrameRotatedAnimated( new boolean[10]);
                }
                pipeMap[i][j].setCanBeRotated(true);
            }
        }
        resetRotatePipes();
    }
        
    public static Stack<PipeModel> getUndoStack() {
        return undoStack;
    }

    public static Stack<PipeModel> getRedoStack() {
        return redoStack;
    }

}
