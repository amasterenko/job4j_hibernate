package ru.job4j.hibernate.lazy;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "models")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "make_id")
    private Make make;

    public Model(String name) {
        this.name = name;
    }

    public Model() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Make getMake() {
        return make;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMake(Make lmake) {
        this.make = lmake;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Model lmodel = (Model) o;
        return id == lmodel.id;
    }

    @Override
    public String toString() {
        return "Model{name='" + name + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
