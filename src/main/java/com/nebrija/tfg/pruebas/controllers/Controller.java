package com.nebrija.tfg.pruebas.controllers;

import com.nebrija.tfg.pruebas.model.api.ApiGeneralResponse;
import com.nebrija.tfg.pruebas.services.MosquittoPublisher;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@Api(tags = "Prueba API")
@RequestMapping(value = "${chen.base_path}")
@Slf4j
public class Controller implements TestApi {
    @Autowired
    private MosquittoPublisher mosquittoPublisher;

    @Autowired
    private MqttServer mqttServer;

    @Override
    public ResponseEntity<ApiGeneralResponse> test() {
        ApiGeneralResponse generalResponse = new ApiGeneralResponse();
        List<String> list= mosquittoPublisher.getClientsSubscribedToTopic("test");
        generalResponse.setCode("200");
        generalResponse.setMessage(list.toString());
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/publish")
    public ResponseEntity<ApiGeneralResponse> testPublish() {
        String message = "Hola mundo desde el publish";
        String topic = "test";
        ApiGeneralResponse generalResponse = new ApiGeneralResponse();
        generalResponse.setCode("200");
        generalResponse.setMessage("Mensaje enviado");
        mosquittoPublisher.publish(topic, message, 2, 1000L);
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/subscribe")
    public ResponseEntity<ApiGeneralResponse> subscribe() {
        String topic = "test";
        String message= mosquittoPublisher.subscribe(topic, 2);
        ApiGeneralResponse generalResponse = new ApiGeneralResponse();
        generalResponse.setCode("200");
        generalResponse.setMessage("Mensaje recibido: "+ message);
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/disconnect")
    public ResponseEntity<ApiGeneralResponse> disconnect(@ApiParam(value = "client_id",required = true) String client_id) {
        mosquittoPublisher.disconnect(client_id);
        ApiGeneralResponse generalResponse = new ApiGeneralResponse();
        generalResponse.setCode("200");
        generalResponse.setMessage("Desconectado");
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

   /* @RequestMapping(value = "/publish2")
    public ResponseEntity<ApiGeneralResponse> testPublish2() {
        mqttServer.sendMQTTMessage("test", "Hola mundo desde el publish2",2);
        ApiGeneralResponse generalResponse = new ApiGeneralResponse();
        generalResponse.setCode("200");
        generalResponse.setMessage("Mensaje enviado");
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/subscribe2")
    public ResponseEntity<ApiGeneralResponse> subscribe2() {
        mqttServer.init("test", 2);
        ApiGeneralResponse generalResponse = new ApiGeneralResponse();
        generalResponse.setCode("200");
        generalResponse.setMessage("Mensaje recibido: ");
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }*/
}
