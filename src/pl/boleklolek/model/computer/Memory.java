package pl.boleklolek.model.computer;

import pl.boleklolek.model.computer.trip.Trip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Ta klasa opisuje pamięć komputera pokładowego.
 */
public class Memory implements Serializable, Repository<Trip>
{
    /**
     * Stałe pole zawierające identyfikator wersji.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Lista podróży.
     */
    private final List<Trip> trips;

    /**
     * Konstruktor pamięci komputera pokładowego.
     */
    public Memory()
    {
        this.trips = new ArrayList<>();
    }

    /**
     * Zamienia podróże ze sobą w kolekcji.
     *
     * @param i indeks podróży 1
     * @param j indeks podróży 2
     */
    public void swap(int i, int j)
    {
        Collections.swap(trips, i, j);
    }

    /**
     * Getter wszystkich podróży.
     *
     * @return wszystkie podróże
     */
    @Override
    public List<Trip> getAll()
    {
        return new ArrayList<>(trips);
    }

    /**
     * Getter konkretnej podróży.
     *
     * @param index indeks
     * @return obiekt podróży określony przez indeks
     */
    @Override
    public Trip get(int index)
    {
        return trips.get(index);
    }

    /**
     * Setter konkretnej podróży.
     *
     * @param index indeks
     * @param trip  podróż
     */
    @Override
    public void set(int index, Trip trip)
    {
        trips.set(index, trip);
    }

    /**
     * Dodaje podróż do pamięci.
     *
     * @param trip podróż
     */
    @Override
    public void add(Trip trip)
    {
        trips.add(trip);
    }

    /**
     * Dodaje podróż pod dany indeks.
     *
     * @param index indeks
     * @param trip  podróż
     */
    public void add(int index, Trip trip)
    {
        trips.add(index, trip);
    }

    /**
     * Usuwa podróż po indeksie.
     *
     * @param index indeks
     */
    @Override
    public void remove(int index)
    {
        trips.remove(index);
    }

    /**
     * Sortuje podróże względem komparatora.
     * Jeżeli komparator będzie pusty, to zostanie wywołane sortowanie po interfejsie Comparable.
     *
     * @param tripComparator komparator podróży
     */
    @Override
    public void sort(Comparator<Trip> tripComparator)
    {
        trips.sort(tripComparator);
    }

    /**
     * Pobiera wielkość zbioru podróży.
     *
     * @return wielkość zbioru podróży
     */
    @Override
    public int size()
    {
        return trips.size();
    }

    /**
     * Zwraca informację o podróżach w pamięci.
     *
     * @return string z informacjami o podróżach w pamięci
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("-- PAMIĘĆ --").append(System.lineSeparator());
        for (int i = 0; i < trips.size(); i++)
        {
            sb.append("[").append(i + 1).append("] ").append(trips.get(i)).append(System.lineSeparator());
        }
        sb.deleteCharAt(sb.lastIndexOf(System.lineSeparator()));
        return sb.toString();
    }
}
