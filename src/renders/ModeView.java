package renders;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import config.LevelLoader;
import config.Modes;
import config.Modes.Mode;
import gui.Game;
import inputs.EventManager;

public class ModeView extends View {

    private Game game;
    private EventManager eventManager;
    private JButton normalButton,limitedMoveButton, timerButton, backButton;
    private JPanel  buttonsPanel; //modePanel pour selectionner le mode, et buttonsPanel pour tous les boutons (+ backButton)
    private BufferedImage background;
    private JLabel backgroundLabel;


    public ModeView (Game game) {
        super(game);
        this.game = game;
        eventManager = new EventManager(this);
        addKeyListener(eventManager);
        addMouseListener(eventManager);
    }

    @Override
    protected void initializeComponents() {
        setLayout(new BorderLayout());

        int widthButton= 275;
        int heightButton= 60;
        initializeButtons(widthButton, heightButton);

        int vgrap= 70;
        int heightPanel= this.getButtonsList().size()*heightButton+ (this.getButtonsList().size()-1)*vgrap;
        makeButtonsPanel(this.getButtonsList().size(), 1, 0, vgrap, widthButton, heightPanel);
        makeBackgroundLabel(widthButton+150, heightPanel+150);
        add(backgroundLabel, BorderLayout.CENTER);
    }

    protected void initializeButtons(int width, int height){
        this.normalButton= makeButton("NORMAL",22);
        this.normalButton.addActionListener(e -> {
            Modes.setActualMode(Mode.NORMAL);;
            game.switchLevelView(Modes.getActualMode(), LevelLoader.getActualDifficulty());
        });
        this.getButtonsList().add(normalButton);

        this.limitedMoveButton= makeButton("LIMITED MOVE",22);
        this.limitedMoveButton.addActionListener(e -> {
            Modes.setActualMode(Mode.LIMITED_MOVE);
            game.switchLevelView(Modes.getActualMode(), LevelLoader.getActualDifficulty());
        });
        this.getButtonsList().add(limitedMoveButton);

        this.timerButton= makeButton("TIMER",22);
        this.timerButton.addActionListener(e -> {
            Modes.setActualMode(Mode.TIMER);;
            game.switchLevelView(Modes.getActualMode(), LevelLoader.getActualDifficulty());
        });
        this.getButtonsList().add(timerButton);

        this.backButton= makeButton("BACK",22);
        this.backButton.addActionListener(e -> game.switchLevelView(Modes.getActualMode(), LevelLoader.getActualDifficulty()));
        this.getButtonsList().add(backButton);

        addButtonsIcons(this.getButtonsList(),normalButtonsIcons, width, height);

    }

    private void makeButtonsPanel(int nbButtons, int cols, int hgrap, int vgrap, int width, int height) {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(nbButtons, cols, hgrap, vgrap));
        buttonsPanel.setPreferredSize(new  Dimension(width, height));
        buttonsPanel.setBackground(new Color(89, 86, 82));

        for (JButton b : this.getButtonsList()){
            buttonsPanel.add(b);
        }
        buttonsPanel.add(backButton);
    }

    private void makeBackgroundLabel(int width, int height) {
        this.background= resizeImage(backgroundMenuImg, width, height);
        backgroundLabel= new JLabel(new ImageIcon(background));
        backgroundLabel.setLayout(new GridBagLayout());

        backgroundLabel.add(buttonsPanel);
    }

    
}
