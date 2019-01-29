package test;

import com.cnblogs.hellxz.myutils.XmlUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class XmlUtilsTest {

    @Test
    public void test1() {
        Document document = XmlUtils.createDocumentWithRootElement("Request");
        Element element = null;
        List<Element> elements = new LinkedList<>();
        int count = 0;
        for (int i = 0; i < 5; i++) {
            count = ++count;
            element = XmlUtils.createElement("Tag" + count, "testData" + count);
            elements.add(element);
        }
        document = XmlUtils.addElements2rootElement(document, elements);
        String s = document.asXML();
        System.out.println(s + "\n\n");

        List<Element> subElementList = XmlUtils.getSubElementList(document.getRootElement());
        for (Element element1 : subElementList) {
            System.out.println(element1.getTextTrim());
        }
    }

    @Test
    public void test2() throws DocumentException {
        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<message>\n" +
                        "    <msg_type>02</msg_type>\n" +
                        "    <msg_sender>12370000495571840L</msg_sender>\n" +
                        "    <msg_orgsn>0</msg_orgsn>\n" +
                        "    <msg_time>1548647522671</msg_time>\n" +
                        "    <msg_rspcode>0</msg_rspcode>\n" +
                        "    <msg_rspdesc>解析成功！</msg_rspdesc>\n" +
                        "    <msg_sign>5</msg_sign>\n" +
                        "    <msg_body></msg_body>\n" +
                        "</message>";
        Element rootElement = XmlUtils.getRootElement(xml);
        String s = rootElement.asXML();
        System.out.println(s);
        String elementText = XmlUtils.getElementText(rootElement, "msg_rspdesc");
        System.out.println(elementText);
    }
}
