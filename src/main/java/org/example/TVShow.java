package org.example;

import java.util.ArrayList;

class TVShow extends Movie{
    /*
    *The TVShow should have a title , genre, release year, duration and a rating.
     *The TVShow should have an ArrayList of the cast.
     */
     private int numberOfSeasons;
     private int lastReleaseYear;

    public TVShow(String title, String genre, int releaseYear, int duration, float rating, int numberOfSeasons, int lastRealeaseYear) {
        super(title, genre, releaseYear, duration, rating);
        this.numberOfSeasons = numberOfSeasons;
        this.lastReleaseYear = lastRealeaseYear;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public int getLastReleaseYear() {
        return lastReleaseYear;
    }

    public void setLastReleaseYear(int lastReleaseYear) {
        this.lastReleaseYear = lastReleaseYear;
    }

    @Override
    public String toString() {
        return "TVShow{" + super.toString() +
                "numberOfSeasons=" + numberOfSeasons +
                ", lastReleaseYear=" + lastReleaseYear +
                '}';
    }
}
