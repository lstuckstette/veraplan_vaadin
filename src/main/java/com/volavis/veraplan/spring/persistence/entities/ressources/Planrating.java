package com.volavis.veraplan.spring.persistence.entities.ressources;

import com.volavis.veraplan.spring.persistence.entities.User;

import javax.persistence.*;

@Entity
@Table(name = "planratings")
public class Planrating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int planId;

    private int rating;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public Planrating(int rating) {
        this.rating = rating;
    }

    public Planrating() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
