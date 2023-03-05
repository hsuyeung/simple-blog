package com.hsuyeung.blog.rss;

import com.hsuyeung.blog.util.AssertUtil;
import org.springframework.http.MediaType;

import javax.validation.constraints.NotNull;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * RSS 文件输出工具
 *
 * @author hsuyeung
 * @date 2023/03/04
 */
public class Writer {
    /**
     * RSS 数据
     */
    private final RSS rss;

    /**
     * 输出文件的路径
     */
    private final String outputFilePath;

    public Writer(@NotNull RSS rss, @NotNull File outputFile) {
        AssertUtil.notNull(rss, "feed can not be null");
        AssertUtil.notNull(outputFile, "outputFile can not be null");
        this.rss = rss;
        this.outputFilePath = outputFile.getPath();
    }

    public Writer(@NotNull RSS rss, @NotNull String outputFilePath) {
        AssertUtil.notNull(rss, "feed can not be null");
        AssertUtil.notNull(outputFilePath, "outputFilePath can not be null");
        this.rss = rss;
        this.outputFilePath = outputFilePath;
    }

    /**
     * 输出文件
     *
     * @throws IOException        输出路径不存在
     * @throws XMLStreamException 构建 XML 失败
     */
    public void write() throws IOException, XMLStreamException {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(Files.newOutputStream(Paths.get(outputFilePath)));
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");

        // 创建 xml 开始标签
        StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);
        eventWriter.add(end);

        // 创建 rss 开始标签
        eventWriter.add(eventFactory.createStartElement("", "", "rss"));
        eventWriter.add(eventFactory.createAttribute("version", "2.0"));
        eventWriter.add(eventFactory.createNamespace("atom", "http://www.w3.org/2005/Atom"));
        eventWriter.add(end);

        // 创建 Channel 开始标签
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createStartElement("", "", "channel"));
        eventWriter.add(end);

        // 创建 atom 标签
        eventWriter.add(tab);
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createStartElement("", "", "atom:link"));
        eventWriter.add(eventFactory.createAttribute("href", String.format("%s/feed", rss.getLink())));
        eventWriter.add(eventFactory.createAttribute("rel", "self"));
        eventWriter.add(eventFactory.createAttribute("type", MediaType.APPLICATION_RSS_XML_VALUE));
        eventWriter.add(eventFactory.createEndElement("", "", "atom:link"));
        eventWriter.add(end);

        // 创建 Channel 标签下的结点
        this.createNode(eventWriter, "title", rss.getTitle(), 2);
        this.createNode(eventWriter, "link", rss.getLink(), 2);
        this.createNode(eventWriter, "description", rss.getDescription(), 2);
        this.createNode(eventWriter, "language", rss.getLanguage(), 2);
        this.createNode(eventWriter, "copyright", rss.getCopyright(), 2);
        this.createNode(eventWriter, "managingEditor", rss.getManagingEditor(), 2);
        this.createNode(eventWriter, "webMaster", rss.getWebMaster(), 2);
        this.createNode(eventWriter, "pubDate", rss.getPubDate(), 2);
        this.createNode(eventWriter, "lastBuildDate", rss.getLastBuildDate(), 2);
        this.createNode(eventWriter, "category", rss.getCategory(), 2);
        this.createNode(eventWriter, "generator", rss.getGenerator(), 2);
        this.createNode(eventWriter, "docs", rss.getDocs(), 2);
        this.createNode(eventWriter, "cloud", rss.getCloud(), 2);
        this.createNode(eventWriter, "ttl", rss.getTtl(), 2);
        this.createNode(eventWriter, "image", rss.getImage(), 2);
        this.createNode(eventWriter, "rating", rss.getRating(), 2);
        this.createNode(eventWriter, "textInput", rss.getTextInput(), 2);
        this.createNode(eventWriter, "skipHours", rss.getSkipHours(), 2);
        this.createNode(eventWriter, "skipDays", rss.getSkipDays(), 2);

        List<Item> itemList = rss.getItemList();
        if (itemList != null && itemList.size() > 0) {
            int size = itemList.size();
            for (int i = 0; i < size; i++) {
                Item item = itemList.get(i);
                eventWriter.add(tab);
                eventWriter.add(tab);
                eventWriter.add(eventFactory.createStartElement("", "", "item"));
                eventWriter.add(end);
                this.createNode(eventWriter, "title", item.getTitle(), 3);
                this.createNode(eventWriter, "link", item.getLink(), 3);
                this.createNode(eventWriter, "description", item.getDescription(), 3);
                this.createNode(eventWriter, "author", item.getAuthor(), 3);
                this.createNode(eventWriter, "category", item.getCategory(), 3);
                this.createNode(eventWriter, "comments", item.getComments(), 3);
                this.createNode(eventWriter, "enclosure", item.getEnclosure(), 3);
                this.createNode(eventWriter, "guid", item.getGuid(), 3);
                this.createNode(eventWriter, "pubDate", item.getPubDate(), 3);
                this.createNode(eventWriter, "source", item.getSource(), 3);
                eventWriter.add(tab);
                eventWriter.add(tab);
                eventWriter.add(eventFactory.createEndElement("", "", "item"));
                if (i < size - 1) {
                    eventWriter.add(end);
                }
            }
        }

        eventWriter.add(end);
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createEndElement("", "", "channel"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndElement("", "", "rss"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.close();
    }

    /**
     * 创建一个结点
     *
     * @param eventWriter {@link XMLEventWriter}
     * @param name        结点名称
     * @param value       结点值
     * @throws XMLStreamException .
     */
    private void createNode(XMLEventWriter eventWriter, String name, Object value, int tabNum)
            throws XMLStreamException {
        if (value == null || "".equals(value)) {
            return;
        }
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        for (int i = 0; i < tabNum; i++) {
            eventWriter.add(tab);
        }
        eventWriter.add(eventFactory.createStartElement("", "", name));
        if ("source".equals(name)) {
            Item.Source source = (Item.Source) value;
            eventWriter.add(eventFactory.createAttribute("url", source.getUrl()));
            eventWriter.add(eventFactory.createCharacters(source.getValue()));
        } else {
            eventWriter.add(eventFactory.createCharacters(String.valueOf(value)));
        }
        eventWriter.add(eventFactory.createEndElement("", "", name));
        eventWriter.add(end);
    }
}
