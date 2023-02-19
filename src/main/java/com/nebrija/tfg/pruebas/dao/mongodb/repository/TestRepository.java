package com.nebrija.tfg.pruebas.dao.mongodb.repository;

import com.nebrija.tfg.pruebas.dao.mongodb.entities.Test;

import java.util.List;

public interface TestRepository {

    List<Test> findAll();
}
