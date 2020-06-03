package pl.boleklolek.view.gui;

import eu.hansolo.steelseries.gauges.Radial;
import eu.hansolo.steelseries.tools.*;
import pl.boleklolek.dao.DAO;
import pl.boleklolek.dao.MemoryDAO;
import pl.boleklolek.model.Dashboard;
import pl.boleklolek.model.lights.enums.FogLightsLocation;
import pl.boleklolek.model.lights.enums.HeadLightsPosition;
import pl.boleklolek.model.lights.enums.SignalLightsDirection;
import pl.boleklolek.utils.SingletonExecutor;
import pl.boleklolek.view.gui.callbacks.DashboardKeyListener;
import pl.boleklolek.view.gui.lcd.DashboardLcd;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ta klasa odpowiada za główne okno aplikacji.
 */
public class DashboardFrame
{
    /**
     * Deska rozdzielcza.
     */
    private final Dashboard dashboard;
    /**
     * Wyświetlacz LCD.
     */
    private final DashboardLcd dashboardLcd;
    /**
     * DAO pamięci.
     */
    private final Map<DAO, MemoryDAO> memoryDAOs;
    /**
     * Ramka.
     */
    private final JFrame frame;
    /**
     * Komponent prędkościomierza.
     */
    private final Radial speedometer;
    /**
     * Indeks aktualnej podróży (A - 0 lub B - 1).
     */
    private int actualTripIndex;
    /**
     * Lewy kierunkowskaz.
     */
    private JLabel lightTurnLeft;
    /**
     * Prawy kierunkowskaz.
     */
    private JLabel lightTurnRight;
    /**
     * Światła długie.
     */
    private JLabel lightHighbeam;
    /**
     * Światła mijania.
     */
    private JLabel lightLowbeam;
    /**
     * Światła dzienne.
     */
    private JLabel lightDaily;
    /**
     * Światła wyłączone.
     */
    private JLabel lightOff;
    /**
     * Radio Button świateł długich.
     */
    private JRadioButton rdbtnHighbeam;
    /**
     * Radio Button świateł mijania.
     */
    private JRadioButton rdbtnLowbeam;
    /**
     * Radio Button świateł dziennych.
     */
    private JRadioButton rdbtnDaily;
    /**
     * Radio Button świateł wyłączonych.
     */
    private JRadioButton rdbtnOff;
    /**
     * Światła przeciwmgielne przednie.
     */
    private JLabel lightFogFront;
    /**
     * Światła przeciwmgielne tylne.
     */
    private JLabel lightFogBack;
    /**
     * Checkbox świateł przeciwmgielnych przednich.
     */
    private JCheckBox chckbxFogFront;
    /**
     * Checkbox świateł przeciwmgielnych tylnch.
     */
    private JCheckBox chckbxFogBack;
    /**
     * Toggle button tempomatu.
     */
    private JToggleButton tglbtnLimiter;
    /**
     * Pole tekstowe pokazujące nazwę pliku MP3.
     */
    private JTextField textSong;

    /**
     * Konstruktor ramki deski rodzielczej.
     * Wywołuje dwie pętle w osobnych wątkach z pomocą SingletonExecutora.
     * Jedna z nich odpowiada za odświeżanie okienka co około 60 klatek na sekundę, natomiast druga aktualizuje ekran LCD 5 razy na sekundę.
     *
     * @param dashboard  deska rozdzielcza
     * @param memoryDAOs DAO pamięci
     */
    public DashboardFrame(Dashboard dashboard, Map<DAO, MemoryDAO> memoryDAOs)
    {
        this.dashboard = dashboard;
        this.memoryDAOs = memoryDAOs;

        this.frame = createFrame();
        this.speedometer = createSpeedometer();
        this.dashboardLcd = new DashboardLcd(this.speedometer);
        this.actualTripIndex = getTripLcdIndex();
        this.frame.getContentPane().add(this.speedometer);
        this.createLights();
        this.createRadio();
        this.frame.setJMenuBar(createMenuBar());

        this.frame.setFocusable(true);
        this.frame.addKeyListener(new DashboardKeyListener(this.dashboard, this));

        SingletonExecutor.getInstance().scheduleAtFixedRate(this::update, 0, 1_000_000 / 60, TimeUnit.MICROSECONDS);
        SingletonExecutor.getInstance().scheduleAtFixedRate(this::updateLcd, 0, 1_000_000 / 5, TimeUnit.MICROSECONDS);
    }

    /**
     * Wyświetla główne okno aplikacji.
     */
    public void display()
    {
        frame.setVisible(true);
    }

    /**
     * Getter indeksu wybranej podróży.
     *
     * @return indeks wybranej podróży
     */
    public int getActualTripIndex()
    {
        return actualTripIndex;
    }

    /**
     * Pokazuje następną informację na ekranie LCD.
     */
    public void nextInfoLcd()
    {
        dashboardLcd.nextInfo();
    }

    /**
     * Pokazuje poprzednią informację na ekranie LCD.
     */
    public void prevInfoLcd()
    {
        dashboardLcd.prevInfo();
    }

    /**
     * Pokazuje okienko dialogowe z informacjami o podróży.
     *
     * @param tripIndex indeks podróży (A - 0 lub B - 1)
     */
    public void showTripInfo(int tripIndex)
    {
        JOptionPane.showMessageDialog(frame, dashboard.getComputer().getTrips()[tripIndex], "Podróż " + (char) ('A' + tripIndex), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Wybiera plik MP3 piosenki z dysku twardego komputera.
     *
     * @return plik MP3 piosenki
     */
    public File selectSongFile()
    {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setFileFilter(new FileNameExtensionFilter("Plik MP3 (*.mp3)", "mp3"));
        int result = jfc.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File file = jfc.getSelectedFile();
            if (file.getAbsolutePath().endsWith(".mp3"))
            {
                return file;
            }
            new ErrorFrame(frame, "Wymagany plik MP3!");
        }
        return null;
    }

    /**
     * Setter nazwy pliku piosenki.
     *
     * @param text nazwa pliku piosenki
     */
    public void setTextSong(String text)
    {
        textSong.setText(text);
    }

    /**
     * Tworzy ramkę z określonymi parametrami.
     *
     * @return obiekt ramki
     */
    private JFrame createFrame()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ignored)
        {
        }

        JFrame frame = new JFrame("Car Dashboard");
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.getContentPane().setLayout(null);
        frame.setIconImage(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/cardashboard.png")).getImage());
        return frame;
    }

    /**
     * Tworzy górny pasek menu.
     * Składa się ono z 4 submenu: Podróż, Pamięć, Ustawienia oraz Pomoc.
     * W menu Podróż mamy dwie podróże (A i B) oraz managera podróży.
     * W menu Pamięć możemy importować lub eksportować podróże z pliku XML lub z bazy danych.
     * W menu Ustawienia mamy ogólne ustawienia dotyczące wyboru podróży na ekranie LCD
     * i szybkości aktualizacji prędkości średniej (domyślnie 2s).
     * W menu Pomoc możemy sprawdzić klawiszologię i informacje o programie.
     *
     * @return obiekt menu
     */
    private JMenuBar createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu mnTrip = new JMenu("Podróż");
        mnTrip.add(createMenuTrip("A", 0));
        mnTrip.add(createMenuTrip("B", 1));

        JMenuItem mntmTripManager = new JMenuItem("Manager");
        mntmTripManager.addActionListener(e -> new TripManagerFrame(dashboard.getComputer()));
        mnTrip.add(mntmTripManager);

        menuBar.add(mnTrip);

        JMenu mnMemory = new JMenu("Pamięć");
        menuBar.add(mnMemory);

        JMenu mnImport = new JMenu("Import");
        mnMemory.add(mnImport);

        JMenuItem mntmXMLimport = new JMenuItem("XML");
        mntmXMLimport.addActionListener(e ->
        {
            int result = JOptionPane.showConfirmDialog(frame, "Cała pamięć komputera pokładowego wraz z aktualnymi podróżami zostanie UTRACONA!", "UWAGA", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.OK_OPTION)
            {
                JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
                jfc.setFileFilter(new FileNameExtensionFilter("Plik XML (*.xml)", "xml"));

                int res = jfc.showOpenDialog(frame);
                if (res == JFileChooser.APPROVE_OPTION)
                {
                    SingletonExecutor.getInstance().submit(() ->
                    {
                        Optional<String> prevSaveFile = dashboard.getMemoryFile();
                        if (prevSaveFile.isPresent())
                        {
                            try
                            {
                                dashboard.setMemoryFile(jfc.getSelectedFile().getCanonicalPath());
                                dashboard.loadMemory();
                                JOptionPane.showMessageDialog(frame, "Pomyślnie wczytano pamięć z pliku XML!", "SUKCES", JOptionPane.INFORMATION_MESSAGE);
                            }
                            catch (Exception exception)
                            {
                                new ErrorFrame(frame, "Błąd pliku XML!");
                            }
                            finally
                            {
                                dashboard.setMemoryFile(prevSaveFile.get());
                            }
                        }
                        else
                        {
                            new ErrorFrame(frame, "DAO nie jest ustawione na plik XML!");
                        }
                    });
                }
            }
        });
        mnImport.add(mntmXMLimport);

        JMenuItem mntmSQLimport = new JMenuItem("SQL");
        mntmSQLimport.addActionListener(e ->
        {
            int result = JOptionPane.showConfirmDialog(frame, "Cała pamięć komputera pokładowego wraz z aktualnymi podróżami zostanie UTRACONA!", "UWAGA", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.OK_OPTION)
            {
                SingletonExecutor.getInstance().submit(() ->
                {
                    dashboard.setMemoryDAO(memoryDAOs.get(DAO.DATABASE));
                    JDialog loading = getLoading();
                    try
                    {
                        dashboard.loadMemory();
                        JOptionPane.showMessageDialog(frame, "Pomyślnie wczytano pamięć z bazy danych!", "SUKCES", JOptionPane.INFORMATION_MESSAGE);
                    }
                    catch (Exception exception)
                    {
                        new ErrorFrame(frame, "Błąd połączenia z bazą danych!");
                    }
                    finally
                    {
                        loading.dispose();
                        dashboard.setMemoryDAO(memoryDAOs.get(DAO.XML));
                    }
                });
            }
        });
        mnImport.add(mntmSQLimport);

        JMenu mnExport = new JMenu("Export");
        mnMemory.add(mnExport);

        JMenuItem mntmXMLexport = new JMenuItem("XML");
        mntmXMLexport.addActionListener(e ->
        {
            JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
            jfc.setFileFilter(new FileNameExtensionFilter("Plik XML (*.xml)", "xml"));

            int result = jfc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION)
            {
                SingletonExecutor.getInstance().submit(() ->
                {
                    Optional<String> prevSaveFile = dashboard.getMemoryFile();
                    if (prevSaveFile.isPresent())
                    {
                        try
                        {
                            dashboard.setMemoryFile(jfc.getSelectedFile().getCanonicalPath() + "." + ((FileNameExtensionFilter) jfc.getFileFilter()).getExtensions()[0]);
                            dashboard.saveMemory();
                            JOptionPane.showMessageDialog(frame, "Pomyślnie zapisano pamięć do pliku XML!", "SUKCES", JOptionPane.INFORMATION_MESSAGE);
                        }
                        catch (Exception exception)
                        {
                            new ErrorFrame(frame, "Błąd pliku XML!");
                        }
                        finally
                        {
                            dashboard.setMemoryFile(prevSaveFile.get());
                        }
                    }
                    else
                    {
                        new ErrorFrame(frame, "DAO nie jest ustawione na plik XML!");
                    }
                });
            }
        });
        mnExport.add(mntmXMLexport);

        JMenuItem mntmSQLexport = new JMenuItem("SQL");
        mntmSQLexport.addActionListener(e ->
                SingletonExecutor.getInstance().submit(() ->
                {
                    dashboard.setMemoryDAO(memoryDAOs.get(DAO.DATABASE));
                    JDialog loading = getLoading();
                    try
                    {
                        dashboard.saveMemory();
                        JOptionPane.showMessageDialog(frame, "Pomyślnie zapisano pamięć do bazy danych!", "SUKCES", JOptionPane.INFORMATION_MESSAGE);
                    }
                    catch (Exception exception)
                    {
                        new ErrorFrame(frame, "Błąd połączenia z bazą danych!");
                    }
                    finally
                    {
                        loading.dispose();
                        dashboard.setMemoryDAO(memoryDAOs.get(DAO.XML));
                    }
                }));
        mnExport.add(mntmSQLexport);

        JMenu mnSettings = new JMenu("Ustawienia");
        menuBar.add(mnSettings);

        JMenu mnLcdTrip = new JMenu("LCD Podróż");
        mnSettings.add(mnLcdTrip);

        ButtonGroup tripLcdGroup = new ButtonGroup();

        JRadioButtonMenuItem rdbtnmntmLcdTripA = new JRadioButtonMenuItem("A");
        createIndexTripItem(mnLcdTrip, rdbtnmntmLcdTripA, 0);
        tripLcdGroup.add(rdbtnmntmLcdTripA);

        JRadioButtonMenuItem rdbtnmntmLcdTripB = new JRadioButtonMenuItem("B");
        createIndexTripItem(mnLcdTrip, rdbtnmntmLcdTripB, 1);
        tripLcdGroup.add(rdbtnmntmLcdTripB);

        JMenu mnAvgSpeed = new JMenu("Średnia prędkość");
        mnSettings.add(mnAvgSpeed);

        JMenuItem mntmAvgSpeedFreq = new JMenuItem("Szybkość aktualizacji - " + dashboard.getSettings().getFrequencyOfAvgSpeed() + " s");
        mntmAvgSpeedFreq.addActionListener(e ->
        {
            String value;
            Matcher matcher;
            do
            {
                value = JOptionPane.showInputDialog("Zmiana nastąpi przy ponownym uruchomieniu programu lub przy utworzeniu nowej podróży." + System.lineSeparator() + "Podaj dodatnią liczbę całkowitą:");
                if (value == null)
                {
                    return;
                }
                Pattern pattern = Pattern.compile("^[1-9]\\d*$");
                matcher = pattern.matcher(value);
            } while (!matcher.find());
            int frequency = Integer.parseInt(value);
            dashboard.setFreqOfAvgSpeed(frequency);
            mntmAvgSpeedFreq.setText("Szybkość aktualizacji - " + dashboard.getSettings().getFrequencyOfAvgSpeed() + " s");
        });
        mnAvgSpeed.add(mntmAvgSpeedFreq);

        JMenuItem mntmSaveSettings = new JMenuItem("Zapisz ustawienia");
        mntmSaveSettings.addActionListener(e -> dashboard.saveSettings());
        mnSettings.add(mntmSaveSettings);

        JMenu mnHelp = new JMenu("Pomoc");
        menuBar.add(mnHelp);

        JMenuItem mntmKeyboardControls = new JMenuItem("Klawiszologia");
        String keyboardControls = "<html>" +
                "<tr><td><b>Strzałka w górę</b></td><td>Przyspiesz</td><td><b>Klawisz ENTER</b></td><td>RADIO: Włącz</td></tr>" +
                "<tr><td><b>Strzałka w dół</b></td><td>Zwolnij</td><td><b>Klawisz SPACJA</b></td><td>RADIO: Pauza</td></tr>" +
                "<tr><td><b>Klawisz T</b></td><td>Tempomat</td><td><b>Klawisz S</b></td><td>RADIO: Wybierz</td></tr>" +
                "<tr><td><b>Strzałka w lewo</b></td><td>Lewy kierunkowskaz</td><td><b>Klawisz Numpad +</b></td><td>RADIO: Zwiększ głośność</td></tr>" +
                "<tr><td><b>Strzałka w lewo</b></td><td>Prawy kierunkowskaz</td><td><b>Klawisz Numpad -</b></td><td>RADIO: Zmniejsz głośność</td></tr>" +
                "<tr><td><b>Klawisz L</b></td><td>Światła drogowe</td><td><b>Klawisz M</b></td><td>RADIO: Wycisz</td></tr>" +
                "<tr><td><b>Klawisz K</b></td><td>Światła mijania</td><td><b>Klawisz N</b></td><td>Nowa podróż</td></tr>" +
                "<tr><td><b>Klawisz J</b></td><td>Światła pozycyjne</td><td><b>Klawisz R</b></td><td>Reset podróży</td></tr>" +
                "<tr><td><b>Klawisz H</b></td><td>Światła wyłączone</td><td><b>Klawisz P</b></td><td>Przebieg podróży</td></tr>" +
                "<tr><td><b>Klawisz [</b></td><td>Światła przeciwmgielne przednie</td><td><b>Klawisz PageUp</b></td><td>LCD: W górę</td></tr>" +
                "<tr><td><b>Klawisz ]</b></td><td>Światła przeciwmgielne tylne</td><td><b>Klawisz PageDown</b></td><td>LCD: W dół</td></tr></html>";
        JLabel keyboardLabel = new JLabel(keyboardControls);
        keyboardLabel.setFont(new Font("Lato", Font.PLAIN, 12));
        mntmKeyboardControls.addActionListener(e -> JOptionPane.showMessageDialog(frame, keyboardLabel, mntmKeyboardControls.getText(), JOptionPane.INFORMATION_MESSAGE));
        mnHelp.add(mntmKeyboardControls);

        JMenuItem mntmAbout = new JMenuItem("O programie");
        String about = "<html><b>Programowanie komponentowe</b><br>" +
                "<h1>Symulacja deski rozdzielczej<br>samochodu</h1>" +
                "<tr><td>Maciej Błażewicz</td><td>224264</td></tr>" +
                "<tr><td>Sebastian Nawrocki</td><td>224386</td></tr></html>";
        JLabel infoLabel = new JLabel(about);
        infoLabel.setFont(new Font("Lato", Font.PLAIN, 16));
        ImageIcon aboutIcon = new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/cardashboard.png"));
        mntmAbout.addActionListener(e -> JOptionPane.showMessageDialog(frame, infoLabel, mntmAbout.getText(), JOptionPane.INFORMATION_MESSAGE, aboutIcon));
        mnHelp.add(mntmAbout);

        return menuBar;
    }

    /**
     * Tworzy menu podróży.
     * Tworzenie nowej podróży jest rozumiane, jako usunięcie/zapisanie poprzedniej podróży, jeśli takowa wystąpiła
     * i stworzenie nowej podróży z wyzerowanymi statystykami.
     *
     * @param name      nazwa
     * @param tripIndex indeks podróży
     * @return obiekt menu podróży
     */
    private JMenu createMenuTrip(String name, int tripIndex)
    {
        JMenu mnTrip = new JMenu(name);

        JMenuItem mntmNewTrip = new JMenuItem("Nowa podróż");
        mntmNewTrip.addActionListener(e ->
        {
            int result = JOptionPane.showConfirmDialog(frame, "Czy chcesz zapisać podróż?", mntmNewTrip.getText(), JOptionPane.YES_NO_OPTION);
            switch (result)
            {
                case JOptionPane.YES_OPTION:
                {
                    dashboard.startNewTrip(tripIndex);
                    break;
                }
                case JOptionPane.NO_OPTION:
                {
                    dashboard.resetTrip(tripIndex);
                    break;
                }
                case JOptionPane.DEFAULT_OPTION:
                {
                    break;
                }
            }
        });
        mnTrip.add(mntmNewTrip);

        JMenuItem mntmInfoTrip = new JMenuItem("Przebieg podróży");
        mntmInfoTrip.addActionListener(e -> JOptionPane.showMessageDialog(frame, dashboard.getComputer().getTrips()[tripIndex], "Podróż " + name, JOptionPane.INFORMATION_MESSAGE));
        mnTrip.add(mntmInfoTrip);

        return mnTrip;
    }

    /**
     * Tworzy item indeksu podróży.
     *
     * @param optionMenu          menu
     * @param radioButtonMenuItem radio button menu item
     * @param tripIndex           indeks podróży
     */
    private void createIndexTripItem(JMenu optionMenu, JRadioButtonMenuItem radioButtonMenuItem, int tripIndex)
    {
        radioButtonMenuItem.setSelected(actualTripIndex == tripIndex);
        radioButtonMenuItem.addItemListener(e ->
        {
            actualTripIndex = tripIndex;
            dashboard.setActualTripIndex(actualTripIndex);
        });
        optionMenu.add(radioButtonMenuItem);
    }

    /**
     * Tworzy komponent prędkościomierza z odpowiednimi atrybutami.
     *
     * @return komponent prędkościomierza
     */
    private Radial createSpeedometer()
    {
        Radial speedometer = new Radial();
        speedometer.setBounds(160, 0, 480, 480);
        speedometer.setValueCoupled(false);
        speedometer.setTitle("km/h");
        speedometer.setUnitString("");
        speedometer.setDigitalFont(true);
        speedometer.setLcdUnitStringVisible(true);
        speedometer.setMaxNoOfMajorTicks(18);
        speedometer.setMaxNoOfMinorTicks(4);
        speedometer.setMinValue(0.0);
        speedometer.setMaxValue(240.0);
        speedometer.setLedVisible(false);
        speedometer.setPointerType(PointerType.TYPE11);
        speedometer.setLcdColor(LcdColor.BLACKRED_LCD);
        speedometer.setKnobStyle(KnobStyle.BLACK);
        speedometer.setFrameEffect(FrameEffect.EFFECT_INNER_FRAME);
        speedometer.setFrameDesign(FrameDesign.TILTED_BLACK);
        speedometer.setForegroundType(ForegroundType.FG_TYPE4);
        return speedometer;
    }

    /**
     * Tworzy graficzny interfejs świateł i tempomatu.
     * Wgrywa ich ikony, ustawia pozycje i określa, co mają robić po ich wciśnięciu.
     */
    private void createLights()
    {
        lightTurnLeft = new JLabel(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/lights/turn_left.png")));
        lightTurnLeft.setEnabled(false);
        lightTurnLeft.setBounds(60, 20, 100, 110);
        frame.getContentPane().add(lightTurnLeft);

        lightTurnRight = new JLabel(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/lights/turn_right.png")));
        lightTurnRight.setEnabled(false);
        lightTurnRight.setBounds(640, 20, 100, 110);
        frame.getContentPane().add(lightTurnRight);

        JPanel panelLeft = new JPanel();
        panelLeft.setBounds(20, 150, 105, 300);
        frame.getContentPane().add(panelLeft);
        panelLeft.setLayout(null);

        ButtonGroup lightsButtonGroup = new ButtonGroup();

        lightHighbeam = new JLabel(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/lights/highbeam.png")));
        lightHighbeam.setEnabled(dashboard.getLights().getHeadLights().getPosition() == HeadLightsPosition.HIGHBEAM);
        lightHighbeam.setBounds(0, 0, 75, 75);
        lightHighbeam.setToolTipText("Światła drogowe");
        panelLeft.add(lightHighbeam);

        lightLowbeam = new JLabel(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/lights/lowbeam.png")));
        lightLowbeam.setEnabled(dashboard.getLights().getHeadLights().getPosition() == HeadLightsPosition.LOWBEAM);
        lightLowbeam.setBounds(0, 85, 75, 75);
        lightLowbeam.setToolTipText("Światła mijania");
        panelLeft.add(lightLowbeam);

        lightDaily = new JLabel(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/lights/daily.png")));
        lightDaily.setEnabled(dashboard.getLights().getHeadLights().getPosition() == HeadLightsPosition.DAILY);
        lightDaily.setBounds(0, 165, 75, 75);
        lightDaily.setToolTipText("Światła dzienne/pozycyjne");
        panelLeft.add(lightDaily);

        lightOff = new JLabel(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/lights/off.png")));
        lightOff.setEnabled(dashboard.getLights().getHeadLights().getPosition() == HeadLightsPosition.OFF);
        lightOff.setBounds(12, 250, 50, 50);
        lightOff.setToolTipText("Światła wyłączone");
        panelLeft.add(lightOff);

        rdbtnHighbeam = new JRadioButton();
        rdbtnHighbeam.addItemListener(e -> dashboard.changeHeadlights(HeadLightsPosition.HIGHBEAM));
        rdbtnHighbeam.setBounds(80, 25, 25, 25);
        panelLeft.add(rdbtnHighbeam);
        lightsButtonGroup.add(rdbtnHighbeam);

        rdbtnLowbeam = new JRadioButton();
        rdbtnLowbeam.addItemListener(e -> dashboard.changeHeadlights(HeadLightsPosition.LOWBEAM));
        rdbtnLowbeam.setBounds(80, 110, 25, 25);
        panelLeft.add(rdbtnLowbeam);
        lightsButtonGroup.add(rdbtnLowbeam);

        rdbtnDaily = new JRadioButton();
        rdbtnDaily.addItemListener(e -> dashboard.changeHeadlights(HeadLightsPosition.DAILY));
        rdbtnDaily.setBounds(80, 190, 25, 25);
        panelLeft.add(rdbtnDaily);
        lightsButtonGroup.add(rdbtnDaily);

        rdbtnOff = new JRadioButton();
        rdbtnOff.addItemListener(e -> dashboard.changeHeadlights(HeadLightsPosition.OFF));
        rdbtnOff.setBounds(80, 265, 25, 25);
        panelLeft.add(rdbtnOff);
        lightsButtonGroup.add(rdbtnOff);

        JLabel lblOff = new JLabel("OFF");
        lblOff.setHorizontalAlignment(SwingConstants.CENTER);
        lblOff.setFont(new Font("Calibri", Font.PLAIN, 13));
        lblOff.setBounds(27, 275, 21, 17);
        panelLeft.add(lblOff);

        Enumeration<AbstractButton> buttons = lightsButtonGroup.getElements();
        while (buttons.hasMoreElements())
        {
            AbstractButton btn = buttons.nextElement();
            btn.setFocusable(false);
            btn.setHorizontalAlignment(SwingConstants.CENTER);
            btn.addActionListener(e -> updateLights());
        }

        JPanel panelRight = new JPanel();
        panelRight.setBounds(675, 150, 105, 240);
        frame.getContentPane().add(panelRight);
        panelRight.setLayout(null);

        lightFogFront = new JLabel(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/lights/fog_front.png")));
        lightFogFront.setEnabled(false);
        lightFogFront.setBounds(30, 0, 75, 75);
        lightFogFront.setToolTipText("Światła przeciwmgielne przednie");
        panelRight.add(lightFogFront);

        chckbxFogFront = new JCheckBox();
        chckbxFogFront.setHorizontalAlignment(SwingConstants.CENTER);
        chckbxFogFront.setFocusable(false);
        chckbxFogFront.addActionListener(e ->
        {
            dashboard.toggleFogLights(FogLightsLocation.FRONT);
            updateLights();
        });
        chckbxFogFront.setBounds(0, 25, 25, 25);
        panelRight.add(chckbxFogFront);

        lightFogBack = new JLabel(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/lights/fog_back.png")));
        lightFogBack.setEnabled(false);
        lightFogBack.setBounds(30, 80, 75, 75);
        lightFogBack.setToolTipText("Światła przeciwmgielne tylne");
        panelRight.add(lightFogBack);

        chckbxFogBack = new JCheckBox();
        chckbxFogBack.setHorizontalAlignment(SwingConstants.CENTER);
        chckbxFogBack.setFocusable(false);
        chckbxFogBack.addActionListener(e ->
        {
            dashboard.toggleFogLights(FogLightsLocation.BACK);
            updateLights();
        });
        chckbxFogBack.setBounds(0, 105, 25, 25);
        panelRight.add(chckbxFogBack);

        tglbtnLimiter = new JToggleButton();
        tglbtnLimiter.setHorizontalAlignment(SwingConstants.CENTER);
        tglbtnLimiter.addActionListener(e -> dashboard.toggleLimiter());
        tglbtnLimiter.setFocusable(false);
        tglbtnLimiter.setSelectedIcon(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/limiter_on.png")));
        tglbtnLimiter.setIcon(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/limiter_off.png")));
        tglbtnLimiter.setBounds(15, 165, 75, 75);
        panelRight.add(tglbtnLimiter);

        updateLights();
    }

    /**
     * Tworzy interfejs graficzny radia i wszystkich przycisków.
     */
    private void createRadio()
    {
        JPanel panelRadio = new JPanel();
        panelRadio.setBounds(20, 465, 760, 75);
        panelRadio.setLayout(null);
        frame.getContentPane().add(panelRadio);

        JLabel radioPlay = new JLabel(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/radio/play.png")));
        radioPlay.setBounds(0, 0, 75, 75);
        radioPlay.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                dashboard.radioPlay();
            }
        });
        panelRadio.add(radioPlay);

        JLabel radioPause = new JLabel(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/radio/pause.png")));
        radioPause.setBounds(85, 0, 75, 75);
        radioPause.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                dashboard.radioPause();
            }
        });
        panelRadio.add(radioPause);

        textSong = new JTextField();
        textSong.setBounds(255, 25, 250, 25);
        panelRadio.add(textSong);
        textSong.setText("MUZYKA");
        textSong.setEditable(false);
        textSong.setHorizontalAlignment(SwingConstants.CENTER);
        textSong.setFont(new Font("Calibri", Font.PLAIN, 13));

        JLabel radioSelect = new JLabel(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/radio/select.png")));
        radioSelect.setBounds(170, 0, 75, 75);
        radioSelect.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                File musicFile = selectSongFile();
                if (musicFile != null)
                {
                    dashboard.radioSelect(musicFile);
                    setTextSong(musicFile.getName());
                }
            }
        });
        panelRadio.add(radioSelect);

        JLabel radioVolUp = new JLabel(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/radio/vol_up.png")));
        radioVolUp.setBounds(515, 0, 75, 75);
        radioVolUp.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                dashboard.radioChangeVolume(10);
            }
        });
        panelRadio.add(radioVolUp);

        JLabel radioVolDown = new JLabel(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/radio/vol_down.png")));
        radioVolDown.setBounds(600, 0, 75, 75);
        radioVolDown.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                dashboard.radioChangeVolume(-10);
            }
        });
        panelRadio.add(radioVolDown);

        JLabel radioMute = new JLabel(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/radio/mute.png")));
        radioMute.setBounds(685, 0, 75, 75);
        radioMute.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                dashboard.radioToggleMute();
            }
        });
        panelRadio.add(radioMute);
    }

    /**
     * Tworzy okienko dialogowe z informacją o ładowaniu.
     *
     * @return okienko dialogowe o ładowaniu
     */
    private JDialog getLoading()
    {
        JDialog loading = new JDialog();
        loading.setSize(125, 75);
        loading.setUndecorated(true);
        loading.getContentPane().setLayout(new GridBagLayout());
        loading.setLocationRelativeTo(frame);
        JLabel load = new JLabel("Ładowanie...");
        load.setFont(new Font("Lato", Font.PLAIN, 13));
        loading.getContentPane().add(load);
        loading.setVisible(true);
        return loading;
    }

    /**
     * Getter indeksu aktualnej podróży.
     *
     * @return indeks aktualnej podróży
     */
    private int getTripLcdIndex()
    {
        int index = dashboard.getSettings().getActualTripIndex();
        if (index < 0 || index > dashboard.getComputer().getTrips().length - 1)
        {
            index = 0;
            dashboard.setActualTripIndex(index);
        }
        return index;
    }

    /**
     * Aktualizuje prędkościomierz w zależności od bieżącej prędkości.
     */
    private void updateSpeedometer()
    {
        double currentSpeed = dashboard.getSpeedometer().getSpeed();
        if (currentSpeed > 0.0 && currentSpeed < dashboard.getSpeedometer().getMaxSpeed())
        {
            if (!(dashboard.getSpeedometer().isLimiter() && currentSpeed == dashboard.getSpeedometer().getLimiterSpeed()))
            {
                speedometer.setValue(currentSpeed);
            }
        }
    }

    /**
     * Aktualizuje informację wyświetlaną na ekranie LCD.
     * Możliwość wyboru 6 opcji: prędkość, przebieg podróży, przebieg całkowity, średnia prędkość, prędkość maksymalna oraz czas podróży.
     */
    public void updateLcd()
    {
        switch (dashboardLcd.getLcdInformation())
        {
            case SPEED:
            {
                speedometer.setLcdValue(dashboard.getSpeedometer().getSpeed());
                break;
            }
            case TRIP_MILEAGE:
            {
                speedometer.setLcdValue(dashboard.getOdometer().getDailyMileages()[actualTripIndex].get());
                break;
            }
            case TOTAL_MILEAGE:
            {
                speedometer.setLcdValue(dashboard.getOdometer().getTotalMileage());
                break;
            }
            case AVG_SPEED:
            {
                speedometer.setLcdValue(dashboard.getComputer().getTrips()[actualTripIndex].getAverageSpeed());
                break;
            }
            case MAX_SPEED:
            {
                speedometer.setLcdValue(dashboard.getComputer().getTrips()[actualTripIndex].getMaxSpeed());
                break;
            }
            case TIME:
            {
                speedometer.setLcdValue(dashboard.getComputer().getTrips()[actualTripIndex].getElapsedTime());
                break;
            }
        }
    }

    /**
     * Aktualizuje światła w zależności od ich wyboru względem warstwy logiki.
     */
    public void updateLights()
    {
        rdbtnHighbeam.setSelected(dashboard.getLights().getHeadLights().getPosition() == HeadLightsPosition.HIGHBEAM);
        rdbtnLowbeam.setSelected(dashboard.getLights().getHeadLights().getPosition() == HeadLightsPosition.LOWBEAM);
        rdbtnDaily.setSelected(dashboard.getLights().getHeadLights().getPosition() == HeadLightsPosition.DAILY);
        rdbtnOff.setSelected(dashboard.getLights().getHeadLights().getPosition() == HeadLightsPosition.OFF);
        lightHighbeam.setEnabled(rdbtnHighbeam.isSelected());
        lightLowbeam.setEnabled(rdbtnLowbeam.isSelected());
        lightDaily.setEnabled(rdbtnDaily.isSelected());
        lightOff.setEnabled(rdbtnOff.isSelected());

        chckbxFogFront.setSelected(dashboard.getLights().getFogLights().getLights()[FogLightsLocation.FRONT.ordinal()]);
        chckbxFogBack.setSelected(dashboard.getLights().getFogLights().getLights()[FogLightsLocation.BACK.ordinal()]);
        lightFogFront.setEnabled(chckbxFogFront.isSelected());
        lightFogBack.setEnabled(chckbxFogBack.isSelected());
    }

    /**
     * Aktualizuje prędkościomierz, tempomat i światła kierunkowe względem warstwy logiki.
     */
    private void update()
    {
        updateSpeedometer();
        lightTurnLeft.setEnabled(dashboard.getLights().getSignalLights().getLights()[SignalLightsDirection.LEFT.ordinal()]);
        lightTurnRight.setEnabled(dashboard.getLights().getSignalLights().getLights()[SignalLightsDirection.RIGHT.ordinal()]);
        tglbtnLimiter.setSelected(dashboard.getSpeedometer().isLimiter());
    }
}
