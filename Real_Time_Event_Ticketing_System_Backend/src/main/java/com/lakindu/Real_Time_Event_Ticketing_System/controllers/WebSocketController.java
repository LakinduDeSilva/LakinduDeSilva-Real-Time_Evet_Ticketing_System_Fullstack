package com.lakindu.Real_Time_Event_Ticketing_System.controllers;

import com.lakindu.Real_Time_Event_Ticketing_System.services.ConfigurationService;
import com.lakindu.Real_Time_Event_Ticketing_System.services.WebSocketTicketHandler;
import com.lakindu.Real_Time_Event_Ticketing_System.utils.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * WebSocketController handles the API endpoints for starting and stopping the ticketing system.
 */
@RestController
@RequestMapping("/api/ticketing")
public class WebSocketController {
    private final ConfigurationService configurationService;
    private final WebSocketTicketHandler ticketHandler;
    private static boolean isTicketingRunning = false;

    /**
     * Constructor for WebSocketController.
     *
     * @param configurationService the service to manage configurations
     * @param ticketHandler the handler to manage WebSocket ticketing
     */
    @Autowired
    public WebSocketController(ConfigurationService configurationService, WebSocketTicketHandler ticketHandler) {
        this.configurationService = configurationService;
        this.ticketHandler = ticketHandler;
    }

    /**
     * Endpoint to start the ticketing process.
     *
     * @return a message indicating the result of the start operation
     */
    @PostMapping("/start")
    public synchronized String startTicketing() {
        if (isTicketingRunning) {
            return "Ticketing system is already running.";
        }

        // Load configuration from the service
        Configuration config = configurationService.loadConfigurationObject();
        if (config == null) {
            return "No configuration found. Please save a configuration first.";
        }

        // Start the ticketing process
        ticketHandler.startTicketing(config);
        isTicketingRunning = true; // Mark the ticketing system as running
        return "Ticketing started with the loaded configuration.";
    }

    /**
     * Endpoint to stop the ticketing process.
     *
     * @return a message indicating the result of the stop operation
     */
    @PostMapping("/stop")
    public synchronized String stopTicketing() {
        if (!isTicketingRunning) {
            return "Ticketing system is not running.";
        }

        // Stop the ticketing process
        ticketHandler.stopTicketing();
        isTicketingRunning = false; // Mark the ticketing system as stopped
        return "Ticketing system has been stopped.";
    }
}
