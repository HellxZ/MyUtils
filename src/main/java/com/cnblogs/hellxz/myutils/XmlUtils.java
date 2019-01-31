package com.cnblogs.hellxz.myutils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;

/**
 * <b>类名</b>: XmlUtils
 * <p><b>描    述</b> Xml工具类，使用Dom4J实现 </p>
 *
 * <p><b>创建日期</b>: 2019/1/28 11:07 </p>
 *
 * @author HELLXZ 张
 * @version 1.0
 * @since jdk 1.8
 */
public class XmlUtils {

    /**
     * 通过xml字符串来得到根节点对象
     *
     * @param requestXml xml字符串
     * @return 根节点element对象
     * @throws DocumentException 文档异常
     */
    public static Element getRootElement(String requestXml) throws DocumentException {
        Document document = DocumentHelper.parseText(requestXml);
        Element root = document.getRootElement();
        return root;
    }

    /**
     * 通过根节点对象得到子节点List
     *
     * @param root 根节点对象
     * @return element对象list
     */
    @SuppressWarnings("unchecked")
    public static List<Element> getSubElementList(Element root) {
        List<Element> elements = root.elements();
        return elements;
    }

    /**
     * 创建一个文档对象
     *
     * @return 文档对象
     */
    public static Document createDocument() {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        return document;
    }

    /**
     * 创建一个文档对象，并用给定名称创建根节点
     *
     * @param rootElementName 根节点名称
     * @return 文档对象
     */
    public static Document createDocumentWithRootElement(String rootElementName) {
        Document document = createDocument();
        Element rootElement = DocumentHelper.createElement(rootElementName);
        document.setRootElement(rootElement);
        document.setXMLEncoding("UTF-8");
        return document;
    }

    /**
     * 创建一个节点，并赋值
     *
     * @param elementName 节点名
     * @param data        节点内的字符串名
     * @return element节点
     */
    public static Element createElement(String elementName, String data) {
        Element element = DocumentHelper.createElement(elementName);
        element.setText(data);
        return element;
    }

    /**
     * 给定一个已有根结点的文档，将elements集合的节点添加到其根节点下
     *
     * @param document 已有根结点的文档
     * @param elements elements集合
     * @return 完成添加的文档
     */
    public static Document addElements2rootElement(Document document, List<Element> elements) {
        Element rootElement = document.getRootElement();
        for (Element element : elements) {
            rootElement.add(element);
        }
        return document;
    }

    /**
     * 得到根节点下某个节点名的数据
     * @param rootElement 根节点
     * @param nodeName 节点名
     * @return 节点内容字符串
     */
    public static String getElementText(Element rootElement, String nodeName) {
        Element element = rootElement.element(nodeName);
        if (null != element) {
            String text = element.getTextTrim();
            return text;
        }
        return "";
    }
}
