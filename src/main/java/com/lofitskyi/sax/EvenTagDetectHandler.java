package com.lofitskyi.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EvenTagDetectHandler extends DefaultHandler {

    private Path destPath;

    private static int currentLevel = 0;
    private static int prevLevel = 0;
    private static boolean[] isPrevElementCounted = new boolean[100_100];
    private static boolean isGonnaBeRemoved = false;
    private static int levelGonnaBeRemoved = 0;
    private static StringBuilder builder = new StringBuilder();

    public EvenTagDetectHandler(Path destPath) {
        this.destPath = destPath;
    }

    @Override
    public void startDocument() throws SAXException {
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentLevel++;

        if (isGonnaBeRemoved) return;

        if (currentLevel == prevLevel && !isPrevElementCounted[currentLevel]){
            isGonnaBeRemoved = true;
            levelGonnaBeRemoved = currentLevel;
            isPrevElementCounted[currentLevel] = true;
        } else {
            isPrevElementCounted[currentLevel] = false;
            if (attributes.getLength() == 0){
                builder.append("<").append(qName).append(">");
            } else {
                builder.append("<").append(qName).append(" ");

                for (int i = 0; i < attributes.getLength(); i++){
                    builder.append(attributes.getQName(i)).append("=\"").append(attributes.getValue(i)).append("\" ");
                }

                builder.append(">");
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (!isGonnaBeRemoved){
            builder.append(new String(ch, start, length));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        prevLevel = currentLevel;
        currentLevel--;

        if (isGonnaBeRemoved) {
            if (levelGonnaBeRemoved == currentLevel+1){
                isGonnaBeRemoved = false;
                return;
            }else {
                return;
            }
        }

        builder.append("</").append(qName).append(">\n");
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            Files.write(this.destPath, builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
