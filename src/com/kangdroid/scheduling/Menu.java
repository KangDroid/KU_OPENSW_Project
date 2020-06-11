package com.kangdroid.scheduling;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    public void mainMenu() {
        int input = 0;
        Scanner sysin = new Scanner(System.in);

        while (true) {
            System.out.println("1. About Scheduling algorithm");
            System.out.println("2. Non-Preemptive Scheduling Simulator");
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
                    schedulingAlgorithmDes();
                    break;
                case 2:
                    schedulingAlgorithmEx();
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

    public String enterFileName() {
        String input_name;
        Scanner inputFile = new Scanner(System.in);
        System.out.print("Enter JSON File Location(Leave blank to use default testcase): ");
        input_name = inputFile.nextLine();

        if (input_name.length() == 0) {
            System.out.println("Using default JSON.");
            input_name = "testSmall.json";
        } else {
            input_name = inputFile.nextLine();
        }

        return input_name;
    }

    public void schedulingAlgorithmEx() {
        int input = 0;
        Scanner sysInput = new Scanner(System.in);

        while (true) {
            String file_loc = enterFileName();
            System.out.println("This will execute pre-defined schedule algorithm based on json-shaped working file");
            System.out.println("1. FCFS");
            System.out.println("2. SJF");
            System.out.println("3. Priority Scheduling");
            System.out.println("4. Exit");
            try {
                input = sysInput.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("Only numbers are allowed. Please enter again.");
                if (sysInput.hasNextLine()) sysInput.nextLine(); // Clears buffer
                continue;
            }

            if (input == 1) {
                FCFSSimulator fcsim = new FCFSSimulator(file_loc);
                fcsim.executor();
                return;
            } else if (input == 2) {
                SJFSimulator sjfsim = new SJFSimulator(file_loc);
                sjfsim.executor();
                return;
            } else if (input == 3) {
                PrioritySimulator psm = new PrioritySimulator(file_loc);
                psm.executor();
                return;
            } else if (input == 4) {
                return;
            } else {
                System.out.println("Wrong number entered. Please enter again.");
            }
        }
    }

    public void schedulingAlgorithmDes() {
        int input = 0;
        Scanner sysInDescriptor = new Scanner(System.in);
        while (true) {
            System.out.println("This content will explain how selected scheduling algorithm\nworks in Operating System.");
            System.out.println("1. FCFS");
            System.out.println("2. SJF");
            System.out.println("3. Priority Scheduling");
            System.out.println("4. Exit");
            try {
                input = sysInDescriptor.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("Only numbers are allowed. Please enter again.");
                if (sysInDescriptor.hasNextLine()) sysInDescriptor.nextLine(); // Clears buffer
                continue;
            }

            if (input == 1) {
                // FCFS - Basic description
                System.out.println("Personally, I would describe this as: \"Most-Simplest Scheduling algorithm ever.\" ");
                System.out.println("FCFS stands for \"First Come First Served\", which means every job comes to processor");
                System.out.println("will be executed sequentially.\n\n");
                System.out.println("For example, Let's say 3 job comes to processor like: J1 - J2 - J3");
                System.out.println("Then, FCFS Scheduler will execute its job in order of:");
                System.out.println("1. J1");
                System.out.println("2. J2");
                System.out.println("3. J3\n");
                System.out.println("What it stands for, \"First Come First Served!\" \n\n");
                // FCFS - Cons
                System.out.println("But, it has also bad effects like \"Convoy Effect\". ");
                System.out.println("Let's think about each job's runtime. ");
                System.out.println("J1 has runtime of 300s, J2 has runtime of 30s, J3 has runtime of 1s.");
                System.out.println("Also, consider single-core / Single-threaded computer.");
                System.out.println("Suppose those J1-J3 came in same order as described above.");
                System.out.println("Think about how much each job has to be waited.\n");
                System.out.println("Even job has same contents and runtime, whole wait-time and average return time is \nmassively different depends on arriving order.\n");
            } else if (input == 2) {
                // SJF
                System.out.println("SJF stands for \"Shortest Job First\"");
                System.out.println("This algorithm acutally fixes cons of FCFS, which is about average waiting-time.");
                System.out.println("\n- This program does not include I/O Time in any order. -\n");
                System.out.println("We have special word called, \'Burst time\', which stands for \'CPU Runtime before I/O Request comes.\'");
                System.out.println("In easy way, we could say burst time as runtime.(Since this program does not account for I/O Request)");
                System.out.println("Anyway, this algorithm will sort every work based on \'burst time\', in smallest order.\n\n");
                System.out.println("Suppose 3 Jobs, J1 has burst time of 300s, J2 has burst time of 30s, J3 has burst time of 1s.");
                System.out.println("Then, SJF will sort jobs in order like this: J3 - J2 - J1, smallest burst time.\n");
            } else if (input == 3) {
                // Priority
                System.out.println("Priority Algorithm");
                System.out.println("FCFS Algorithm executes its job in arrival-order, SJF executes its job in Burst-Time(least) order,");
                System.out.println("This Priority one executes its job in Priority-Order");
                System.out.println("Operating System sets its job's priority internally, considering deadline, Memory limitation, etc");
                System.out.println("We won't talk about how OS sets its priority but, those one is simple definition of Priority.\n");

                System.out.println("Anyway, suppose we have 3 jobs, each J1 has priority number of 1, J2: 3, J3: 2.");
                System.out.println("This algorithm will sort execution order like this: J1 - J3 - J2, in order of Priority number.\n");

                System.out.println("But, this simple - priority algorithm could cause some problem called \"Starvation\".");
                System.out.println("What if higher priority job is keep coming to algorithm?");
                System.out.println("Likely low-priority job will be paused for long-long time, and there is no prove and sure that\nlow-priority job could be executed.");
                System.out.println("So, in priority algorithm, we can solve this problem by \'aging\'. Which when time passes, low-priority job gets more higher priority.");
                System.out.println("In this program, old jobs gets 1 more priority when each job comes.\n");
            } else if (input == 4) {
                return;
            } else {
                System.out.println("Wrong number entered. Please enter again.");
                continue;
            }
        }
    }
}
