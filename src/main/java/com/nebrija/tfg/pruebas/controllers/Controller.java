package com.nebrija.tfg.pruebas.controllers;

import com.nebrija.tfg.pruebas.config.MqttBeans;
import com.nebrija.tfg.pruebas.model.api.ApiGeneralResponse;
import com.nebrija.tfg.pruebas.services.MosquittoPublisher;
import io.swagger.annotations.Api;

import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mongodb.assertions.Assertions.assertNotNull;


@RestController
@Api(tags = "Prueba API")
@RequestMapping(value = "${chen.base_path}")
public class Controller implements TestApi {
    @Autowired
    private MqttBeans mqttBeans;
    @Autowired
    private MosquittoPublisher mosquittoPublisher;
    @Override
    public ResponseEntity<ApiGeneralResponse> test() {
        mqttBeans.clientConnect();

        if (mqttBeans!=null) {
            System.out.println("Conectado");
        }
        mqttBeans.disconnect();

        ApiGeneralResponse generalResponse = new ApiGeneralResponse();
        generalResponse.setCode("200");
        generalResponse.setMessage("Hola mundo");
        return new ResponseEntity<>(generalResponse,HttpStatus.OK);
    }

    @PostMapping(value = "/publish")
    public ResponseEntity<ApiGeneralResponse> testPublish() {
        String message = "Hola mundo desde el publish";

        String topic = "test";
        ApiGeneralResponse generalResponse = new ApiGeneralResponse();
        generalResponse.setCode("200");
        generalResponse.setMessage("Mensaje enviado");
        mosquittoPublisher.publish(topic,message,2,1000L);
        mqttBeans.disconnect();
        return new ResponseEntity<>(generalResponse,HttpStatus.OK);
    }

    @GetMapping(value = "/subscribe")
    public ResponseEntity<ApiGeneralResponse> subscribe() {
        String topic = "test";
        ApiGeneralResponse generalResponse = new ApiGeneralResponse();
        generalResponse.setCode("200");
        generalResponse.setMessage("Suscrito y recibido");
        mqttBeans.subscribe(topic,2);
        return new ResponseEntity<>(generalResponse,HttpStatus.OK);
    }





}
