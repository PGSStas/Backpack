package controller;

import containers.Backpack;
import data.Shape;
import gui.GUI;

public class Controller {
    private final int BACKPACK_CAPACITY = 100;
    private final Backpack<Shape> backpack = new Backpack<>(BACKPACK_CAPACITY);

    public Controller() {
        GUI app = new GUI(this);
        app.setVisible(true);
    }

    public String backpackStat() {
        return "Backpack Size: " + backpack.size() + ", Backpack Capacity: " + BACKPACK_CAPACITY;
    }

    public int addElementToBackpack(Shape new_element) {
        return backpack.add(new_element);
    }

    public void remove(int i) {
        backpack.remove(i);
    }
}
