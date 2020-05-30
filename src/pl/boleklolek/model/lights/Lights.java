package pl.boleklolek.model.lights;

import pl.boleklolek.model.lights.enums.FogLightsLocation;
import pl.boleklolek.model.lights.enums.HeadLightsPosition;
import pl.boleklolek.model.lights.enums.SignalLightsDirection;

import java.io.Serializable;

/**
 * Ta klasa opisuje wszystkie światła.
 */
public class Lights implements Serializable
{
    /**
     * Stałe pole zawierające identyfikator wersji.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Światła główne.
     */
    private final HeadLights headLights;
    /**
     * Światła kierunkowe.
     */
    private final SignalLights signalLights;
    /**
     * Światła przeciwmgielne.
     */
    private final FogLights fogLights;

    /**
     * Konstruktor wszystkich świateł.
     */
    public Lights()
    {
        this.headLights = new HeadLights();
        this.signalLights = new SignalLights();
        this.fogLights = new FogLights();
    }

    /**
     * Getter świateł głównych.
     *
     * @return obiekt świateł głównych
     */
    public HeadLights getHeadLights()
    {
        return headLights;
    }

    /**
     * Getter świateł kierunkowych.
     *
     * @return obiekt świateł kierunkowych
     */
    public SignalLights getSignalLights()
    {
        return signalLights;
    }

    /**
     * Getter świateł przeciwmgielnych.
     *
     * @return obiekt świateł przeciwmgielnych
     */
    public FogLights getFogLights()
    {
        return fogLights;
    }

    /**
     * Zmienia pozycję świateł głównych.
     *
     * @param position pozycja
     */
    public void changeHeadlights(HeadLightsPosition position)
    {
        headLights.setPosition(position);
    }

    /**
     * Włącza kierunkowskaz.
     *
     * @param direction kierunek
     */
    public void turnOnSignal(SignalLightsDirection direction)
    {
        signalLights.turnOn(direction);
    }

    /**
     * Wyłącza kierunkowskaz.
     */
    public void turnOffSignal()
    {
        signalLights.turnOff();
    }

    /**
     * Włącza światła przeciwmgielne.
     *
     * @param location lokacja
     */
    public void toggleFogLights(FogLightsLocation location)
    {
        fogLights.toggle(location);
    }

    /**
     * Zwraca informację o wszystkich światłach.
     *
     * @return string z informacją o wszystkich światłach
     */
    @Override
    public String toString()
    {
        return "-- ŚWIATŁA --" + System.lineSeparator() +
                headLights + System.lineSeparator() +
                signalLights + System.lineSeparator() +
                fogLights;
    }
}
