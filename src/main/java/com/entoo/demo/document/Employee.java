package com.entoo.demo.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("employee")
@Setter
@Getter
public class Employee
{
    @Id
    private String id;
    private String gender;
}
