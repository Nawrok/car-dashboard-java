package pl.boleklolek.dao;

import pl.boleklolek.model.computer.Memory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Ten interfejs DAO (Data Access Object) zapewnia metody zapisu/odczytu pamięci komputera pokładowego.
 */
public interface MemoryDAO
{
    /**
     * Zapisuje pamięć komputera pokładowego.
     *
     * @param memory pamięć, której stan chcemy zapisać
     * @throws SQLException           wyjątek SQL
     * @throws ClassNotFoundException wyjątek klasa nie znaleziona
     */
    void saveMemory(Memory memory) throws SQLException, ClassNotFoundException;

    /**
     * Wczytuje pamięć komputera pokładowego.
     *
     * @return wczytany obiekt pamięci
     * @throws SQLException           wyjątek SQL
     * @throws ClassNotFoundException wyjątek klasa nie znaleziona
     * @throws IOException            wyjątek wejścia/wyjścia
     */
    Memory loadMemory() throws SQLException, ClassNotFoundException, IOException;
}
