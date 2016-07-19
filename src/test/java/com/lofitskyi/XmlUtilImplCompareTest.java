package com.lofitskyi;

import com.lofitskyi.dom.DomXmlUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XmlUtilImplCompareTest {

    private final static Path givenPath = Paths.get("src/test/resources/test.xml");
    private final static Path resultPathDom = Paths.get("src/test/resources/dom.xml");
    private final static Path resultPathSax = Paths.get("src/test/resources/sax.xml");

    @Test
    public void parsersShouldReturnEqualResults() throws Exception {

        removeEvenNodesDom();
        removeEvenNodesSax();

        String domStr = String.join(" ", Files.readAllLines(resultPathDom));
        String saxStr = String.join(" ", Files.readAllLines(resultPathSax));

        domStr = domStr.replaceAll("\\s", "");
        saxStr = saxStr.replaceAll("\\s", "");

        Assert.assertEquals(domStr, saxStr);
    }


    private void removeEvenNodesDom() throws Exception {
        XmlUtil util = new DomXmlUtil();
        util.removeEvenNodes(givenPath.toString(), resultPathDom.toString());
    }

    private void removeEvenNodesSax() throws Exception {
        XmlUtil util = new DomXmlUtil();
        util.removeEvenNodes(givenPath.toString(), resultPathSax.toString());
    }
}