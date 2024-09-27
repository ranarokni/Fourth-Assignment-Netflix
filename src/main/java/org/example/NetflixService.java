package org.example;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
class NetflixService {
    /*
     *The NetflixService should have an Arraylist of users, tv shows and movies.
     *The NetflixService should have a User object which represents current user.
     */
    private Connection connection;
    private String fileName;
    public NetflixService(String filename){
        this.fileName = filename;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void connect() {
//        this.connection = null;
        try {
//            DriverManager.registerDriver(new org.sqlite.JDBC.Driver());
//            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:netflix.db";
            this.connection = DriverManager.getConnection(url);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (this.connection != null) {
                    this.connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void createUserTable(){
        String query = "CREATE TABLE IF NOT EXISTS Users(" +
                "username text PRIMARY KEY " +
                "password text NOT NULL" +
                "watch history text)";
        try {
            Statement statement = this.connection.createStatement();
            statement.execute(query);
        } catch (Exception e){
            System.out.println("HEY");
        }
    }

    public void createFavoriteMoviesTable(){
        String query = "CREATE TABLE IF NOT EXISTS FavoriteMovies(" +
                "username text NOT NULL" +
                "movieName text NOT NULL)";
        try {
            Statement statement = this.connection.createStatement();
            statement.execute(query);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void createMovieTable(){
        String query = "CREATE TABLE IF NOT EXISTS Movies(" +
                "title text PRIMARY KEY" +
                "genre text NOT NULL" +
                "releaseYear integer NOT NULL" +
                "duration integer NOT NULL" +
                "rating REAL NOT NULL" +
                "casts text NOT NULL)";
        try {
            Statement statement = this.connection.createStatement();
            statement.execute(query);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void createTVShowTable(){
        String query = "CREATE TABLE IF NOT EXISTS TVShows(" +
                "title text PRIMARY KEY" +
                "genre text NOT NULL" +
                "releaseYear integer NOT NULL" +
                "duration integer NOT NULL" +
                "rating REAL NOT NULL" +
                "casts text NOT NULL" +
                "numberOfSeasons integer NOT NULL" +
                    "lastReleaseYear integer NOT NULL)";
        try {
            Statement statement = this.connection.createStatement();
            statement.execute(query);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void addTVShow(TVShow tvShow){
        try {
            String query = "INSERT INTO TVShows(title, genre, releaseYear, duration, rating, casts, numberOfSeasons, lastReleaseYear) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, tvShow.getTitle());
            preparedStatement.setString(2, tvShow.getGenre());
            preparedStatement.setInt(3, tvShow.getReleaseYear());
            preparedStatement.setInt(4, tvShow.getDuration());
            preparedStatement.setDouble(5, tvShow.getRating());
            preparedStatement.setString(6, tvShow.getCasts());
            preparedStatement.setInt(7, tvShow.getNumberOfSeasons());
            preparedStatement.setInt(8, tvShow.getLastReleaseYear());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void addMovie(Movie movie) {
        try {
            String query = "INSERT INTO MOVIES(title, genre, releaseYear, duration, rating, casts) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getGenre());
            preparedStatement.setInt(3, movie.getReleaseYear());
            preparedStatement.setInt(4, movie.getDuration());
            preparedStatement.setDouble(5, movie.getRating());
            preparedStatement.setString(6, movie.getCasts());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public boolean createAccount(String username, String password) {
        try {
            String query = "INSERT INTO Users(username, password, isLoggedIn) VALUES (?, ?, 1)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            return  true;
        } catch (Exception e){
            System.out.println("Invalid input, try again");
        }
        return false;
    }

    public boolean login(String username, String password) throws SQLException{
        String query = "SELECT * FROM Users WHERE username = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()){
            System.out.println("User name not found, try again");
            return false;
        }
        if(!resultSet.getString("password").equals(password)){
            System.out.println("Wrong password, try again");
            return false;
        }

        return true;
    }

    public void logout() {
    }

    public Movie searchByTitle(String title) throws SQLException {
        String query = "SELECT * FROM Movies WHERE title = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setString(1, title);
        ResultSet resultSet = preparedStatement.executeQuery();
        Movie movie = new Movie("","", 0, 0, 0);
        if (resultSet.next()){
            movie.setTitle(resultSet.getString(1));
            movie.setGenre(resultSet.getString(2));
            movie.setReleaseYear(resultSet.getInt(3));
            movie.setDuration(resultSet.getInt(4));
            movie.setRating(resultSet.getDouble(5));
            movie.setCasts(resultSet.getString(6));
        }
        return movie;
    }

    public ArrayList<Movie> searchByGenre(String genre) throws SQLException{
        String query = "SELECT * FROM Movies WHERE genre = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setString(2, genre);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Movie> result = new ArrayList<Movie>();
        while (resultSet.next()){
            Movie movie = new Movie("","",0,0,0);
            movie.setTitle(resultSet.getString("title"));
            movie.setGenre(resultSet.getString("genre"));
            movie.setReleaseYear(resultSet.getInt("releaseYear"));
            movie.setDuration(resultSet.getInt("duration"));
            movie.setRating(resultSet.getInt("rating"));
            result.add(movie);

        }
        return result;
    }

    public ArrayList<Movie> searchByReleaseYear(int year) throws SQLException {
        String query = "SELECT * FROM Movies WHERE releaseYear = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setInt(3, year);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Movie> result = new ArrayList<Movie>();
        while (resultSet.next()){
            Movie movie = new Movie("","",0,0,0);
            movie.setTitle(resultSet.getString("title"));
            movie.setGenre(resultSet.getString("genre"));
            movie.setReleaseYear(resultSet.getInt("releaseYear"));
            movie.setDuration(resultSet.getInt("duration"));
            movie.setRating(resultSet.getInt("rating"));
            result.add(movie);

        }
        return result;
    }

    public void addToFavorites(Movie movie, String username) throws SQLException{
        String query = "INSERT INTO FavoriteMovies(username, movieName) VALUES (?,?)";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, movie.getTitle());
    }

    public String viewFavorites(String username) throws SQLException{
        String query = "SELECT * FROM FavoriteMovies WHERE username = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        String result = "";
        while (resultSet.next()){
            result += getMovie(resultSet.getString(2));
            result += "\n";
        }
        return result;
    }

    public String getMovie(String title) throws SQLException{
        String query = "SELECT * FROM Movies WHERE title = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setString(1, title);
        ResultSet resultSet = preparedStatement.executeQuery();
        String result = "";
        while (resultSet.next()){
            result += resultSet.getString(1) + " : ";
            result += resultSet.getString(2)+ " : ";
            result += Integer.toString(resultSet.getInt(3))+ " : ";
            result += Integer.toString(resultSet.getInt(4))+ " : ";
            result += resultSet.getString(5)+ " : ";
            result += resultSet.getString(6);
        }
        return result;
    }

    public void showMovieList() throws SQLException{
        String query = "SELECT * FROM Movies";
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            System.out.println(resultSet.getString(1) + " : " +
                            resultSet.getString(2) + " : " +
                            resultSet.getInt(3) + " : " +
                            resultSet.getInt(4) + " : " +
                            resultSet.getString(5) + " : " +
                            resultSet.getString(6));
        }
    }

    public void showTVShowList()throws SQLException{
        String query = "SELECT * FROM TVShows";
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            System.out.println(resultSet.getString(1) + " : " +
                    resultSet.getString(2) + " : " +
                    resultSet.getInt(3) + " : " +
                    resultSet.getInt(4) + " : " +
                    resultSet.getString(5) + " : " +
                    resultSet.getString(6) + " : " +
                    resultSet.getInt(7) + " : " +
                    resultSet.getInt(8));
        }
    }

    public boolean doesMovieExists(String name) throws SQLException{
        String query = "SELECT * FROM Movies WHERE title = ?";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return true;
        }
        return false;
    }

}

