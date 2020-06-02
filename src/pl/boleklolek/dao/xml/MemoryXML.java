package pl.boleklolek.dao.xml;

import pl.boleklolek.dao.MemoryDAO;
import pl.boleklolek.dao.serializers.Serializer;
import pl.boleklolek.dao.serializers.XMLSerializer;
import pl.boleklolek.model.computer.Memory;
import pl.boleklolek.model.computer.trip.Trip;

import java.io.IOException;

/**
 * Ta klasa obsługuje odczyt/zapis pamięci komputera pokładowego w pliku XML.
 */
public class MemoryXML implements MemoryDAO
{
    /**
     * Serializer.
     */
    private final Serializer serializer;
    /**
     * Plik XML pamięci komputera pokładowego.
     */
    private String memoryFile;

    /**
     * Konstruktor MemoryXML.
     * Tworzy domyślny plik pamięci komputera pokładowego w formacie XML w folderze aplikacji.
     * Plik pamięci komputera pokładowego: dashboard_memory.xml
     */
    public MemoryXML()
    {
        this.serializer = new XMLSerializer();
        this.memoryFile = "dashboard_memory.xml";
        ((XMLSerializer) this.serializer).alias("memory", Memory.class);
        ((XMLSerializer) this.serializer).alias("trip", Trip.class);
    }

    /**
     * Getter pliku pamięci.
     *
     * @return string z nazwą i rozszerzeniem pliku pamięci
     */
    public String getMemoryFile()
    {
        return memoryFile;
    }

    /**
     * Setter pliku pamięci.
     *
     * @param memoryFile string z nazwą pliku pamięci
     */
    public void setMemoryFile(String memoryFile)
    {
        this.memoryFile = memoryFile;
    }

    /**
     * Zapisuje pamięć komputera pokładowego.
     *
     * @param memory pamięć, której stan chcemy zapisać
     */
    @Override
    public void saveMemory(Memory memory)
    {
        try
        {
            serializer.serialize(memory, memoryFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Wczytuje pamięć komputera pokładowego.
     *
     * @return wczytany obiekt pamięci
     * @throws IOException wyjątek wejścia/wyjścia
     */
    @Override
    public Memory loadMemory() throws IOException, ClassNotFoundException
    {
        return serializer.deserialize(Memory.class, memoryFile);
    }
}
