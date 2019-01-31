package test;

import com.cnblogs.hellxz.myutils.Word2PdfUtils;
import org.apache.poi.util.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Word2PdfUtilsTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(Word2PdfUtilsTest.class);

    @Test
    public void test1() {
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
            Word2PdfUtils.wordConverterToPdf(source, target, params);
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
