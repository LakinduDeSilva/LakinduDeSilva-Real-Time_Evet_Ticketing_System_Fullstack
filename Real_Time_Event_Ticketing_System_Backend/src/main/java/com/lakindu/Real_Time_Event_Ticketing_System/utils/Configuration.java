package com.lakindu.Real_Time_Event_Ticketing_System.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Configuration {

    private final int totalTickets;  // Total tickets available in the system
    private final int ticketReleaseRate;  // Rate at which tickets are released
    private final int customerRetrievalRate;  // Rate at which customers retrieve tickets
    private final int maxTicketCapacity;  // Maximum capacity of the ticket pool

    // Constructor using @JsonCreator to initialize properties from JSON file
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

    // Getters for each field
    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }


}
