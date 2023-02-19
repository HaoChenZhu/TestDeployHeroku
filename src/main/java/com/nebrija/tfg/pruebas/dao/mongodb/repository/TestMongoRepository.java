package com.nebrija.tfg.pruebas.dao.mongodb.repository;

import com.nebrija.tfg.pruebas.dao.mongodb.entities.Test;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMongoRepository extends MongoRepository<Test, ObjectId> {

}
