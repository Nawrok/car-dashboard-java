package pl.boleklolek.view.console;

import io.bretty.console.view.ActionView;
import io.bretty.console.view.MenuView;
import io.bretty.console.view.ViewConfig;
import pl.boleklolek.model.lights.enums.FogLightsLocation;
import pl.boleklolek.model.lights.enums.HeadLightsPosition;
import pl.boleklolek.model.lights.enums.SignalLightsDirection;

/**
 * Ta klasa zapewnia obsługę samochodu (deski rozdzielczej).
 */
class Drive extends MenuView
{
    /**
     * Menu.
     */
    private final Menu menu;

    /**
     * Konstruktor Drive.
     * Tworzy strukturę trybu jazdy.
     *
     * @param menu             menu
     * @param runningTitle     nazwa czynności
     * @param nameInParentMenu nazwa w menu
     * @param viewConfig       konfiguracja
     */
    public Drive(Menu menu, String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
    {
        super(runningTitle, nameInParentMenu, viewConfig);
        this.menu = menu;
        this.addMenuItem(new AccelerateAction("Przyspieszanie...", "Przyspiesz", menu.config));
        this.addMenuItem(new BrakeAction("Hamowanie...", "Hamuj", menu.config));
        this.addMenuItem(new IdleAction("Wrzucanie luzu...", "Wrzuć na luz", menu.config));
        this.addMenuItem(new LimiterAction("Przełączanie tempomatu...", "Przełącz tempomat", menu.config));
        this.addMenuItem(new HeadLightsAction("Przełączanie świateł głównych...", "Przełącz światła główne", menu.config));
        this.addMenuItem(new SignalLightsLeftAction("Włączanie lewego kierunkowskazu...", "Włącz lewy kierunkowskaz", menu.config));
        this.addMenuItem(new SignalLightsRightAction("Włączanie prawego kierunkowskazu...", "Włącz prawy kierunkowskaz", menu.config));
        this.addMenuItem(new SignalLightsOffAction("Wyłączanie kierunkowskazu...", "Wyłącz kierunkowskaz", menu.config));
        this.addMenuItem(new FogLightsFrontAction("Przełączanie świateł przeciwmgielnych przednich...", "Przełącz światła przeciwmgielne przednie", menu.config));
        this.addMenuItem(new FogLightsBackAction("Przełączanie świateł przeciwmgielnych tylnych...", "Przełącz światła przeciwmgielne tylne", menu.config));
        this.addMenuItem(new RefreshAction("Odświeżanie...", "Odśwież", menu.config));
        this.refreshState();
    }

    /**
     * Odświeża stan deski rozdzielczej w menu.
     */
    private void refreshState()
    {
        String car = menu.dashboard.getSpeedometer() + System.lineSeparator() +
                menu.dashboard.getOdometer() + System.lineSeparator() +
                menu.dashboard.getLights() + System.lineSeparator();
        this.setRunningTitle(car);
    }

    /**
     * Klasa czynności obsługującej przyspieszenie samochodu.
     */
    class AccelerateAction extends ActionView
    {
        /**
         * Konstruktor AccelerateAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public AccelerateAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Przyspiesza samochód.
         */
        @Override
        public void executeCustomAction()
        {
            menu.dashboard.accelerateCar();
            refreshState();
            goBack();
        }
    }

    /**
     * Klasa czynności obsługującej hamowanie samochodu.
     */
    class BrakeAction extends ActionView
    {
        /**
         * Konstruktor BrakeAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public BrakeAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Hamuje samochód.
         */
        @Override
        public void executeCustomAction()
        {
            menu.dashboard.brakeCar();
            refreshState();
            goBack();
        }
    }

    /**
     * Klasa czynności obsługującej stan bezczynności samochodu.
     */
    class IdleAction extends ActionView
    {
        /**
         * Konstruktor IdleAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public IdleAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Wprowadza samochód w stan bezczynności (bieg jałowy).
         */
        @Override
        public void executeCustomAction()
        {
            menu.dashboard.idleCar();
            refreshState();
            goBack();
        }
    }

    /**
     * Klasa czynności obsługującej tempomat.
     */
    class LimiterAction extends ActionView
    {
        /**
         * Konstruktor LimiterAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public LimiterAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Przełącza tempomat.
         */
        @Override
        public void executeCustomAction()
        {
            menu.dashboard.toggleLimiter();
            refreshState();
            goBack();
        }
    }

    /**
     * Klasa czynności obsługującej zmianę głównych świateł.
     */
    class HeadLightsAction extends ActionView
    {
        /**
         * Aktualna pozycja świateł.
         */
        private int position;

        /**
         * Konstruktor HeadLightsAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public HeadLightsAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
            this.position = menu.dashboard.getLights().getHeadLights().getPosition().ordinal();
        }

        /**
         * Przełącza światła główne w kierunku najmocniejszych świateł.
         */
        @Override
        public void executeCustomAction()
        {
            position++;
            if (position > HeadLightsPosition.values().length - 1)
            {
                position = 0;
            }
            switch (position)
            {
                case 0:
                {
                    menu.dashboard.changeHeadlights(HeadLightsPosition.OFF);
                    break;
                }
                case 1:
                {
                    menu.dashboard.changeHeadlights(HeadLightsPosition.DAILY);
                    break;
                }
                case 2:
                {
                    menu.dashboard.changeHeadlights(HeadLightsPosition.LOWBEAM);
                    break;
                }
                case 3:
                {
                    menu.dashboard.changeHeadlights(HeadLightsPosition.HIGHBEAM);
                    break;
                }
            }
            refreshState();
            goBack();
        }
    }

    /**
     * Klasa czynności obsługującej lewy kierunkowskaz.
     */
    class SignalLightsLeftAction extends ActionView
    {
        /**
         * Konstruktor SignalLightsLeftAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public SignalLightsLeftAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Włącza lewy kierunkowskaz.
         */
        @Override
        public void executeCustomAction()
        {
            menu.dashboard.turnOnSignalLight(SignalLightsDirection.LEFT);
            refreshState();
            goBack();
        }
    }

    /**
     * Klasa czynności obsługującej prawy kierunkowskaz.
     */
    class SignalLightsRightAction extends ActionView
    {
        /**
         * Konstruktor SignalLightsRightAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public SignalLightsRightAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Włącza prawy kierunkowskaz.
         */
        @Override
        public void executeCustomAction()
        {
            menu.dashboard.turnOnSignalLight(SignalLightsDirection.RIGHT);
            refreshState();
            goBack();
        }
    }

    /**
     * Klasa czynności obsługującej wyłączenie kierunkowskazu.
     */
    class SignalLightsOffAction extends ActionView
    {
        /**
         * Konstruktor SignalLightsOffAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public SignalLightsOffAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Wyłącza kierunkowskaz.
         */
        @Override
        public void executeCustomAction()
        {
            menu.dashboard.turnOffSignalLight();
            refreshState();
            goBack();
        }
    }

    /**
     * Klasa czynności obsługującej światła przeciwmgielne przednie.
     */
    class FogLightsFrontAction extends ActionView
    {
        /**
         * Konstruktor FogLightsFrontAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public FogLightsFrontAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Przełącza światła przeciwmgielne przednie.
         */
        @Override
        public void executeCustomAction()
        {
            menu.dashboard.toggleFogLights(FogLightsLocation.FRONT);
            refreshState();
            goBack();
        }
    }

    /**
     * Klasa czynności obsługującej światła przeciwmgielne tylne.
     */
    class FogLightsBackAction extends ActionView
    {
        /**
         * Konstruktor FogLightsBackAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public FogLightsBackAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Przełącza światła przeciwmgielne tylne.
         */
        @Override
        public void executeCustomAction()
        {
            menu.dashboard.toggleFogLights(FogLightsLocation.BACK);
            refreshState();
            goBack();
        }
    }

    /**
     * Klasa czynności obsługującej odświeżanie stanu deski rozdzielczej.
     */
    class RefreshAction extends ActionView
    {
        /**
         * Konstruktor RefreshAction.
         *
         * @param runningTitle     nazwa czynności
         * @param nameInParentMenu nazwa w menu
         * @param viewConfig       konfiguracja
         */
        public RefreshAction(String runningTitle, String nameInParentMenu, ViewConfig viewConfig)
        {
            super(runningTitle, nameInParentMenu, viewConfig);
        }

        /**
         * Odświeża informacje o desce rozdzielczej.
         */
        @Override
        public void executeCustomAction()
        {
            refreshState();
            goBack();
        }
    }
}
