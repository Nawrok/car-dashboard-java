package pl.boleklolek.model.odometer;

import java.io.Serializable;

/**
 * Ta klasa opisuje przebieg.
 */
public class Mileage implements Serializable
{
    /**
     * Stałe pole zawierające identyfikator wersji.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Dystans.
     */
    private double distance;

    /**
     * Konstruktor przebiegu.
     */
    public Mileage()
    {
        this.distance = 0.0;
    }

    /**
     * Konstruktor przebiegu o danym dystansie.
     *
     * @param distance dystans
     * @throws MileageException wyjątek przebiegu, który zostanie rzucony w przypadku podania ujemnego dystansu
     */
    public Mileage(double distance) throws MileageException
    {
        if (distance < 0)
        {
            throw new MileageException("Dystans nie może być ujemny!");
        }
        this.distance = distance;
    }

    /**
     * Getter dystansu.
     *
     * @return wartość dystansu
     */
    public double get()
    {
        return distance;
    }

    /**
     * Setter dystansu.
     *
     * @param distance dystans
     * @throws MileageException wyjątek przebiegu, który zostanie rzucony w przypadku podania ujemnego dystansu
     */
    public void set(double distance) throws MileageException
    {
        if (distance < 0)
        {
            throw new MileageException("Dystans nie może być ujemny!");
        }
        this.distance = distance;
    }

    /**
     * Dodaje dystans.
     *
     * @param distance dystans
     * @throws MileageException wyjątek przebiegu, który zostanie rzucony w przypadku podania ujemnego dystansu
     */
    public void add(double distance) throws MileageException
    {
        if (distance < 0)
        {
            throw new MileageException("Dystans nie może być ujemny!");
        }
        this.distance += distance;
    }

    /**
     * Zwraca informację o dystansie.
     *
     * @return string z wartością dystansu
     */
    @Override
    public String toString()
    {
        return String.valueOf(distance);
    }
}
