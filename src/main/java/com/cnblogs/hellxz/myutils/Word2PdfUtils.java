package com.cnblogs.hellxz.myutils;

import org.apache.commons.collections4.MapUtils;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
        <!--
            必需的依赖，注意其他版本的poi-ooxml依赖会导致NoSuchMethodError
            apache-commons之类的工具包依赖参考pom.xml
        -->

       <!--POI依赖-->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>3.14</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>3.14</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml-schemas</artifactId>
            <version>3.14</version>
        </dependency>
        <dependency>
            <groupId>fr.opensagres.xdocreport</groupId>
            <artifactId>org.apache.poi.xwpf.converter.core</artifactId>
            <version>1.0.6</version>
         </dependency>
         <dependency>
            <groupId>fr.opensagres.xdocreport</groupId>
            <artifactId>org.apache.poi.xwpf.converter.pdf</artifactId>
            <version>1.0.6</version>
         </dependency>
*/

/**
 * <b>类名</b>: Word2PdfUtils
 * <p><b>描    述</b> Word文档转换PDF </p>
 *
 * <p><b>创建日期</b>: 2018/9/29 11:32 </p>
 *
 * @author HELLXZ
 * @version 1.0
 * @since jdk 1.8
 */
public class Word2PdfUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Word2PdfUtils.class);

    /**
     * 将word文档， 转换成pdf, 中间替换掉变量
     *
     * @param source 源为word文档， 必须为docx文档
     * @param target 目标输出
     * @param params 需要替换的变量
     * @throws Exception 可能抛出的异常
     */
    public static void wordConverterToPdf(InputStream source,
                                          OutputStream target, Map<String, String> params) throws Exception {
        wordConverterToPdf(source, target, null, params);
    }

    /**
     * 将word文档， 转换成pdf, 中间替换掉变量
     *
     * @param source  源为word文档， 必须为docx文档
     * @param target  目标输出
     * @param params  需要替换的变量
     * @param options PdfOptions.create().fontEncoding( "windows-1250" ) 可修改
     * @throws Exception 可能抛出的异常
     */
    public static void wordConverterToPdf(InputStream source, OutputStream target,
                                          PdfOptions options, Map<String, String> params) throws Exception {
        LOGGER.info("开始转换word2pdf！");
        XWPFDocument doc = new XWPFDocument(source);
        //替换参数
        paragraphReplace(doc.getParagraphs(), params);
        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    paragraphReplace(cell.getParagraphs(), params);
                }
            }
        }
        PdfConverter.getInstance().convert(doc, target, options);
        LOGGER.info("转换完成！");
    }

    /**
     * 替换段落中内容
     *
     * @param paragraphs 段落对象List
     * @param params     参数Map
     */
    private static void paragraphReplace(List<XWPFParagraph> paragraphs, Map<String, String> params) {
        if (MapUtils.isNotEmpty(params)) {
            for (XWPFParagraph p : paragraphs) {
                for (XWPFRun r : p.getRuns()) {
                    String content = r.getText(r.getTextPosition());
                    if (StringUtils.isNotEmpty(content) && params.containsKey(content)) {
                        r.setText(params.get(content), 0);
                    }
                }
            }
        }
    }

    @Test
    public void test() {
        String filepath = "C:\\Users\\Administrator\\Desktop\\文档模板.docx";
        String outpath = "C:\\Users\\Administrator\\Desktop\\输出.pdf";

        InputStream source = null;
        OutputStream target = null;
        try {
            source = new FileInputStream(filepath);
            target = new FileOutputStream(outpath);
            Map<String, String> params = new HashMap<>();
            //替换word中参数请添加 k-v
//            params.put("param1","hello");
            //替换、转换并输出
            wordConverterToPdf(source, target, params);
        } catch (Exception e) {
            LOGGER.error("转换出现异常，错误堆栈为：", e);
            throw new RuntimeException("转换出现异常!");
        } finally {
            //关流
            IOUtils.closeQuietly(source);
            IOUtils.closeQuietly(target);
        }
    }
}
