package pl.boleklolek.loop;

import pl.boleklolek.utils.SingletonExecutor;

import java.util.concurrent.TimeUnit;

/**
 * Ta klasa obsługuje pętlę główną aplikacji w celu aktualizacji zmiennych.
 */
public class DashboardLoop
{
    /**
     * Stałe pole TICK - okres jednego kroku w pętli aplikacji (mikrosekundy).
     */
    private static final long TICK = 10_000L;
    /**
     * Stałe pole DELTA TIME - czas pomiędzy kolejnymi aktualizacjami w mikrosekundach.
     */
    public static final long DELTA_TIME = 1_000_000L / TICK;
    /**
     * Pole informujące, czy pętla jest uruchomiona.
     */
    private static boolean running;
    /**
     * Obiekt implementujący interfejs Updateable (deska rozdzielcza).
     */
    private final Updateable dashboard;

    /**
     * Konstruktor pętli deski rozdzielczej.
     *
     * @param dashboard deska rozdzielcza
     */
    public DashboardLoop(Updateable dashboard)
    {
        this.dashboard = dashboard;
    }

    /**
     * Startuje pętlę, jeżeli nie została ona wcześniej uruchomiona.
     * Wywołuje w każdym kroku aktualizację danych w obiekcie.
     */
    public void start()
    {
        if (!running)
        {
            SingletonExecutor.getInstance().scheduleAtFixedRate(dashboard::update, 0, TICK, TimeUnit.MICROSECONDS);
            running = true;
        }
    }
}
