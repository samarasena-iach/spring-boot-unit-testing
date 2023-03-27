package com.samiach.springbootunittesting.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee {
    /**
     * @Entity annotation is used to mark the class as a persistent Java class.
     * @Table annotation is used to provide the details of the table that this entity will be mapped to.
     * @Id annotation is used to define the primary key.
     * @GeneratedValue annotation is used to define the primary key generation strategy.
     * In the above case, we have declared the primary key to be an Auto Increment field.
     * @Column annotation is used to define the properties of the column that will be mapped to the annotated field.
     * You can define several properties like name, length, nullable, updateable, etc.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;
}


