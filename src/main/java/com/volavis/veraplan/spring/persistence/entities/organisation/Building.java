package com.volavis.veraplan.spring.persistence.entities.organisation;


import com.volavis.veraplan.spring.persistence.entities.ressources.Room;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "buildings")
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max=40)
    private String shortName;

    @OneToMany(mappedBy = "building")
    private List<Room> rooms;


    public Building() {
    }

    public Building(@NotBlank @Size(max = 100) String name) {
        this.name = name;
    }



    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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
