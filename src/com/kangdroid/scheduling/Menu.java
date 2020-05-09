package com.kangdroid.scheduling;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    public void mainMenu() {
        int input = 0;
        Scanner sysin = new Scanner(System.in);

        while (true) {
            System.out.println("1. About Scheduling algorithm");
            System.out.println("2. Non-Preemptive Scheduling Gannt Chart");
            System.out.println("3. Exit Program");
            System.out.print("Input Menu number: ");

            try {
                input = sysin.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("Only numbers are allowed. Please enter again.");
                if (sysin.hasNextLine()) sysin.nextLine(); // Clears buffer
                continue;
            }

            switch (input) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    System.out.println("Good Bye!");
                    sysin.close();
                    return; // Exit function
                default:
                    System.out.println("Wrong number entered. Please enter again.");
            }
        }
    }
}
