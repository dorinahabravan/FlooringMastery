package com.mthree.view;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UserIOConsoleImpl implements  UserIO {

    // Scanner used to read input from the console
    private Scanner console = new Scanner(System.in);


    /**
     * Displays a message to the user
     *
     * @param message the message to display
     */

    @Override
    public void print(String message) {
        System.out.println(message);

    }

    /**
     * Prompts the user and reads a String value
     *
     * @param prompt
     * @return the user's input
     */
    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        return console.nextLine();
    }


    /**
     * Prompts the user until a valid integer is entered
     *
     * @param prompt
     * @return return a valid integer entered by the user
     */
    @Override
    public int readInt(String prompt) {

        while (true) {
            String input = readString(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                print("Invalid input. Please enter a whole  number");
            }
        }
    }


    /**
     * Prompts the user until an integer within the specified range is entered
     *
     * @param prompt
     * @param min
     * @param max
     * @return a valid integer within the spcified range
     */
    @Override
    public int readInt(String prompt, int min, int max) {

        while (true) {
            int input = readInt(prompt);

            if (input >= min && input <= max) {
                return input;
            }

            print("Please enter a number between " + min + " and " + max + ".");
        }
    }



}