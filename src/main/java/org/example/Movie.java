package org.example;

import java.util.ArrayList;

class Movie {
    /*
     *Movie is extended from TVShow and has extra attribute length.
     */

    private String title;
    private String genre;
    private int releaseYear;
    private int duration;
    private double rating;
    private String casts;

    public Movie(String title, String genre, int realeseYear, int duration, float rating) {
        this.title = title;
        this.genre = genre;
        this.releaseYear = realeseYear;
        this.duration = duration;
        this.rating = rating;
        this.casts = "";
    }


    public void addCast(String cast){
        String s = " , " + cast;
        this.casts += s;
    }

    public void setCasts(String casts) {
        this.casts = casts;
    }

    public String getCasts() {
        return casts;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int realeseYear) {
        this.releaseYear = realeseYear;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseYear=" + releaseYear +
                ", duration=" + duration +
                ", rating=" + rating +
                ", casts=" + this.getCasts() +
                '}';
    }

}
