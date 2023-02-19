package com.nebrija.tfg.pruebas.dao.mongodb.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@Document(collection = "qrnotify_test")
public class Test extends AuditableEntity{

    @MongoId
    private ObjectId _id;

    @Field("name")
    private String name;

    @Field("subname")
    private String subname;
}
