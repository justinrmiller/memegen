package com.justinrmiller.memegen.controllers;

import java.awt.image.BufferedImage;

/*
 *   Author: Justin Miller
 */
public interface MemeGenController {
    String getMemes();
    BufferedImage generate(String memeID, String topText, String bottomText, String face);
}
