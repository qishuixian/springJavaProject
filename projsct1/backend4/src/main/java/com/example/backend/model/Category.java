package com.example.backend.model;

import jakarta.persistence.*;

import javax.naming.Name;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 分类名称不能重复
    @Column(nullable = false,unique = true)
    private  String name;

    // 无参构造
    public Category() {}

    //全参构造
    public  Category(Long id,String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
