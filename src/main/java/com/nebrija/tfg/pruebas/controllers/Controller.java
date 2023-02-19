package com.nebrija.tfg.pruebas.controllers;

import com.nebrija.tfg.pruebas.model.api.ApiGeneralResponse;
import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = "Prueba API")
@RequestMapping(value = "${chen.base_path}")
public class Controller implements TestApi {

    @Override
    public ResponseEntity<ApiGeneralResponse> test() {
        ApiGeneralResponse generalResponse = new ApiGeneralResponse();
        generalResponse.setCode("200");
        generalResponse.setMessage("Hola mundo");
        return new ResponseEntity<>(generalResponse,HttpStatus.OK);
    }

}
