package com.hsuyeung.blog.sitemap;

import com.hsuyeung.blog.constant.DateFormatConstants;
import com.hsuyeung.blog.util.DateUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;


/**
 * 站点地图文件生成器
 *
 * @author hsuyeung
 * @date 2023/11/10
 */
public final class SitemapGenerator {
    public static void generate(final Collection<URLNode> urlNodes,
                                final String outputFilePath)
            throws ParserConfigurationException, IOException, TransformerException {
        if (urlNodes == null || urlNodes.isEmpty()) {
            return;
        }

        // 创建 XML 文档
        Document sitemapXML = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .newDocument();

        // 创建根节点
        Element rootElement = sitemapXML.createElement("urlset");
        rootElement.setAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");
        rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.setAttribute("xsi:schemaLocation", "http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd");
        sitemapXML.appendChild(rootElement);

        // 遍历查询结果，生成 XML
        for (URLNode urlNode : urlNodes) {
            Element urlElement = sitemapXML.createElement("url");
            rootElement.appendChild(urlElement);

            Element loc = sitemapXML.createElement("loc");
            String locVal = encodeNonAscii(
                    urlNode.getLoc()
                            .replace("&", "&amp;")
                            .replace("'", "&apos;")
                            .replace("\"", "&quot;")
                            .replace(">", "&gt;")
                            .replace("<", "&lt;")
            );
            loc.appendChild(sitemapXML.createTextNode(locVal));
            urlElement.appendChild(loc);

            Element lastmodElement = sitemapXML.createElement("lastmod");
            lastmodElement.appendChild(sitemapXML.createTextNode(DateUtil.formatLocalDateTime(urlNode.getLastMod(), DateFormatConstants.ISO_8601)));
            urlElement.appendChild(lastmodElement);

            Element changefreqElement = sitemapXML.createElement("changefreq");
            changefreqElement.appendChild(sitemapXML.createTextNode(urlNode.getChangeFreq().getValue()));
            urlElement.appendChild(changefreqElement);

            Element priorityElement = sitemapXML.createElement("priority");
            priorityElement.appendChild(sitemapXML.createTextNode(String.valueOf(urlNode.getPriority())));
            urlElement.appendChild(priorityElement);
        }

        // 将 XML 写入文件
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");  // 设置缩进的空格数
        transformer.transform(new DOMSource(sitemapXML), new StreamResult(new FileWriter(outputFilePath)));
    }

    private static String encodeNonAscii(String input) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (char ch : input.toCharArray()) {
            if (ch > 127) {
                // 对非 ASCII 字符进行 URL 编码
                result.append(URLEncoder.encode(String.valueOf(ch), "UTF-8"));
            } else {
                // 对 ASCII 字符保持原样
                result.append(ch);
            }
        }

        return result.toString();
    }

    private SitemapGenerator() {
    }
}
