package pl.boleklolek.model.computer;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;

/**
 * Interfejs repozytorium.
 *
 * @param <T> dowolna klasa T implementująca interfejs Serializable
 */
public interface Repository<T extends Serializable>
{
    /**
     * Getter wszystkich elementów kolekcji.
     *
     * @return kolekcja obiektów klasy T
     */
    Collection<T> getAll();

    /**
     * Getter konkretnego elementu.
     *
     * @param index indeks
     * @return obiekt klasy T
     */
    T get(int index);

    /**
     * Setter konkretnego elementu.
     *
     * @param index indeks
     * @param t     obiekt klasy T
     */
    void set(int index, T t);

    /**
     * Dodaje element do kolekcji.
     *
     * @param t obiekt klasy T
     */
    void add(T t);

    /**
     * Usuwa element z kolekcji.
     *
     * @param index indeks
     */
    void remove(int index);

    /**
     * Sortuje względem komparatora.
     *
     * @param comp komparator
     */
    void sort(Comparator<T> comp);

    /**
     * Pobiera wielkość kolekcji.
     *
     * @return wielkość
     */
    int size();

    /**
     * Zwraca informację o kolekcji.
     *
     * @return string z informacjami o danej kolekcji
     */
    String toString();
}
