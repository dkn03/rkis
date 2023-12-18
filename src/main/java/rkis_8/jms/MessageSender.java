package rkis_8.jms;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    ConnectionFactory connectionFactory;
    private JmsTemplate jmsTemplate;

    @Autowired
    MessageSender(ConnectionFactory connectionFactory){
        this.connectionFactory = connectionFactory;
        this.jmsTemplate = new JmsTemplate(connectionFactory);
    }


    public void sendMessage(String queueName, String message) {
        jmsTemplate.convertAndSend(queueName, message);
    }
}
