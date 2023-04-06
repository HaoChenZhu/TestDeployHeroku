package com.nebrija.tfg.pruebas.dao.mongodb.entities;

import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

public class ClientTurn {
    @Id
    private String id;
    @Field("client_id")
    private String clientId;
    @Field("turn_number")
    private String turnNumber;
    @Field("status")
    private String status;

}
