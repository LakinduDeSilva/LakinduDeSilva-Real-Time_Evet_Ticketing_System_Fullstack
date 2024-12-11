package com.lakindu.Real_Time_Event_Ticketing_System.controllers;

import com.lakindu.Real_Time_Event_Ticketing_System.services.ConfigurationService;
import com.lakindu.Real_Time_Event_Ticketing_System.utils.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    // Endpoint to save configuration
    @PostMapping("/save")
    public ResponseEntity<String> saveConfiguration(@RequestBody Configuration config) {
        String result = configurationService.saveConfiguration(config);
        return ResponseEntity.ok(result);
    }

    // Endpoint to load configuration
    @GetMapping("/load")
    public ResponseEntity<Map<String, Object>> loadConfiguration() {
        Configuration config = configurationService.loadConfigurationObject();

        if (config == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No configuration file found."));
        }

        return ResponseEntity.ok(Map.of(
                "totalTickets", config.getTotalTickets(),
                "ticketReleaseRate", config.getTicketReleaseRate(),
                "customerRetrievalRate", config.getCustomerRetrievalRate(),
                "maxTicketCapacity", config.getMaxTicketCapacity()
        ));
    }

}
