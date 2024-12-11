package com.lakindu.Real_Time_Event_Ticketing_System.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration class represents the configuration settings for the ticketing system.
 */
public class Configuration {
    private final int totalTickets;  // Total tickets available in the system
    private final int ticketReleaseRate;  // Rate at which tickets are released
    private final int customerRetrievalRate;  // Rate at which customers retrieve tickets
    private final int maxTicketCapacity;  // Maximum capacity of the ticket pool

    /**
     * Constructor for Configuration.
     * Uses @JsonCreator to initialize properties from a JSON file.
     *
     * @param totalTickets the total number of tickets available
     * @param ticketReleaseRate the rate at which tickets are released
     * @param customerRetrievalRate the rate at which customers retrieve tickets
     * @param maxTicketCapacity the maximum capacity of the ticket pool
     */
    @JsonCreator
    public Configuration(@JsonProperty("totalTickets") int totalTickets,
                         @JsonProperty("ticketReleaseRate") int ticketReleaseRate,
                         @JsonProperty("customerRetrievalRate") int customerRetrievalRate,
                         @JsonProperty("maxTicketCapacity") int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    /**
     * Gets the total number of tickets available.
     *
     * @return the total number of tickets
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * Gets the rate at which tickets are released.
     *
     * @return the ticket release rate
     */
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    /**
     * Gets the rate at which customers retrieve tickets.
     *
     * @return the customer retrieval rate
     */
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    /**
     * Gets the maximum capacity of the ticket pool.
     *
     * @return the maximum ticket pool capacity
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
}