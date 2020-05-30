package pl.boleklolek.view.console;

import io.bretty.console.view.ActionView;
import io.bretty.console.view.MenuView;
import io.bretty.console.view.ViewConfig;

/**
 * Ta klasa zapewnia obsługę menu ustawień.
 */
class Settings extends MenuView
{
    /**
     * Menu.
     */
    private final Menu menu;

    /**
     * Konstruktor Settings.
     * Tworzy strukturę menu ustawień.
     *
     * @param menu             menu
     * @param runningTitle     nazwa czynności
     * @param nameInParentMenu nazwa w menu
     * @param viewConfig       konfiguracja
     */
    public Settings(Menu menu, String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
    {
        super(runningTitle, nameInParentMenu, viewConfig);
        this.menu = menu;
        this.addMenuItem(new AvgSpeedFreqAction("Ustawianie szybkości aktualizacji prędkości średniej", "Zmień szybkość aktualizacji prędkości średniej", menu.config));
        this.addMenuItem(new TripIndexLcdAction("Zmiana wyświetlanej podróży na ekranie LCD", "Zmień wyświetlaną podróż na LCD", menu.config));
        this.addMenuItem(new SaveAction("Zapis ustawień", "Zapisz", menu.config));
    }

    /**
     * Klasa czynności obsługującej zmianę szybkości aktualizacji średniej prędkości.
     */
    class AvgSpeedFreqAction extends ActionView
    {
        /**
         * Konstruktor AvgSpeedFreqAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public AvgSpeedFreqAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Zmienia szybkość aktualizacji średniej prędkości.
         */
        @Override
        public void executeCustomAction()
        {
            System.out.println("Zmiana nastąpi przy ponownym uruchomieniu programu lub przy utworzeniu nowej podróży.");
            System.out.println("Aktualna szybkość aktualizacji prędkości średniej: " + menu.dashboard.getSettings().getFrequencyOfAvgSpeed() + " sekundy");
            int freq = this.prompt("Podaj dodatnią liczbę całkowitą: ", Integer.class);
            if (freq > 0)
            {
                menu.dashboard.getSettings().setFreqOfAvgSpeed(freq);
                this.actionSuccessful();
            }
            else
            {
                this.actionFailed();
            }
        }
    }

    /**
     * Klasa czynności obsługującej zmianę aktualnie wyświetlanej podróży na ekranie LCD.
     */
    class TripIndexLcdAction extends ActionView
    {
        /**
         * Konstruktor TripIndexLcdAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public TripIndexLcdAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Zmienia aktualnie wyświetlaną podróż na ekranie LCD.
         */
        @Override
        public void executeCustomAction()
        {
            System.out.println("Aktualnie wyświetlana podróż: " + (menu.dashboard.getSettings().getActualTripIndex() == 0 ? "A" : "B"));
            int trip = this.prompt("Wybierz podróż (A - 0, B - 1): ", Integer.class);
            if (trip == 0 || trip == 1)
            {
                menu.dashboard.getSettings().setActualTripIndex(trip);
                this.actionSuccessful();
            }
            else
            {
                this.actionFailed();
            }
        }
    }

    /**
     * Klasa czynności obsługującej zapis ustawień.
     */
    class SaveAction extends ActionView
    {
        /**
         * Konstruktor SaveAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public SaveAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Zapisuje ustawienia.
         */
        @Override
        public void executeCustomAction()
        {
            menu.dashboard.saveSettings();
            System.out.println("Zapisano ustawienia!");
        }
    }
}
