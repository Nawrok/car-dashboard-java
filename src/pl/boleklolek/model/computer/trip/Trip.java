package pl.boleklolek.model.computer.trip;

import pl.boleklolek.model.odometer.Mileage;
import pl.boleklolek.model.odometer.MileageException;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.UUID;

/**
 * Ta klasa opisuje podróż.
 */
public class Trip implements Serializable, Comparable<Trip>
{
    /**
     * Stałe pole zawierające identyfikator wersji.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Unikalne ID podróży.
     */
    private final UUID uuid;
    /**
     * Przebieg podróży.
     */
    private final Mileage mileage;
    /**
     * Średnia prędkość podróży.
     */
    private double averageSpeed;
    /**
     * Maksymalna prędkość podróży.
     */
    private double maxSpeed;
    /**
     * Czas, który upłynął od początku podróży.
     */
    private long elapsedTime;

    /**
     * Konstruktor nowej podróży.
     * Przydziela losowe UUID podróży.
     */
    public Trip()
    {
        this.uuid = UUID.randomUUID();
        this.mileage = new Mileage();
    }

    /**
     * Konstruktor podróży.
     *
     * @param uuid         uuid
     * @param averageSpeed średnia prędkość
     * @param maxSpeed     maksymalna prędkość
     * @param mileage      przebieg
     * @param elapsedTime  czas podróży
     */
    public Trip(UUID uuid, double averageSpeed, double maxSpeed, Mileage mileage, long elapsedTime)
    {
        this.uuid = uuid;
        this.averageSpeed = averageSpeed;
        this.maxSpeed = maxSpeed;
        this.mileage = mileage;
        this.elapsedTime = elapsedTime;
    }

    /**
     * Getter UUID.
     *
     * @return unikatowy identyfikator
     */
    public UUID getUuid()
    {
        return uuid;
    }

    /**
     * Getter średniej prędkości.
     *
     * @return wartość średniej prędkości
     */
    public double getAverageSpeed()
    {
        return averageSpeed;
    }

    /**
     * Setter średniej prędkości.
     *
     * @param averageSpeed średnia prędkość
     */
    public void setAverageSpeed(double averageSpeed)
    {
        this.averageSpeed = averageSpeed;
    }

    /**
     * Getter maksymalnej prędkości.
     *
     * @return wartość maksymalnej prędkości
     */
    public double getMaxSpeed()
    {
        return maxSpeed;
    }

    /**
     * Setter maksymalnej prędkości.
     *
     * @param maxSpeed maksymalna prędkość
     */
    public void setMaxSpeed(double maxSpeed)
    {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Getter przebiegu.
     *
     * @return obiekt przebiegu
     */
    public Mileage getMileage()
    {
        return mileage;
    }

    /**
     * Getter czasu, który upłynął w sekundach.
     *
     * @return ilość sekund, które upłynęły
     */
    public long getElapsedTime()
    {
        return elapsedTime;
    }

    /**
     * Getter czasu w przeformatowanym stringu (00:00:00).
     *
     * @return przeformatowany string z upłyniętym czasem
     */
    public String getElapsedTimeString()
    {
        return String.format("%02d:%02d:%02d", elapsedTime / 3600, (elapsedTime % 3600) / 60, elapsedTime % 60);
    }

    /**
     * Inkrementuje czas w sekundach.
     *
     * @param seconds liczba sekund do dodania w czasie
     */
    public void addTime(long seconds)
    {
        elapsedTime += seconds;
    }

    /**
     * Resetuje podróż.
     */
    public void reset()
    {
        averageSpeed = 0;
        maxSpeed = 0;
        try
        {
            mileage.set(0);
        }
        catch (MileageException e)
        {
            System.err.println(e.getMessage());
        }
        elapsedTime = 0;
    }

    /**
     * Domyślne porównywanie obiektów Trip.
     * Porównuje podróże względem przebiegu podróży malejąco.
     * Gdy podróże mają ten sam przebieg, sortuje po czasie ich trwania.
     *
     * @param trip podróż
     * @return wartość porównania
     */
    @Override
    public int compareTo(Trip trip)
    {
        int result = -Double.compare(mileage.get(), trip.mileage.get());
        if (result == 0)
        {
            result = -Long.compare(elapsedTime, trip.elapsedTime);
        }
        return result;
    }

    /**
     * Zwraca informację o podróży, czyli UUID, średniej prędkości, prędkości maksymalnej,
     * przebiegu oraz czasu jej trwania.
     *
     * @return string z informacjami o podróży
     */
    @Override
    public String toString()
    {
        DecimalFormat df = new DecimalFormat("0.0");

        return "[Podróż]" + System.lineSeparator() +
                "UUID: " + uuid + System.lineSeparator() +
                "Średnia prędkość: " + df.format(averageSpeed) + " km/h" + System.lineSeparator() +
                "Maksymalna prędkość: " + df.format(maxSpeed) + " km/h" + System.lineSeparator() +
                "Przebieg: " + df.format(mileage.get()) + " km" + System.lineSeparator() +
                "Czas trwania: " + getElapsedTimeString();
    }

    /**
     * Zwraca skrócone informacje o podróży.
     *
     * @return string ze skróconymi informacjami o podróży
     */
    public String toStringShort()
    {
        DecimalFormat df = new DecimalFormat("0.0");
        return "UUID: " + uuid + " | Przebieg: " + df.format(mileage.get()) + " km | Czas trwania: " + getElapsedTimeString();
    }

    /**
     * Sprawdza identyczność podróży względem UUID.
     *
     * @param o obiekt (podróż)
     * @return prawda, jeśli podróże mają równe UUID
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Trip trip = (Trip) o;
        return uuid.equals(trip.uuid);
    }

    /**
     * Podaje wartość hash podróży na podstawie UUID.
     *
     * @return hash kod wartości UUID
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(uuid);
    }
}
