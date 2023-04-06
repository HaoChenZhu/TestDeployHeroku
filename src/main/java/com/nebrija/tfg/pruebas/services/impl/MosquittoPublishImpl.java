package com.nebrija.tfg.pruebas.services.impl;

import com.nebrija.tfg.pruebas.config.MqttBeans;
import com.nebrija.tfg.pruebas.dao.mongodb.repository.TurnMongoRepository;
import com.nebrija.tfg.pruebas.model.api.ApiTurnResponseDto;
import com.nebrija.tfg.pruebas.services.MosquittoPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.nebrija.tfg.pruebas.constants.Constants.*;
import static com.nebrija.tfg.pruebas.constants.Constants.MQTT_BROKER_CERT;

@Service
public class MosquittoPublishImpl implements MosquittoPublisher {

    @Autowired
    private MqttBeans client;

    @Autowired
    TurnMongoRepository turnMongoRepository;

    private List<MqttBeans> activeSubscriptions = new ArrayList<>();

    @Override
    public boolean publish(String topic, String message, int qos, long timeout) {
            client.publish(topic,message,qos,timeout);
            return true;
    }

    @Override
    public String subscribe(String topic, int qos) {
        try{
        this.client = new MqttBeans(MQTT_BROKER_HOST, MQTT_BROKER_USER, MQTT_BROKER_PASSWORD, MQTT_BROKER_CERT, UUID.randomUUID().toString());
        activeSubscriptions.add(this.client);
        client.subscribe(topic,qos);
        String messageReceived = client.getMessageReceived();
        return messageReceived;
        }catch (Exception e){
            return "Error";
        }
    }

    @Override
    public List<String> getClientsSubscribedToTopic(String topic) {
        List<String> clients = new ArrayList<>();
        for (MqttBeans subscription : activeSubscriptions) {
          //  if (subscription.topic.equals(topic)) {
            if(subscription.getClientId()!=null){
                clients.add(subscription.getClientId());
            }
           // }
        }
        return clients;
    }

    @Override
    public String disconnect(String clientId) {
        for (MqttBeans subscription : activeSubscriptions) {
            if (subscription.getClientId() != null) {
                System.out.println(subscription.getClientId());
                if (subscription.getClientId().equals(clientId)) {
                    subscription.disconnect();
                    return "Disconnect: " + clientId ;
                }
            }
        }
        return "Client not found";
    }

    @Override
    public ApiTurnResponseDto addTurn(String name) {
        return null;
    }
}
