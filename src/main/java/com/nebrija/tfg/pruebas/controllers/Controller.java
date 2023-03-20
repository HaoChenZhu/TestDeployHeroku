package com.nebrija.tfg.pruebas.controllers;

import com.nebrija.tfg.pruebas.config.MqttBeans;
import com.nebrija.tfg.pruebas.model.api.ApiGeneralResponse;
import io.swagger.annotations.Api;

import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mongodb.assertions.Assertions.assertNotNull;


@RestController
@Api(tags = "Prueba API")
@RequestMapping(value = "${chen.base_path}")
public class Controller implements TestApi {
    @Autowired
    private MqttBeans mqttBeans;
    @Override
    public ResponseEntity<ApiGeneralResponse> test() {
        MqttClient client = mqttBeans.mqttClient();
        if (client != null && client.isConnected()) {
            System.out.println("MQTT connection established successfully");
            // Aquí puedes enviar y recibir mensajes MQTT utilizando el objeto 'client'
        } else {
            System.out.println("MQTT connection failed");
        }
        ApiGeneralResponse generalResponse = new ApiGeneralResponse();
        generalResponse.setCode("200");
        generalResponse.setMessage("Hola mundo");
        return new ResponseEntity<>(generalResponse,HttpStatus.OK);
    }





}
