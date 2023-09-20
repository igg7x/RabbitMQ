package com.example.RabbitMQ.dto;

import lombok.Data;

@Data
public class User {

    private Integer id;
    private String firstName;
    private String lastName;

    @Override
    public String toString() {
        return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }

}
