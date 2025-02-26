package renders;

import javax.swing.JButton;

import java.awt.Color;

import java.awt.Font;
import java.awt.FontMetrics;


import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Point;

import config.Constant;
import config.Hints;
import config.LevelLoader;
import config.Modes;
import config.PipeMap;

import config.Modes.Mode;
import gui.Game;
import inputs.EventManager;
import models.PipeModel;

import java.awt.event.MouseEvent;



public class GameView extends View {

    private PipeView pipeView;
    private ScoreView scoreView;
    private PipeMap map;
    private Game game;
    private EventManager eventManager;
    private JButton animationButton = makeButton(null,22);
    private JButton menuButton = makeButton(null,22);
    private JButton undoButton= makeButton(null,22);
    private JButton redoButton= makeButton(null,22);
    private JButton restartButton = makeButton(null,22);
    private JButton hintButton = makeButton(null,22);
    private JButton levelsButton = makeButton(null,22);
    private boolean isButtonsAdded=false;
    private static boolean isAnimating = false;

    private final int heightTop= 80;
    private final int sizeButton= 70;
    private final int pipeMapY= Constant.GAP_FROM_SIDE_Y *Constant.PIPE_SIZE -20;

    public GameView(Game game) {
        super(game);
        this.game = game;
        eventManager = new EventManager(this);
        addKeyListener(eventManager);
        addMouseListener(eventManager);
        addMouseMotionListener(eventManager);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // pipeView.drawPipe(g, new PipeT(),100,100);
        drawMap(g, pipeView);
        scoreView.drawRemaining(g);
    }

    @Override
    protected void initializeComponents() {
        setLayout(new BorderLayout());

        this.pipeView = new PipeView(game);
        this.scoreView= new ScoreView(this,game,this.heightTop,this.sizeButton);

        map = new PipeMap();
        PipeMap.createMapFromLevel(LevelLoader.getSelectedLevel());
        // PipeMap.createMap();
        map.afficherMap();
    }

    public void drawMap(Graphics g, PipeView pipeView){
        if(!isButtonsAdded){
            initializeButtons(sizeButton, sizeButton);
            isButtonsAdded=true;
        }
        hintButton.setEnabled( !Hints.isZeroHint() && !isAnimating);
        redoButton.setEnabled(!isAnimating);
        undoButton.setEnabled(!isAnimating);
        restartButton.setEnabled(!isAnimating);

        int img_i = Constant.GAP_FROM_SIDE_X;
        int img_j = Constant.GAP_FROM_SIDE_Y;
        g.setColor(new Color(38, 34, 30));
        g.fillRect(img_i * Constant.PIPE_SIZE - 20, img_j * Constant.PIPE_SIZE - 20,
                Constant.BOARD_SIZE * Constant.PIPE_SIZE + 40, Constant.BOARD_SIZE * Constant.PIPE_SIZE + 40);
        
        for (int i = 0; i < PipeMap.getPipeMap().length; i++) {
            for (int j = 0; j < PipeMap.getPipeMap()[0].length; j++) {
                
                if(PipeMap.getPipe(i, j).getNbComponent() >1){
                    Boolean background = true;
                    for(PipeModel p : PipeMap.getPipe(i, j).getPipeEncapsulated()){
                        pipeView.drawPipe(g,p,img_i,img_j,background,isAnimating);
                        background = false;
                    }
                   
                }
                else{
                    pipeView.drawPipe(g,PipeMap.getPipe(i, j),img_i,img_j,true,isAnimating);
                }

                img_i += 1;

            }
            img_i = Constant.GAP_FROM_SIDE_X;
            img_j += 1;
        }
    }

    protected void initializeButtons(int width, int height){   
        setLayout(null);
        
        this.getButtonsList().add(hintButton);
        this.getButtonsList().add(undoButton);
        this.getButtonsList().add(redoButton);
        this.getButtonsList().add(restartButton);
        this.getButtonsList().add(menuButton);
        this.getButtonsList().add(levelsButton);
        this.getButtonsList().add(animationButton);

        int locationGap=this.getButtonsList().size()-4;
        int indice=0;

        for(JButton b : this.getButtonsList()){
            b.setIcon(makeIcon(gameButtonIcons, 0+indice, width, height));
            b.setPressedIcon(makeIcon(gameButtonIcons, 1+indice, width, height));
            if(b==animationButton){
                b.setBounds(20, this.heightTop, width, height);
            }
            else if (b==menuButton){
                b.setBounds(Constant.GAME_WIDTH -20 -width, this.heightTop, width, height);
                locationGap--;
            }
            else if(b==levelsButton){
                b.setBounds(Constant.GAME_WIDTH -40 -width*2, this.heightTop, width, height);
                locationGap--;
            }
            else {
                b.setBounds(Constant.GAME_WIDTH -20 -width, this.pipeMapY+ (height+24)*locationGap, width, height);
                locationGap--;
            }
            add(b);
            indice += 2;
        }
        animationButton.addActionListener(e -> {

            if(!isAnimating){
                
            
                if (PipeMap.isPlayerWon())
                    isAnimating = true;
                    
                PipeMap.resetAnimationAttributes();
                PipeView.resetAll();
                Modes.stopTimer();
                pipeView.importArray();
            }
        });
        menuButton.addActionListener(e -> game.switchMenuView());
        levelsButton.addActionListener(e -> game.switchLevelView(Modes.getActualMode(), LevelLoader.getActualDifficulty()));
        redoButton.addActionListener(e -> Modes.redoMove());
        undoButton.addActionListener(e -> Modes.undoMove());
        restartButton.addActionListener(e -> Modes.restart());
        hintButton.addActionListener(e -> Hints.activateHint());
    }


    @Override
    public void handleMousePressed(MouseEvent e) {
        Point p = getMousePoint(e.getX(), e.getY());

        PipeMap.rotateOnClick((int)p.getY(), (int)p.getX());
    }
    @Override
    public void handleMouseReleased(MouseEvent e) {
        Point p = getMousePoint(e.getX(), e.getY());
        
        if(PipeMap.onMatrix((int)p.getY(), (int)p.getX()) && ! PipeMap.isEmpty((int)p.getY(), (int)p.getX())){
            game.setCursor(cursors.get(1));
        }
    }

    @Override
    public void handleMouseMoved(MouseEvent e) {
        Point p = getMousePoint(e.getX(), e.getY());
        if(PipeMap.onMatrix((int)p.getY(), (int)p.getX()) && ! PipeMap.isEmpty((int)p.getY(), (int)p.getX())){
            game.setCursor(cursors.get(1));
        }
        else{
            game.setCursor(cursors.get(0));
        }
    }

    public Point getMousePoint(int x, int y){

        int caseX = (int) Math.round(x / 64);
        int caseY = (int) Math.round(y / 64);

        return new Point(caseX, caseY);
    }

    public void addAssets() {
        pipeView.addAssets();
    }

    public static boolean isAnimating() {
        return isAnimating;
    }

    public static void setAnimating(boolean isAnimating) {
        GameView.isAnimating = isAnimating;
    }

    public void update() {
        if (PipeView.isAnimationEnded()) {
            try {
                Thread.sleep(1000);
                PipeView.resetAll();
                game.switchWinView();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (Modes.isLost()) {
            game.switchLoseView();
        }
    }

}
