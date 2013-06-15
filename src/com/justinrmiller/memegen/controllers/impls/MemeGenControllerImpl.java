package com.justinrmiller.memegen.controllers.impls;

import com.google.common.base.Joiner;
import com.justinrmiller.memegen.MemeGen;
import com.justinrmiller.memegen.controllers.MemeGenController;
import com.justinrmiller.memegen.utils.ImageProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.justinrmiller.memegen.MemeGen.MEMES;

/*
 *   Author: Justin Miller
 */
public class MemeGenControllerImpl implements MemeGenController {
    @Override
    public String getMemes() {
        Joiner memeJoiner = Joiner.on(",").skipNulls();
        return memeJoiner.join(MemeGen.MEMES.keySet());
    }

    @Override
    public BufferedImage generate(String memeID, String topText, String bottomText) {
        BufferedImage image = null;

        if (MemeGen.MEMES.containsKey(memeID)) {
            try {
                BufferedImage original = ImageIO.read(new ByteArrayInputStream(MEMES.get(memeID)));
                image = ImageProcessor.overlay(original, topText, bottomText);
            } catch (IOException ioex) {
                // add logging, optional
            }
        }

        return image;
    }
}
