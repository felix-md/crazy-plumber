package models;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;


public enum WaterColor {
    BLUE, RED, PURPLE,NONE;


    public java.awt.Color getColor() {
        //fonction temporaire pour tester
        switch (this) {
            case RED:
                return java.awt.Color.RED;
          
            case BLUE:
                return java.awt.Color.BLUE;
            case PURPLE:
                return java.awt.Color.MAGENTA;
            
           
            default:
                return java.awt.Color.WHITE;
        }
    }

    public WaterColor addColor(WaterColor Color){
        if(this == Color){
            return this;
        }
        if(this == WaterColor.NONE){
            return Color;
        }
        if(Color == WaterColor.NONE){
            return this;
        }
        if(this == WaterColor.BLUE && Color == WaterColor.RED){
            return WaterColor.PURPLE;
        }
        if(this == WaterColor.RED && Color == WaterColor.BLUE){
            return WaterColor.PURPLE;
        }
       
        return WaterColor.PURPLE;
    }


    public LookupTable getLookupTable() {
        short[] red = new short[256];
        short[] green = new short[256];
        short[] blue = new short[256];
        short[] alpha = new short[256];
        for (int i = 0; i < 256; i++) {
            alpha[i] = (short) i;
            red[i] = (short) getColor().getRed();
            green[i] = (short) getColor().getGreen();
            blue[i] = (short) getColor().getBlue();
        }
        short[][] table = {red, green, blue, alpha};
        return new ShortLookupTable(0, table);
    }
        
        
}