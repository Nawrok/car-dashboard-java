package pl.boleklolek.view.gui.callbacks;

import pl.boleklolek.model.Dashboard;
import pl.boleklolek.model.lights.enums.FogLightsLocation;
import pl.boleklolek.model.lights.enums.HeadLightsPosition;
import pl.boleklolek.model.lights.enums.SignalLightsDirection;
import pl.boleklolek.view.gui.DashboardFrame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Ta klasa obsługuje wykonanie odpowiedniej czynności po wciśnięciu klawisza.
 */
public class DashboardKeyListener extends KeyAdapter
{
    /**
     * Deska rozdzielcza.
     */
    private final Dashboard dashboard;
    /**
     * Okienko deski rozdzielczej.
     */
    private final DashboardFrame dashboardFrame;

    /**
     * Konstruktor DashboardKeyListener.
     *
     * @param dashboard      deska rozdzielcza
     * @param dashboardFrame okienko deski rozdzielczej
     */
    public DashboardKeyListener(Dashboard dashboard, DashboardFrame dashboardFrame)
    {
        this.dashboard = dashboard;
        this.dashboardFrame = dashboardFrame;
    }

    /**
     * Ustala czynności po wciśnięciu danego klawisza.
     * Wykonywane czynności są zgodne z klawiszologią zawartą w programie.
     *
     * @param e zdarzenie klawisza
     */
    @Override
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP)
        {
            dashboard.accelerateCar();
        }
        if (key == KeyEvent.VK_DOWN)
        {
            dashboard.brakeCar();
        }
        if (key == KeyEvent.VK_LEFT)
        {
            dashboard.turnOnSignalLight(SignalLightsDirection.LEFT);
        }
        if (key == KeyEvent.VK_RIGHT)
        {
            dashboard.turnOnSignalLight(SignalLightsDirection.RIGHT);
        }
        if (key == KeyEvent.VK_T)
        {
            dashboard.toggleLimiter();
        }
    }

    /**
     * Ustala czynności po puszczeniu danego klawisza.
     * Wykonywane czynności są zgodne z klawiszologią zawartą w programie.
     *
     * @param e zdarzenie klawisza
     */
    @Override
    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)
        {
            dashboard.idleCar();
        }
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT)
        {
            dashboard.turnOffSignalLight();
        }
        lightsControls(e);
        lcdControls(e);
        radioControls(e);
        tripControls(e);
    }

    /**
     * Obsługuje światła po naciśnięciu klawisza.
     *
     * @param e zdarzenie klawisza
     */
    private void lightsControls(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_L)
        {
            dashboard.changeHeadlights(HeadLightsPosition.HIGHBEAM);
        }
        if (key == KeyEvent.VK_K)
        {
            dashboard.changeHeadlights(HeadLightsPosition.LOWBEAM);
        }
        if (key == KeyEvent.VK_J)
        {
            dashboard.changeHeadlights(HeadLightsPosition.DAILY);
        }
        if (key == KeyEvent.VK_H)
        {
            dashboard.changeHeadlights(HeadLightsPosition.OFF);
        }
        if (key == KeyEvent.VK_OPEN_BRACKET)
        {
            dashboard.toggleFogLights(FogLightsLocation.FRONT);
        }
        if (key == KeyEvent.VK_CLOSE_BRACKET)
        {
            dashboard.toggleFogLights(FogLightsLocation.BACK);
        }
        switch (key)
        {
            case KeyEvent.VK_L:
            case KeyEvent.VK_K:
            case KeyEvent.VK_J:
            case KeyEvent.VK_H:
            case KeyEvent.VK_OPEN_BRACKET:
            case KeyEvent.VK_CLOSE_BRACKET:
            {
                dashboardFrame.updateLights();
                break;
            }
        }
    }

    /**
     * Obsługuje ekran LCD po naciśnięciu klawisza.
     *
     * @param e zdarzenie klawisza
     */
    private void lcdControls(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_PAGE_UP)
        {
            dashboardFrame.nextInfoLcd();
        }
        if (key == KeyEvent.VK_PAGE_DOWN)
        {
            dashboardFrame.prevInfoLcd();
        }
        switch (key)
        {
            case KeyEvent.VK_PAGE_UP:
            case KeyEvent.VK_PAGE_DOWN:
            {
                dashboardFrame.updateLcd();
                break;
            }
        }
    }

    /**
     * Obsługuje radio po naciśnięciu klawisza.
     *
     * @param e zdarzenie klawisza
     */
    private void radioControls(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER)
        {
            dashboard.radioPlay();
        }
        if (key == KeyEvent.VK_SPACE)
        {
            dashboard.radioPause();
        }
        if (key == KeyEvent.VK_S)
        {
            File musicFile = dashboardFrame.selectSongFile();
            if (musicFile != null)
            {
                dashboard.radioSelect(musicFile);
                dashboardFrame.setTextSong(musicFile.getName());
            }
        }
        if (key == KeyEvent.VK_ADD)
        {
            dashboard.radioChangeVolume(5);
        }
        if (key == KeyEvent.VK_SUBTRACT)
        {
            dashboard.radioChangeVolume(-5);
        }
        if (key == KeyEvent.VK_M)
        {
            dashboard.radioToggleMute();
        }
    }

    /**
     * Obsługuje zdarzenia związane z aktualną podróżą po naciśnięciu klawisza.
     *
     * @param e zdarzenie klawisza
     */
    private void tripControls(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_N)
        {
            dashboard.startNewTrip(dashboardFrame.getActualTripIndex());
        }
        if (key == KeyEvent.VK_R)
        {
            dashboard.resetTrip(dashboardFrame.getActualTripIndex());
        }
        if (key == KeyEvent.VK_P)
        {
            dashboardFrame.showTripInfo(dashboardFrame.getActualTripIndex());
        }
    }
}
