package com.cnblogs.hellxz.myutils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
/**
 * Created by wangpeng on 2018/02/01.
 */
public class PdfUtils {
    // 利用模板生成pdf  
    public static void pdfout(Map<String,Object> o) {
        // 模板路徑  
        String templatePath = "C:\\Users\\Administrator\\Desktop\\山东省立医院进修人员申请表-converted.pdf";
        // 生成的新文檔路徑  
        String newPDFPath = "C:\\Users\\Administrator\\Desktop\\test2.pdf";

        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            BaseFont bf = BaseFont.createFont("c://windows//fonts//simsun.ttc,1" , BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font FontChinese = new Font(bf, 5, Font.NORMAL);
            out = new FileOutputStream(newPDFPath);// 輸出流
            reader = new PdfReader(templatePath);// 讀取pdf模板  
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            //文本類的內容處理
            Map<String,String> datemap = (Map<String,String>)o.get("datemap");
            form.addSubstitutionFont(bf);
            for(String key : datemap.keySet()){
                String value = datemap.get(key);
                form.setField(key,value);
            }
            //圖片類的內容處理
            Map<String,String> imgmap = (Map<String,String>)o.get("imgmap");
            for(String key : imgmap.keySet()) {
                String value = imgmap.get(key);
                String imgpath = value;
                int pageNo = form.getFieldPositions(key).get(0).page;
                Rectangle signRect = form.getFieldPositions(key).get(0).position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
                //根據路徑讀取圖片
                Image image = Image.getInstance(imgpath);
                //獲取圖片頁面
                PdfContentByte under = stamper.getOverContent(pageNo);
                //圖片大小自適應
                image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                //添加圖片
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
            stamper.setFormFlattening(true);// 如果為false，生成的PDF文檔可以編輯，如果為true，生成的PDF文檔不可以編輯
            stamper.close();
            Document doc = new Document();
            Font font = new Font(bf, 32);
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

        } catch (IOException e) {
            System.out.println(e);
        } catch (DocumentException e) {
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        Map<String,String> map = new HashMap();
        map.put("学校名称","23012611111111551");

        Map<String,String> map2 = new HashMap();
//        map2.put("img","C:\\Users\\Administrator\\Downloads\\meiliderichu.jpg");

        Map<String,Object> o=new HashMap();
        o.put("datemap",map);
        o.put("imgmap",map2);
        pdfout(o);
    }
}  