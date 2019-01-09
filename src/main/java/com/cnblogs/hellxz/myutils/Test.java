package com.cnblogs.hellxz.myutils;

import com.aspose.pdf.*;

public class Test {

    /**
     * 源文件路径
     */
    private static String SRC_PATH = "C:\\Users\\Administrator\\Desktop\\山东省立医院进修人员申请表-converted.pdf";

    /**
     * 输入文件路径
     */
    private static String TARGET_PATH = "C:\\Users\\Administrator\\Desktop\\converted.pdf";

    /**
     * 需要替换的文本
     */
    private static String SRC_TEXT = "${futherSchool}";

    /**
     * 替换的目标文本
     */
    private static String TARGET_TEXT = "Hello";

    public static void main(String[] args) {
        String key = "";
        try {
            new License().setLicense(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Document pdfDoc = new Document(SRC_PATH);
        TextFragmentAbsorber textFragmentAbsorber = new TextFragmentAbsorber(SRC_TEXT);
        PageCollection pages = pdfDoc.getPages();
        System.out.println("文档总页码数：" + pages.size());
        pages.accept(textFragmentAbsorber);
        int i = 0;
        for (TextFragment textFragment : textFragmentAbsorber.getTextFragments()) {
            textFragment.setText(TARGET_TEXT);
            //添加红色背景
            textFragment.getTextState().setBackgroundColor(com.aspose.pdf.Color.getWhite());
            System.out.println(++i);
        }
        pdfDoc.save(TARGET_PATH);
        System.out.println("总共替换" + i + "处");
        System.out.println("OK");
    }

}
