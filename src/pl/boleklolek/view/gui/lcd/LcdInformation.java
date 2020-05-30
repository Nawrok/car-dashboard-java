package pl.boleklolek.view.gui.lcd;

/**
 * Ta klasa enum opisuje informacje wyświetlane na ekranie LCD.
 */
public enum LcdInformation
{
    /**
     * Informacja LCD: Prędkość.
     */
    SPEED
            {
                @Override
                public LcdInformation prev()
                {
                    return values()[values().length - 1];
                }
            },
    /**
     * Informacja LCD: Przebieg aktualnej podróży.
     */
    TRIP_MILEAGE,
    /**
     * Informacja LCD: Przebieg całkowity.
     */
    TOTAL_MILEAGE,
    /**
     * Informacja LCD: Średnia prędkość.
     */
    AVG_SPEED,
    /**
     * Informacja LCD: Prędkość maksymalna.
     */
    MAX_SPEED,
    /**
     * Informacja LCD: Czas podróży.
     */
    TIME
            {
                @Override
                public LcdInformation next()
                {
                    return values()[0];
                }
            };

    /**
     * Zwraca następną informację na ekranie LCD.
     *
     * @return nazwa informacji LCD
     */
    public LcdInformation next()
    {
        return values()[ordinal() + 1];
    }

    /**
     * Zwraca poprzednią informację na ekranie LCD.
     *
     * @return nazwa informacji LCD
     */
    public LcdInformation prev()
    {
        return values()[ordinal() - 1];
    }
}
