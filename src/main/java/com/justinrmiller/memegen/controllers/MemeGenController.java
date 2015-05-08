package com.justinrmiller.memegen.controllers;

import com.justinrmiller.memegen.service.MemeGenService;
import com.justinrmiller.memegen.utils.ImageProcessor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Justin Miller (Copyright 2015)
 */
@RestController
public class MemeGenController {
    @Autowired
    private MemeGenService memeGenService;

    @RequestMapping(
            value = "meme",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity list() {
        return new ResponseEntity<>(memeGenService.getMemes(), HttpStatus.OK);
    }

    @RequestMapping(
            value = "meme/{meme}",
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity get(
            @PathVariable("meme") String memeID,
            @RequestParam(required = false) String top,
            @RequestParam(required = false) String bottom,
            @RequestParam(required = false, defaultValue = "Arial") String face) throws IOException {
        Optional<BufferedImage> image = memeGenService.generate(
            memeID,
            top != null ? top : "",
            bottom != null ? bottom : "",
            face
        );

        return image
                .map(x -> new ResponseEntity<>(ImageProcessor.bufferedImageToByteArray(x), HttpStatus.OK))
                .orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }
}
