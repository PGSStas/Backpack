package gui;

import controller.Controller;
import data.Ball;
import data.Cube;
import data.Shape;
import data.Tetrahedron;

import javax.swing.*;
import java.awt.*;
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
        if (message != null && isNumeric(message) && Integer.parseInt(message) >= 0) {
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
}
