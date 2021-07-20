package com.borgescloud.datastax.retailer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/hackathon")
public class HackathonRestController {

    private volatile boolean iot;

    /**
     * IOT - simulates IOT events
     */
    @RequestMapping(value = "/iot", method = RequestMethod.GET)
    public String iot() {

        log.info("Hackathon IOT demo called");

        iot = !iot;
        return (iot ? "IOT Simulation running" : "IOT Simulation stopped");
    }
}
