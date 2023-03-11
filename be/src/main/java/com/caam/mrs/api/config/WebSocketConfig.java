package com.caam.mrs.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//@Configuration
//@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {

	@Value(("${spring.rabbitmq.host}"))
    String rabbitMQHost;
    
    @Value(("${spring.rabbitmq.port}"))
    Integer rabbitMQPort;
    
    @Value(("${spring.rabbitmq.username}"))
    String rabbitMQUsername;
    
    @Value(("${spring.rabbitmq.password}"))
    String rabbitMQUserPassword;
	
	@Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/topic", "/queue/");
		config.setApplicationDestinationPrefixes("/app");
		config.setUserDestinationPrefix("/user");
		config.enableStompBrokerRelay("/topic", "/queue", "/exchange", "/amq/queue")
			.setRelayHost(rabbitMQHost)
			.setRelayPort(rabbitMQPort)
			.setClientLogin(rabbitMQUsername)
			.setClientPasscode(rabbitMQUserPassword);
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/mmrs-notification")
        .setAllowedOrigins("*").withSockJS();
    }

}
