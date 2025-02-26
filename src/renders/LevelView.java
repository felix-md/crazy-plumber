package renders;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import config.LevelLoader;
import config.Modes;
import config.PipeMap;
import gui.Game;
import inputs.EventManager;

public class LevelView extends View{
    private Game game;
    private EventManager eventManager;
    private int numberLevels = LevelLoader.getNumberOfLevels();
    private static JButton backButton, modeButton, difficultyButton;
    private ArrayList<JButton> normalButtonList;
    private JPanel levelPanel, buttonsPanel, normalButtonsPanel; 
    private BufferedImage background;
    private JLabel backgroundLabel;
    private JTextPane topPane;
    private Insets topPaneInsets, levelButtonsInsets, normalButtonsInsets, buttonsInsets;
    private GridBagConstraints topPaneConstraints, levelButtonsConstraints, normalButtonsConstraints, buttonsConstraints;
    
    private static int sizeLevelButton= 120;
    private static int ligne=0;    

    public LevelView(Game game) {
        super(game);
        this.game = game;
        eventManager = new EventManager(this);
        addKeyListener(eventManager);
        addMouseListener(eventManager);
    }


    @Override
    protected void initializeComponents() {
        var levels = LevelLoader.getLevels();
        this.numberLevels = levels.size();

        setLayout(new BorderLayout());
        initializeInsets();
        initializeConstraints();

        int widthButton= 215;
        int heightButton= 50;
        initializeButtons(widthButton, heightButton);

        int row=3, cols=5;
        int hgrap=30, vgrap= 35;
        int widthLevel= cols*sizeLevelButton+ (cols-1)*hgrap;
        int heightLevel= row*sizeLevelButton+ (row-1)*vgrap;
        int heightTopPane=50;
        int widthPanel=widthLevel+130;
        int heightPanel= heightButton+heightLevel+backButton.getHeight()+50;
        makeTextPane(40, heightTopPane, 30);
        makeLevelPanel(row, cols, hgrap, vgrap, widthLevel, heightLevel); // seulement pour les boutons des levels
        makeButtonsPanel(widthPanel, heightPanel, widthLevel, 20); // pour tous les boutons, dont celui du menu
        makeBackgroundLabel(widthPanel*7/6, (heightPanel+heightTopPane+20)*7/6);

        add(backgroundLabel, BorderLayout.CENTER);
    }

    @Override
    protected void initializeButtons(int width, int height){
        makeLevelButtons(15, numberLevels, sizeLevelButton);

        this.normalButtonList=new ArrayList<>();
        modeButton = makeButton("MODE",22);
        modeButton.addActionListener(e -> game.switchModeView());
        this.normalButtonList.add(modeButton);
        difficultyButton = makeButton("DIFFICULTY",22);
        difficultyButton.addActionListener(e -> game.switchDifficultyView());
        this.normalButtonList.add(difficultyButton);
        backButton = makeButton("BACK",22);
        backButton.addActionListener(e ->game.switchMenuView());
        this.normalButtonList.add(backButton);

        addButtonsIcons(normalButtonList, normalButtonsIcons, width, height);
    }

    private void initializeInsets() {
        topPaneInsets= new Insets(0, 0, 20, 0);
        levelButtonsInsets= new Insets(0, 0, 0, 0);
        normalButtonsInsets= new Insets(25, 0, 0,0);
        buttonsInsets= new Insets(5, 5, 5, 5);
    }

    private void initializeConstraints() {
        topPaneConstraints= setConstraints(ligne++, GridBagConstraints.PAGE_START, topPaneInsets, GridBagConstraints.NONE, GridBagConstraints.NONE);
        buttonsConstraints= setConstraints(ligne++, GridBagConstraints.PAGE_END, buttonsInsets, GridBagConstraints.NONE, GridBagConstraints.NONE);
        levelButtonsConstraints= setConstraints(ligne++, GridBagConstraints.PAGE_END, levelButtonsInsets, GridBagConstraints.NONE, GridBagConstraints.NONE);
        normalButtonsConstraints= setConstraints(ligne, GridBagConstraints.PAGE_END, normalButtonsInsets, GridBagConstraints.NONE, GridBagConstraints.NONE);
        
    }

    private void makeLevelButtons(int nbButtons, int nbLevelCreated, int size) {
        JButton button;
        for (int i=0; i<nbLevelCreated; i++) {
            final int levelIndex=i;
            button = makeButton(String.valueOf(i+1),22);
            button.addActionListener(e -> {
                LevelLoader.setSelectedLevel(levelIndex+1);
                PipeMap.resetMap(); 
                PipeMap.createMapFromLevel(LevelLoader.getSelectedLevel());
                game.switchGameView();});
            this.getButtonsList().add(button);
            button.setEnabled(false);
        }
        addButtonsIcons(getButtonsList(), squareButtonsIcons, sizeLevelButton, sizeLevelButton); 
    }

    private void makeLevelPanel(int row, int cols, int hgrap, int vgrap, int width, int height) {
        levelPanel= new JPanel();
        levelPanel.setLayout(new GridLayout(row, cols, hgrap, vgrap));
        levelPanel.setPreferredSize(new Dimension(width, height));
        levelPanel.setBackground(new Color(89, 86, 82));

        for(JButton b : this.getButtonsList()){
            levelPanel.add(b);
        }    
    }

    private void makeButtonsPanel(int width, int height, int widthLevelPanel, int hgrap) {
        normalButtonsPanel= new JPanel();
        normalButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, hgrap, 0));
        normalButtonsPanel.setPreferredSize(new Dimension(width-widthLevelPanel, height));
        normalButtonsPanel.setBackground(new Color(255, 255, 255, 0));
        for(JButton b : normalButtonList){
            normalButtonsPanel.add(b);
        }

        buttonsPanel= new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        buttonsPanel.setPreferredSize(new  Dimension(width, height));
        buttonsPanel.setBackground(new Color(255, 255, 255, 0));
        buttonsPanel.add(levelPanel, levelButtonsConstraints);
        buttonsPanel.add(normalButtonsPanel, normalButtonsConstraints);
    }

    private void makeBackgroundLabel(int width, int height) {
        this.background= resizeImage(backgroundLevelImg, width, height);
        backgroundLabel= new JLabel(new ImageIcon(background));
        backgroundLabel.setLayout(new GridBagLayout());
        backgroundLabel.add(topPane, topPaneConstraints);
        backgroundLabel.add(buttonsPanel, buttonsConstraints);
    }

    public void updateLevelButtons() {
        boolean[] unlockedLevels = game.getPlayer().getUnlockedLevels();
        int i = 0;
        final int start = numberLevels * LevelLoader.getActualDifficultyIndex();

        do {

            this.getButtonsList().get(i).setEnabled(true);
            i++;
        } while (i < numberLevels && unlockedLevels[start + i]);
    }

    private void makeTextPane(int width, int height, int size) {
        topPane= new JTextPane();
        topPane.setPreferredSize(new Dimension(width, height));
        topPane.setLayout(new BorderLayout());
        topPane.setBackground(new Color(78, 74, 78));
        String text = LevelLoader.getActualDifficulty().name()+" - "+ Modes.getActualMode().toString();
        topPane= makeText(text, size);
    }
}
