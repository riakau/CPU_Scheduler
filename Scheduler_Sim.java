package cpu_scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Scheduler_Sim {

    public static void SRTF(Workload processes) {
        int time = 0; //clock
        int utilized_time = 0; // if task is being processed at the current time stamp, increment
        List<task> ready_queue = new ArrayList<>(); // the ready queue (heap) will keep track of each shortest job for us
        Queue<task> completed = new LinkedList<>();
        //print_task_queue(processes.queue);
        while (!processes.queue.isEmpty() || !ready_queue.isEmpty()) { // while a process still is not complete
            while ( // if the arrival time has come, add it to ready queue
                !processes.queue.isEmpty() &&
                processes.queue.peek().arrival_time <= time
            ) {
                ready_queue.add(processes.queue.poll());
                Collections.sort(ready_queue, new CompareByBurst());
            }

            if (!ready_queue.isEmpty()) { // if something is in queue, time interval is not wasted, updated utilized time
                utilized_time++;
            }
            if ( // if process is completed this cycle, update its stats, remove it from ready queue, and add it to completed queue
                !ready_queue.isEmpty() && ready_queue.get(0).remaining_work <= 1
            ) {
                ready_queue.get(0).remaining_work--;
                ready_queue.get(0).completion_time = time;
                ready_queue.get(0).turnaround =
                    time - ready_queue.get(0).arrival_time;
                ready_queue.get(0).wait_time =
                    ready_queue.get(0).turnaround -
                    ready_queue.get(0).work_length;
                completed.add(ready_queue.remove(0));
            } else if (!ready_queue.isEmpty()) { // else process task (decrement burst time), and remove and readd the task to re-heapify the priority queue
                task t = ready_queue.remove(0);
                t.remaining_work--;
                ready_queue.add(t);
            }
            time++;
            Collections.sort(ready_queue, new CompareByBurst());
        }
        // store results in workload object
        processes.completed = completed;
        processes.cpu_utilization = (double) utilized_time / (double) time;
        processes.total_time = time;
        //System.out.println(processes.cpu_utilization);
    }

    public static void EDF(Workload processes) {
        int time = 0; //clock
        int utilized_time = 0; // if task is being processed at the current time stamp, increment
        List<task> ready_queue = new ArrayList<>(); // the ready queue (heap) will keep track of each shortest job for us
        Queue<task> completed = new LinkedList<>();
        //print_task_queue(processes.queue);
        while (!processes.queue.isEmpty() || !ready_queue.isEmpty()) { // while a process still is not complete
            while ( // if the arrival time has come, add it to ready queue
                !processes.queue.isEmpty() &&
                processes.queue.peek().arrival_time <= time
            ) {
                ready_queue.add(processes.queue.poll());
                Collections.sort(ready_queue, new CompareByDeadline());
            }

            if (!ready_queue.isEmpty()) { // if something is in queue, time interval is not wasted, updated utilized time
                utilized_time++;
            }
            if ( // if process is completed this cycle, update its stats, remove it from ready queue, and add it to completed queue
                !ready_queue.isEmpty() && ready_queue.get(0).remaining_work <= 1
            ) {
                ready_queue.get(0).remaining_work--;
                ready_queue.get(0).completion_time = time;
                ready_queue.get(0).turnaround =
                    time - ready_queue.get(0).arrival_time;
                ready_queue.get(0).wait_time =
                    ready_queue.get(0).turnaround -
                    ready_queue.get(0).work_length;
                completed.add(ready_queue.remove(0));
            } else if (!ready_queue.isEmpty()) { // else process task (decrement burst time), and remove and readd the task to re-heapify the priority queue
                task t = ready_queue.remove(0);
                t.remaining_work--;
                ready_queue.add(t);
            }
            time++;
            Collections.sort(ready_queue, new CompareByDeadline());
        }
        // store results in workload object
        processes.completed = completed;
        processes.cpu_utilization = (double) utilized_time / (double) time;
        processes.total_time = time;
        //System.out.println(processes.cpu_utilization);
    }

    public static void print_task_queue(Queue<task> queue) { // prints task queue in workload
        while (queue.peek() != null) {
            System.out.println(queue.poll());
        }
    }

    /*
- Average Waiting Time (AWT) - How long processes wait before execution.
– Average Turnaround Time (ATT) - Total time taken for process completion.
– CPU Utilization (%) - Percentage of time the CPU is actively executing tasks.
– Throughput (Processes per Second) - Rate of process completion.
– Response Time (RT) [Optional] - Time from arrival to first execution. */
    public static String generate_statistics(Workload processes) {
        if (processes.completed.isEmpty()) {
            return "Workload has not been processed.";
        }
        int agg_wait = 0;
        int agg_turnaround = 0;
        for (task t : processes.completed) {
            agg_wait += t.wait_time;
            agg_turnaround += t.turnaround;
        }
        int average_wait_time = agg_wait / processes.size;
        int average_turnaround_time = agg_turnaround / processes.size;
        double processes_per_second =
            (double) processes.size / (double) processes.total_time;
        return (
            "AWT: " +
            average_wait_time +
            "\nATT: " +
            average_turnaround_time +
            "\nCPU UTILIZATION: " +
            String.format("%.2f", processes.cpu_utilization * 100) +
            "%" +
            "\nPROCESSES / TIMEUNIT : " +
            String.format("%.4f", processes_per_second)
        );
    }

    public static void main(String[] args) {
        Workload sample1 = new Workload(10);
        Workload sample2 = new Workload(20);
        Workload sample3 = new Workload(30);
        Workload sample4 = new Workload(40);
        Workload sample5 = new Workload(50);
        SRTF(sample1);
        SRTF(sample2);
        SRTF(sample3);
        SRTF(sample4);
        SRTF(sample5);
        System.out.println("SRTF RUNS : ");

        System.out.println(generate_statistics(sample1));
        System.out.println("______________________");
        System.out.println(generate_statistics(sample2));
        System.out.println("______________________");
        System.out.println(generate_statistics(sample3));
        System.out.println("______________________");
        System.out.println(generate_statistics(sample4));
        System.out.println("______________________");
        System.out.println(generate_statistics(sample5));
        System.out.println("\n\nEDF RUNS : ");
        sample1 = new Workload("1");
        sample3 = new Workload("3");
        sample2 = new Workload("2");
        sample4 = new Workload("4");
        sample5 = new Workload("5");
        EDF(sample1);
        EDF(sample2);
        EDF(sample3);
        EDF(sample4);
        EDF(sample5);
        System.out.println(generate_statistics(sample1));
        System.out.println("______________________");
        System.out.println(generate_statistics(sample2));
        System.out.println("______________________");
        System.out.println(generate_statistics(sample3));
        System.out.println("______________________");
        System.out.println(generate_statistics(sample4));
        System.out.println("______________________");
        System.out.println(generate_statistics(sample5));
    }
}
