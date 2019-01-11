package com.volavis.veraplan.spring.persistence.entities.ressources;

import com.volavis.veraplan.spring.persistence.entities.organisation.Building;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 50)
    private String shortName;

    private int chairCount;

    private int capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

//    @OneToMany(mappedBy = "room")
//    private List<Equipment> equipment = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RoomType type;

    public Room() {
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getChairCount() {
        return chairCount;
    }

    public void setChairCount(int chairCount) {
        this.chairCount = chairCount;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

//    public List<Equipment> getEquipment() {
//        return equipment;
//    }
//
//    public void setEquipment(List<Equipment> equipment) {
//        this.equipment = equipment;
//    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }
}
