package pl.boleklolek.dao.database;

import pl.boleklolek.dao.MemoryDAO;
import pl.boleklolek.model.computer.Memory;
import pl.boleklolek.model.computer.trip.Trip;
import pl.boleklolek.model.odometer.Mileage;
import pl.boleklolek.model.odometer.MileageException;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * Ta klasa obsługuje odczyt/zapis pamięci komputera pokładowego w bazie danych.
 */
public class MemoryDB implements MemoryDAO
{
    /**
     * Baza danych.
     */
    private final Database database;

    /**
     * Konstruktor MemoryDB.
     * Wczytuje ustawienia bazy danych.
     * Jeżeli wystąpi błąd wczytywania, zostaną wygenerowane domyślne wartości.
     */
    public MemoryDB()
    {
        this.database = new Database();
        this.loadSettings();
        this.database.saveSettings();
    }

    /**
     * Zapisuje pamięć podróży do bazy danych poprzez zapytania SQL z uzupełnionymi wartościami podróży.
     * Jeżeli podróż występuje w bazie, to aktualizuje jej dane zapytaniem UPDATE.
     * Jeżeli podróż nie występuje w bazie, to dodaje ją zapytaniem INSERT.
     *
     * @param memory pamięć, której stan chcemy zapisać
     * @throws SQLException           wyjątek SQL
     * @throws ClassNotFoundException wyjątek klasa nie znaleziona
     */
    @Override
    public void saveMemory(Memory memory) throws SQLException, ClassNotFoundException
    {
        loadSettings();

        Memory dbMemory;
        try
        {
            dbMemory = loadMemory();
        }
        catch (SQLException | ClassNotFoundException throwables)
        {
            dbMemory = new Memory();
        }

        database.connect();
        String data = database.getDatabaseName() + ".dbo." + database.getTableName();

        String insertQuery = "INSERT INTO " + data + " (UUID, averageSpeed, maxSpeed, mileage, elapsedTime) VALUES (?, ?, ?, ?, ?)";
        String updateQuery = "UPDATE " + data + " SET averageSpeed = ?, maxSpeed = ?, mileage = ?, elapsedTime = ? WHERE UUID = ?";

        PreparedStatement insert = database.getConnection().prepareStatement(insertQuery);
        PreparedStatement update = database.getConnection().prepareStatement(updateQuery);
        for (Trip trip : memory.getAll())
        {
            if (dbMemory.getAll().contains(trip))
            {
                update.setDouble(1, trip.getAverageSpeed());
                update.setDouble(2, trip.getMaxSpeed());
                update.setDouble(3, trip.getMileage().get());
                update.setLong(4, trip.getElapsedTime());
                update.setString(5, trip.getUuid().toString());
                update.execute();
            }
            else
            {
                insert.setString(1, trip.getUuid().toString());
                insert.setDouble(2, trip.getAverageSpeed());
                insert.setDouble(3, trip.getMaxSpeed());
                insert.setDouble(4, trip.getMileage().get());
                insert.setLong(5, trip.getElapsedTime());
                insert.execute();
            }
        }
        insert.close();
        update.close();
        database.disconnect();
    }

    /**
     * Wczytuje pamięć z bazy danych poprzez zapytanie SQL typu SELECT.
     *
     * @return wczytany obiekt pamięci
     * @throws SQLException           wyjątek SQL
     * @throws ClassNotFoundException wyjątek klasa nie znaleziona
     */
    @Override
    public Memory loadMemory() throws SQLException, ClassNotFoundException
    {
        loadSettings();

        database.connect();
        String data = database.getDatabaseName() + ".dbo." + database.getTableName();

        Statement statement = database.getConnection().createStatement();
        String query = "SELECT * FROM " + data;

        Memory memory = new Memory();

        ResultSet rs = statement.executeQuery(query);
        while (rs.next())
        {
            UUID uuid = UUID.fromString(rs.getString("UUID"));
            double avgSpeed = rs.getDouble("averageSpeed");
            double maxSpeed = rs.getDouble("maxSpeed");
            Mileage mileage;
            try
            {
                mileage = new Mileage(rs.getDouble("mileage"));
            }
            catch (MileageException e)
            {
                mileage = new Mileage();
            }
            long time = rs.getLong("elapsedTime");

            memory.add(new Trip(uuid, avgSpeed, maxSpeed, mileage, time));
        }
        statement.close();
        database.disconnect();

        return memory;
    }

    /**
     * Wczytuje ustawienia połączenia z bazą danych.
     */
    private void loadSettings()
    {
        try
        {
            database.setDbSettings(database.loadSettings());
        }
        catch (IOException e)
        {
            database.setDbSettings(new DBSettings());
        }
    }
}
