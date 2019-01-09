/**********************************************************************
 * <pre>
 * FILE : PdfTextReplacer.java
 * CLASS : PdfTextReplacer
 *
 * AUTHOR : caoxu-yiyang@qq.com
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2016年11月8日|caoxu-yiyang@qq.com| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.cnblogs.hellxz.myutils.replacepdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 替换PDF文件某个区域内的文本
 *
 * @author : caoxu-yiyang@qq.com
 * @date : 2016年11月8日
 */
public class PdfReplacer {
    private static final Logger logger = LoggerFactory.getLogger(PdfReplacer.class);

    private int fontSize;
    private Map<String, ReplaceRegion> replaceRegionMap = new HashMap<String, ReplaceRegion>();
    private Map<String, Object> replaceTextMap = new HashMap<String, Object>();
    private ByteArrayOutputStream output;
    private PdfReader reader;
    private PdfStamper stamper;
    private PdfContentByte canvas;
    private Font font;

    public PdfReplacer(byte[] pdfBytes) throws DocumentException, IOException {
        init(pdfBytes);
    }

    public PdfReplacer(String fileName) throws IOException, DocumentException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(fileName);
            byte[] pdfBytes = new byte[in.available()];
            in.read(pdfBytes);
            init(pdfBytes);
        } finally {
            in.close();
        }
    }

    private void init(byte[] pdfBytes) throws DocumentException, IOException {
        logger.info("初始化开始");
        reader = new PdfReader(pdfBytes);
        output = new ByteArrayOutputStream();
        stamper = new PdfStamper(reader, output);
        canvas = stamper.getOverContent(1);
        setFont(10);
        logger.info("初始化成功");
    }

    private void close() throws DocumentException, IOException {
        if (reader != null) {
            reader.close();
        }
        if (output != null) {
            output.close();
        }

        output = null;
        replaceRegionMap = null;
        replaceTextMap = null;
    }

    public void replaceText(float x, float y, float w, float h, String text) {
        //用文本作为别名
        ReplaceRegion region = new ReplaceRegion(text);
        region.setH(h);
        region.setW(w);
        region.setX(x);
        region.setY(y);
        addReplaceRegion(region);
        this.replaceText(text, text);
    }

    public void replaceText(String name, String text) {
        this.replaceTextMap.put(name, text);
    }

    /**
     * 替换文本
     */
    private void process() throws DocumentException, IOException {
        try {
            parseReplaceText();
            canvas.saveState();
            Set<Entry<String, ReplaceRegion>> entrys = replaceRegionMap.entrySet();
            for (Entry<String, ReplaceRegion> entry : entrys) {
                ReplaceRegion value = entry.getValue();
                canvas.setColorFill(BaseColor.RED);
                canvas.rectangle(value.getX(), value.getY(), value.getW(), value.getH());
            }
            canvas.fill();
            canvas.restoreState();
            //开始写入文本
            canvas.beginText();
            for (Entry<String, ReplaceRegion> entry : entrys) {
                ReplaceRegion value = entry.getValue();
                //设置字体
                canvas.setFontAndSize(font.getBaseFont(), getFontSize());
                /*修正背景与文本的相对位置*/
                canvas.setTextMatrix(value.getX(), value.getY() + 2);
                canvas.showText((String) replaceTextMap.get(value.getAliasName()));
            }
            canvas.endText();
        } finally {
            if (stamper != null) {
                stamper.close();
            }
        }
    }

    /**
     * 未指定具体的替换位置时，系统自动查找位置
     */
    private void parseReplaceText() {
        PdfPositionParse parse = new PdfPositionParse(reader);
        Set<Entry<String, Object>> entrys = this.replaceTextMap.entrySet();
        for (Entry<String, Object> entry : entrys) {
            if (this.replaceRegionMap.get(entry.getKey()) == null) {
                parse.addFindText(entry.getKey());
            }
        }

        try {
            Map<String, ReplaceRegion> parseResult = parse.parse();
            Set<Entry<String, ReplaceRegion>> parseEntrys = parseResult.entrySet();
            for (Entry<String, ReplaceRegion> entry : parseEntrys) {
                if (entry.getValue() != null) {
                    this.replaceRegionMap.put(entry.getKey(), entry.getValue());
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * 生成新的PDF文件
     *
     * @param fileName 文件名
     */
    public void toPdf(String fileName) throws DocumentException, IOException {
        FileOutputStream fileOutputStream = null;
        try {
            process();
            fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(output.toByteArray());
            fileOutputStream.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            close();
        }
        logger.info("文件生成成功");
    }

    /**
     * 将生成的PDF文件转换成二进制数组
     *
     * @return
     * @throws DocumentException
     * @throws IOException
     * @user : caoxu-yiyang@qq.com
     * @date : 2016年11月9日
     */
    public byte[] toBytes() throws DocumentException, IOException {
        try {
            process();
            logger.info("二进制数据生成成功");
            return output.toByteArray();
        } finally {
            close();
        }
    }

    /**
     * 添加替换区域
     *
     * @param replaceRegion
     * @user : caoxu-yiyang@qq.com
     * @date : 2016年11月9日
     */
    public void addReplaceRegion(ReplaceRegion replaceRegion) {
        this.replaceRegionMap.put(replaceRegion.getAliasName(), replaceRegion);
    }

    /**
     * 通过别名得到替换区域
     *
     * @param aliasName
     * @return
     * @user : caoxu-yiyang@qq.com
     * @date : 2016年11月9日
     */
    public ReplaceRegion getReplaceRegion(String aliasName) {
        return this.replaceRegionMap.get(aliasName);
    }

    public int getFontSize() {
        return fontSize;
    }

    /**
     * 设置字体大小
     *
     * @param fontSize
     * @throws DocumentException
     * @throws IOException
     * @user : caoxu-yiyang@qq.com
     * @date : 2016年11月9日
     */
    public void setFont(int fontSize) throws DocumentException, IOException {
        if (fontSize != this.fontSize) {
            this.fontSize = fontSize;
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            font = new Font(bf, this.fontSize, Font.BOLD);
        }
    }

    public void setFont(Font font) {
        if (font == null) {
            throw new NullPointerException("font is null");
        }
        this.font = font;
    }

    public static void main(String[] args) throws IOException, DocumentException {
        PdfReplacer textReplacer = new PdfReplacer("I://test.pdf");
        textReplacer.replaceText("陈坤", "小白");
        textReplacer.replaceText("本科", "社会大学");
        textReplacer.replaceText("0755-29493863", "15112345678");
        textReplacer.toPdf("I://ticket_out.pdf");
    }
}
