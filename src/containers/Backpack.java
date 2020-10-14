package containers;

import data.Shape;
import exception.BackpackOverflow;

import java.util.ArrayList;

public class Backpack<T extends Shape> {
    private ArrayList<T> elements;
    private final int n;

    public Backpack(int n) {
        this.n = n;
        try {
            elements = new ArrayList<>();
        } catch (NegativeArraySizeException e) {
            System.out.println("NegativeArraySize");
        }
    }

    public int add(T element) {
        if (elements.size() == n) {
            throw new BackpackOverflow();
        } else {
            elements.add(element);
            sort();
            return elements.indexOf(element);
        }
    }

    public void remove(int i) {
        if (elements.size() != 0) {
            elements.remove(i);
        }
    }

    public int size() {
        return elements.size();
    }

    public void sort() {
        elements.sort((a, b)-> (int)(b.getVolume() - a.getVolume()));
    }
}
