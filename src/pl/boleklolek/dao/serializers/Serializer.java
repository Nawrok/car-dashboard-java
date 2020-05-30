package pl.boleklolek.dao.serializers;

import java.io.IOException;
import java.io.Serializable;

/**
 * Ten interfejs pozwala serializować dowolne klasy do pliku.
 */
public interface Serializer
{
    /**
     * Serializuje do pliku.
     *
     * @param <T>      dowolna klasa T implementująca interfejs Serializable
     * @param t        obiekt klasy T
     * @param filepath ścieżka pliku
     * @throws IOException wyjątek wejścia/wyjścia
     */
    <T extends Serializable> void serialize(T t, String filepath) throws IOException;

    /**
     * Deserializuje z pliku.
     *
     * @param <T>      dowolna klasa T implementująca interfejs Serializable
     * @param tClass   instancja klasy T
     * @param filepath ścieżka pliku
     * @return obiekt klasy T
     * @throws IOException            wyjątek wejścia/wyjścia
     * @throws ClassNotFoundException wyjątek klasa nie znaleziona
     */
    <T extends Serializable> T deserialize(Class<T> tClass, String filepath) throws IOException, ClassNotFoundException;
}
