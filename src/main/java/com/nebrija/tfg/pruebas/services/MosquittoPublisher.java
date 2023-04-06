package com.nebrija.tfg.pruebas.services;


import com.nebrija.tfg.pruebas.model.api.ApiTurnResponseDto;

import java.util.List;

public interface MosquittoPublisher {

    boolean publish(String topic, String message, int qos, long timeout);
    String subscribe(String topic, int qos);

    List<String> getClientsSubscribedToTopic(String topic);

    String disconnect(String clientId);

    ApiTurnResponseDto addTurn(String name);


}
