package com.lofitskyi.dom;

import com.lofitskyi.XmlUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DomXmlUtilTest {

    private final static Path givenPath = Paths.get("src/test/resources/test.xml");
    private final static Path expectedResultPath = Paths.get("src/test/resources/expected.xml");
    private final static Path resultPath = Paths.get("src/test/resources/dom.xml");

    @Test
    public void removeEvenNodes() throws Exception {
        XmlUtil util = new DomXmlUtil();
        util.removeEvenNodes(givenPath.toString(), resultPath.toString());

        String expectedStr = String.join(" ", Files.readAllLines(expectedResultPath));
        String resultStr = String.join(" ", Files.readAllLines(resultPath));

        expectedStr = expectedStr.replaceAll("\\s", "");
        resultStr = resultStr.replaceAll("\\s", "");

        Assert.assertEquals(expectedStr, resultStr);
    }
}