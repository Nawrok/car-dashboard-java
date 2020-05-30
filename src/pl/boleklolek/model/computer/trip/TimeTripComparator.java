package pl.boleklolek.model.computer.trip;

import java.util.Comparator;

/**
 * Ta klasa służy jako komparator podróży po czasie trwania.
 */
public class TimeTripComparator implements Comparator<Trip>
{
    /**
     * Porównuje dwie podróże względem upłyniętego czasu.
     * Ustawia podróże od najkrócej trwającej do najdłużej trwającej.
     *
     * @param trip1 podróż 1
     * @param trip2 podróż 2
     * @return wartość porównania
     */
    @Override
    public int compare(Trip trip1, Trip trip2)
    {
        return Long.compare(trip1.getElapsedTime(), trip2.getElapsedTime());
    }
}
