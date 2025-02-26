package renders;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import config.LevelLoader;
import config.Modes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import java.awt.image.BufferedImage;


import gui.Game;
import inputs.EventManager;

public class MenuView extends View{

    private Game game;
    private EventManager eventManager;
    private JButton playButton, achievementButton, rulesButton, quitButton;
    private JPanel buttonsPanel;
    private BufferedImage background;
    private JLabel backgroundLabel;


    public MenuView(Game game) {
        super(game);
        this.game = game;
        eventManager = new EventManager(this);
        addKeyListener(eventManager);
        addMouseListener(eventManager);
        initializeFont();
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
        playButton = makeButton("PLAY",22);
        achievementButton= makeButton("achievement",22);
        rulesButton = makeButton("rules",22);
        quitButton = makeButton("QUIT",22);


        playButton.addActionListener(e -> game.switchLevelView(Modes.getActualMode(), LevelLoader.getActualDifficulty()));
        achievementButton.addActionListener(e -> game.switchAchievementView());
        rulesButton.addActionListener(e -> game.switchRulesView());
        quitButton.addActionListener(e -> System.exit(0));

        this.getButtonsList().add(playButton);
        this.getButtonsList().add(achievementButton);
        this.getButtonsList().add(rulesButton);
        this.getButtonsList().add(quitButton);

        addButtonsIcons(this.getButtonsList(), normalButtonsIcons, width, height);
    }

    private void makeButtonsPanel(int nbButtons, int cols, int hgrap, int vgrap, int width, int height) {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(nbButtons, cols, hgrap, vgrap));
        buttonsPanel.setPreferredSize(new Dimension(width, height));
        buttonsPanel.setBackground(new Color(89, 86, 82));//pour verif le focus

        for(JButton b : this.getButtonsList()){
            buttonsPanel.add(b);
        }
    }

    private void makeBackgroundLabel(int width, int height) {
        this.background= resizeImage(backgroundMenuImg, width, height);
        backgroundLabel= new JLabel(new ImageIcon(background));
        backgroundLabel.setLayout(new GridBagLayout());

        backgroundLabel.add(buttonsPanel, new GridBagConstraints());
    }
    
}

    