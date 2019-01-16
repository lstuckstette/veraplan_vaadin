package com.volavis.veraplan.spring.views.components;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class RatingComponent extends HorizontalLayout {

    private int rating = 0;
    private int maxscore = 5;
    private boolean clickToChange = false;

    public RatingComponent() {
        this.addClassName("rating-component");
        render();
    }

    public void activateClickToChangeRating() {
        this.clickToChange = true;
        render();
    }

    public RatingComponent(int rating) {
        this.rating = rating;
        this.addClassName("rating-component");
        render();
    }

    public void setRating(int rating) {
        this.rating = rating;
        render();
    }

    public int getRating() {
        return rating;
    }

    private void render() {
        this.removeAll();
        //add rating = filled stars
        for (int r = 0; r < rating; r++) {
            Icon icon = new Icon(VaadinIcon.STAR);
            if (clickToChange) {
                final int finalCopy = r;
                icon.addClickListener(iconClickEvent -> this.setRating(finalCopy+1));
            }
            this.add(icon);
        }
        //add not set rating until maxscore = empty stars
        for (int e = rating; e < maxscore; e++) {
            Icon icon = new Icon(VaadinIcon.STAR_O);
            if (clickToChange) {
                final int finalCopy = e;
                icon.addClickListener(iconClickEvent -> this.setRating(finalCopy+1));
            }
            this.add(icon);
        }
    }
}
