package com.banks.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by banks on 6/4/17.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private int age;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private Boolean activeStatus;

    public Person(int age, String name, String email, Boolean activeStatus) {
        this.age = age;
        this.name = name;
        this.email = email;
        this.activeStatus = activeStatus;
    }
}
