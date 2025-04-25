package cpu_scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class Workload {

    public Queue<task> queue = new LinkedList<>(); // queue that holds all processes in order of arrival
    public int size; // # of tasks
    public Queue<task> completed; // holds completed tasks for statistics generation later on
    public double cpu_utilization; // stores utilized_time / total cputime
    public int total_time = -1;

    public Workload(int num_of_tasks) { // code used to generate workload
        Random rand = new Random();

        size = num_of_tasks;
        int last_time = 0; // keep track of last assigned time to ensure cronological order

        for (int i = 0; i < num_of_tasks; i++) {
            last_time = rand.nextInt(last_time, last_time + 5); // tasks can either start at teh same time as prev or up to 10 units later
            queue.add(
                new task(last_time, rand.nextInt(1, 10), rand.nextInt(1, 50))
            ); // add task to list
        }
        try {
            File f = new File(num_of_tasks + "_processes.txt");
            FileWriter fw = new FileWriter(f);
            for (task t : queue) {
                fw.append(
                    t.arrival_time +
                    " " +
                    t.remaining_work +
                    " " +
                    t.deadline +
                    "\n"
                );
            }
            fw.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Workload(String preset) {
        String path = "";
        switch (preset) {
            case "1":
                path = "10_processes.txt";
                break;
            case "2":
                path = "20_processes.txt";
                break;
            case "3":
                path = "30_processes.txt";
                break;
            case "4":
                path = "40_processes.txt";
                break;
            case "5":
                path = "50_processes.txt";
                break;
        }
        try {
            File f = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                int arrival = Integer.parseInt(line.split(" ")[0]);
                int burst = Integer.parseInt(line.split(" ")[1]);
                int deadline = Integer.parseInt(line.split(" ")[2]);

                queue.add(new task(arrival, burst, deadline));
            }
            size = queue.size();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public String toString() {
        String output = "";
        if (!queue.isEmpty()) {
            for (task t : queue) {
                output += t.toString() + "\n";
            }
        } else if (!completed.isEmpty()) {
            for (task t : completed) {
                output += t.toString() + "\n";
            }
        }
        return output;
    }
}
