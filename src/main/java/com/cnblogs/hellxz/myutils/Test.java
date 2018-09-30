package com.cnblogs.hellxz.myutils;

import com.aspose.pdf.*;

public class Test {

    private static String srcPath = "C:\\Users\\Administrator\\Desktop\\山东省立医院进修人员申请表-converted.pdf";         //源文件路径
    private static String targetPath = "C:\\Users\\Administrator\\Desktop\\converted.pdf";     //输入文件路径

    private static String srcText = "${futherSchool}";               //需要替换的文本
    private static String targetText = "Hello";             //替换的目标文本

    public static void main(String[] args) {
        String key = "";
        try {
            new License().setLicense(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Document pdfDoc = new Document(srcPath);
        TextFragmentAbsorber textFragmentAbsorber = new TextFragmentAbsorber(srcText);
        PageCollection pages = pdfDoc.getPages();
        System.out.println("文档总页码数：" + pages.size());
        pages.accept(textFragmentAbsorber);
        int i = 0;
        for (TextFragment textFragment : (Iterable<TextFragment>) textFragmentAbsorber.getTextFragments()) {
            textFragment.setText(targetText);
            textFragment.getTextState().setBackgroundColor(com.aspose.pdf.Color.getWhite());  //添加红色背景
            System.out.println(++i);
        }
        pdfDoc.save(targetPath);
        System.out.println("总共替换" + i + "处");
        System.out.println("OK");
    }

}
