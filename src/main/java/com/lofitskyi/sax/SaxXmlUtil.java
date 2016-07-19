package com.lofitskyi.sax;

import com.lofitskyi.XmlUtil;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaxXmlUtil implements XmlUtil{

    @Override
    public void removeEvenNodes(String src, String dest) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        Path srcPath = Paths.get(src);
        Path destPath = Paths.get(dest);

        checkSourcePath(srcPath);
        checkDestPath(destPath);

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        DefaultHandler customParser = new EvenTagDetectHandler(destPath);
        parser.parse(srcPath.toFile(), customParser);
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
}
