package renders;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import config.Player;
import config.SaveManagement;
import gui.Game;
import inputs.EventManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

public class PlayerView extends View {

    private Game game;
    private Player player;
    private EventManager eventManager;
    private JTextField playerNameField;
    private ArrayList<JButton> loadedPlayers;
    private JLabel backgroundLabel;
    private BufferedImage background;
    private Color textColor = new Color(15, 6, 25);
    private Color fondColor= new Color(125, 119, 125);

    public PlayerView(Game game) {
        super(game);
        this.game = game;
        this.player = game.getPlayer();
        eventManager = new EventManager(this);
        addKeyListener(eventManager);
        addMouseListener(eventManager);
    }

    @Override
    protected void initializeComponents() {
        setLayout(new BorderLayout());

        makeBackgroundLabel();
        initializeButtons(140,35);
        makePanels();
        updatePlayerList();

        add(backgroundLabel);
    }

    @Override
    protected void initializeButtons(int width, int height) {

        String[] buttonNames = {"Create", "Load"};
        ArrayList<JButton> array = makeButtons(buttonNames, 12);

        array.get(0).addActionListener(e -> createPlayer());
        array.get(1).addActionListener(e -> showPlayerSelectionDialog());

        this.setButtonsList(array);

        addButtonsIcons(this.getButtonsList(), normalButtonsIcons, width, height);
    }

    public ArrayList<JButton> makeButtons(String[] s, int size){
        ArrayList<JButton> liste = new ArrayList<>();
        for(String str : s){
            liste.add(makeButton(str, size));
        }
        return liste;
    }

    public void makePanels(){

        JTextPane playerNameLabel = makeText("Enter your name :", 15);
        playerNameField = new JTextField(15);
        playerNameField.setPreferredSize(new Dimension(150, 30));
        playerNameField.setFont(new Font("GravityBold8", Font.TRUETYPE_FONT, 13));
        playerNameField.setHorizontalAlignment(JTextField.CENTER);
        playerNameField.setBackground(new Color(125,119,125));
        playerNameField.setForeground(new Color(15, 6, 25));
        playerNameField.setBorder(new LineBorder(textColor));

        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new FlowLayout());
        fieldPanel.setPreferredSize(new Dimension(300, 70));
        fieldPanel.setBackground(new Color(89, 86, 82));

        fieldPanel.add(playerNameLabel);
        fieldPanel.add(playerNameField);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setPreferredSize(new Dimension(300, 40));
        buttonsPanel.setBackground(new Color(89, 86, 82));

        for (JButton b : this.getButtonsList()) {
            buttonsPanel.add(b);
        }

        backgroundLabel.add(fieldPanel, new GridBagConstraints());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.PAGE_END;

        backgroundLabel.add(buttonsPanel, gbc);
    }
    
    private void initializeUIManager(){
        UIManager.put("OptionPane.background", fondColor);
        UIManager.put("Panel.background", fondColor);
        UIManager.put("OptionPane.messageFont", new Font("GravityBold8", Font.TRUETYPE_FONT, 16)); // Police du message
        UIManager.put("OptionPane.messageForeground", textColor);
    }

    private String[] getPlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        for (Player player : SaveManagement.getSaveList()) {
            playerNames.add(player.getName());
        }
        return playerNames.toArray(new String[0]);
    }

    private void updatePlayerList() {
        loadedPlayers = new ArrayList<>();
        for (Player player : SaveManagement.getSaveList()) {
            JButton loadPlayerButton = new JButton(player.getName());
            loadedPlayers.add(loadPlayerButton);
            loadPlayerButton.addActionListener(e -> {
                game.setPlayer(player);
                game.switchMenuView();
            });
            add(loadPlayerButton);
        }
    }

    private void createPlayer() {
        String playerName = playerNameField.getText();
        if (!playerName.isEmpty()) {
            player = new Player(playerName);

            if (SaveManagement.playerNameExists(playerName)) {
                showPopup();
                return;
            }

            SaveManagement.createPlayer(player);
            System.out.println("Player created: " + playerName);
            selectPlayer(player);
        } else {
            System.out.println("Please enter a name.");
        }
    }
    
    /**
     * popup qui gère la suppression d'une partie
     * @param playerName
     */
    private void deletePlayer(String playerName) {
        initializeUIManager();
        String[] buttonNames = {"yes", "no"};
        ArrayList<JButton> array = makeButtons(buttonNames, 12);
        addButtonsIcons(array, normalButtonsIcons, 70, 35);

        array.get(0).addActionListener(e -> {
            SaveManagement.deletePlayer(playerName);
            System.out.println("Player deleted: " + playerName);
            updatePlayerList(); // Mise à jour de la liste des joueurs
            JOptionPane.getRootFrame().dispose();
        });
        array.get(1).addActionListener(e -> JOptionPane.getRootFrame().dispose());
        
        Object[] options = array.toArray();
        JOptionPane.showOptionDialog(null, "Are you sure you want to delete "+ playerName + "?", "Delete Player",
        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        return;    
    }

    /**
     * Popup qui montre les différents joueurs sauvegardé. Le joueur peut soit charger
     * la partie soit supprimer une partie
     */
    private void showPlayerSelectionDialog() {
        initializeUIManager();
        String[] playerNames = getPlayerNames();
        if (playerNames.length == 0) {
            showNotFound();
            return;
        }

        // Afficher la selection avec les noms des joueurs
        JComboBox<String> playerList = new JComboBox<>(playerNames);
        String longestName = SaveManagement.getLongestPlayerName();

        // Obtenir les dimensions du texte du nom le plus long
        Font font = playerList.getFont();
        FontMetrics fontMetrics = playerList.getFontMetrics(font);
        int textWidth = fontMetrics.stringWidth(longestName);
        playerList.setBackground(new Color(220, 220, 220));
        playerList.setOpaque(false);
        playerList.setBorder(new LineBorder(textColor));
        playerList.setFont(new Font("GravityBold8", Font.TRUETYPE_FONT, playerList.getFont().getSize()));
        playerList.setFocusable(false);
        // Définir la largeur du JComboBox selon le nom le plus long
        playerList.setPreferredSize(new Dimension(textWidth+65, playerList.getPreferredSize().height+25));
        //Modifier le design des noms en selection
        playerList.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value.toString());
            label.setOpaque(true);

            if (isSelected) {
                label.setBackground(new Color(100, 100, 100)); // Couleur de fond de l'élément sélectionné
                label.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 4));
                label.setForeground(Color.WHITE); // Couleur du texte de l'élément sélectionné
            } else {
                label.setBackground(new Color(220, 220, 220)); 
                label.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 4));
                label.setForeground(textColor); 
            }
            label.setFont(new Font("GravityBold8", Font.TRUETYPE_FONT, 15)); // Police de la liste déroulante
            FontMetrics fontMetric = label.getFontMetrics(label.getFont());
            int textHeight = fontMetric.getHeight();
            label.setSize(label.getHeight(), textHeight+50);
            return label;
        });

        JPanel panel = new JPanel();
        panel.setBackground(fondColor);
        JLabel label =new JLabel("Select a player:");
        label.setFont(new Font("GravityBold8", Font.TRUETYPE_FONT, 13));
        label.setForeground(textColor);
        panel.add(label);
        panel.add(playerList);

        //Les boutons
        String[] buttonNames = {"Load", "Delete", "Cancel"};
        ArrayList<JButton> array = makeButtons(buttonNames, 11);

        array.get(0).addActionListener(e -> {
            // Charger le joueur sélectionné
            String selectedPlayerName = (String) playerList.getSelectedItem();
            loadPlayer(selectedPlayerName);
            JOptionPane.getRootFrame().dispose();
        });
    
        array.get(1).addActionListener(e -> {
            // Supprimer le joueur sélectionné
            String selectedPlayerName = (String) playerList.getSelectedItem();
            deletePlayer(selectedPlayerName);
            JOptionPane.getRootFrame().dispose();
        });
    
        array.get(2).addActionListener(e -> {
            JOptionPane.getRootFrame().dispose();
        });
    
        addButtonsIcons(array, normalButtonsIcons, 130, 35);

        Object[] options = array.toArray();
        JOptionPane.showOptionDialog(null, panel, "Manage Player",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    }

    
    /**
     * Popup qui s'affiche lorsque le joueur entre un nom de joueur déjà présent
     * et qu'il clique sur create. Le joueur aura trois options : écrire par dessus
     * la sauvegarde, charger la partie ou annuler
     */
    private void showPopup() {
        initializeUIManager();
        String[] buttonNames = {"Overwrite", "Load", "Cancel"};
        ArrayList<JButton> array = makeButtons(buttonNames, 11);
    
        array.get(0).addActionListener(e -> {
            System.out.println("You chose: Overwrite");
            SaveManagement.createPlayer(player);
            System.out.println("Player created: " + player.getName());
            game.setPlayer(player);
            game.switchMenuView();
            // Fermer la boîte de dialogue
            JOptionPane.getRootFrame().dispose();
        });
    
        array.get(1).addActionListener(e -> {
            System.out.println("You chose: Load");
            loadPlayer();
            game.switchMenuView();
            // Fermer la boîte de dialogue
            JOptionPane.getRootFrame().dispose();
        });
    
        array.get(2).addActionListener(e -> {
            System.out.println("You chose: Cancel");
            // Fermer la boîte de dialogue
            JOptionPane.getRootFrame().dispose();
        });
    
        addButtonsIcons(array, normalButtonsIcons, 130, 35);
    
        // Options de la boîte de dialogue
        Object[] options = array.toArray();
        JOptionPane.showOptionDialog(null, "Choose an option", "Options",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[2]);
    
    }
    // Popup si on ne trouve pas de joueur
    private void showNotFound(){
        initializeUIManager();
        JButton okButton = makeButton("ok", 15);
        addIcon(okButton, normalButtonsIcons, 70, 35, true);
        okButton.addActionListener(e -> {
            JOptionPane.getRootFrame().dispose();
        });
        Object[] options = {okButton};
        JOptionPane.showOptionDialog(null, "Not found", "Error",
            JOptionPane.INFORMATION_MESSAGE, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        return;
    }

    private void loadPlayer() {
        for (Player p : SaveManagement.getSaveList()){
            if (p.getName().equals(playerNameField.getText())){
                selectPlayer(p);
                return; 
            }
        }
        showNotFound();
    }

    private void loadPlayer(String playerName) {
        for (Player p : SaveManagement.getSaveList()) {
            if (p.getName().equals(playerName)) {
                selectPlayer(p);
                return;
            }
        }
    }

    private void selectPlayer(Player player) {
        game.setPlayer(player);
        game.switchMenuView();
    }

    private void makeBackgroundLabel() {
        this.background = resizeImage(backgroundLevelImg, 350, 150);
        backgroundLabel = new JLabel(new ImageIcon(background));
        backgroundLabel.setLayout(new GridBagLayout());
    }
}
