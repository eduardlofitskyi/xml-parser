package com.lofitskyi.dom;

import com.lofitskyi.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class DomXmlUtil implements XmlUtil{

    public void removeEvenNodes(String src, String dest) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        Path srcPath = Paths.get(src);
        Path destPath = Paths.get(dest);

        checkSourcePath(srcPath);
        checkDestPath(destPath);

        Document document = initDocument(srcPath);
        document.setXmlStandalone(true);
        scanAndEditDocument(document);
        write(document, destPath);
    }

    private void checkDestPath(Path path) {
        if (Files.notExists(path)) throw new IllegalArgumentException("Given source path doesn't exist");
        if (Files.isDirectory(path)) throw new IllegalArgumentException("Given source path point at directory");
        if (!Files.isWritable(path)) throw new IllegalArgumentException("Cannot write to file, access denied");
    }

    private void checkSourcePath(Path path) {
        if (Files.notExists(path)) throw new IllegalArgumentException("Given source path doesn't exist");
        if (Files.isDirectory(path)) throw new IllegalArgumentException("Given source path point at directory");
        if (!Files.isReadable(path)) throw new IllegalArgumentException("File cannot be redden");
    }

    private Document initDocument(Path path) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();

        return db.parse(new FileInputStream(path.toFile()));
    }

    private void scanAndEditDocument(Node node) {
        NodeList list = node.getChildNodes();

        Set<Element> targetElements = new HashSet<>();

        int evenCounter = 3; //for counting even elements

        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {

                if (evenCounter % 2 == 0){
                    targetElements.add((Element) list.item(i));
                }

                evenCounter++;

                scanAndEditDocument(list.item(i));
            }
        }

        for (Element e: targetElements){
            e.getParentNode().removeChild(e);
        }

    }

    private void write(Document document, Path pathTo) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result =
                new StreamResult(pathTo.toFile());
        transformer.transform(source, result);
    }
}
