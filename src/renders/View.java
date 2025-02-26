package renders;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import config.Constant;
import gui.Game;
import inputs.EventManager;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class View extends JPanel implements Graphic {

    Game game;
    private EventManager eventManager;
    protected ArrayList<BufferedImage> normalButtonsIcons=new ArrayList<>();
    protected ArrayList <BufferedImage> squareButtonsIcons = new ArrayList<>();
    protected ArrayList <BufferedImage> gameButtonIcons = new ArrayList<>();
    protected ArrayList <BufferedImage> starIcons = new ArrayList<>();
    protected ArrayList <BufferedImage> backgroundIcons = new ArrayList<>();
    protected ArrayList <Cursor> cursors = new ArrayList<>();

    protected BufferedImage backgroundMenuImg, backgroundLevelImg, gameBackground, boardBackground, barreImg, starsImg, winBackground, looseBackground, cursorImg, succesIcon, succesBG,pane;
    private ArrayList<JButton> buttonsList= new ArrayList<>();

    public View(Game game) {
        Dimension size = new Dimension(Constant.GAME_HEIGHT, Constant.GAME_WIDTH);
        setPreferredSize(size);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        
        addKeyListener(eventManager);
        addMouseListener(eventManager);
        addMouseMotionListener(eventManager);

        this.game=game;
        addInitialAssets();
        initializeComponents();
        this.setBackground(new Color(110,110,110));
    }

    public ArrayList<Cursor> getCursors() {
        return cursors;
    }

    public ArrayList<JButton> getButtonsList() {
        return buttonsList;
    }

    public void setButtonsList(ArrayList<JButton> buttonsList) {
        this.buttonsList = buttonsList;
    }

    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        g.drawImage(gameBackground, 0, 0, Constant.GAME_WIDTH, Constant.GAME_HEIGHT,null);

    }

    public void addInitialAssets(){
        BufferedImage buttons =getImage("puzzle_pipes/menu/buttons/buttonsV2.png");
        BufferedImage buttons2=getImage("puzzle_pipes/menu/buttons/buttons_level.png");
        BufferedImage buttons3=getImage("puzzle_pipes/ui game/buttons_imgs.png");
        BufferedImage buttonsIcons=getImage("puzzle_pipes/ui game/icones.png");
        BufferedImage winLoose=getImage("puzzle_pipes/ui game/WinLooseBackground.png");
        BufferedImage cursor2=getImage("puzzle_pipes/cursor2.png");

        this.backgroundMenuImg= getImage("puzzle_pipes/menu/buttons/background_menu.png");
        this.backgroundLevelImg= getImage("puzzle_pipes/menu/buttons/background_level.png");
        this.gameBackground= getImage("puzzle_pipes/ui game/background.png");
        this.boardBackground=getImage("puzzle_pipes/ui game/plateaubckgrd.png");
        this.barreImg=getImage("puzzle_pipes/ui game/barre.png");
        this.starsImg=getImage("puzzle_pipes/ui game/stars.png");
        this.winBackground= winLoose.getSubimage(180, 0, 180, 200);
        this.looseBackground= winLoose.getSubimage(0, 0, 180, 200);
        this.pane= getImage("puzzle_pipes/ui game/pane.png");
        this.cursorImg= getImage("puzzle_pipes/cursor.png");
        BufferedImage icon =getImage("puzzle_pipes/succes.png");
        this.succesBG=getImage("puzzle_pipes/succesBG.png");
        this.succesIcon=fusionImages(succesBG, icon, 128, 128);

        BufferedImage img1=buttons3.getSubimage(0, 0, 24, 24);
        BufferedImage img2= buttons3.getSubimage(24, 0, 24, 24);
        BufferedImage wheel=getImage("puzzle_pipes/items/wheel_icon.png");
        
        for(int ligne=0;ligne<6;ligne++){
            if(ligne<3){
                normalButtonsIcons.add(buttons.getSubimage(0, ligne*28, 122, 28));
                squareButtonsIcons.add(buttons2.getSubimage(0, ligne*26, 25, 26));
            }
            BufferedImage img = buttonsIcons.getSubimage(ligne*15, 0, 15, 15);
            gameButtonIcons.add(fusionImages(img1, img, 44, 44));
            gameButtonIcons.add(fusionImages(img2, img, 44, 44));
        }
        gameButtonIcons.add(fusionImages(img1, wheel, 44, 44));
        gameButtonIcons.add(fusionImages(img2, wheel, 44, 44));

        for (int ligne=0; ligne<2; ligne++) {
            starIcons.add(starsImg.getSubimage(12*ligne, 0, 12, 12));
            backgroundIcons.add(starsImg.getSubimage(12*ligne, 0, 12, 12));
        }
        addCursors(cursor2);
    }

    public void addCursors(BufferedImage img){
        Cursor cursor;
        for (int ligne=0; ligne<3; ligne++) {
            cursor=Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(img.getSubimage(17*ligne, 0, 17, 17)).getImage(), new Point(0,0), "cursor");
            cursors.add(cursor);
        }
    }
    // Méthode pour charger le contenu de la fenêtre
    protected abstract void initializeComponents();
    // méthode pour initialiser et ajouter les boutons du panel
    protected abstract void initializeButtons(int width, int height);

    // Méthodes génériques pour gérer les événements de souris et de clavier (à surcharger dans les classes filles)
    public void handleKeyTyped(KeyEvent e) {}

    public void handleKeyPressed(KeyEvent e) {}

    public void handleKeyReleased(KeyEvent e) {}

    public void handleMouseClicked(MouseEvent e) {
    }

    public void handleMousePressed(MouseEvent e) {
    }

    public void handleMouseReleased(MouseEvent e) {
    }

    public void handleMouseEntered(MouseEvent e) {
        game.setCursor(cursors.get(0));
    }

    public void handleMouseExited(MouseEvent e) {
        game.setCursor(cursors.get(1));
    }

    public void handleMouseDragged(MouseEvent e) {}

    public void handleMouseMoved(MouseEvent e) {
        game.setCursor(cursors.get(0));
    }

   

    
}
