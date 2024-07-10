package com.github.heitorgomes900.apiConnectRedis.rest;

import com.github.heitorgomes900.apiConnectRedis.service.RedisService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


import java.util.List;

@Path("/redis")
public class RedisResource {

    @Inject
    RedisService redisService;

    @GET
    @Path("/keys/{pattern}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getKeys(@PathParam("pattern") String pattern) {
        return redisService.findKeysByPattern(pattern);
    }

    @GET
    @Path("/object/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getObjectByKey(@PathParam("key") String key) {
        return redisService.getObjectByKey(key);
    }

    @GET
    @Path("/objects/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getObjectByKeyPattern(@PathParam("key") String key) {
        return redisService.getObjectsByKeyPattern(key);
    }
}