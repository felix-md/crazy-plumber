package renders;

import java.util.ArrayList;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import config.Player;
import gui.Game;
import inputs.EventManager;
import models.Achievement;
import config.LevelLoader;

public class AchievementView extends View {

    private Player player;
    private EventManager eventManager;

    private JButton backButton;
    private JPanel achievementsPanel, mainAchievementPanel ,container;
    private BufferedImage background;
    private JLabel backgroundLabel , descriptionLabel;

    private ArrayList<JButton> successBList = new ArrayList<>();

    public AchievementView (Game game) {
        super(game);
        eventManager = new EventManager(this);
        addKeyListener(eventManager);
        addMouseListener(eventManager);
        makeAchievementsButtons();
    }

    @Override
    protected void initializeComponents() {
        setLayout(new BorderLayout());
        this.player = game.getPlayer();
        initializeButtons(200,50);
        makeAchievementsPanel();
        makeAchievementsContainerPanel();
        makeDescriptionLabel();
        makeBackgroundLabel();
        
        add(backgroundLabel, BorderLayout.CENTER);

    }

    protected void initializeButtons(int width, int height){
       
        backButton= makeButton("BACK",22);
        backButton.addActionListener(e -> game.switchMenuView());
        backButton.setPreferredSize(new Dimension(width,height));
        this.getButtonsList().add(backButton);

        addButtonsIcons(this.getButtonsList(),normalButtonsIcons,200,50);
    }

    private void makeAchievementsContainerPanel(){
        container= new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setPreferredSize(new Dimension(850, 450));
        container.setBackground(new Color(89, 86, 82));
    }

    private void makeAchievementsPanel() {
        achievementsPanel= new JPanel();
        achievementsPanel.setLayout(new GridLayout(2, 4,70, 70));
        achievementsPanel.setPreferredSize(new Dimension(722, 326));
        achievementsPanel.setBackground(new Color(89, 86, 82));

        //panel qui va contenir le achievementPanel pour pouvoir le centrer
        mainAchievementPanel = new JPanel(new GridBagLayout());
        mainAchievementPanel.setBackground(new Color(89, 86, 82));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 1; 
        gbc.anchor = GridBagConstraints.CENTER;

        mainAchievementPanel.add(achievementsPanel,gbc);

    }

    private void makeBackgroundLabel() {
        this.background= resizeImage(backgroundLevelImg, 900, 600);
        backgroundLabel= new JLabel(new ImageIcon(background));
        backgroundLabel.setLayout(new GridBagLayout());

        container.add(mainAchievementPanel);
        container.add(descriptionLabel);
        backgroundLabel.add(container);
        placeBackButton();
    }

    private void placeBackButton() {      
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 1; 
        gbc.anchor = GridBagConstraints.PAGE_END;

        backgroundLabel.add(backButton, gbc); 
    }

    private void makeAchievementsButtons() {
        for(Achievement achievement : player.getAchievementsList()) {
            JButton button = makeButton(String.valueOf(achievement.getId() + 1),22);
            button.addActionListener(e -> {showDescription(achievement);});
            successBList.add(button);
            achievementsPanel.add(button);
            button.setEnabled(false);
        }
        for(JButton b: successBList){
            b.setIcon(new ImageIcon(resizeImage(succesIcon, 128, 128)));
        }

        for (JButton button : successBList) {
            button.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {}

                @Override
                public void mousePressed(MouseEvent e) {}

                @Override
                public void mouseReleased(MouseEvent e) {}

                @Override
                public void mouseEntered(MouseEvent e) {
                    // Affiche la description lorsque la souris survole le bouton
                    showDescription(player.getAchievementsList().get(successBList.indexOf(button)));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Reset de la description lorsque la souris quitte le bouton
                    resetDescription();
                }
            });
        }
    }

    private void makeDescriptionLabel() {
        descriptionLabel = new JLabel();
        descriptionLabel.setForeground(Color.BLACK);
        descriptionLabel.setFont(new Font("GravityBold8",Font.TRUETYPE_FONT, 12)); 
        descriptionLabel.setPreferredSize(new Dimension(850,80));
        //centrer le descriptionlabel par rapport Ã  container
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
    }   

    private void showDescription(Achievement success) {
        descriptionLabel.setText(success.getName() + " : " + success.getDescription());
    }   

    private void resetDescription() {
        descriptionLabel.setText("");
    }

    public void updateAchievements() {
     
        for(Achievement achievement : player.getAchievementsList()){
            System.out.println("INSIDE");
            System.out.println(successBList.size());
            if(achievement.isUnlocked()){
                System.out.println("IDDDDD : "+ achievement.getId());
                successBList.get(achievement.getId()).setEnabled(true);
            }
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}