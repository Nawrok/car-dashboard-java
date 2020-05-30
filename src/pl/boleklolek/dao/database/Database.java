package pl.boleklolek.dao.database;

import pl.boleklolek.dao.serializers.XMLSerializer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Ta klasa obsługuje połączenie z bazą danych.
 */
class Database
{
    /**
     * Ścieżka do pliku ustawień bazy danych w formacie XML.
     */
    private final String saveFile;
    /**
     * URL połączenia.
     */
    private final String URLconnection;
    /**
     * Ustawienia połączenia z bazą danych.
     */
    private DBSettings dbSettings;
    /**
     * Pole połączenia z bazą danych.
     */
    private Connection connection;

    /**
     * Konstruktor bazy danych.
     * Ustawia domyślny plik zapisu ustawień db_settings.xml w folderze aplikacji.
     * Tworzy URL connection na podstawie wartości podanych w ustawieniach.
     */
    public Database()
    {
        this.saveFile = "db_settings.xml";
        this.dbSettings = new DBSettings();
        this.URLconnection = "jdbc:sqlserver://" + dbSettings.getHost() + ":" + dbSettings.getPort() + ";" +
                "databaseName=" + dbSettings.getDatabaseName();
    }

    /**
     * Getter połączenia.
     *
     * @return obiekt połączenia
     */
    public Connection getConnection()
    {
        return connection;
    }

    /**
     * Getter nazwy bazy danych.
     *
     * @return string z nazwą bazy danych
     */
    public String getDatabaseName()
    {
        return dbSettings.getDatabaseName();
    }

    /**
     * Getter nazwy tabeli.
     *
     * @return string z nazwą tabeli
     */
    public String getTableName()
    {
        return dbSettings.getTableName();
    }

    /**
     * Setter ustawień bazy danych.
     *
     * @param dbSettings obiekt ustawień bazy danych
     */
    public void setDbSettings(DBSettings dbSettings)
    {
        this.dbSettings = dbSettings;
    }

    /**
     * Ustanawia połączenie między aplikacją a serwerem bazy danych, autoryzując użytkownika.
     *
     * @throws ClassNotFoundException wyjątek klasa nie znaleziona
     * @throws SQLException           wyjątek SQL
     */
    public void connect() throws ClassNotFoundException, SQLException
    {
        Class.forName(dbSettings.getDriver());
        connection = DriverManager.getConnection(URLconnection, dbSettings.getUser(), dbSettings.getPassword());
    }

    /**
     * Kończy połączenie między aplikacją a bazą danych.
     *
     * @throws SQLException wyjątek SQL
     */
    public void disconnect() throws SQLException
    {
        if (connection != null)
        {
            connection.close();
        }
    }

    /**
     * Wczytuje ustawienia bazy danych z pliku XML.
     *
     * @return obiekt ustawień bazy danych
     * @throws IOException wyjątek wejścia/wyjścia
     */
    public DBSettings loadSettings() throws IOException
    {
        XMLSerializer xmlSerializer = new XMLSerializer();
        xmlSerializer.alias("dbsettings", DBSettings.class);
        return xmlSerializer.deserialize(DBSettings.class, saveFile);
    }

    /**
     * Zapisuje ustawienia bazy danych do pliku XML.
     */
    public void saveSettings()
    {
        XMLSerializer xmlSerializer = new XMLSerializer();
        xmlSerializer.alias("dbsettings", DBSettings.class);
        try
        {
            xmlSerializer.serialize(dbSettings, saveFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
