package pl.boleklolek.dao.xml;

import pl.boleklolek.dao.serializers.Serializer;
import pl.boleklolek.dao.serializers.XMLSerializer;

import java.io.IOException;
import java.io.Serializable;

/**
 * Ta klasa przechowuje ustawienia aplikacji w pliku XML.
 */
public class Settings implements Serializable
{
    /**
     * Stałe pole zawierające identyfikator wersji.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Serializer.
     */
    private final transient Serializer serializer;

    /**
     * Szybkość aktualizacji średniej prędkości w sekundach.
     */
    private int frequencyOfAvgSpeed;
    /**
     * Indeks podróży na ekranie LCD.
     */
    private int actualTripIndex;

    /**
     * Konstruktor ustawień aplikacji.
     * Tworzy domyślne ustawienia.
     */
    public Settings()
    {
        this.serializer = new XMLSerializer();
        ((XMLSerializer) this.serializer).alias("settings", Settings.class);
        initDefault();
    }

    /**
     * Getter szybkości aktualizacji prędkości średniej.
     *
     * @return szybkość odświeżania w sekundach
     */
    public int getFrequencyOfAvgSpeed()
    {
        return frequencyOfAvgSpeed;
    }

    /**
     * Setter szybkości aktualizacji prędkości średniej.
     *
     * @param frequency szybkość odświeżania w sekundach
     */
    public void setFreqOfAvgSpeed(int frequency)
    {
        this.frequencyOfAvgSpeed = frequency;
    }

    /**
     * Getter indeksu ekranu LCD podróży.
     *
     * @return indeks ekranu LCD podróży
     */
    public int getActualTripIndex()
    {
        return actualTripIndex;
    }

    /**
     * Setter indeksu ekranu LCD podróży.
     *
     * @param tripIndex indeks ekranu LCD podróży
     */
    public void setActualTripIndex(int tripIndex)
    {
        this.actualTripIndex = tripIndex;
    }

    /**
     * Zapisuje ustawienia aplikacji do pliku XML (settings.xml).
     * Tworzy też ich kopie (settings_backup.xml).
     */
    public void save()
    {
        try
        {
            serializer.serialize(this, "settings.xml");
            serializer.serialize(this, "settings_backup.xml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Wczytuje ustawienia aplikacji z pliku XML (settings.xml).
     *
     * @throws IOException wyjątek wejścia/wyjścia
     */
    public void load() throws IOException, ClassNotFoundException
    {
        Settings settings = serializer.deserialize(Settings.class, "settings.xml");
        if (settings.getFrequencyOfAvgSpeed() <= 0 || settings.getActualTripIndex() < 0 || settings.getActualTripIndex() > 1)
        {
            initDefault();
        }
        else
        {
            frequencyOfAvgSpeed = settings.getFrequencyOfAvgSpeed();
            actualTripIndex = settings.getActualTripIndex();
        }
    }

    /**
     * Inicjalizuje ustawienia domyślne.
     * Szybkość aktualizacji prędkości średniej: 2 sekundy.
     * Indeks podróży na ekranie LCD: 0 (podróż A).
     */
    private void initDefault()
    {
        frequencyOfAvgSpeed = 2;
        actualTripIndex = 0;
    }
}
