package ru.job4j.hibernate.lazy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "makes")
public class Make {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(mappedBy = "make")
    private final List<Model> models = new ArrayList<>();

    public Make(String name) {
        this.name = name;
    }

    public Make() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Make lmake = (Make) o;
        return id == lmake.id;
    }

    @Override
    public String toString() {
        return "Make{name='" + name + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
