package com.nebrija.tfg.pruebas.dao.mongodb.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class AuditableEntity {

    @Field("date_created")
    @CreatedDate
    private LocalDateTime dateCreated;

    @Field("modification_date")
    @LastModifiedDate
    private LocalDateTime modificationDate;

    @Field("deleted_date")
    private LocalDateTime deletedDate;

    @Field("creation_user")
    @CreatedBy
    private String creationUser;

    @Field("modification_user")
    @LastModifiedBy
    private String modificationUser;

}