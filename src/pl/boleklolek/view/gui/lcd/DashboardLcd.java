package pl.boleklolek.view.gui.lcd;

import eu.hansolo.steelseries.gauges.Radial;

/**
 * Ta klasa obsługuje ekran LCD prędkościomierza.
 */
public class DashboardLcd
{
    /**
     * Komponent prędkościomierza.
     */
    private final Radial speedometer;
    /**
     * Informacja na ekranie LCD.
     */
    private LcdInformation lcdInformation;

    /**
     * Konstruktor klasy ekranu LCD.
     *
     * @param speedometer komponent prędkościomierza
     */
    public DashboardLcd(Radial speedometer)
    {
        this.speedometer = speedometer;
        this.lcdInformation = LcdInformation.SPEED;
        changeLcdInformation();
    }

    /**
     * Getter informacji wyświetlanej na ekranie LCD.
     *
     * @return nazwa informacji LCD
     */
    public LcdInformation getLcdInformation()
    {
        return lcdInformation;
    }

    /**
     * Ustawia następną informację na ekranie LCD.
     */
    public void nextInfo()
    {
        lcdInformation = lcdInformation.next();
        changeLcdInformation();
    }

    /**
     * Ustawia poprzednią informację na ekranie LCD.
     */
    public void prevInfo()
    {
        lcdInformation = lcdInformation.prev();
        changeLcdInformation();
    }

    /**
     * Zmienia informację na ekranie LCD. W zależności od wyboru zostanie wyświetlana dana wartość statystyczna podróży z jednostką.
     */
    private void changeLcdInformation()
    {
        switch (lcdInformation)
        {
            case SPEED:
            {
                speedometer.setLcdInfoString("prędkość");
                speedometer.setLcdDecimals(0);
                speedometer.setLcdUnitString("km/h");
                break;
            }
            case TRIP_MILEAGE:
            {
                speedometer.setLcdInfoString("przebieg podróży");
                speedometer.setLcdDecimals(1);
                speedometer.setLcdUnitString("km");
                break;
            }
            case TOTAL_MILEAGE:
            {
                speedometer.setLcdInfoString("przebieg całkowity");
                speedometer.setLcdDecimals(1);
                speedometer.setLcdUnitString("km");
                break;
            }
            case AVG_SPEED:
            {
                speedometer.setLcdInfoString("średnia prędkość");
                speedometer.setLcdDecimals(1);
                speedometer.setLcdUnitString("km/h");
                break;
            }
            case MAX_SPEED:
            {
                speedometer.setLcdInfoString("maksymalna prędkość");
                speedometer.setLcdDecimals(1);
                speedometer.setLcdUnitString("km/h");
                break;
            }
            case TIME:
            {
                speedometer.setLcdInfoString("czas podróży");
                speedometer.setLcdDecimals(0);
                speedometer.setLcdUnitString("s");
                break;
            }
        }
    }
}
