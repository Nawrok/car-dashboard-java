package pl.boleklolek.dao.serializers;

import com.thoughtworks.xstream.XStream;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

/**
 * Ta klasa obsługuje serializację obiektów do plików XML.
 */
public class XMLSerializer extends XStream implements Serializer
{
    /**
     * Konstruktor serializera XML.
     */
    public XMLSerializer()
    {
        super();
        this.allowTypeHierarchy(Collection.class);
    }

    /**
     * Serializuje do pliku XML.
     *
     * @param <T>      dowolna klasa T implementująca interfejs Serializable
     * @param t        obiekt klasy T
     * @param filepath ścieżka pliku
     * @throws IOException wyjątek wejścia/wyjścia
     */
    @Override
    public <T extends Serializable> void serialize(T t, String filepath) throws IOException
    {
        FileWriter fw = new FileWriter(filepath);
        String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator();
        fw.write(XML_HEADER + toXML(t));
        fw.close();
    }

    /**
     * Deserializuje z pliku XML.
     *
     * @param <T>      dowolna klasa T implementująca interfejs Serializable
     * @param tClass   instancja klasy T
     * @param filepath ścieżka pliku
     * @return obiekt klasy T
     * @throws IOException wyjątek wejścia/wyjścia
     */
    @Override
    public <T extends Serializable> T deserialize(Class<T> tClass, String filepath) throws IOException
    {
        FileReader fr = new FileReader(filepath);
        T t = tClass.cast(fromXML(fr));
        fr.close();
        return t;
    }
}
