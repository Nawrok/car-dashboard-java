package pl.boleklolek.view.console;

import io.bretty.console.view.MenuView;
import io.bretty.console.view.ViewConfig;
import pl.boleklolek.dao.DAO;
import pl.boleklolek.dao.MemoryDAO;
import pl.boleklolek.model.Dashboard;

import java.util.Map;

/**
 * Ta klasa obsługuje menu aplikacji w konsoli.
 */
public class Menu
{
    /**
     * Deska rozdzielcza.
     */
    final Dashboard dashboard;
    /**
     * DAO pamięci.
     */
    final Map<DAO, MemoryDAO> memoryDAOs;
    /**
     * Konfiguracja komunikatów menu.
     */
    final ViewConfig config;
    /**
     * Obiekt widoku menu.
     */
    private final MenuView menu;

    /**
     * Konstruktor Menu.
     * Tworzy konfigurację komunikatów menu.
     *
     * @param dashboard  deska rozdzielcza
     * @param memoryDAOS DAO pamięci
     */
    public Menu(Dashboard dashboard, Map<DAO, MemoryDAO> memoryDAOS)
    {
        this.dashboard = dashboard;
        this.memoryDAOs = memoryDAOS;
        this.config = buildConfig();
        this.menu = createMenuView();
    }

    /**
     * Wyświetla menu.
     */
    public void display()
    {
        menu.display();
    }

    /**
     * Tworzy konfigurację komunikatów menu.
     *
     * @return konfiguracja widoku menu
     */
    private ViewConfig buildConfig()
    {
        return new ViewConfig.Builder()
                .setInputErrorMessage("Błąd! Wybór: ")
                .setMenuSelectionMessage("Wybór: ")
                .setPauseMessage("Wciśnij enter, aby kontynuować...")
                .setQuitMessage("Zamykanie...")
                .setActionCanceledMessage("Anulowano!")
                .setActionSuccessfulMessage("Sukces!")
                .setActionFailedMessage("Błąd!")
                .setBackMenuName("Cofnij")
                .setQuitMenuName("Wyjdź")
                .build();
    }

    /**
     * Tworzy strukturę głównego menu.
     *
     * @return widok menu
     */
    private MenuView createMenuView()
    {
        MenuView menuView = new MenuView("Menu symulacji deski rozdzielczej samochodu", "", config);
        menuView.addMenuItem(new Drive(this, "Jazda", "Tryb jazdy", config));
        menuView.addMenuItem(new Trips(this, "Aktualne podróże", "Aktualne podróże", config));
        menuView.addMenuItem(new Memory(this, "Zarządzanie pamięcią komputera pokładowego", "Pamięć komputera pokładowego", config));
        menuView.addMenuItem(new Settings(this, "Ustawienia", "Ustawienia", config));
        return menuView;
    }
}
