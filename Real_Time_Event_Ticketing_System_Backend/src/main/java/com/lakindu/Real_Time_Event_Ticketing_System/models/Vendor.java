package com.lakindu.Real_Time_Event_Ticketing_System.models;

import com.lakindu.Real_Time_Event_Ticketing_System.services.WebSocketTicketHandler;

import java.util.logging.Logger;

public class Vendor implements Runnable {
    private final int vendorId;
    private final WebSocketTicketHandler ticketPool;
    private final int ticketReleaseRate;
    private final int totalTickets;
    private static int currentTicketId = 1;
    private static final Logger logger = Logger.getLogger(Vendor.class.getName());
    private static final Object lock = new Object();
    public static Boolean allReleased = false;

    public Vendor(int vendorId, WebSocketTicketHandler ticketPool, int ticketReleaseRate, int totalTickets) {
        this.vendorId = vendorId;
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.totalTickets = totalTickets;
    }

    public static synchronized int getCurrentTicketId() {
        return currentTicketId++;
    }

    @Override
    public void run() {
        while (currentTicketId<=totalTickets){
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
    public void changeAllReleased(){
        System.out.println("Vendor "+vendorId + " has released all tickets.");
        allReleased=true;
    }
}