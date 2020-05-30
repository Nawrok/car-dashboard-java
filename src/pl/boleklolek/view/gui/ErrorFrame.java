package pl.boleklolek.view.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Ta klasa obsługuje okienko błędu.
 */
public class ErrorFrame
{
    /**
     * Konstruktor ramki błędu.
     * Wyświetla wiadomość błędu.
     *
     * @param parent  rodzic
     * @param message wiadomość
     */
    public ErrorFrame(Component parent, String message)
    {
        JOptionPane.showMessageDialog(parent, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
