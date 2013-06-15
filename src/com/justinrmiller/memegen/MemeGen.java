package com.justinrmiller.memegen;

/*
 *   Author: Justin Miller
 */

import com.google.common.collect.MapMaker;
import com.google.common.io.Files;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.spi.spring.container.SpringComponentProviderFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.imageio.ImageIO;
import javax.ws.rs.core.UriBuilder;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentMap;

public class MemeGen {
    public static boolean isProduction = false;

    public static String CONFIGURATION_FILE = "/conf/prod/configuration.xml";

    public static ConcurrentMap<String, byte[]> MEMES = new MapMaker()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .maximumSize(100)
            .makeMap();

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage: java -jar <jarfile> <ip address> <port> <env>");
            System.exit(0);
        }

        final String host = args[0];
        final int port = Integer.valueOf(args[1]);
        final String env = args[2];

        if ("prod".equals(env)) {
            isProduction = true;
        }

        System.out.println("Starting server at host: " + host + " on port: " + port + " in env mode: " + env);

        CONFIGURATION_FILE = "./conf/" + env + "/configuration.xml";

        // default Time Zone to GMT
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

        // load meme images
        final File memes = new File("memes");
        for (final File fileEntry : memes.listFiles()) {
            if (!fileEntry.isDirectory()) {
                String [] memeName = fileEntry.getName().split("\\.");
                System.out.println("" + memeName[0]);
                MEMES.put(memeName[0], Files.toByteArray(fileEntry));
            }
        }

        final HttpServer server = startWebContainer(host, port, env);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized(server) {
                    server.stop();
                    server.notifyAll();
                }
            }
        }));

        synchronized(server) {
            try {
                server.wait();
            } catch (InterruptedException ex) {
                System.out.println("Thread interrupted.");
            }
        }
    }

    public static HttpServer startWebContainer(String host, int port, String env) throws Exception {
        URI baseUri = UriBuilder.fromUri("http://" + host + "/").port(port).build();

        ResourceConfig rc = new DefaultResourceConfig();

        ConfigurableApplicationContext cac = new FileSystemXmlApplicationContext("/conf/" + env + "/spring-config.xml");
        IoCComponentProviderFactory factory = new SpringComponentProviderFactory(rc, cac);

        return GrizzlyServerFactory.createHttpServer(baseUri, rc, factory);
    }

}
