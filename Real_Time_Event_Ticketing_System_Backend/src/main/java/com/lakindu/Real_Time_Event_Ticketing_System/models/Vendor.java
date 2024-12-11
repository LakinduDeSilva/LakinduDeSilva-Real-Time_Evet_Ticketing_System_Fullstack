package com.lakindu.Real_Time_Event_Ticketing_System.models;

import com.lakindu.Real_Time_Event_Ticketing_System.services.WebSocketTicketHandler;
import java.util.logging.Logger;

/**
 * Vendor class represents a vendor in the ticketing system.
 * Implements Runnable to allow vendor actions to be executed in a separate thread.
 */
public class Vendor implements Runnable {
    private final int vendorId;
    private final WebSocketTicketHandler ticketPool;
    private final int ticketReleaseRate;
    private final int totalTickets;
    private static int currentTicketId = 1;
    private static final Logger logger = Logger.getLogger(Vendor.class.getName());
    private static final Object lock = new Object();
    public static Boolean allReleased = false;

    /**
     * Constructor for Vendor.
     *
     * @param vendorId the unique ID of the vendor
     * @param ticketPool the WebSocketTicketHandler managing the ticket pool
     * @param ticketReleaseRate the rate at which the vendor releases tickets
     * @param totalTickets the total number of tickets to be released by the vendor
     */
    public Vendor(int vendorId, WebSocketTicketHandler ticketPool, int ticketReleaseRate, int totalTickets) {
        this.vendorId = vendorId;
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.totalTickets = totalTickets;
    }

    /**
     * Gets the current ticket ID and increments it.
     *
     * @return the current ticket ID
     */
    public static synchronized int getCurrentTicketId() {
        return currentTicketId++;
    }

    /**
     * The run method contains the logic for the vendor's actions.
     * Continuously releases tickets to the ticket pool at the specified rate until all tickets are released.
     */
    @Override
    public void run() {
        while (currentTicketId <= totalTickets) {
            synchronized (lock) {
                try {
                    Thread.sleep(ticketReleaseRate);
                    int ticket = Vendor.getCurrentTicketId();
                    ticketPool.addTickets(ticket, vendorId);
                    lock.notifyAll();
                    if (currentTicketId <= totalTickets) {
                        lock.wait(); // Wait for the other vendor to release tickets
                    }
                } catch (InterruptedException e) {
                    logger.warning("Vendor " + vendorId + " interrupted: " + e.getMessage());
                } catch (Exception e) {
                    logger.severe("Error in Vendor " + vendorId + " thread: " + e.getMessage());
                }
            }
        }
        changeAllReleased();
    }

    /**
     * Changes the allReleased flag to true and logs that all tickets have been released.
     */
    public void changeAllReleased() {
        System.out.println("Vendor " + vendorId + " has released all tickets.");
        allReleased = true;
    }
}