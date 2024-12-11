package com.lakindu.Real_Time_Event_Ticketing_System.models;

import com.lakindu.Real_Time_Event_Ticketing_System.services.WebSocketTicketHandler;

import java.util.logging.Logger;

public class Customer implements Runnable {
    private final WebSocketTicketHandler ticketPool;
    private final int customerRetrievalRate;
    private final int customerId;
    private static final Logger logger = Logger.getLogger(Customer.class.getName());
    private static final Object lock = new Object();

    public Customer(WebSocketTicketHandler ticketPool, int customerRetrievalRate, int customerId) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lock){
                try {
                    Thread.sleep(customerRetrievalRate);
                    ticketPool.removeTicket(customerId);
                    lock.notifyAll();
                    lock.wait();
                } catch (InterruptedException e) {
                    logger.warning("Customer" +customerId+ " interrupted: " + e.getMessage());

                } catch (Exception e) {
                    logger.severe("Error in Customer" +customerId+ " thread: " + e.getMessage());
                }
            }
        }
    }
}
