package pl.boleklolek.dao;

import pl.boleklolek.model.lights.Lights;
import pl.boleklolek.model.odometer.Odometer;

import java.io.IOException;

/**
 * Ten interfejs DAO (Data Access Object) zawiera metody zapisu/odczytu stanu licznika przebiegu i świateł.
 */
public interface DashboardDAO
{
    /**
     * Zapisuje stan licznika przebiegu.
     *
     * @param odometer licznik przebiegu, którego stan chcemy zapisać
     */
    void saveOdometer(Odometer odometer);

    /**
     * Wczytuje stan licznika przebiegu.
     *
     * @return wczytany obiekt licznika przebiegu
     * @throws IOException            wyjątek wejścia/wyjścia
     * @throws ClassNotFoundException wyjątek klasa nie znaleziona
     */
    Odometer loadOdometer() throws IOException, ClassNotFoundException;

    /**
     * Zapisuje stan świateł.
     *
     * @param lights światła, których stan chcemy zapisać
     */
    void saveLights(Lights lights);

    /**
     * Wczytuje stan świateł.
     *
     * @return wczytany obiekt świateł
     * @throws IOException            wyjątek wejścia/wyjścia
     * @throws ClassNotFoundException wyjątek klasa nie znaleziona
     */
    Lights loadLights() throws IOException, ClassNotFoundException;
}
