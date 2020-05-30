package pl.boleklolek.dao.database;

import java.io.Serializable;

/**
 * Ta klasa przechowuje ustawienia połączenia z bazą danych.
 */
class DBSettings implements Serializable
{
    /**
     * Stałe pole zawierające identyfikator wersji.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Sterownik serwera SQL.
     */
    private final String driver;
    /**
     * Host.
     */
    private final String host;
    /**
     * Numer portu.
     */
    private final String port;
    /**
     * Nazwa użytkownika.
     */
    private final String user;
    /**
     * Hasło.
     */
    private final String password;
    /**
     * Nazwa bazy danych.
     */
    private final String databaseName;
    /**
     * Nazwa tabeli.
     */
    private final String tableName;

    /**
     * Konstruktor ustawień bazy danych.
     * Ustawia domyślne wartości połączenia z bazą danych.
     * Sterownik: com.microsoft.sqlserver.jdbc.SQLServerDriver
     * Host: localhost
     * Numer portu: 1433
     * Nazwa użytkownika: admin
     * Hasło: password
     * Nazwa bazy danych: CarDashboard
     * Nazwa tabeli (w schemacie dbo): Memory
     */
    public DBSettings()
    {
        this.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        this.host = "localhost";
        this.port = "1433";
        this.user = "admin";
        this.password = "password";
        this.databaseName = "CarDashboard";
        this.tableName = "Memory";
    }

    /**
     * Getter sterownika.
     *
     * @return string ze sterownikiem
     */
    public String getDriver()
    {
        return driver;
    }

    /**
     * Getter hosta.
     *
     * @return string z hostem
     */
    public String getHost()
    {
        return host;
    }

    /**
     * Getter portu.
     *
     * @return string z numerem portu
     */
    public String getPort()
    {
        return port;
    }

    /**
     * Getter nazwy użytkownika.
     *
     * @return string z nazwą użytkownika
     */
    public String getUser()
    {
        return user;
    }

    /**
     * Getter hasla.
     *
     * @return string z hasłem
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Getter nazwy bazy danych.
     *
     * @return string z nazwą bazy danych
     */
    public String getDatabaseName()
    {
        return databaseName;
    }

    /**
     * Getter nazwy tabeli.
     *
     * @return string z nazwą tabeli
     */
    public String getTableName()
    {
        return tableName;
    }
}
