package com.nebrija.tfg.pruebas.dao.mongodb.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "turns")
public class Turns {
    @Id
    private String _id;
    @Field("name")
    private String name;
    @Field("estimated_time")
    private int estimatedTime;
    @Field("client_turn_list")
    private List<ClientTurn> clientTurnList;
}
