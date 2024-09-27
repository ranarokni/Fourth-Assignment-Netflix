package org.example;
import com.mysql.cj.callback.UsernameCallback;

import java.io.Console;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;
import java.sql.SQLException;


public class Main {
    //don't limit yourself to our template ***


    public static void main(String[] args) {

        NetflixService netflixService = new NetflixService("netflix.db");
        try {
            netflixService.connect();
            netflixService.createMovieTable();
            netflixService.createTVShowTable();
            netflixService.createUserTable();
            netflixService.createFavoriteMoviesTable();
//        System.out.println("Congrats!");
            int operation = runMenu();
            switch (operation){
                case 1:
                    userRegistration(netflixService);
                    break;
                case 2:
                    userLogin(netflixService);
                    break;
            }


        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    public static int runMenu(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("----Welcome to Netflix's AP Version!----");
        System.out.println("Please select an item");
        System.out.println("1) New user? Register now!");
        System.out.println("2) Already have an account? Log in now!");

        int inputNumber = 0;
        boolean invalidInmput = true;
        while (invalidInmput) {
            while (!scanner.hasNextInt()) {
                System.err.println("Please enter valid number");
                scanner.next();
            }
            inputNumber = scanner.nextInt();
            if(inputNumber > 2 || inputNumber < 1){
                System.err.println("You can only choose 2 option");
            }
            else {
                invalidInmput = false;
                break;
            }
        }

        return inputNumber;
    }

    public static void userLogin(NetflixService netflixService){
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("-------USER LOGIN-------");
        System.out.println("Press q to exit\n");
        System.out.print("Enter your username: ");

        String userName = scanner.next();
        System.out.print("Enter your password: ");
        String pass = scanner.next();
        String password = doHashing(pass);

        if(userName.equals("q")){
            return;
        }

            try {
                while (!netflixService.login(userName, password)){
                    System.out.print("Enter your username: ");
                    userName = scanner.next();
                    System.out.print("Enter your password: ");
                    pass = scanner.next();
                    password = doHashing(pass);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        System.out.println("You entered successfully!");

        User user = new User(userName, password);

        userMenu(netflixService, user);
        return;

    }

    public static void userMenu(NetflixService netflixService, User user){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter any key to continue, press q to exit the program");
        String input = scanner.next();
        while (!input.equals("q")){
            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println("What do you want to do? select from the list below");
            System.out.println("1) show movie list");
            System.out.println("2) show TVShow list");
            System.out.println("3) add movie to your watching list history");
            System.out.println("4) add movie to your favorite list");
            System.out.println("5) view favorites");
            int inputNumber = 0;
            boolean invalidInmput = true;
            while (invalidInmput) {
                while (!scanner.hasNextInt()) {
                    System.err.println("Please enter valid number");
                    scanner.next();
                }
                inputNumber = scanner.nextInt();
                if(inputNumber > 6 || inputNumber < 1){
                    System.err.println("You can only choose 6 option");
                }
                else {
                    invalidInmput = false;
                    break;
                }
            }

            String temp;

            switch (inputNumber){
                case 1:
                    try {
                        netflixService.showMovieList();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2:
                    try {
                        netflixService.showTVShowList();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3:
//                    Movie movie = new Movie("", "", 0,0,0);
                    System.out.println("Please enter movie title");
                    temp = scanner.next();
                    user.addToWatchHistory(temp, netflixService);
                    break;
                case 4:
                    System.out.println("Please enter movie title");
                    temp = scanner.next();
                    try {
                        if(netflixService.doesMovieExists(temp)){
                            Movie movie = new Movie("", "", 0,0,0);
                            movie = netflixService.searchByTitle(temp);
                            netflixService.addToFavorites(movie, user.getUsername());
                        }
                        else System.out.println("No such movie title");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case 5:
                    try {
                        System.out.println(netflixService.viewFavorites(user.getUsername()));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }


            }
            input = scanner.next();

        }

    }

    public static void userRegistration(NetflixService netflixService){
        Scanner scanner = new Scanner(System.in);
//        Console console = System.console();

        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("-------USER REGISTRATION-------");
        System.out.println("Press q to exit\n");
        System.out.print("Enter your username: ");

        String userName = scanner.next();
        System.out.print("Enter your password: ");
        String pass = scanner.next();
        String password = doHashing(pass);

        if(userName.equals("q")){
            return;
        }

        try {
            while (!netflixService.login(userName, password)){
                System.out.print("Enter your username: ");
                userName = scanner.next();
                System.out.print("Enter your password: ");
                pass = scanner.next();
                password = doHashing(pass);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Repeat your password: ");
//        pass = console.readPassword();
        pass = scanner.next();
        String repeatedPassword = doHashing(String.valueOf(pass));
        while (!repeatedPassword.equals( password)){
            System.err.println("Two passwords does not match, repeat your password");
//            pass = console.readPassword();
            pass = scanner.next();
            repeatedPassword = doHashing(String.valueOf(pass));
        }

        System.out.println("You registered successfully!");
        User user = new User(userName, password);
        userMenu(netflixService, user);

    }
    public static String doHashing(String password){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());

            byte[] result = messageDigest.digest();
            StringBuilder sb = new StringBuilder();

            for(byte b : result){
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "";
    }
}
