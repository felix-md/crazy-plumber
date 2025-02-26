package models.pipes;

import models.Direction;
import models.PipeModel;
import renders.Graphic;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import config.Constant;

public class PipeStraight extends PipeModel implements Graphic {

    public PipeStraight() {
        super(PipeType.STRAIGHT);
        this.setImage(getImage("puzzle_pipes/vertical/pipe_vertical.png"));

        


        var waterAsset = new ArrayList<BufferedImage>();
        waterAsset.add(getImage("puzzle_pipes/vertical/water_vertical_bottom_strip11.png"));

        for (int i = 0; i < 11; i++) {
            waterAsset.add(waterAsset.get(0).getSubimage(i * 128, 0, 128, 128));
        }
       
        for (int i = 0; i < waterAsset.size(); i++) {
            waterAsset.set(i, resizeImage(waterAsset.get(i), Constant.PIPE_SIZE, Constant.PIPE_SIZE));
        }

        this.setWaterAnimation(waterAsset);

    }

}
