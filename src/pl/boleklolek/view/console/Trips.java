package pl.boleklolek.view.console;

import io.bretty.console.view.ActionView;
import io.bretty.console.view.ViewConfig;

/**
 * Klasa czynności obsługującej wyświetlenie aktualnych podróży.
 */
class Trips extends ActionView
{
    /**
     * Menu.
     */
    private final Menu menu;

    /**
     * Konstruktor Trips.
     *
     * @param menu             menu
     * @param runningTitle     nazwa czynności
     * @param nameInParentMenu nazwa w menu
     * @param viewConfig       konfiguracja
     */
    public Trips(Menu menu, String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
    {
        super(runningTitle, nameInParentMenu, viewConfig);
        this.menu = menu;
    }

    /**
     * Wyświetla informacje o aktualnych podróżach (A i B).
     */
    @Override
    public void executeCustomAction()
    {
        System.out.println("[Podróż A]" + menu.dashboard.getComputer().getTrips()[0].toString().substring(8) + System.lineSeparator());
        System.out.println("[Podróż B]" + menu.dashboard.getComputer().getTrips()[1].toString().substring(8) + System.lineSeparator());
    }
}
