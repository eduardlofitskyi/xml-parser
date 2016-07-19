package com.lofitskyi;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface XmlUtil {
    void removeEvenNodes(String src, String dest) throws IOException, SAXException, ParserConfigurationException, TransformerException;
}
