package org.example;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

class User {
    /*
    * The user should contain username password.
    * The user should contain an ArrayList of favorite shows and watch history.
    * FUNCTION: the user should have a function to watch a show and add it to watch history.
    *  *** NOTE: All search functions in user are for searching in favorite shows ***
    */

    private String password;
    private String username;
    private String watchHistory;

    public String getWatchHistory() {
        return watchHistory.toString();
    }

    public void addToWatchHistory(String title, NetflixService netflixService){
        try {
            if(netflixService.doesMovieExists(title)){
                String temp = " , ";
                this.watchHistory += temp + title;
            }
            else System.out.println("No such movie title");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User(String password, String username) {
        this.password = password;
        this.username = username;
        watchHistory = "";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<TVShow> searchByTitle(String title, NetflixService netflixService) {
        ArrayList<TVShow> tvShows = new ArrayList<TVShow>();

        return null;
    }
    public ArrayList<Movie> searchByGenre(String genre, NetflixService netflixService) throws SQLException {
        return netflixService.searchByGenre(genre);
    }
    public ArrayList<Movie> searchByReleaseYear(int year, NetflixService netflixService) throws SQLException {
        return netflixService.searchByReleaseYear(year);
    }
    public void addToFavorites(Movie movie, NetflixService netflixService) throws SQLException{
        netflixService.addToFavorites(movie, this.username);
    }
    public void viewFavorites(NetflixService netflixService) throws  SQLException {
        System.out.println(netflixService.viewFavorites(this.username));
    }
    public ArrayList<TVShow> getRecommendations() {
        // Implement get recommendations logic here
        return null;
    }
}
