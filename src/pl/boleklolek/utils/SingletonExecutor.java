package pl.boleklolek.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Ta klasa zawiera serwis przydzielający pulę wątków o charakterze Singleton.
 */
public class SingletonExecutor
{
    /**
     * Konstruktor prywatny.
     */
    private SingletonExecutor()
    {
    }

    /**
     * Stały getter instancji ScheduledExecutorService - zapewnia tylko jeden obiekt w obrębie całej aplikacji.
     *
     * @return instancja serwisu
     */
    public static ScheduledExecutorService getInstance()
    {
        return Holder.INSTANCE;
    }

    /**
     * Klasa trzymająca instancję serwisu przydzielającego wątki.
     */
    private static class Holder
    {
        /**
         * Stała instancja serwisu z przydzielonymi 8 wątkami.
         */
        private static final ScheduledExecutorService INSTANCE = Executors.newScheduledThreadPool(8);
    }
}
