package pl.boleklolek.loop;

/**
 * Interfejs Updateable.
 * Umożliwia aktualizowanie danych w obiektach, które implementują ten interfejs.
 */
public interface Updateable
{
    /**
     * Okres jednego kroku w pętli aplikacji (mikrosekundy).
     */
    long TICK = 10_000L;
    /**
     * Czas pomiędzy kolejnymi aktualizacjami w mikrosekundach.
     */
    long DELTA_TIME = 1_000_000L / TICK;

    /**
     * Aktualizuje zmienne danego obiektu.
     */
    void update();
}
