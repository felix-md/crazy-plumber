package renders;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


import config.Modes;
import config.LevelLoader;
import config.LevelLoader.difficulty;

import gui.Game;
import inputs.EventManager;

import java.awt.image.BufferedImage;

public class DifficultyView extends View{

    private Game game;
    private EventManager eventManager;
    private JButton easyButton, mediumButton, hardButton, backButton;
    private JPanel buttonsPanel;
    private BufferedImage background;
    private JLabel backgroundLabel;

    public DifficultyView(Game game) {
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
        easyButton = makeButton("EASY",22);
        mediumButton = makeButton("MEDIUM",22);
        hardButton = makeButton("HARD",22);
        backButton= makeButton("BACK",22);

        easyButton.addActionListener(e -> game.switchLevelView(Modes.getActualMode(),difficulty.EASY));
        mediumButton.addActionListener(e -> game.switchLevelView(Modes.getActualMode(),difficulty.MEDIUM));
        hardButton.addActionListener(e -> game.switchLevelView(Modes.getActualMode(),difficulty.HARD));
        backButton.addActionListener(e -> game.switchLevelView(Modes.getActualMode(), LevelLoader.getActualDifficulty()));

        this.getButtonsList().add(easyButton);
        this.getButtonsList().add(mediumButton);
        this.getButtonsList().add(hardButton);
        this.getButtonsList().add(backButton);

        addButtonsIcons(this.getButtonsList(), normalButtonsIcons, width, height);
    }

    private void makeButtonsPanel(int nbButton, int cols, int hgrap, int vgrap, int width, int height) {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(nbButton, cols, hgrap, vgrap));
        buttonsPanel.setPreferredSize(new Dimension(width, height));
        buttonsPanel.setBackground(new Color(89, 86, 82));

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