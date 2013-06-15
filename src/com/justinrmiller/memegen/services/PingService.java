package com.justinrmiller.memegen.services;

/*
 *  Author: Justin Miller
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

@Component
@Path(PingService.RESOURCE_PATH)
public class PingService {
    protected static final String RESOURCE_PATH = "ping";

    @GET
    public String ping() {
        return "pong";
    }
}
