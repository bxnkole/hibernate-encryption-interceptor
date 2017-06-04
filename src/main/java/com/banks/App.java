package com.banks;

import com.banks.model.Person;

/**
 * Created by banks on 6/4/17.
 */
public class App {

    public static void main(String[] args) {
        HibernateUtil hibernateUtil = new HibernateUtil();
        Person person = new Person(10, "Bankole", "johndoe@gmail.com", false);
        hibernateUtil.create(person);
        System.out.println(person);

        person.setEmail("janedoe@gmail.com");
        hibernateUtil.update(person);
        System.out.println(person);
    }

}
