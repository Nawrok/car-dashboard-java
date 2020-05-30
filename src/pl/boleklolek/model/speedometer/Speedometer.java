package pl.boleklolek.model.speedometer;

import pl.boleklolek.loop.DashboardLoop;

/**
 * Ta klasa opisuje prędkościomierz.
 */
public class Speedometer
{
    /**
     * Prędkość maksymalna.
     */
    private final double maxSpeed;
    /**
     * Domyślne przyspieszenie.
     */
    private final double defaultAcceleration;
    /**
     * Prędkość.
     */
    private double speed;
    /**
     * Przyspieszenie.
     */
    private double acceleration;
    /**
     * Tempomat.
     */
    private boolean limiter;
    /**
     * Prędkość ustawiona przez tempomat.
     */
    private double limiterSpeed;

    /**
     * Konstruktor prędkościomierza.
     * Domyślnie ustawia maksymalna prędkość na 230 km/h.
     */
    public Speedometer()
    {
        this.maxSpeed = 230.0;
        this.defaultAcceleration = -7.0;
        this.acceleration = defaultAcceleration;
    }

    /**
     * Getter prędkości maksymalnej.
     *
     * @return wartość prędkości maksymalnej
     */
    public double getMaxSpeed()
    {
        return maxSpeed;
    }

    /**
     * Getter domyślnego przyspieszenia.
     *
     * @return wartość domyślnego przyspieszenia
     */
    public double getDefaultAcceleration()
    {
        return defaultAcceleration;
    }

    /**
     * Getter prędkości.
     *
     * @return wartość prędkości
     */
    public double getSpeed()
    {
        return speed;
    }

    /**
     * Setter przyspieszenia.
     *
     * @param acceleration przyśpieszenie
     */
    public void setAcceleration(double acceleration)
    {
        this.acceleration = acceleration;
    }

    /**
     * Sprawdza, czy tempomat jest włączony.
     *
     * @return prawda, jeśli tempomat włączony
     */
    public boolean isLimiter()
    {
        return limiter;
    }

    /**
     * Setter tempomatu.
     *
     * @param limiter tempomat
     */
    public void setLimiter(boolean limiter)
    {
        this.limiter = limiter;
    }

    /**
     * Getter prędkości ustawionej przez tempomat.
     *
     * @return wartość prędkości ustawionej przez tempomat
     */
    public double getLimiterSpeed()
    {
        return limiterSpeed;
    }

    /**
     * Przełącza tempomat. Tempomat nie włączy się poniżej prędkości 20 km/h.
     */
    public void toggleLimiter()
    {
        if (speed >= 20)
        {
            limiter = !limiter;
            if (limiter)
            {
                limiterSpeed = speed;
            }
        }
    }

    /**
     * Aktualizuje prędkość względem przyspieszenia i tempomatu.
     */
    public void update()
    {
        speed += acceleration / DashboardLoop.DELTA_TIME;

        if (speed < 0.0)
        {
            speed = 0.0;
        }
        if (speed > maxSpeed)
        {
            speed = maxSpeed;
        }
        if (limiter && speed < limiterSpeed)
        {
            speed = limiterSpeed;
        }
    }

    /**
     * Zwraca informacje o predkościomierzu, czyli prędkość i stan tempomatu.
     *
     * @return string z informacją o prędkościomierzu
     */
    @Override
    public String toString()
    {
        return "-- PRĘDKOŚCIOMIERZ --" + System.lineSeparator() +
                "Prędkość: " + (int) speed + " km/h" + System.lineSeparator() +
                "Tempomat: " + (limiter ? "aktywny (" + (int) limiterSpeed + " km/h)" : "nieaktywny");
    }
}
