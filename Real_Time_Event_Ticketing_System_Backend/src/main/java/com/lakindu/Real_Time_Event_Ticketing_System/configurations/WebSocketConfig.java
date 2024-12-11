package com.lakindu.Real_Time_Event_Ticketing_System.configurations;

import com.lakindu.Real_Time_Event_Ticketing_System.services.WebSocketTicketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocketConfig configures WebSocket and CORS settings for the application.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketTicketHandler ticketHandler;

    /**
     * Configures CORS settings for the application.
     *
     * @return a WebMvcConfigurer with CORS settings
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000") // Frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    /**
     * Constructor for WebSocketConfig.
     *
     * @param ticketHandler the WebSocket handler for ticketing
     */
    public WebSocketConfig(WebSocketTicketHandler ticketHandler) {
        this.ticketHandler = ticketHandler;
    }

    /**
     * Registers WebSocket handlers.
     *
     * @param registry the WebSocketHandlerRegistry to register handlers with
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(ticketHandler, "/ws/ticket").setAllowedOrigins("*");
    }
}