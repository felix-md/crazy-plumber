package models.pipes;

import java.util.ArrayList;

import config.Constant;
import models.PipeModel;
import renders.Graphic;
import java.awt.image.BufferedImage;

public class PipeT extends PipeModel implements Graphic{

    public PipeT(){
        super(PipeType.T);
     
        this.setImage(getImage("puzzle_pipes/bonus_pipes/pipe_t.png"));

        var waterAsset = new ArrayList<BufferedImage>();
        waterAsset.add(getImage("puzzle_pipes/bonus_pipes/water_T_strip11.png"));

        for (int i = 0; i < 11; i++) {
            waterAsset.add(waterAsset.get(0).getSubimage(i * 128, 0, 128, 128));
        }
     
        for (int i = 0; i < waterAsset.size(); i++) {
            waterAsset.set(i, resizeImage(waterAsset.get(i), Constant.PIPE_SIZE, Constant.PIPE_SIZE));
        }

        this.setWaterAnimation(waterAsset);

        var waterAsset2 = new ArrayList<BufferedImage>();
        waterAsset2.add(getImage("puzzle_pipes/bonus_pipes/water_T2_strip11.png"));

        for (int i = 0; i < 11; i++) {
            waterAsset2.add(waterAsset2.get(0).getSubimage(i * 128, 0, 128, 128));
        }

        for (int i = 0; i < waterAsset2.size(); i++) {
            waterAsset2.set(i, resizeImage(waterAsset2.get(i), Constant.PIPE_SIZE, Constant.PIPE_SIZE));
        }

        this.setWaterAnimation2(waterAsset2);

    }

}
