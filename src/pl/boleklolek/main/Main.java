package pl.boleklolek.main;

import pl.boleklolek.dao.DAO;
import pl.boleklolek.dao.MemoryDAO;
import pl.boleklolek.dao.database.MemoryDB;
import pl.boleklolek.dao.xml.MemoryXML;
import pl.boleklolek.model.Dashboard;
import pl.boleklolek.utils.SingletonExecutor;
import pl.boleklolek.view.console.Menu;
import pl.boleklolek.view.gui.DashboardFrame;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Ta klasa jest klasą główną zawierającą metodę main.
 */
public class Main
{
    /**
     * Początek aplikacji.
     * W zależności od argumentów wiersza poleceń zostanie włączona dana instancja aplikacji.
     * 1. Okrojona wersja konsolowa (argument: console).
     * 2. Aplikacja z interfejsem graficznym stworzonym w bibliotece Swing (domyślnie bez argumentów).
     *
     * @param args argumenty wejścia
     */
    public static void main(String[] args)
    {
        Map<DAO, MemoryDAO> memoryDAOs = new HashMap<>();
        memoryDAOs.put(DAO.XML, new MemoryXML());
        memoryDAOs.put(DAO.DATABASE, new MemoryDB());

        Dashboard dashboard = new Dashboard(memoryDAOs.get(DAO.XML));
        dashboard.startComputer();

        if (args.length >= 1 && args[0].equals("console"))
        {
            SingletonExecutor.getInstance().submit(() -> new Menu(dashboard, memoryDAOs).display());
        }
        else
        {
            SwingUtilities.invokeLater(() -> new DashboardFrame(dashboard, memoryDAOs).display());
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            SingletonExecutor.getInstance().shutdown();
            dashboard.saveAll();
        }));
    }
}
