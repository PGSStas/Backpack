package gui;

import controller.Controller;
import data.Ball;
import data.Cube;
import data.Shape;
import data.Tetrahedron;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class GUI extends JFrame {
    private final DefaultListModel<Shape> listModel = new DefaultListModel<>();
    private final JList<Shape> list = new JList<>(listModel);

    private final JButton addCube = new JButton();
    private final JButton addBall = new JButton();
    private final JButton addTetrahedron = new JButton();
    private final JButton deleteComponent = new JButton();

    private final JLabel backpackStat = new JLabel();

    private final Controller controller;
    private final String[] statuses = new String[]{"edge of cube", "ball radius", "edge of tetrahedron"};

    public GUI(Controller controller) {
        this.controller = controller;
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.add(list);

        setMinimumSize(new Dimension(800, 800));
        setTitle("Backpack v0.1");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        createLayout(getContentPane());
        setElementsInfo();
        setJMenuBar(createMenuBar());
    }

    private void createLayout(Container pane) {
        BorderLayout layout = new BorderLayout();
        pane.setLayout(layout);
        pane.add(list, BorderLayout.CENTER);
        pane.add(backpackStat, BorderLayout.SOUTH);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        panel.add(addCube);
        panel.add(addBall);
        panel.add(addTetrahedron);
        panel.add(deleteComponent);
        pane.add(panel, BorderLayout.EAST);
    }

    private final Pattern NUM_EX = Pattern.compile("-?\\d+(\\.\\d+)?");

    private boolean isNumeric(String strNum) {
        return NUM_EX.matcher(strNum).matches();
    }

    private void setElementsInfo() {
        addCube.setText("Add cube");
        addCube.addActionListener(actionEvent -> displayInputDialog(0));
        addBall.setText("Add ball");
        addBall.addActionListener(actionEvent -> displayInputDialog(1));
        addTetrahedron.setText("Add tetrahedron");
        addTetrahedron.addActionListener(actionEvent -> displayInputDialog(2));

        deleteComponent.setText("Remove selected shape from Backpack");
        deleteComponent.addActionListener(actionEvent -> {
            if (!list.isSelectionEmpty()) {
                int index = list.getSelectedIndex();
                listModel.remove(index);
                controller.remove(index);
                backpackStat.setText(controller.backpackStat());
            } else {
                JOptionPane.showMessageDialog(GUI.this,
                        "Select shape to delete first",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);

        backpackStat.setText(controller.backpackStat());
    }

    private void displayInputDialog(int type) {
        String message = JOptionPane.showInputDialog(
                GUI.this,
                "<html><h1>Enter " + statuses[type] + " (non-negative number)");
        if (message != null) {
            if (isNumeric(message) && Integer.parseInt(message) >= 0) {
                switch (type) {
                    case 0: {
                        addToBackpack(new Cube(Integer.parseInt(message)));
                        break;
                    }
                    case 1: {
                        addToBackpack(new Ball(Integer.parseInt(message)));
                        break;
                    }
                    case 2: {
                        addToBackpack(new Tetrahedron(Integer.parseInt(message)));
                        break;
                    }
                }
            } else {
                inputError();
            }
        }
    }

    private void addToBackpack(Shape shape) {
        int index = controller.addElementToBackpack(shape);
        listModel.add(index, shape);
        backpackStat.setText(controller.backpackStat());
    }

    private void inputError() {
        JOptionPane.showMessageDialog(GUI.this,
                "Seems, you enter something that is not a non-negative number :)",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu menu = new JMenu("File");
        JMenuItem load = new JMenuItem("Load");
        JMenuItem save = new JMenuItem("Save");
        menu.add(load);
        menu.add(save);
        load.addActionListener(actionEvent -> loadFile());
        save.addActionListener(actionEvent -> saveFile());
        return menu;
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load table to xml file");
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        fileChooser.addChoosableFileFilter(xmlFilter);
        fileChooser.setFileFilter(xmlFilter);

        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            ArrayList<Shape> newListModel;
            try {
                newListModel = controller.loadFromXML(fileChooser.getSelectedFile());
            } catch (IOException | SAXException | ParserConfigurationException e) {
                JOptionPane.showMessageDialog(GUI.this,
                        "Can`t read XML file (It can be corrupted or \"Backpack v0.1\" " +
                                "have insufficient access rights)",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            listModel.clear();
            for (Shape shape : newListModel) {
                listModel.addElement(shape);
            }

            backpackStat.setText(controller.backpackStat());
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save table to xml file");
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        fileChooser.addChoosableFileFilter(xmlFilter);
        fileChooser.setFileFilter(xmlFilter);

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String s = fileChooser.getSelectedFile().getAbsolutePath();
            if (s.length() <= 4 || !s.endsWith(".xml")) {
                s += ".xml";
            }
            File file = new File(s);

            try {
                if (!file.createNewFile()) {
                    PrintWriter writer = new PrintWriter(file);
                    writer.print("");
                    writer.close();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(GUI.this,
                        "Can`t create new XML file",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                controller.saveToXML(file);
            } catch (TransformerException | ParserConfigurationException | FileNotFoundException e) {
                JOptionPane.showMessageDialog(GUI.this,
                        "Can`t write data to this XML file",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
