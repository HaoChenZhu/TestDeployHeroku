package com.nebrija.tfg.pruebas.dao.mongodb.repository;

import com.nebrija.tfg.pruebas.dao.mongodb.entities.Turns;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnMongoRepository extends MongoRepository<Turns, String> {
}
