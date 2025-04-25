package cpu_scheduler;

import java.util.Comparator;

class CompareByBurst implements Comparator<task> {

    @Override
    public int compare(task a, task b) {
        return Integer.compare(a.remaining_work, b.remaining_work);
    }
}
