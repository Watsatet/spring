package com.entoo.demo.document;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Document("user")
@Getter
@Setter
public class User
{
    @Id
    private String id;
    @NotNull
    private String name;
    @NotBlank
    private String phone;
    @NotBlank
    @Email
    private String email;
    @Pattern(regexp = "^[0-9]{10}$")
    private String password;
    private String gender;
    private String favourite_fruit;
    private LocalDate dob;
}


