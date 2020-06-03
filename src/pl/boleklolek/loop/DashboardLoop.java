package pl.boleklolek.loop;

import pl.boleklolek.utils.SingletonExecutor;

import java.util.concurrent.TimeUnit;

/**
 * Ta klasa obsługuje pętlę główną aplikacji w celu aktualizacji zmiennych.
 */
public class DashboardLoop
{
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
     * Wywołuje w każdym kroku metodę update().
     */
    public void start()
    {
        if (!running)
        {
            SingletonExecutor.getInstance().scheduleAtFixedRate(dashboard::update, 0, Updateable.TICK, TimeUnit.MICROSECONDS);
            running = true;
        }
    }
}
