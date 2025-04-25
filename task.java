package cpu_scheduler;

class task {

    int arrival_time; // general stats about the process
    int work_length;
    int deadline = -1;
    int remaining_work;
    int completion_time = -1;
    int wait_time = -1;
    int turnaround = -1;

    public task(int arrival, int burst) {
        this.arrival_time = arrival;
        this.remaining_work = work_length = burst;
    }

    public task(int arrival, int burst, int deadline) {
        this.arrival_time = arrival;
        this.remaining_work = work_length = burst;
        this.deadline = deadline;
    }

    @Override
    public String toString() { // for easier debugging
        return (
            "Arival: " +
            arrival_time +
            " work: " +
            work_length +
            " remaining work: " +
            remaining_work +
            " Completion: " +
            completion_time +
            " Deadline (if applicable): " +
            deadline
        );
    }
    // for heap to work taks needs to be comparable. This function ensures that the priority is based on remaining time
    // @Override
    // public int compareTo(task other) {
    //     if (remaining_work < other.remaining_work) {
    //         return -1;
    //     }
    //     if (remaining_work > other.remaining_work) {
    //         return 1;
    //     }
    //     return 0;
    // }
}
