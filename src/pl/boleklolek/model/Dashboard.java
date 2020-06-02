package pl.boleklolek.model;

import pl.boleklolek.dao.DashboardDAO;
import pl.boleklolek.dao.MemoryDAO;
import pl.boleklolek.dao.state.DashboardState;
import pl.boleklolek.dao.xml.MemoryXML;
import pl.boleklolek.dao.xml.Settings;
import pl.boleklolek.loop.DashboardLoop;
import pl.boleklolek.loop.Updateable;
import pl.boleklolek.model.computer.Computer;
import pl.boleklolek.model.computer.Memory;
import pl.boleklolek.model.lights.Lights;
import pl.boleklolek.model.lights.enums.FogLightsLocation;
import pl.boleklolek.model.lights.enums.HeadLightsPosition;
import pl.boleklolek.model.lights.enums.SignalLightsDirection;
import pl.boleklolek.model.odometer.Odometer;
import pl.boleklolek.model.radio.Radio;
import pl.boleklolek.model.speedometer.Speedometer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Ta klasa opisuje deskę rozdzielczą. Nadzoruje ona swoje elementy składowe.
 */
public class Dashboard implements Updateable
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
     * Światła.
     */
    private final Lights lights;
    /**
     * Komputer pokładowy.
     */
    private final Computer computer;
    /**
     * Radio.
     */
    private final Radio radio;
    /**
     * Ustawienia.
     */
    private final Settings settings;
    /**
     * DAO deski rozdzielczej.
     */
    private final DashboardDAO dashboardDAO;
    /**
     * DAO pamięci komputera pokładowego.
     */
    private MemoryDAO memoryDAO;

    /**
     * Konstruktor deski rozdzielczej.
     * Wczytuje ustawienia, stan, pamięć i ustawienia składowych deski rozdzielczej.
     * Gdy próba wczytania któregokolwiek z nich się nie powiedzie, tworzony jest jego nowy obiekt.
     *
     * @param memoryDAO DAO pamięci komputera pokładowego
     */
    public Dashboard(MemoryDAO memoryDAO)
    {
        this.settings = new Settings();
        try
        {
            this.settings.load();
        }
        catch (IOException | ClassNotFoundException e)
        {
            this.settings.save();
        }

        this.dashboardDAO = new DashboardState();
        this.memoryDAO = memoryDAO;
        Odometer odometer;
        try
        {
            odometer = this.dashboardDAO.loadOdometer();
        }
        catch (IOException | ClassNotFoundException e)
        {
            odometer = new Odometer();
        }
        Lights lights;
        try
        {
            lights = this.dashboardDAO.loadLights();
        }
        catch (IOException | ClassNotFoundException e)
        {
            lights = new Lights();
        }
        Memory memory;
        try
        {
            memory = this.memoryDAO.loadMemory();
        }
        catch (SQLException | ClassNotFoundException | IOException throwables)
        {
            memory = new Memory();
        }
        this.speedometer = new Speedometer();
        this.odometer = odometer;
        this.lights = lights;
        this.computer = new Computer(memory, this.speedometer, this.odometer);
        this.computer.setFreqOfAvgSpeed(settings.getFrequencyOfAvgSpeed());
        this.radio = new Radio();
        new DashboardLoop(this).start();
    }

    /**
     * Getter prędkościomierza.
     *
     * @return obiekt prędkościomierza
     */
    public Speedometer getSpeedometer()
    {
        return speedometer;
    }

    /**
     * Getter licznik przebiegu.
     *
     * @return obiekt licznika przebiegu
     */
    public Odometer getOdometer()
    {
        return odometer;
    }

    /**
     * Getter świateł.
     *
     * @return obiekt świateł
     */
    public Lights getLights()
    {
        return lights;
    }

    /**
     * Getter komputera pokładowego.
     *
     * @return obiekt komputera pokładowego
     */
    public Computer getComputer()
    {
        return computer;
    }

    /**
     * Getter ustawień.
     *
     * @return obiekt ustawień
     */
    public Settings getSettings()
    {
        return settings;
    }

    /**
     * Przyspiesza samochód.
     */
    public void accelerateCar()
    {
        double acceleration = speedometer.getDefaultAcceleration() + 25.0;
        speedometer.setAcceleration(acceleration);
    }

    /**
     * Wprowadza w stan bezczynności samochodu (nieprzyspieszanie ani niezwalnianie - bieg jałowy).
     */
    public void idleCar()
    {
        speedometer.setAcceleration(speedometer.getDefaultAcceleration());
    }

    /**
     * Zwalnia samochód.
     * Włączony tempomat jest dezaktywowany.
     */
    public void brakeCar()
    {
        double acceleration = speedometer.getDefaultAcceleration() - 35.0;
        speedometer.setAcceleration(acceleration);
        if (speedometer.isLimiter())
        {
            speedometer.setLimiter(false);
        }
    }

    /**
     * Przełącza tempomat.
     */
    public void toggleLimiter()
    {
        speedometer.toggleLimiter();
    }

    /**
     * Zmienia pozycję świateł głównych.
     *
     * @param position pozycja
     */
    public void changeHeadlights(HeadLightsPosition position)
    {
        lights.changeHeadlights(position);
    }

    /**
     * Włącza światła przeciwmgielne.
     *
     * @param location lokacja
     */
    public void toggleFogLights(FogLightsLocation location)
    {
        lights.toggleFogLights(location);
    }

    /**
     * Włącza kierunkowskaz.
     *
     * @param direction kierunek
     */
    public void turnOnSignalLight(SignalLightsDirection direction)
    {
        lights.turnOnSignal(direction);
    }

    /**
     * Wyłącza kierunkowskaz.
     */
    public void turnOffSignalLight()
    {
        lights.turnOffSignal();
    }

    /**
     * Włącza radio.
     */
    public void radioPlay()
    {
        radio.play();
    }

    /**
     * Pauzuje radio.
     */
    public void radioPause()
    {
        radio.pause();
    }

    /**
     * Wybiera piosenkę w radiu.
     *
     * @param file plik MP3
     */
    public void radioSelect(File file)
    {
        radio.select(file);
    }

    /**
     * Zmienia głośność radia.
     *
     * @param deltaVolume delta głośności
     */
    public void radioChangeVolume(int deltaVolume)
    {
        radio.changeVolume(deltaVolume);
    }

    /**
     * Wycisza radia.
     */
    public void radioToggleMute()
    {
        radio.toggleMute();
    }

    /**
     * Uruchamia komputer pokładowy.
     * Rozpoczyna pomiar czasu podróży.
     */
    public void startComputer()
    {
        for (int i = 0; i < computer.getTrips().length; i++)
        {
            computer.startTrip(i);
        }
        computer.startTimer();
    }

    /**
     * Startuje nową podróż.
     *
     * @param tripIndex indeks podróży (A - 0 lub B - 1)
     */
    public void startNewTrip(int tripIndex)
    {
        computer.createNewTrip(tripIndex);
        computer.startTrip(tripIndex);
    }

    /**
     * Resetuje podróż.
     *
     * @param tripIndex indeks podróży (A - 0 lub B - 1)
     */
    public void resetTrip(int tripIndex)
    {
        computer.resetTrip(tripIndex);
    }

    /**
     * Setter DAO pamięci.
     *
     * @param memoryDAO DAO pamięci
     */
    public void setMemoryDAO(MemoryDAO memoryDAO)
    {
        this.memoryDAO = memoryDAO;
    }

    /**
     * Wczytuje pamięć komputera pokładowego.
     * Wczytuje podróże i je startuje.
     *
     * @throws SQLException           wyjątek SQL
     * @throws ClassNotFoundException wyjątek klasa nie znaleziona
     * @throws IOException            wyjątek wejścia/wyjścia
     */
    public void loadMemory() throws SQLException, ClassNotFoundException, IOException
    {
        computer.setMemory(memoryDAO.loadMemory());
        for (int i = 0; i < computer.getTrips().length; i++)
        {
            computer.loadTrip(i);
            computer.startTrip(i);
        }
    }

    /**
     * Zapisuje pamięć komputera pokładowego.
     *
     * @throws SQLException           wyjątek SQL
     * @throws ClassNotFoundException wyjątek klasa nie znaleziona
     */
    public void saveMemory() throws SQLException, ClassNotFoundException
    {
        memoryDAO.saveMemory(computer.getMemory());
    }

    /**
     * Zapisuje ustawienia.
     */
    public void saveSettings()
    {
        settings.save();
    }

    /**
     * Zapisuje wszystkie elementy.
     */
    public void saveAll()
    {
        dashboardDAO.saveOdometer(odometer);
        dashboardDAO.saveLights(lights);
        settings.save();
        try
        {
            saveMemory();
        }
        catch (SQLException | ClassNotFoundException ignored)
        {
        }
    }

    /**
     * Setter szybkości aktualizacji średniej prędkości.
     *
     * @param frequency szybkość aktualizacji średniej prędkości w sekundach
     */
    public void setFreqOfAvgSpeed(int frequency)
    {
        computer.setFreqOfAvgSpeed(frequency);
        settings.setFreqOfAvgSpeed(frequency);
    }

    /**
     * Setter indeksu aktualnej podróży.
     *
     * @param index indeks podróży (A - 0 lub B - 1)
     */
    public void setActualTripIndex(int index)
    {
        settings.setActualTripIndex(index);
    }

    /**
     * Getter pliku pamięci.
     *
     * @return string z nazwą i rozszerzeniem pliku pamięci
     */
    public Optional<String> getMemoryFile()
    {
        String file = null;
        if (memoryDAO instanceof MemoryXML)
        {
            file = ((MemoryXML) memoryDAO).getMemoryFile();
        }
        return Optional.ofNullable(file);
    }

    /**
     * Setter pliku zapisu pamięci.
     *
     * @param saveFile plik zapisu
     */
    public void setMemoryFile(String saveFile)
    {
        if (memoryDAO instanceof MemoryXML)
        {
            ((MemoryXML) memoryDAO).setMemoryFile(saveFile);
        }
    }

    /**
     * Aktualizuje prędkościomierz i komputer pokładowy.
     */
    @Override
    public void update()
    {
        speedometer.update();
        computer.update();
    }

    /**
     * Zwraca informacje o desce rozdzielczej (predkościomierz, licznik przebiegu, światła, komputer pokładowy i radio).
     *
     * @return string z informacjami o desce rozdzielczej
     */
    @Override
    public String toString()
    {
        return "== DESKA ROZDZIELCZA ==" + System.lineSeparator() +
                speedometer + System.lineSeparator() +
                odometer + System.lineSeparator() +
                lights + System.lineSeparator() +
                radio + System.lineSeparator() +
                computer;
    }
}
