package pl.boleklolek.model.odometer;

/**
 * Ta klasa wyjątku jest powiązana z przebiegiem.
 */
public class MileageException extends Exception
{
    /**
     * Stałe pole zawierające identyfikator wersji.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Tworzy nowy wyjątek przebiegu.
     *
     * @param message wiadomość
     */
    public MileageException(String message)
    {
        super(message);
    }
}
