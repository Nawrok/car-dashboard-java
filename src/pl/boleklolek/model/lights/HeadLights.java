package pl.boleklolek.model.lights;

import pl.boleklolek.model.lights.enums.HeadLightsPosition;

import java.io.Serializable;

/**
 * Ta klasa opisuje światła główne.
 */
public class HeadLights implements Serializable
{
    /**
     * Stałe pole zawierające identyfikator wersji.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Pozycja świateł głównych.
     */
    private HeadLightsPosition position;

    /**
     * Konstruktor świateł głównych.
     * Domyślnie światła są ustawione w pozycji OFF.
     */
    public HeadLights()
    {
        this.position = HeadLightsPosition.OFF;
    }

    /**
     * Getter pozycji.
     *
     * @return pozycję świateł głównych
     */
    public HeadLightsPosition getPosition()
    {
        return position;
    }

    /**
     * Setter pozycji.
     *
     * @param position pozycja
     */
    public void setPosition(HeadLightsPosition position)
    {
        this.position = position;
    }

    /**
     * Zwraca informację o światłach głównych.
     *
     * @return string, mówiący które światła są aktualnie głównymi
     */
    @Override
    public String toString()
    {
        return "Światła główne: " + position.name();
    }
}
