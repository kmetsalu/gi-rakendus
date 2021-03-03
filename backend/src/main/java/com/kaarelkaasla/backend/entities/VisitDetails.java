package com.kaarelkaasla.backend.entities;

import javax.persistence.*;

@Entity
@Table(name = "visit_details")
public class VisitDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "dep")
    private String dep;

    @Column(name = "visit_time")
    private String visit_time;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "email")
    private String email;

    @Column(name = "identification")
    private String identification;

    @Column(name = "date_of_birth")
    private String date_of_birth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age_at_visit")
    private Integer age_at_visit;

    public VisitDetails() {
    }

    public VisitDetails(String code, String dep, String visit_time,
                        String first_name, String last_name, String email,
                        String identification, String date_of_birth, String gender, Integer age_at_visit) {
        this.code = code;
        this.dep = dep;
        this.visit_time = visit_time;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.identification = identification;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.age_at_visit = age_at_visit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getVisit_time() {
        return visit_time;
    }

    public void setVisit_time(String visit_time) {
        this.visit_time = visit_time;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String id_code) {
        this.identification = id_code;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge_at_visit() {
        return age_at_visit;
    }

    public void setAge_at_visit(Integer age_at_visit) {
        this.age_at_visit = age_at_visit;
    }
}
