package pl.boleklolek.dao.serializers;

import java.io.*;
import java.util.Base64;

/**
 * Ta klasa obsługuje serializację obiektów do plików wraz z ich kodowaniem Base64.
 */
public class ObjectSerializer implements Serializer
{
    /**
     * Serializuje do pliku.
     *
     * @param <T>      dowolna klasa T implementująca interfejs Serializable
     * @param t        obiekt klasy T
     * @param filepath ścieżka pliku
     * @throws IOException wyjątek wejścia/wyjścia
     */
    @Override
    public <T extends Serializable> void serialize(T t, String filepath) throws IOException
    {
        try (FileOutputStream fos = new FileOutputStream(filepath);
             ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(toString(t));
        }
    }

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
    @Override
    public <T extends Serializable> T deserialize(Class<T> tClass, String filepath) throws IOException, ClassNotFoundException
    {
        try (FileInputStream fis = new FileInputStream(filepath);
             ObjectInputStream ois = new ObjectInputStream(fis))
        {
            return fromString(tClass, (String) ois.readObject());
        }
    }

    /**
     * Serializuje obiekt do stringa wraz z kodowaniem Base64.
     *
     * @param <T> dowolna klasa T implementująca interfejs Serializable
     * @param t   obiekt klasy T
     * @return zserializowany string
     * @throws IOException wyjątek wejścia/wyjścia
     */
    private <T extends Serializable> String toString(T t) throws IOException
    {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos))
        {
            oos.writeObject(t);
            return new String(Base64.getEncoder().encode(bos.toByteArray()));
        }
    }

    /**
     * Deserializuje string wraz z dekodowaniem Base64.
     *
     * @param <T>    klasa typu T
     * @param tClass obiekt typu t
     * @param string string, z którego odczytujemy
     * @return obiekt klasy t
     * @throws IOException            wyjątek wejścia/wyjścia
     * @throws ClassNotFoundException wyjątek klasa nie znaleziona
     */
    private <T extends Serializable> T fromString(Class<T> tClass, String string) throws IOException, ClassNotFoundException
    {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(string.getBytes()));
             ObjectInputStream ois = new ObjectInputStream(bis))
        {
            return tClass.cast(ois.readObject());
        }
    }
}
