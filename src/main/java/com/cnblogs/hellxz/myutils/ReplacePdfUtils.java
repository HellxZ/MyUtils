package com.cnblogs.hellxz.myutils;

import com.cnblogs.hellxz.myutils.replacePdf.PdfReplacer;
import com.itextpdf.text.DocumentException;
import org.junit.Test;

import java.io.IOException;

public class ReplacePdfUtils {

    public static void out() throws IOException, DocumentException {
        PdfReplacer textReplacer = new PdfReplacer(
                "C:\\Users\\Administrator\\Desktop\\山东省立医院进修人员申请表-converted.pdf");
        textReplacer.replaceText("${futherName}","山东省立医院山东省立医院山东省立医院");
        textReplacer.toPdf("C:\\Users\\Administrator\\Desktop\\converted.pdf");
    }

    @Test
    public void test() throws Exception{
        out();
    }
}
