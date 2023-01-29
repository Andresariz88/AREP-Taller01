package org.arep;

public class Main {

    private static final String GET_URL = "https://omdbapi.com/?s=%S&apikey=c2d09dcc";


    public static void main(String[] args) {
        String formatted = String.format(GET_URL, "avatar");
        System.out.println(formatted);
    }
}