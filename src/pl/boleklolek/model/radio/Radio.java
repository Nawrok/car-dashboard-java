package pl.boleklolek.model.radio;

import jaco.mp3.player.MP3Player;

import java.io.File;

/**
 * Ta klasa opisuje radio MP3.
 */
public class Radio
{
    /**
     * Odtwarzacz MP3.
     */
    private final MP3Player player;
    /**
     * Wyciszenie muzyki.
     */
    private boolean mute;
    /**
     * Poprzednia głośność.
     */
    private int prevVolume;

    /**
     * Konstruktor radia z domyślnie ustawioną głośnością = 50%.
     */
    public Radio()
    {
        this.player = new MP3Player();
        this.player.setVolume(50);
    }

    /**
     * Odtworza radio.
     */
    public void play()
    {
        if (!player.isPlaying())
        {
            player.play();
        }
    }

    /**
     * Pauzuje radio.
     */
    public void pause()
    {
        if (!player.isPaused())
        {
            player.pause();
        }
    }

    /**
     * Wybiera plik MP3 i rozpoczyna jego odtwarzanie.
     *
     * @param file plik mp3
     */
    public void select(File file)
    {
        if (!player.isStopped())
        {
            player.stop();
        }
        player.add(file);
        player.skipForward();
        player.play();
    }

    /**
     * Zmienia poziom głośności. Jeżeli radio jest wyciszone, to zostanie przywrócona jego głośność.
     *
     * @param deltaVolume delta głośności
     */
    public void changeVolume(int deltaVolume)
    {
        if (mute)
        {
            toggleMute();
        }
        int desiredVolume = player.getVolume() + deltaVolume;
        if (desiredVolume >= 0 && desiredVolume <= 100)
        {
            player.setVolume(desiredVolume);
        }
    }

    /**
     * Przełącza wyciszenie.
     */
    public void toggleMute()
    {
        if (!mute)
        {
            prevVolume = player.getVolume();
            player.setVolume(0);
            mute = true;
        }
        else
        {
            player.setVolume(prevVolume);
            mute = false;
        }
    }

    /**
     * Zwraca informacje z aktualnym stanem radia (głośność radia i stan wyciszenia).
     *
     * @return string z informacjami o radiu
     */
    @Override
    public String toString()
    {
        return "-- RADIO --" + System.lineSeparator() +
                "Głośność: " + player.getVolume() + "%" + System.lineSeparator() +
                "Wyciszone: " + (mute ? "TAK" : "NIE");
    }
}
