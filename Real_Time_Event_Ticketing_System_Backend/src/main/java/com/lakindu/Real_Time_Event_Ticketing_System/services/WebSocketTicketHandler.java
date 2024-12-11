package com.lakindu.Real_Time_Event_Ticketing_System.services;

import com.lakindu.Real_Time_Event_Ticketing_System.models.Customer;
import com.lakindu.Real_Time_Event_Ticketing_System.models.Vendor;
import com.lakindu.Real_Time_Event_Ticketing_System.utils.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Component
public class WebSocketTicketHandler implements WebSocketHandler {

    private List<Integer> tickets; // Use ArrayList to store tickets
    private int maxCapacity;
    private static final Logger logger = Logger.getLogger(WebSocketTicketHandler.class.getName());

    private Thread vendorThread1;
    private Thread vendorThread2;
    private Thread customerThread1;
    private Thread customerThread2;

    private WebSocketSession session; // Store the WebSocket session

    private volatile boolean isRunning = true; // Flag to control thread execution

    // Start the ticketing process with configuration
    public void startTicketing(Configuration config) {
        this.maxCapacity = config.getMaxTicketCapacity();
        this.tickets = Collections.synchronizedList(new ArrayList<>());

        isRunning = true; // Set the flag to true when starting the process

        // Initialize Vendor and Customer with the configuration
        Vendor vendor1 = new Vendor(1, this, config.getTicketReleaseRate(), config.getTotalTickets());
        Vendor vendor2 = new Vendor(2, this, config.getTicketReleaseRate(), config.getTotalTickets());
        Customer customer1 = new Customer(this, config.getCustomerRetrievalRate(), 1);
        Customer customer2 = new Customer(this, config.getCustomerRetrievalRate(), 2);

        // Start the threads for vendors and customers
        vendorThread1 = new Thread(vendor1);
        vendorThread2 = new Thread(vendor2);
        customerThread1 = new Thread(customer1);
        customerThread2 = new Thread(customer2);

        vendorThread1.start();
        vendorThread2.start();
        customerThread1.start();
        customerThread2.start();

        System.out.println("Ticketing process started successfully.");
    }

    // Stop the ticketing process
    public void stopTicketing() {
        isRunning = false; // Set the flag to false to stop threads

        synchronized (this) {
            notifyAll(); // Wake up all threads waiting in `wait()`
        }

        // Interrupt threads to release them from blocking calls
        if (vendorThread1 != null && vendorThread1.isAlive()) vendorThread1.interrupt();
        if (vendorThread2 != null && vendorThread2.isAlive()) vendorThread2.interrupt();
        if (customerThread1 != null && customerThread1.isAlive()) customerThread1.interrupt();
        if (customerThread2 != null && customerThread2.isAlive()) customerThread2.interrupt();

        // Wait for threads to finish
        try {
            if (vendorThread1 != null) vendorThread1.join();
            if (vendorThread2 != null) vendorThread2.join();
            if (customerThread1 != null) customerThread1.join();
            if (customerThread2 != null) customerThread2.join();
        } catch (InterruptedException e) {
            logger.warning("Error while waiting for threads to terminate: " + e.getMessage());
        }

        vendorThread1 = null;
        vendorThread2 = null;
        customerThread1 = null;
        customerThread2 = null;

        sendMessageToClient("Ticketing process has been stopped.");
        System.out.println("Ticketing process has been stopped.");
    }

    public synchronized void addTickets(int ticket, int vendorId) {
        while (tickets.size() >= maxCapacity) {
            if (!isRunning) return; // Exit if the system is stopping
            try {
                String message = "TicketPool is full. Vendor " + vendorId + " is waiting to add Ticket " + ticket;
                logger.info(message);
                sendMessageToClient(message);
                sendMessageToClient(String.valueOf(tickets.size()));
                wait(); // Wait until there is space in the pool
            } catch (InterruptedException e) {
                logger.warning("Vendor " + vendorId + " interrupted while waiting.");
                Thread.currentThread().interrupt(); // Re-interrupt the thread
                return;
            }
        }
        if (!isRunning) return; // Check before continuing
        tickets.add(ticket);
        String message = "Ticket " + ticket + " added by Vendor " + vendorId + " | Remaining Total Tickets: " + tickets.size();
        logger.info(message);
        sendMessageToClient(String.valueOf(tickets.size()));
        sendMessageToClient(message);
        notifyAll();
    }

    public synchronized void removeTicket(int customerId) {
        while (tickets.isEmpty()) {
            if (!isRunning) return; // Exit if the system is stopping
            try {
                String message = "No tickets available. Waiting for tickets...";
                logger.info(message);
                sendMessageToClient(message);
                wait();
            } catch (InterruptedException e) {
                logger.warning("Customer " + customerId + " interrupted.");
                Thread.currentThread().interrupt(); // Re-interrupt the thread
                return;
            }
        }
        if (!isRunning) return; // Check before continuing
        int ticket = tickets.removeFirst();
        String message = "Ticket " + ticket + " bought by Customer " + customerId + " | Remaining Total Tickets: " + tickets.size();
        logger.info(message);
        sendMessageToClient(message);
        sendMessageToClient(String.valueOf(tickets.size()));
        notifyAll();
    }

    public void sendMessageToClient(String message) {
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                logger.warning("Error sending message to client: " + e.getMessage());
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session; // Save the session for sending messages
        sendMessageToClient("Connection established.");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // Handle incoming WebSocket messages, if needed
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Transport error occurred: " + exception.getMessage());
        session.close(CloseStatus.SERVER_ERROR);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Connection closed with status: " + status);
        this.session = null; // Clear the session reference
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}

