package pl.boleklolek.model.computer;

import pl.boleklolek.loop.DashboardLoop;
import pl.boleklolek.model.computer.trip.Trip;
import pl.boleklolek.model.odometer.MileageException;
import pl.boleklolek.model.odometer.Odometer;
import pl.boleklolek.model.speedometer.Speedometer;
import pl.boleklolek.utils.SingletonExecutor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Ta klasa opisuje komputer pokładowy.
 */
public class Computer
{
    /**
     * Prędkościomierz.
     */
    private final Speedometer speedometer;
    /**
     * Licznik przebiegu.
     */
    private final Odometer odometer;
    /**
     * Tablica czynności zleconych w ScheduledServiceExecutor.
     */
    private final transient ScheduledFuture<?>[] futures;
    /**
     * Tablica aktualnych podróży.
     */
    private final Trip[] trips;
    /**
     * Pamięć.
     */
    private Memory memory;
    /**
     * Szybkość aktualizacji średniej prędkości.
     */
    private int frequencyOfAvgSpeed;

    /**
     * Konstruktor komputera pokładowego.
     * Wczytuje podróże.
     *
     * @param memory      pamięć
     * @param speedometer prędkościomierz
     * @param odometer    licznik przebiegu
     */
    public Computer(Memory memory, Speedometer speedometer, Odometer odometer)
    {
        this.memory = memory;
        this.speedometer = speedometer;
        this.odometer = odometer;

        int numOfTrips = odometer.getDailyMileages().length;
        this.futures = new ScheduledFuture[numOfTrips];
        this.trips = new Trip[numOfTrips];
        for (int i = 0; i < numOfTrips; i++)
        {
            loadTrip(i);
        }
    }

    /**
     * Getter podróży.
     *
     * @return kopia tablicy podróży
     */
    public Trip[] getTrips()
    {
        return trips.clone();
    }

    /**
     * Getter pamięci.
     *
     * @return obiekt pamięci
     */
    public Memory getMemory()
    {
        return memory;
    }

    /**
     * Setter pamięci.
     *
     * @param memory pamięć
     */
    public void setMemory(Memory memory)
    {
        this.memory = memory;
    }

    /**
     * Setter szybkości aktualizacji średniej prędkości.
     *
     * @param frequency szybkość odświeżania w sekundach
     */
    public void setFreqOfAvgSpeed(int frequency)
    {
        this.frequencyOfAvgSpeed = frequency;
    }

    /**
     * Tworzy nową podróż i dodaje ją do pamięci.
     *
     * @param index indeks podróży (A - 0 lub B - 1)
     */
    public void createNewTrip(int index)
    {
        memory.add(index, new Trip());
        if (memory.size() > trips.length && index != trips.length - 1)
        {
            for (int i = index; i < trips.length - 1; i++)
            {
                memory.swap(i + 1, i + 2);
            }
        }
        loadTripFromMemory(index, index);
    }

    /**
     * Wczytuje daną podróż.
     * Jeżeli nie znajdzie jej w pamięci, to tworzy nową podróż i ją wczytuje.
     *
     * @param index indeks podróży (A - 0 lub B - 1)
     */
    public void loadTrip(int index)
    {
        try
        {
            loadTripFromMemory(index, index);
        }
        catch (IndexOutOfBoundsException e)
        {
            createNewTrip(index);
        }
    }

    /**
     * Wczytuje daną podróż z pamięci komputera pokładowego i ustawia jej przebieg w liczniku.
     *
     * @param tripIndex   indeks podróży (A - 0 lub B - 1)
     * @param memoryIndex indeks podróży w pamięci
     */
    public void loadTripFromMemory(int tripIndex, int memoryIndex)
    {
        trips[tripIndex] = memory.get(memoryIndex);
        odometer.setDailyMileage(tripIndex, trips[tripIndex].getMileage());
    }

    /**
     * Usuwa wybraną, nieużywaną aktualnie, podróż z pamięci.
     *
     * @param index indeks podróży w pamięci
     */
    public void removeTripFromMemory(int index)
    {
        if (!Arrays.asList(trips).contains(memory.get(index)))
        {
            memory.remove(index);
        }
    }

    /**
     * Startuje podróż.
     * Rozpoczęcie naliczania średniej prędkości z wykorzystaniem SingletonExecutora.
     *
     * @param index indeks podróży (A - 0 lub B - 1)
     */
    public void startTrip(int index)
    {
        Trip trip = trips[index];
        double initDistance = Math.abs(odometer.getTotalMileage() - trip.getMileage().get());
        if (futures[index] != null)
        {
            futures[index].cancel(true);
        }
        futures[index] = SingletonExecutor.getInstance().scheduleAtFixedRate(() -> calcAverageSpeed(trip, initDistance), 1, frequencyOfAvgSpeed, TimeUnit.SECONDS);
    }

    /**
     * Resetuje podróż.
     *
     * @param index indeks podróży (A - 0 lub B - 1)
     */
    public void resetTrip(int index)
    {
        trips[index].reset();
        startTrip(index);
    }

    /**
     * Zamienia podróże A i B ze sobą.
     */
    public void swapTrips()
    {
        Trip temp = trips[0];
        trips[0] = trips[1];
        trips[1] = temp;
    }

    /**
     * Uruchamia zliczanie czasu podróżom.
     * Wykorzystuje SingletonExecutora w celu dodawania sekundy do czasu podróży.
     */
    public void startTimer()
    {
        SingletonExecutor.getInstance().scheduleAtFixedRate(() ->
        {
            for (Trip trip : trips)
            {
                trip.addTime(1);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Sortuje podróże w pamięci komputera pokładowego względem komparatora.
     * Jeżeli komparator będzie pusty, to zostanie wywołane sortowanie po interfejsie Comparable.
     *
     * @param tripComparator komparator podróży
     */
    public void sortTripsInMemory(Comparator<Trip> tripComparator)
    {
        memory.sort(tripComparator);
    }

    /**
     * Kalkuluje zmianę prędkości średniej.
     *
     * @param trip         podróż
     * @param initDistance początkowy dystans
     */
    private void calcAverageSpeed(Trip trip, double initDistance)
    {
        double deltaDistance = Math.abs(odometer.getTotalMileage() - initDistance);
        trip.setAverageSpeed(deltaDistance / trip.getElapsedTime() * 3600);
    }

    /**
     * Kalkuluje zmianę prędkości maksymalnej.
     *
     * @param speed aktualna prędkość
     */
    private void calcMaxSpeed(double speed)
    {
        for (Trip trip : trips)
        {
            if (speed > trip.getMaxSpeed())
            {
                trip.setMaxSpeed(speed);
            }
        }
    }

    /**
     * Zwraca informację o podróżach.
     *
     * @return string z informacjami o podróżach
     */
    @Override
    public String toString()
    {
        return "-- KOMPUTER POKŁADOWY --" + System.lineSeparator() +
                "[Podróż A]" + trips[0].toString().substring(8) + System.lineSeparator() +
                "[Podróż B]" + trips[1].toString().substring(8) + System.lineSeparator() +
                memory;
    }

    /**
     * Aktualizuje licznik przebiegu oraz prędkość maksymalną.
     */
    public void update()
    {
        double currentSpeed = speedometer.getSpeed();
        double distance = (currentSpeed / 3600) / DashboardLoop.DELTA_TIME;
        try
        {
            odometer.addDistance(distance);
        }
        catch (MileageException e)
        {
            System.err.println(e.getMessage());
        }
        calcMaxSpeed(currentSpeed);
    }
}
