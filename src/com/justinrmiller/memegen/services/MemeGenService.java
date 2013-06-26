package com.justinrmiller.memegen.services;

/*
 *   Author: Justin Miller
 */

import com.google.common.base.Joiner;
import com.justinrmiller.memegen.utils.ImageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.justinrmiller.memegen.MemeGen.MEMES;

@Component
@Path(MemeGenService.RESOURCE_PATH)
public class MemeGenService {
    protected static final String RESOURCE_PATH = "/memegen";

    @Autowired
    com.justinrmiller.memegen.controllers.MemeGenController memeGenController;

    @GET
    @Path("list")
    public String list() {
        return memeGenController.getMemes();
    }

    @GET
    @Produces("image/jpeg")
    @Path("generate/{meme}")
    public Response generate(
            @PathParam("meme") String memeID,
            @QueryParam("top") String top,
            @QueryParam("bottom") String bottom,
            @QueryParam("face") String face) {
        BufferedImage image = memeGenController.generate(memeID, top != null ? top : "", bottom != null ? bottom : "", face);

        if (image != null) {
            return Response.ok(image).build();
        } else {
            return Response.status(404).build();
        }
    }
}