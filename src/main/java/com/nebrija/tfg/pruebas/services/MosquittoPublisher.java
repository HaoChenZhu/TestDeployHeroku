package com.nebrija.tfg.pruebas.services;

import org.springframework.stereotype.Service;

@Service
public interface MosquittoPublisher {

    boolean publish(String topic, String message, int qos, long timeout);

}
