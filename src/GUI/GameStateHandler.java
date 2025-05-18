package GUI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

import logic.GameLogic;

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

    private GameLogic gameLogic; // Assuming you have a GameLogic class to handle game logic

    public GameStateHandler(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }


    /**
        * Updates the UI based on the currently selected object.
    */
    private void updateUIBasedOnSelection() {
        // Példa: Frissítse az alsó sávot
        // JPanel bottomBar = getBottomBar(); // Feltételezve, hogy van egy metódus az alsó sáv lekérésére
        //updateBottomBar(selectedObject, bottomBar);
    }

    /**
     * Saves the game state to an XML file, given a map of object positions.
     * The XML file should contain a root element called "GameState" which contains
     * an element called "ObjectPositions". The ObjectPositions element should contain
     * a list of "Object" elements, each of which should have the following attributes:
     * <ul>
     * <li>name: the name of the object (e.g. "Fungus1", "Insect2", etc.)</li>
     * <li>x: the x position of the object in the game world</li>
     * <li>y: the y position of the object in the game world</li>
     * </ul>
     * If there is an error writing the file, the function will print an error message.
     * @param objectPositions a map of object positions
     * @param filePath the path to the XML file to write
     */
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

    /**
     * Loads the game state from an XML file, and returns a map of object positions.
     * The XML file should contain a root element called "GameState" which contains
     * an element called "ObjectPositions". The ObjectPositions element should contain
     * a list of "Object" elements, each of which should have the following attributes:
     * <ul>
     * <li>name: the name of the object (e.g. "Fungus1", "Insect2", etc.)</li>
     * <li>x: the x position of the object in the game world</li>
     * <li>y: the y position of the object in the game world</li>
     * </ul>
     * The function will return a map where the keys are the object names, and the
     * values are the positions of the objects. If there is an error reading the file,
     * the function will print an error message and return an empty map.
     * @param filePath the path to the XML file containing the game state
     * @return a map of object positions
     */
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