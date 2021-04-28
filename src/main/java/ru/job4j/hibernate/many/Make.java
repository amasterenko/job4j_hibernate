package ru.job4j.hibernate.many;

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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Model> models = new ArrayList<>();

    public Make(String name) {
        this.name = name;
    }

    public Make() {
    }

    public void addModel(Model model) {
        this.models.add(model);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
        Make make = (Make) o;
        return id == make.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
