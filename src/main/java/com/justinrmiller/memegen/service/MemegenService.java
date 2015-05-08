package com.justinrmiller.memegen.service;

import com.justinrmiller.memegen.utils.ImageProcessor;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.Optional;
import java.util.Set;

import static com.justinrmiller.memegen.Main.MEMES;

/**
 * @author Justin Miller (Copyright 2015)
 */
@Component
public class MemeGenService {
    public Set<String> getMemes() {
        return MEMES.keySet();
    }

    public Optional<BufferedImage> generate(
        final String memeID,
        final String topText,
        final String bottomText,
        final String face) {
        assert face != null;

        if (MEMES.containsKey(memeID)) {
            try {
                BufferedImage original = ImageIO.read(new ByteArrayInputStream(MEMES.get(memeID)));
                BufferedImage meme = ImageProcessor.overlay(original, topText, bottomText, face);
                return Optional.of(meme);
            } catch (IOException ioex) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
