package src;

import javax.swing.*;

public class Main {
    public static void main (String[] args) {
        int menuOption = Integer.parseInt (
                JOptionPane.showInputDialog (
                        null, """
                                Select an option
                                1- See cats
                                2- Exit
                                """
                )
        );
        switch (menuOption) {
            case 1 -> {
            }
            case 2 -> {
                System.exit (-1);
            }
        }
    }

}
