package pl.boleklolek.dao.state;

import pl.boleklolek.dao.DashboardDAO;
import pl.boleklolek.dao.serializers.ObjectSerializer;
import pl.boleklolek.dao.serializers.Serializer;
import pl.boleklolek.model.lights.Lights;
import pl.boleklolek.model.odometer.Odometer;

import java.io.IOException;

/**
 * Ta klasa obsługuje zapis stanu deski rozdzielczej - licznika przebiegu i świateł.
 */
public class DashboardState implements DashboardDAO
{
    /**
     * Serializer.
     */
    private final Serializer serializer;
    /**
     * Plik zapisu licznika przebiegu.
     */
    private final String odometerSaveFile;
    /**
     * Plik zapisu świateł.
     */
    private final String lightsSaveFile;

    /**
     * Konstruktor stanu deski rozdzielczej.
     * Tworzy domyślne pliki zapisu stanu licznika przebiegu i świateł w folderze aplikacji.
     * Plik stanu licznika przebiegu: dashboard_odometer.sav
     * Plik stanu świateł: dashboard_lights.sav
     */
    public DashboardState()
    {
        this.odometerSaveFile = "dashboard_odometer.sav";
        this.lightsSaveFile = "dashboard_lights.sav";
        this.serializer = new ObjectSerializer();
    }

    /**
     * Zapisuje stan licznika przebiegu.
     *
     * @param odometer licznik przebiegu, którego stan chcemy zapisać
     */
    @Override
    public void saveOdometer(Odometer odometer)
    {
        try
        {
            serializer.serialize(odometer, odometerSaveFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Wczytuje stan licznika przebiegu.
     *
     * @return wczytany obiekt licznika przebiegu
     * @throws IOException            wyjątek wejścia/wyjścia
     * @throws ClassNotFoundException wyjątek klasa nie znaleziona
     */
    @Override
    public Odometer loadOdometer() throws IOException, ClassNotFoundException
    {
        return serializer.deserialize(Odometer.class, odometerSaveFile);
    }

    /**
     * Zapisuje stan świateł.
     *
     * @param lights światła, których stan chcemy zapisać
     */
    @Override
    public void saveLights(Lights lights)
    {
        try
        {
            serializer.serialize(lights, lightsSaveFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Wczytuje stan świateł.
     *
     * @return wczytany obiekt świateł
     * @throws IOException            wyjątek wejścia/wyjścia
     * @throws ClassNotFoundException wyjątek klasa nie znaleziona
     */
    @Override
    public Lights loadLights() throws IOException, ClassNotFoundException
    {
        return serializer.deserialize(Lights.class, lightsSaveFile);
    }
}
