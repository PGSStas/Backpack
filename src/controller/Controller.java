package controller;

import containers.Backpack;
import data.Ball;
import data.Cube;
import data.Shape;
import data.Tetrahedron;
import gui.GUI;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    private final int BACKPACK_CAPACITY = 100;
    private final Backpack<Shape> backpack = new Backpack<>(BACKPACK_CAPACITY);
    private final File xsdFile;

    public Controller() {
        GUI app = new GUI(this);
        app.setVisible(true);
        xsdFile = new File("resources/validator.xsd");
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

    public void saveToXML(File file) throws TransformerException, FileNotFoundException, ParserConfigurationException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        Element root = document.createElement("Shapes");
        for (Shape shape : backpack) {
            Element xml_shape = document.createElement("Shape");
            xml_shape.setAttribute("name", shape.getType());
            xml_shape.setAttribute("edge", String.valueOf(shape.getEdge()));
            root.appendChild(xml_shape);
        }
        document.appendChild(root);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new FileOutputStream(file.getAbsolutePath()));
        transformer.transform(source, result);
    }

    public void validateXMLSchema(File xmlFile) throws SAXException, IOException {
        SchemaFactory factory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(xsdFile);
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xmlFile));
    }

    public ArrayList<Shape> loadFromXML(File file) throws IOException, SAXException, ParserConfigurationException {
        validateXMLSchema(file);

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        NodeList nodeList = document.getElementsByTagName("Shape");
        ArrayList<Shape> arrayList = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                NamedNodeMap nodeMap = node.getAttributes();
                String shapeType = nodeMap.getNamedItem("name").getNodeValue();
                double edge = Double.parseDouble(nodeMap.getNamedItem("edge").getNodeValue());

                Shape shape;
                switch (shapeType) {
                    case "Ball":
                        shape = new Ball(edge);
                        break;
                    case "Cube":
                        shape = new Cube(edge);
                        break;
                    case "Tetrahedron":
                        shape = new Tetrahedron(edge);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + shapeType);
                }
                arrayList.add(shape);
            }
        }

        backpack.createFromArrayList(arrayList);
        return arrayList;
    }
}

