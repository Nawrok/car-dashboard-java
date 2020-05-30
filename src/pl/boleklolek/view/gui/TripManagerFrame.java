package pl.boleklolek.view.gui;

import pl.boleklolek.model.computer.Computer;
import pl.boleklolek.model.computer.trip.TimeTripComparator;
import pl.boleklolek.model.computer.trip.Trip;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Ta klasa obsługuje okienko managera podróży.
 */
public class TripManagerFrame
{
    /**
     * Komputer pokładowy.
     */
    private final Computer computer;

    /**
     * Ramka.
     */
    private final JFrame frame;
    /**
     * Layout.
     */
    private final GridBagLayout layout;
    /**
     * Lista podróży.
     */
    private final JList<String> list;

    /**
     * Konstrukor okienka zarządzania podróżami.
     *
     * @param computer komputer pokładowy
     */
    public TripManagerFrame(Computer computer)
    {
        this.computer = computer;

        this.frame = createFrame();
        this.layout = new GridBagLayout();
        this.createLayout();

        this.list = new JList<>();
        this.createList();

        this.createButtons();

        this.frame.setVisible(true);
    }

    /**
     * Tworzy ramkę.
     *
     * @return obiekt ramki
     */
    private JFrame createFrame()
    {
        JFrame frame = new JFrame("Zarządzanie podróżami");
        frame.setMinimumSize(new Dimension(725, 425));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon(DashboardFrame.class.getResource("/pl/boleklolek/view/gui/resources/cardashboard.png")).getImage());
        return frame;
    }

    /**
     * Tworzy ustawienia layoutu.
     */
    private void createLayout()
    {
        layout.columnWidths = new int[]{10, 50, 140, 0};
        layout.rowHeights = new int[]{10, 100, 100, 100, 100, 100, 100, 100, 100, 100, 0};
        layout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        layout.rowWeights = new double[]{0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
        frame.getContentPane().setLayout(layout);
    }

    /**
     * Tworzy listę podróży.
     */
    private void createList()
    {
        list.setBorder(new LineBorder(new Color(0, 0, 0)));
        list.setFont(new Font("Tahoma", Font.PLAIN, 12));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GridBagConstraints gbc_list = new GridBagConstraints();
        gbc_list.fill = GridBagConstraints.BOTH;
        gbc_list.gridheight = 8;
        gbc_list.insets = new Insets(0, 0, 0, 5);
        gbc_list.gridx = 1;
        gbc_list.gridy = 1;
        frame.getContentPane().add(new JScrollPane(list), gbc_list);
        list.setModel(new DefaultListModel<>());
        refreshList();
    }

    /**
     * Tworzy przyciski służące do zarządzania podróżami.
     */
    private void createButtons()
    {
        Dimension btnDim = new Dimension(120, 25);

        JButton btnCheckTripA = new JButton("Sprawdź podróż A");
        btnCheckTripA.setMinimumSize(btnDim);
        btnCheckTripA.addActionListener(e ->
        {
            refreshList();
            Trip tripA = computer.getTrips()[0];
            list.setSelectedIndex(computer.getMemory().getAll().indexOf(tripA));
        });
        GridBagConstraints gbc_btnCheckTripA = new GridBagConstraints();
        gbc_btnCheckTripA.insets = new Insets(0, 0, 5, 0);
        gbc_btnCheckTripA.gridx = 2;
        gbc_btnCheckTripA.gridy = 1;
        frame.getContentPane().add(btnCheckTripA, gbc_btnCheckTripA);

        JButton btnSetTripA = new JButton("Ustaw podróż A");
        btnSetTripA.setMinimumSize(btnDim);
        btnSetTripA.addActionListener(e ->
        {
            int select = list.getSelectedIndex();
            if (select != -1)
            {
                if (computer.getTrips()[0].equals(computer.getMemory().get(select)))
                {
                    return;
                }
                else if (computer.getTrips()[1].equals(computer.getMemory().get(select)))
                {
                    computer.swapTrips();
                }
                else
                {
                    computer.loadTripFromMemory(0, select);
                }
            }
            refreshList();
        });
        GridBagConstraints gbc_btnSetTripA = new GridBagConstraints();
        gbc_btnSetTripA.insets = new Insets(0, 0, 5, 0);
        gbc_btnSetTripA.gridx = 2;
        gbc_btnSetTripA.gridy = 2;
        frame.getContentPane().add(btnSetTripA, gbc_btnSetTripA);

        JButton btnCheckTripB = new JButton("Sprawdź podróż B");
        btnCheckTripB.setMinimumSize(btnDim);
        btnCheckTripB.addActionListener(e ->
        {
            refreshList();
            Trip tripB = computer.getTrips()[1];
            list.setSelectedIndex(computer.getMemory().getAll().indexOf(tripB));
        });
        GridBagConstraints gbc_btnCheckTripB = new GridBagConstraints();
        gbc_btnCheckTripB.insets = new Insets(0, 0, 5, 0);
        gbc_btnCheckTripB.gridx = 2;
        gbc_btnCheckTripB.gridy = 3;
        frame.getContentPane().add(btnCheckTripB, gbc_btnCheckTripB);

        JButton btnSetTripB = new JButton("Ustaw podróż B");
        btnSetTripB.setMinimumSize(btnDim);
        btnSetTripB.addActionListener(e ->
        {
            int select = list.getSelectedIndex();
            if (select != -1)
            {
                if (computer.getTrips()[1].equals(computer.getMemory().get(select)))
                {
                    return;
                }
                else if (computer.getTrips()[0].equals(computer.getMemory().get(select)))
                {
                    computer.swapTrips();
                }
                else
                {
                    computer.loadTripFromMemory(1, select);
                }
            }
            refreshList();
        });
        GridBagConstraints gbc_btnSetTripB = new GridBagConstraints();
        gbc_btnSetTripB.insets = new Insets(0, 0, 5, 0);
        gbc_btnSetTripB.gridx = 2;
        gbc_btnSetTripB.gridy = 4;
        frame.getContentPane().add(btnSetTripB, gbc_btnSetTripB);

        JButton btnRemoveTrip = new JButton("Usuń podróż");
        btnRemoveTrip.setMinimumSize(btnDim);
        btnRemoveTrip.addActionListener(e ->
        {
            int select = list.getSelectedIndex();
            if (select != -1)
            {
                computer.removeTripFromMemory(select);
            }
            refreshList();
        });
        GridBagConstraints gbc_btnRemoveTrip = new GridBagConstraints();
        gbc_btnRemoveTrip.insets = new Insets(0, 0, 5, 0);
        gbc_btnRemoveTrip.gridx = 2;
        gbc_btnRemoveTrip.gridy = 5;
        frame.getContentPane().add(btnRemoveTrip, gbc_btnRemoveTrip);

        JButton btnSortMileage = new JButton("Sortuj (przebieg)");
        btnSortMileage.setMinimumSize(btnDim);
        btnSortMileage.addActionListener(e ->
        {
            computer.sortTripsInMemory(null);
            refreshList();
        });
        GridBagConstraints gbc_btnSortMileage = new GridBagConstraints();
        gbc_btnSortMileage.insets = new Insets(0, 0, 5, 0);
        gbc_btnSortMileage.gridx = 2;
        gbc_btnSortMileage.gridy = 6;
        frame.getContentPane().add(btnSortMileage, gbc_btnSortMileage);

        JButton btnSortTime = new JButton("Sortuj (czas)");
        btnSortTime.setMinimumSize(btnDim);
        btnSortTime.addActionListener(e ->
        {
            computer.sortTripsInMemory(new TimeTripComparator().reversed());
            refreshList();
        });
        GridBagConstraints gbc_btnSortTime = new GridBagConstraints();
        gbc_btnSortTime.insets = new Insets(0, 0, 5, 0);
        gbc_btnSortTime.gridx = 2;
        gbc_btnSortTime.gridy = 7;
        frame.getContentPane().add(btnSortTime, gbc_btnSortTime);

        JButton btnRefresh = new JButton("Odśwież");
        btnRefresh.setMinimumSize(btnDim);
        btnRefresh.addActionListener(e -> refreshList());
        GridBagConstraints gbc_btnRefresh = new GridBagConstraints();
        gbc_btnRefresh.insets = new Insets(0, 0, 5, 0);
        gbc_btnRefresh.gridx = 2;
        gbc_btnRefresh.gridy = 8;
        frame.getContentPane().add(btnRefresh, gbc_btnRefresh);

        JButton btnOk = new JButton("Ok");
        btnOk.setMinimumSize(btnDim);
        btnOk.addActionListener(e -> frame.dispose());
        GridBagConstraints gbc_btnOk = new GridBagConstraints();
        gbc_btnOk.gridx = 2;
        gbc_btnOk.gridy = 9;
        frame.getContentPane().add(btnOk, gbc_btnOk);
    }

    /**
     * Odświeża listę podróży.
     */
    private void refreshList()
    {
        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
        model.clear();
        for (Trip trip : computer.getMemory().getAll())
        {
            model.addElement(trip.toStringShort());
        }
    }
}
