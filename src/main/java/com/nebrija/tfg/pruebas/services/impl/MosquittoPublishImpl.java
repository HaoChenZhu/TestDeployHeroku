package com.nebrija.tfg.pruebas.services.impl;

import com.nebrija.tfg.pruebas.config.MqttBeans;
import com.nebrija.tfg.pruebas.services.MosquittoPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MosquittoPublishImpl implements MosquittoPublisher {

    @Autowired
    private MqttBeans mqttBeans;
    @Override
    public boolean publish(String topic, String message, int qos, long timeout) {
        try {
            mqttBeans.publish(topic,message,qos,timeout);

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
