package renders;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import gui.Game;
import inputs.EventManager;

public class RulesView extends View {

    private Game game;
    private EventManager eventManager;
    private BufferedImage background;
    private JButton backButton;
    private JLabel backgroundLabel, rulesLabel;
    private JTextPane titleText, rulesText;
    private GridBagConstraints buttonConstraints, titleConstraints, textConstraints;
    private Insets titleInsets, rulesInsets, buttonInsets;
    private static int ligne=0;


    public RulesView(Game game) {
        super(game);
        this.game = game;
        eventManager = new EventManager(this);
        addKeyListener(eventManager);
        addMouseListener(eventManager);
    }

    @Override
    protected void initializeComponents() {
        setLayout(new BorderLayout());
        initializeInsets();

        int widthButton= 200;
        int heightButton= 50;
        initializeButtons(widthButton, heightButton);

        int widthRules=870;
        int heightRules=700;
        makeRulesLabel(widthRules, heightRules+heightButton);
        makeBackgroundLabel(widthRules+50, heightRules+heightButton+100);
        
        add(backgroundLabel, BorderLayout.CENTER);
    }

    protected void initializeButtons(int width, int height){
        backButton= makeButton("BACK",22);
        backButton.addActionListener(e -> game.switchMenuView());
        backButton.setPreferredSize(new Dimension(width, height));
        this.getButtonsList().add(backButton);

        addButtonsIcons(this.getButtonsList(),normalButtonsIcons,200,50);
    }

    private void initializeInsets() {
        titleInsets= new Insets(20, 35, 8, 20);
        rulesInsets= new Insets(3, 35, 3, 10);
        buttonInsets= new Insets(3, 0, 30, 0);
    }

    private void initializeConstraint() {
        buttonConstraints= setConstraints(20, 10, buttonInsets, 0, 0);
        titleConstraints= setConstraints(ligne++, 10, titleInsets, 0, 0);
        textConstraints= setConstraints(ligne++, 17, rulesInsets, 1.0, 2);
    }

    private void makeBackgroundLabel(int width, int height) {
        this.background= resizeImage(backgroundLevelImg, width, height);
        backgroundLabel= new JLabel(new ImageIcon(background));
        backgroundLabel.setLayout(new GridBagLayout());
        backgroundLabel.add(rulesLabel);
        placeBackButton();
    }

    private void placeBackButton() {   
        backgroundLabel.add(backButton, buttonConstraints); 
    }
    
    private void makeRulesLabel(int width, int height) {
        rulesLabel= new JLabel();
        rulesLabel.setLayout(new GridBagLayout());
        rulesLabel.setPreferredSize(new Dimension(width, height));
        rulesLabel.setBackground(new Color(255, 255, 255, 0));

        String rules= "Left click on a pipe to rotate."+
            "\nThe goal of this game is to connect all pipes without water leak from all the starts to the end by rotating the pipes."+
            "\nIn each level, the pipes are randomly oriented.";
        makeParagraph("RULES", rules, rulesLabel);

        String mode= "\tNormal Mode \nyou have to connect the pipes from the starting point to the end point, without constraints."+
        "\n\tLimited Move Mode \nyou have to do it only with a limited number of moves."+
        "\n\tTimer mode \nyou have to do it within a limited time frame.";
        makeParagraph("MODES", mode, rulesLabel);

        String gameButton= "\tAnimation Button \nWhen you have finished connecting all the pipes, there is a button at the top left, click on it, if the animation start you have win the level."+
            "\n\n\tUndo Button \nIf you rotate a pipe, you can undo the movement up to the first pipe that was moved."+
            "\n\n\tRedo Button \nIf you have undone a move , you can redo it up."+
            "\n\n\tHint Button \nIn each level, there is a limited number of hints available, which can help you find a solution (note: there can be multiple solutions).";
        makeParagraph("BUTTONS", gameButton, rulesLabel);

    }

    private void makeParagraph(String title, String text, JLabel label) {
        initializeConstraint();
        titleText= makeText(title, 20);
        rulesText= makeText(text, 15);

        label.add(titleText, titleConstraints);
        label.add(rulesText, textConstraints);
    } 



    
}
