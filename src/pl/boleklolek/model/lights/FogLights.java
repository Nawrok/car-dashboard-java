package pl.boleklolek.model.lights;

import pl.boleklolek.model.lights.enums.FogLightsLocation;

import java.io.Serializable;

/**
 * Ta klasa opisuje światła przeciwmgielne.
 */
public class FogLights implements Serializable
{
    /**
     * Stałe pole zawierające identyfikator wersji.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Tablica świateł.
     */
    private final boolean[] lights;

    /**
     * Konstruktor świateł przeciwmgielnych.
     */
    public FogLights()
    {
        this.lights = new boolean[2];
    }

    /**
     * Getter świateł.
     *
     * @return kopia tablicy wartości logicznych zawierająca prawdy, jeśli odpowiednie światła są włączone
     */
    public boolean[] getLights()
    {
        return lights.clone();
    }

    /**
     * Przełącza światła.
     *
     * @param location lokacja
     */
    public void toggle(FogLightsLocation location)
    {
        int index = location.ordinal();
        lights[index] = !lights[index];
    }

    /**
     * Zwraca informację o światłach przeciwmgielnych.
     *
     * @return string z informacją o światłach przeciwmgielnych
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Światła przeciwmgielne: ");
        for (int i = 0; i < lights.length; i++)
        {
            sb.append(FogLightsLocation.values()[i]).append(" [").append(lights[i] ? "ON" : "OFF").append("] ");
        }
        sb.deleteCharAt(sb.lastIndexOf(" "));
        return sb.toString();
    }
}
