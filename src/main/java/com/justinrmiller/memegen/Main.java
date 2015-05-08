package com.justinrmiller.memegen;

import com.google.common.collect.MapMaker;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Justin Miller (Copyright 2015)
 */
@EnableAutoConfiguration
@ComponentScan
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static ConcurrentMap<String, byte[]> MEMES = new MapMaker()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .makeMap();

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            logger.error("Missing meme image set directory.");
        }

        // load meme images
        final File memes = new File(args[0]);
        final File[] files = memes.listFiles();
        for (final File fileEntry : files) {
            if (!fileEntry.isDirectory()) {
                String [] memeName = fileEntry.getName().split("\\.");
                logger.info("" + memeName[0]);
                MEMES.put(memeName[0], Files.toByteArray(fileEntry));
            }
        }

        SpringApplication.run(Main.class, args);
    }
}