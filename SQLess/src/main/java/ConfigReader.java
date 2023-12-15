/**
 * @author LiLin
 * @version 1.0
 * @date 2023/11/1 下午8:44
 */
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigReader {

    public List<String> readSimplificationStrategies(String filePath) throws Exception {
        List<String> strategies = new ArrayList<>();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File(filePath));

        // Normalize the XML structure
        doc.getDocumentElement().normalize();

        // Get the <SimplificationStrategy> node
        Element root = (Element) doc.getElementsByTagName("SimplificationStrategy").item(0);

        // Iterate through all child elements of <SimplificationStrategy>
        NodeList strategyNodes = root.getChildNodes();
        for (int i = 0; i < strategyNodes.getLength(); i++) {
            Node strategyNode = strategyNodes.item(i);
            if (strategyNode.getNodeType() == Node.ELEMENT_NODE) {
                Element strategyElement = (Element) strategyNode;
                if ("true".equalsIgnoreCase(strategyElement.getTextContent().trim())) {
                    strategies.add(strategyElement.getTagName());
                }
            }
        }

        return strategies;
    }
}
