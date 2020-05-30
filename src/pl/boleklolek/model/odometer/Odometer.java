package pl.boleklolek.model.odometer;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Ta klasa opisuje liczniki przebiegu.
 */
public class Odometer implements Serializable
{
    /**
     * Stałe pole zawierające identyfikator wersji.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Tablica dziennych przebiegów.
     */
    private final Mileage[] dailyMileages;
    /**
     * Przebieg całkowity.
     */
    private double totalMileage;

    /**
     * Konstruktor licznika przebiegu.
     */
    public Odometer()
    {
        this.dailyMileages = new Mileage[2];
        for (int i = 0; i < this.dailyMileages.length; i++)
        {
            this.dailyMileages[i] = new Mileage();
        }
    }

    /**
     * Getter dziennych przebiegów.
     *
     * @return kopia tablicy dziennych przebiegów
     */
    public Mileage[] getDailyMileages()
    {
        return dailyMileages.clone();
    }

    /**
     * Setter dziennego przebiegu.
     *
     * @param index        indeks
     * @param dailyMileage dzienny przebieg
     */
    public void setDailyMileage(int index, Mileage dailyMileage)
    {
        this.dailyMileages[index] = dailyMileage;
    }

    /**
     * Getter przebiegu całkowitego.
     *
     * @return wartość przebiegu całkowitego
     */
    public double getTotalMileage()
    {
        return totalMileage;
    }

    /**
     * Dodaje dystans do licznika (wszystkim przebiegom).
     *
     * @param distance dystans, który chcemy dodać do licznika
     * @throws MileageException wyjątek przebiegu, który zostanie rzucony w przypadku podania ujemnego dystansu
     */
    public void addDistance(double distance) throws MileageException
    {
        for (Mileage dailyMileage : dailyMileages)
        {
            dailyMileage.add(distance);
        }
        totalMileage += distance;
    }

    /**
     * Zwraca informację o licznikach przebiegu dziennego i całkowitego.
     *
     * @return string z informacją o licznikach przebiegu
     */
    @Override
    public String toString()
    {
        DecimalFormat df = new DecimalFormat("0.0");

        StringBuilder sb = new StringBuilder();
        sb.append("-- LICZNIK --").append(System.lineSeparator());
        for (int i = 0; i < dailyMileages.length; i++)
        {
            sb.append("Przebieg dzienny [").append(i + 1).append("]: ").append(df.format(dailyMileages[i].get())).append(" km").append(System.lineSeparator());
        }
        sb.append("Przebieg całkowity: ").append(df.format(totalMileage)).append(" km");
        return sb.toString();
    }
}
