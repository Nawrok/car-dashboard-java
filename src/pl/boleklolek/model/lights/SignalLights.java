package pl.boleklolek.model.lights;

import pl.boleklolek.model.lights.enums.SignalLightsDirection;
import pl.boleklolek.utils.SingletonExecutor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Ta klasa opisuje światła kierunkowe.
 */
public class SignalLights implements Serializable
{
    /**
     * Stałe pole zawierające identyfikator wersji.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Tablica świateł kierunkowych.
     */
    private final boolean[] lights;
    /**
     * Stan włączonych kierunkowskazów.
     */
    private boolean active;
    /**
     * Czynność zlecona w ScheduledServiceExecutor.
     */
    private transient ScheduledFuture<?> future;

    /**
     * Konstruktor świateł kierunkowych.
     */
    public SignalLights()
    {
        this.lights = new boolean[2];
    }

    /**
     * Getter świateł kierunkowych.
     *
     * @return kopia tablicy wartości logicznych zawierająca prawdy, jeśli odpowiednie światła są włączone
     */
    public boolean[] getLights()
    {
        return lights.clone();
    }

    /**
     * Włącza światła kierunkowe.
     *
     * @param direction kierunek
     */
    public void turnOn(SignalLightsDirection direction)
    {
        if (!active)
        {
            startFlashing(direction);
            active = true;
        }
    }

    /**
     * Wyłącza światła kierunkowe.
     */
    public void turnOff()
    {
        if (active)
        {
            stopFlashing();
            active = false;
        }
    }

    /**
     * Rozpoczyna miganie kierunkowskazu.
     *
     * @param direction kierunek
     */
    private void startFlashing(SignalLightsDirection direction)
    {
        int index = direction.ordinal();
        future = SingletonExecutor.getInstance().scheduleAtFixedRate(() -> lights[index] = !lights[index], 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Zakańcza miganie kierunkowskazu.
     */
    private void stopFlashing()
    {
        future.cancel(true);
        Arrays.fill(lights, false);
    }

    /**
     * Zwraca informację o światłach kierunkowych.
     *
     * @return string z informacją o światłach kierunkowych
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Światła kierunkowe: ");
        for (int i = 0; i < lights.length; i++)
        {
            sb.append(SignalLightsDirection.values()[i]).append(" [").append(lights[i] ? "ON" : "OFF").append("] ");
        }
        sb.deleteCharAt(sb.lastIndexOf(" "));
        return sb.toString();
    }
}
