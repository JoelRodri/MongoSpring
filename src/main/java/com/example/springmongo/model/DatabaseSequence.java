package com.example.springmongo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Data
@Document(collection = "database_sequences")
public class DatabaseSequence {

    @Id
    private String id;
    private int seq;

    public DatabaseSequence() {}
}
