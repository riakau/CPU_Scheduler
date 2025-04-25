package cpu_scheduler;

import java.util.Comparator;

class CompareByDeadline implements Comparator<task> {

    @Override
    public int compare(task a, task b) {
        return Integer.compare(a.deadline, b.deadline);
    }
}
