package renders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public interface Graphic {
    
    default BufferedImage resizeImage(BufferedImage img ,int width, int height){
        BufferedImage img_resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d_resized = img_resized.createGraphics();
        g2d_resized.drawImage(img, 0, 0, width, height, null);
        g2d_resized.dispose();
        return img_resized;
    }

    default BufferedImage fusionImages(BufferedImage img1, BufferedImage img2, int width, int height){
        BufferedImage img_resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d_resized = img_resized.createGraphics();
        g2d_resized.drawImage(img1, 0, 0, width, height, null);
        g2d_resized.drawImage(img2, (width/6), (height/6), width-(width/3), height-(height/3), null);
        g2d_resized.dispose();
        return img_resized;
    }

    /* Importer une image */
    default BufferedImage getImage(String chemin){
        try {
            BufferedImage image = ImageIO.read(new File(chemin));
            return image;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    default void makeTransparentBackground(JButton j){
        j.setOpaque(false);
        j.setBorderPainted(false);
        j.setContentAreaFilled(false);
        j.setFocusPainted(false);
    }

    /* Mettre la nouvelle police */
    default void setFont(JButton button, int size){
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.getVerticalTextPosition();
        button.setFont(new Font("GravityBold8",Font.TRUETYPE_FONT,size));//police du texte
        button.setForeground(new Color(15, 6, 25));//couleur du texte
    }

    default JButton makeButton(String text, int size){
        JButton button;
        button = (text==null) ? new JButton(): new JButton(text);
        setFont(button, size);
        makeTransparentBackground(button);
        return button;
    }

    /* Ajout des polices */
    default void initializeFont(){
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("puzzle_pipes/menu/fonts/Bongo-8 Mono.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("puzzle_pipes/menu/fonts/GravityBold8.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("puzzle_pipes/menu/fonts/Holotype 9 Basic.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    default void addButtonsIcons(ArrayList <JButton> list, ArrayList<BufferedImage> icons, int width, int height){
        for(JButton b : list){
            b.setPreferredSize(new Dimension(width, height));
            addIcon(b,icons,width,height,true);
        }
    }

    default void addIcon(JButton b, ArrayList<BufferedImage> icons,int width, int height,boolean survol){
        int a=1;
        b.setIcon(new ImageIcon(resizeImage(icons.get(0),width,height)));
        if(survol){
            b.setRolloverIcon(new ImageIcon(resizeImage(icons.get(1),width,height)));
            a=2;
        }
        b.setPressedIcon(new ImageIcon(resizeImage(icons.get(a),width,height)));
    }

    default ImageIcon makeIcon(ArrayList<BufferedImage> icons,int indice,int width, int height){
        return new ImageIcon(resizeImage(icons.get(indice),width,height));
    }

    default GridBagConstraints setConstraints(int y, int anchor, Insets insets, double weightx, int fill) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx= 0; // placement horizontal, ici tout à gauche
        gbc.gridy= y; // placement vertical : GridBagConstraints.PAGE_START=19, et GridBagConstraints.PAGE_END=20
        gbc.anchor= anchor; // centrage : GridBagConstraints.CENTER=10, GridBagConstraints.WEST=17, GridBagConstraints.NORTH=11
        gbc.insets= insets; // marges
        gbc.weightx= weightx; // permet de prendre toute la largeur disponible si=1, par defaut=0
        gbc.fill=fill; // permet le remplissage d'une ligne : GridBagConstraints.NONE=0 (par defaut) et GridBagConstraints.HORIZONTAL=2
        return gbc;
    }

    default void setStyle(JTextPane textPane) {
        StyledDocument document= textPane.getStyledDocument();
        SimpleAttributeSet attribute= new SimpleAttributeSet();
        StyleConstants.setLineSpacing(attribute, 0.3f);
        document.setParagraphAttributes(0, document.getLength(), attribute, false);
    }
    
    default JTextPane makeText(String str, int size) {
        JTextPane textPane= new JTextPane();
        textPane.setText(str);
        textPane.setForeground(new Color(15, 6, 25));
        textPane.setEditable(false);

        this.setStyle(textPane);
        textPane.setOpaque(false);

        // GridBagConstraints textConstraints= setConstraints(y, anchor, insets, weightx, fill);
        textPane.setFont(new Font("GravityBold8",Font.TRUETYPE_FONT, size));

        // label.add(textPane, textConstraints);
        return textPane;
    }
    
    
}
