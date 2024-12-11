package com.lakindu.Real_Time_Event_Ticketing_System.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lakindu.Real_Time_Event_Ticketing_System.utils.Configuration;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ConfigurationService {

    private final String configFilePath = "config.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Save configuration to file
    public String saveConfiguration(Configuration config) {
        try {
            objectMapper.writeValue(new File(configFilePath), config);
            return "Configuration saved successfully.";
        } catch (IOException e) {
            return "Error saving configuration: " + e.getMessage();
        }
    }

    // Load configuration as a Configuration object
    public Configuration loadConfigurationObject() {
        File configFile = new File(configFilePath);
        if (configFile.exists()) {
            try {
                return objectMapper.readValue(configFile, Configuration.class);
            } catch (IOException e) {
                return null; // You could also throw a custom exception here
            }
        } else {
            return null; // No configuration file found
        }
    }
}
