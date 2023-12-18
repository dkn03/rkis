package rkis_8.jms;

import jakarta.jms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Component
public class MessageReceiver {

    private ConnectionFactory connectionFactory;
    private JmsTemplate jmsTemplate;

    @Autowired
    public MessageReceiver(ConnectionFactory connectionFactory){
        this.connectionFactory = connectionFactory;
        this.jmsTemplate = new JmsTemplate(connectionFactory);
    }


    public String receiveMessage(String queueName) throws JMSException {
        jmsTemplate.setReceiveTimeout(100);
        Message message = jmsTemplate.receive(queueName);
        TextMessage textMessage = (TextMessage) message;
        try {
            return textMessage.getText();
        }
        catch (JMSException e) {
            e.printStackTrace();
            return null;
        }
    }

}
