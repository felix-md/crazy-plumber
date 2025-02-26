package renders;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import config.Constant;
import config.LevelLoader;
import config.Modes;
import config.Modes.Mode;
import config.PipeMap;
import gui.Game;

public class ScoreView implements Graphic{

    private Game game;
    private GameView gameView;
    private int sizeButton,heightTop;
    
    public ScoreView(GameView gameView, Game game, int sizeButton, int heightTop) {
        this.gameView=gameView;
        this.game = game;
        this.sizeButton=sizeButton;
        this.heightTop=heightTop;
    }

    public void drawRemaining(Graphics g){
        g.setFont(new Font("GravityBold8",Font.TRUETYPE_FONT,20));
        g.setColor(new Color(15, 6, 25));

        Mode actualMode= Modes.getActualMode();
        int heightBar=55;
        int heightPane=70;
    
        switch (actualMode) {
            case LIMITED_MOVE:
                this.drawRemainingBar(g, "remaining move : ", heightBar, actualMode);
                break;
            case TIMER:
                this.drawRemainingBar(g, "remaining time : ", heightBar, actualMode);
                break;
            default:
                break;
        }
        this.drawBarScore(g, heightBar, this.heightTop+(this.sizeButton-heightBar)/2);
        this.drawPane(g, heightPane, this.heightTop+(this.sizeButton-heightPane*2)/2);
    }

    private void drawPane(Graphics g, int height, int posY) {
        g.setFont(new Font("GravityBold8", Font.TRUETYPE_FONT, 25));
        FontMetrics fontMetrics= g.getFontMetrics();
    
        String actualDifficulty= LevelLoader.getActualDifficulty().toString();
        int difficultyWidth= fontMetrics.stringWidth(actualDifficulty);

        String actualLevel= Integer.toString(LevelLoader.getSelectedLevel());
        int levelWidth= fontMetrics.stringWidth(actualLevel);

        int paneWidth= difficultyWidth+100;
        int middlePaneX= Constant.GAME_WIDTH/2;

        g.drawImage(gameView.pane, middlePaneX-paneWidth/2, posY, paneWidth, height*2, null);
        g.drawString(actualDifficulty, middlePaneX-difficultyWidth/2, posY+(height+10)*3/4);
        g.drawString(actualLevel, middlePaneX-levelWidth/2, posY+height*3/2);
    }

    private void drawBar(Graphics g, int width, int height, String str, int posX, int posY) {
        int size=19;
        int grapX= 50;
        g.setFont(new Font("GravityBold8", Font.TRUETYPE_FONT, size));
        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth= fontMetrics.stringWidth(str);
        g.drawImage(gameView.barreImg, posX, posY, width+grapX, height, null);
        g.drawString(str, posX+(width-textWidth+grapX)/2, posY+height-size+1);
    }

    private void drawBarScore (Graphics g, int height, int posY) {
        FontMetrics fontMetrics = g.getFontMetrics();
        String score= "Score : "+Integer.toString(Modes.getScore());
        String maxScore= "Score : "+Integer.toString(100); // pour que la longeur de la barre de change pas
        int scoreLength= fontMetrics.stringWidth(maxScore);
        this.drawBar(g, scoreLength, height, score, 40 +this.sizeButton, posY); // score
    }
    private void drawRemainingBar(Graphics g, String str, int height, Mode actualMode) {
        FontMetrics fontMetrics = g.getFontMetrics();
        String remaining= str+ Integer.toString(Modes.getRemaining());
        String maxRemaining= Integer.toString(Modes.getMaxRemaining()); // pour que la longeur de la barre de change pas
        int remainingLength= fontMetrics.stringWidth(str+maxRemaining);
        int remainingX= Constant.GAP_FROM_SIDE_X*Constant.PIPE_SIZE-20;
        int remainingY= Constant.GAP_FROM_SIDE_Y*Constant.PIPE_SIZE+30 +PipeMap.getPipeMap().length* Constant.PIPE_SIZE;
        this.drawBar(g, remainingLength, height, remaining, remainingX, remainingY); // remaining
    }
}
