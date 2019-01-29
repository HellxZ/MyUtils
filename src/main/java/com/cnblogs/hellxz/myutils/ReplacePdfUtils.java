package com.cnblogs.hellxz.myutils;

import com.cnblogs.hellxz.myutils.replacepdf.PdfReplacer;
import com.itextpdf.text.DocumentException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <b>类名</b>: ReplacePdfUtils
 * <p><b>描    述</b> 替换pdf文档工具 本地已测试，response开流方式尚未测试</p>
 *
 * <p><b>创建日期</b>: 2019/1/29 17:45 </p>
 *
 * @author HELLXZ 张
 * @version 1.0
 * @since jdk 1.8
 */
public class ReplacePdfUtils {

    /**
     * 本地读取pdf文件，替换文本并输出到本地
     *
     * @param inputPdfPath 输入地址
     * @param params       参数, key来做为被替换的值， value作为待替换的值
     * @param outputPath   输出路径
     * @throws IOException       IO 异常
     * @throws DocumentException 文档异常
     */
    public static void out(String inputPdfPath, Map<String, String> params, String outputPath) throws IOException, DocumentException {
        // 读取文件内容
        PdfReplacer textReplacer = new PdfReplacer(inputPdfPath);
        replace(textReplacer, params);
        // 输出
        textReplacer.toPdf(outputPath);
    }

    /**
     * 本地取文件，通过response开流下载文件【未测试】
     *
     * @param inputPdfPath 文件路径
     * @param params       参数
     * @param outName      输出名称
     * @param request      请求
     * @param response     响应
     */
    public static void out(String inputPdfPath, Map<String, String> params, String outName, HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
        // 读取文件内容
        PdfReplacer textReplacer = new PdfReplacer(inputPdfPath);
        replace(textReplacer, params);
        // 转换成字节数组
        byte[] bytes = textReplacer.toBytes();
        // 下载
        FileDownloadUtil.downloadFile(bytes, outName, request, response);
    }

    /**
     * 替换文本 【未测试】
     * @param replacer pdf替换器
     * @param params 参数
     * @return 替换器
     */
    private static PdfReplacer replace(PdfReplacer replacer, Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (null != key) {
                // 使用key来做为被替换的值， value作为待替换的值
                replacer.replaceText(key, value);
            }
        }
        return replacer;
    }
}
