package com.nebrija.tfg.pruebas.dao.mongodb.repository.impl;

import com.nebrija.tfg.pruebas.dao.mongodb.entities.Test;
import com.nebrija.tfg.pruebas.dao.mongodb.repository.TestMongoRepository;
import com.nebrija.tfg.pruebas.dao.mongodb.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class TestRepositoryImpl implements TestRepository {

    @Autowired
    private TestMongoRepository testMongoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Test> findAll() {
        return testMongoRepository.findAll();
    }
}
