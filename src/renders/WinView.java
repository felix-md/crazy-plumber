package renders;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import config.Hints;
import config.LevelLoader;
import config.Modes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;


import gui.Game;
import config.PipeMap;
import inputs.EventManager;

public class WinView extends View {
    
    private Game game;

    private EventManager eventManager;
    private JButton menuButton, levelsButton, nextButton;
    private JPanel buttonsPanel;
    private BufferedImage background;
    private JLabel backgroundLabel, starLabel;
    private JTextPane topBar, textPane;
    private GridBagConstraints topBarConstraints, starConstraints, textPaneConstraints, buttonsConstraints;
    private Insets topBarInsets, starInsets, textPaneInsets, buttonsInsets;

    public WinView(Game game) {
        super(game);
        this.game = game;
        eventManager = new EventManager(this);
        addKeyListener(eventManager);
        addMouseListener(eventManager);
        initializeFont();

    }

    public void initializeComponents() {
        setLayout(new BorderLayout());

        initializeInsets();
        initializeConstraints();

        int widthButton= 300;
        int heightButton= 70;
        initializeButtons(widthButton, heightButton);

        int widthTopBar= 300;
        int heightTopBar= 50;
        int sizeTextTopBar= 30;
        makeTopBar(widthTopBar, heightTopBar, sizeTextTopBar);

        int widthStarLabel= 400;
        int heightStarLabel= 150;
        makeStarLabel(widthStarLabel, heightStarLabel);

        int widthText= 400;
        int heightText= 100;
        int sizeText= 20;
        makeTextPane(widthText, heightText, sizeText);

        int vgrap= 25;
        int heightPanel= this.getButtonsList().size()*heightButton+ (this.getButtonsList().size()-1)*vgrap;
        makeButtonsPanel(this.getButtonsList().size(), 1, 0, vgrap, widthButton, heightPanel);

        int widthLabel= Math.max(Math.max(widthButton, widthTopBar), Math.max(widthStarLabel, widthText));
        int heightLabel= heightButton+heightTopBar+heightStarLabel+heightText;
        makeBackgroundLabel(widthLabel+150, heightLabel+350);

        add(backgroundLabel, BorderLayout.CENTER);
        
    }

    private void initializeInsets() {
        topBarInsets= new Insets(10, 0, 25, 70);
        starInsets= new Insets(20, 0, 15, 50);
        textPaneInsets= new Insets(10,0, 10, 50);
        buttonsInsets= new Insets(5, 0, 10, 50); 
    }

    private void initializeConstraints() {
        topBarConstraints= setConstraints(0, GridBagConstraints.NORTH, topBarInsets, GridBagConstraints.NONE, GridBagConstraints.NONE);
        starConstraints= setConstraints(1, GridBagConstraints.CENTER, starInsets, GridBagConstraints.NONE, GridBagConstraints.NONE);
        textPaneConstraints= setConstraints(2, GridBagConstraints.CENTER, textPaneInsets, GridBagConstraints.NONE, GridBagConstraints.NONE);
        buttonsConstraints= setConstraints(3, GridBagConstraints.SOUTH, buttonsInsets, GridBagConstraints.NONE, GridBagConstraints.VERTICAL);
    } 

    protected void initializeButtons(int width, int height) {
        nextButton = makeButton("NEXT",22);
        levelsButton = makeButton("LEVELS",22);
        menuButton = makeButton("MENU",22);

        nextButton.addActionListener(e -> {LevelLoader.getNextLevel(); 
            PipeMap.createMapFromLevel(LevelLoader.getSelectedLevel());
            Hints.resetHints();
            game.switchGameView();});
        levelsButton.addActionListener(e -> game.switchLevelView(Modes.getActualMode(),LevelLoader.getActualDifficulty()));
        menuButton.addActionListener(e -> game.switchMenuView());
    
        this.getButtonsList().add(nextButton);
        this.getButtonsList().add(levelsButton);
        this.getButtonsList().add(menuButton);
        
        addButtonsIcons(this.getButtonsList(),normalButtonsIcons, width, height);

    }

    private void makeButtonsPanel(int nbButtons, int cols, int hgrap, int vgrap, int width, int height) {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(nbButtons, cols, hgrap, vgrap));
        buttonsPanel.setPreferredSize(new  Dimension(width, height));
        buttonsPanel.setBackground(new Color(78, 74, 78));

        for(JButton b : this.getButtonsList()){
            buttonsPanel.add(b);
        }
    }

    private void makeBackgroundLabel(int width, int height) {
        this.background= resizeImage(winBackground, width, height);
        backgroundLabel= new JLabel(new ImageIcon(background));
        backgroundLabel.setLayout(new GridBagLayout());
        backgroundLabel.setPreferredSize(new Dimension(width, height));

        backgroundLabel.add(topBar, topBarConstraints);
        backgroundLabel.add(starLabel, starConstraints);
        backgroundLabel.add(textPane, textPaneConstraints);
        backgroundLabel.add(buttonsPanel, buttonsConstraints);
    }

    private void makeTopBar(int width, int height, int size) {
        topBar= new JTextPane();
        topBar.setPreferredSize(new Dimension(width, height));
        topBar.setLayout(new BorderLayout());
        topBar.setBackground(new Color(78, 74, 78));
        topBar= makeText("level complete", size);
    }

    private void makeStarLabel(int width, int height) {
        starLabel= new JLabel();
        starLabel.setPreferredSize(new Dimension(width, height));
        starLabel.setLayout(new BorderLayout());
        starLabel.setBackground(new Color(89, 86, 82));
        starLabel.add(new JLabel() {
            @Override
            public void paintComponent(Graphics g) {  
                super.paintComponent(g);
                drawStar(g);  
            }
        }, BorderLayout.CENTER);

    }

    private void makeTextPane(int width, int height, int size) {
        textPane= new JTextPane();
        textPane.setPreferredSize(new Dimension(width, height));
        textPane.setLayout(new BorderLayout());
        //textPane.setBackground(new Color(78, 74, 78));

        int time= Modes.getTime();
        int hours= time/3600;
        int minutes= (time%3600)/60;
        int seconds= time%60;
        String text= "time : "+ String.format("%02d:%02d:%02d", hours, minutes, seconds)
            +"\nmove : "+ Modes.getLimitedMove().getNbMove()
            +"\nscore : "+ Modes.getScore();
        textPane= makeText(text, size); 
    }

    private void star(Graphics g, int index, int size, int x, int y) {
        if (index<Modes.getStar()) {
            g.drawImage(resizeImage(starIcons.get(0), size, size), x, y, null);
        }
        else {
            g.drawImage(resizeImage(starIcons.get(1), size, size), x, y, null);
        }
    }

    private void drawStar(Graphics g) {
        int grap=130;
        int small= 70, big= 115;
        int x= (starLabel.getWidth()-big)/2;
        int y= starLabel.getHeight();
        star(g, 0, small, x-grap+(big-small)/2, y-small);
        star(g, 1, big, x, y-big);
        star(g, 2, small, x+grap+(big-small)/2, y-small);
    }

    public void updateNextButton() {
        if (LevelLoader.hasNextLevel()) {
            nextButton.setEnabled(true);
        } 
        else {
            nextButton.setEnabled(false);
        }
    }


    
}
