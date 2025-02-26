package models.pipes;

import models.Direction;
import models.PipeModel;
import renders.Graphic;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import config.Constant;

public class PipeCorner extends PipeModel implements Graphic {

    public PipeCorner() {
        super(PipeType.CORNER);
        
        
        this.setImage(getImage("puzzle_pipes/bottom_right/pipe_corner_bottom_right.png"));

        var waterAsset = new ArrayList<BufferedImage>();
        waterAsset.add(getImage("puzzle_pipes/bottom_right/water_corner_bottom_right_bottom_strip11.png"));

        for (int i = 0; i < 11; i++) {
            waterAsset.add(waterAsset.get(0).getSubimage(i * 128, 0, 128, 128));
        }
        
        for (int i = 0; i < waterAsset.size(); i++) {
            waterAsset.set(i, resizeImage(waterAsset.get(i), Constant.PIPE_SIZE, Constant.PIPE_SIZE));
        }

        this.setWaterAnimation(waterAsset);
        
    }


    
}
