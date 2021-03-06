package ru.job4j.hibernate.hql;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table (name = "candidates")
public class Candidate {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int experience;
    private float salary;

    @OneToOne(fetch = FetchType.LAZY)
    private VacBase vacbase;

    public Candidate(String name, int experience, float salary) {
        this.name = name;
        this.experience = experience;
        this.salary = salary;
    }

    public Candidate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public VacBase getVacBase() {
        return vacbase;
    }

    public void setVacBase(VacBase vacbase) {
        this.vacbase = vacbase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return id == candidate.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Candidate{id=" + id
                + ", name='" + name + '\''
                + ", experience=" + experience
                + ", salary=" + salary
                + ", vacBase=" + vacbase
                + '}';
    }
}
