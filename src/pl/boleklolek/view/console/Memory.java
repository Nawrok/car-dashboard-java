package pl.boleklolek.view.console;

import io.bretty.console.view.ActionView;
import io.bretty.console.view.MenuView;
import io.bretty.console.view.ViewConfig;
import pl.boleklolek.dao.DAO;

/**
 * Ta klasa zapewnia zarządzanie pamięcią komputera pokładowego.
 */
class Memory extends MenuView
{
    /**
     * Menu.
     */
    private final Menu menu;

    /**
     * Konstruktor Memory.
     * Tworzy strukturę menu zarządzania pamięcią.
     *
     * @param menu             menu
     * @param runningTitle     nazwa czynności
     * @param nameInParentMenu nazwa w menu
     * @param viewConfig       konfiguracja
     */
    public Memory(Menu menu, String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
    {
        super(runningTitle, nameInParentMenu, viewConfig);
        this.menu = menu;
        this.addMenuItem(new NewTripAction("Tworzenie nowej podróży", "Nowa podróż", menu.config));
        this.addMenuItem(new ResetTripAction("Resetowanie podróży", "Reset podróży", menu.config));
        this.addMenuItem(new ShowMemoryAction("Zawartość komputera pokładowego", "Sprawdź pamięć", menu.config));
        String warning = "Cała pamięć komputera pokładowego wraz z aktualnymi podróżami zostanie UTRACONA!";
        this.addMenuItem(new ImportMenu("Import pamięci" + System.lineSeparator() + warning, "Import", menu.config));
        this.addMenuItem(new ExportMenu("Export pamięci", "Export", menu.config));
    }

    /**
     * Klasa czynności obsługującej tworzenie nowej podróży.
     */
    class NewTripAction extends ActionView
    {
        /**
         * Konstruktor NewTripAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public NewTripAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Zapisuje poprzednią i tworzy nową podróż.
         */
        @Override
        public void executeCustomAction()
        {
            int trip = this.prompt("Wybierz podróż (A - 0, B - 1): ", Integer.class);
            if (trip == 0 || trip == 1)
            {
                menu.dashboard.startNewTrip(trip);
                this.actionSuccessful();
            }
            else
            {
                this.actionFailed();
            }
        }
    }

    /**
     * Klasa czynności obsługującej resetowanie podróży.
     */
    class ResetTripAction extends ActionView
    {
        /**
         * Konstruktor ResetTripAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public ResetTripAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Resetuje daną podróż.
         */
        @Override
        public void executeCustomAction()
        {
            int trip = this.prompt("Wybierz podróż (A - 0, B - 1): ", Integer.class);
            if (trip == 0 || trip == 1)
            {
                menu.dashboard.resetTrip(trip);
                this.actionSuccessful();
            }
            else
            {
                this.actionFailed();
            }
        }
    }

    /**
     * Klasa czynności obsługującej wyświetlenie informacji o podróżach.
     */
    class ShowMemoryAction extends ActionView
    {
        /**
         * Konstruktor ShowMemoryAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public ShowMemoryAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Wyświetla informacje o podróżach z komputera pokładowego.
         */
        @Override
        public void executeCustomAction()
        {
            System.out.println(menu.dashboard.getComputer().getMemory());
        }
    }

    /**
     * Klasa obsługująca strukturę menu importu pamięci.
     */
    class ImportMenu extends MenuView
    {
        /**
         * Konstruktor ImportMenu.
         * Tworzy strukturę import menu.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public ImportMenu(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
            this.addMenuItem(new ImportXmlAction("Import XML", "XML", menu.config));
            this.addMenuItem(new ImportSqlAction("Import SQL", "SQL", menu.config));
        }

        /**
         * Klasa czynności obsługującej import plików XML.
         */
        class ImportXmlAction extends ActionView
        {
            /**
             * Konstruktor ImportXmlAction.
             *
             * @param runningTitle     nazwa czynności
             * @param nameInParentMenu nazwa w menu
             * @param viewConfig       konfiguracja
             */
            public ImportXmlAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
            {
                super(runningTitle, nameInParentMenu, viewConfig);
            }

            /**
             * Wczytuje plik XML z pamięcią komputera pokładowego.
             */
            @Override
            public void executeCustomAction()
            {
                String filepath = this.prompt("Wprowadź ścieżkę do pliku XML: ", String.class);
                String prevSaveFile = menu.dashboard.getMemoryFile();
                try
                {
                    menu.dashboard.setMemoryFile(filepath);
                    menu.dashboard.loadMemory();
                    this.actionSuccessful();
                }
                catch (Exception exception)
                {
                    this.actionFailed();
                }
                finally
                {
                    menu.dashboard.setMemoryFile(prevSaveFile);
                }
            }
        }

        /**
         * Klasa czynności obsługującej import z bazy danych.
         */
        class ImportSqlAction extends ActionView
        {
            /**
             * Konstruktor ImportSqlAction.
             *
             * @param runningTitle     nazwa czynności
             * @param nameInParentMenu nazwa w menu
             * @param viewConfig       konfiguracja
             */
            public ImportSqlAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
            {
                super(runningTitle, nameInParentMenu, viewConfig);
            }

            /**
             * Wczytuje pamięć komputera pokładowego z bazy danych.
             */
            @Override
            public void executeCustomAction()
            {
                menu.dashboard.setMemoryDAO(menu.memoryDAOs.get(DAO.DATABASE));
                try
                {
                    menu.dashboard.loadMemory();
                    this.actionSuccessful();
                }
                catch (Exception exception)
                {
                    this.actionFailed();
                }
                finally
                {
                    menu.dashboard.setMemoryDAO(menu.memoryDAOs.get(DAO.XML));
                }
            }
        }
    }

    /**
     * Klasa obsługująca strukturę menu exportu pamięci.
     */
    class ExportMenu extends MenuView
    {
        /**
         * Konstruktor ExportMenu.
         * Tworzy strukturę export menu.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public ExportMenu(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
            this.addMenuItem(new ExportXmlAction("Export XML", "XML", menu.config));
            this.addMenuItem(new ExportSqlAction("Export SQL", "SQL", menu.config));
        }

        /**
         * Klasa czynności obsługującej export plików XML.
         */
        class ExportXmlAction extends ActionView
        {
            /**
             * Konstruktor ExportXmlAction.
             *
             * @param runningTitle     nazwa czynności
             * @param nameInParentMenu nazwa w menu
             * @param viewConfig       konfiguracja
             */
            public ExportXmlAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
            {
                super(runningTitle, nameInParentMenu, viewConfig);
            }

            /**
             * Zapisuje pamięć komputera pokładowego do pliku XML.
             */
            @Override
            public void executeCustomAction()
            {
                String filepath = this.prompt("Wprowadź nazwę pliku XML (bez rozszerzenia): ", String.class);
                String prevSaveFile = menu.dashboard.getMemoryFile();
                try
                {
                    menu.dashboard.setMemoryFile(filepath + ".xml");
                    menu.dashboard.saveMemory();
                    this.actionSuccessful();
                }
                catch (Exception exception)
                {
                    this.actionFailed();
                }
                finally
                {
                    menu.dashboard.setMemoryFile(prevSaveFile);
                }
            }
        }

        /**
         * Klasa czynności obsługującej export do bazy danych.
         */
        class ExportSqlAction extends ActionView
        {
            /**
             * Konstruktor ExportSqlAction.
             *
             * @param runningTitle     nazwa czynności
             * @param nameInParentMenu nazwa w menu
             * @param viewConfig       konfiguracja
             */
            public ExportSqlAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
            {
                super(runningTitle, nameInParentMenu, viewConfig);
            }

            /**
             * Zapisuje pamięć komputera pokładowego do bazy danych.
             */
            @Override
            public void executeCustomAction()
            {
                menu.dashboard.setMemoryDAO(menu.memoryDAOs.get(DAO.DATABASE));
                try
                {
                    menu.dashboard.saveMemory();
                    this.actionSuccessful();
                }
                catch (Exception exception)
                {
                    this.actionFailed();
                }
                finally
                {
                    menu.dashboard.setMemoryDAO(menu.memoryDAOs.get(DAO.XML));
                }
            }
        }
    }
}
