package GUI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GameStateHandler {
    public void saveGameState(Map<String, Point> objectPositions, String filePath) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // Create root element
            Element root = document.createElement("GameState");
            document.appendChild(root);

            // Create object positions element
            Element positionsElement = document.createElement("ObjectPositions");
            root.appendChild(positionsElement);

            // Iterate through object positions and create XML elements
            for (Map.Entry<String, Point> entry : objectPositions.entrySet()) {
                Element objectElement = document.createElement("Object");
                objectElement.setAttribute("name", entry.getKey());
                objectElement.setAttribute("x", String.valueOf(entry.getValue().x));
                objectElement.setAttribute("y", String.valueOf(entry.getValue().y));
                positionsElement.appendChild(objectElement);
            }

            // Write the content into XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(filePath));

            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Point> loadGameState(String filePath) {
        Map<String, Point> objectPositions = new HashMap<>();
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);

            // Normalize the document
            document.getDocumentElement().normalize();

            // Get the ObjectPositions element
            NodeList objectList = document.getElementsByTagName("Object");

            // Iterate through the objects and populate the map
            for (int i = 0; i < objectList.getLength(); i++) {
                Node node = objectList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String name = element.getAttribute("name");
                    int x = Integer.parseInt(element.getAttribute("x"));
                    int y = Integer.parseInt(element.getAttribute("y"));
                    objectPositions.put(name, new Point(x, y));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectPositions;
    }
}