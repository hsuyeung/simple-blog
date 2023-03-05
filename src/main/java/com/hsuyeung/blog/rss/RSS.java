package com.hsuyeung.blog.rss;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * RSS 2.0 结构定义
 *
 * <p>
 * 参考链接：
 * <a href="https://www.rssboard.org/rss-specification">RSS 2.0 Specification</a>
 * </p>
 *
 * @author hsuyeung
 * @date 2023/03/04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RSS implements Serializable {
    private static final long serialVersionUID = -5120923461953614636L;

    @Specification(required = true, description = "The name of the channel. It's how people refer to your service ." +
            "If you have an HTML website that contains the same information as your RSS file," +
            "the title of your channel should be the same as the title of your website.")
    private String title;

    @Specification(required = true, description = "The URL to the HTML website corresponding to the channel.")
    private String link;

    @Specification(required = true, description = "Phrase or sentence describing the channel.")
    private String description;

    @Specification(description = "The language the channel is written in." +
            "This allows aggregators to group all Italian language sites, for example, on a single page." +
            "A list of allowable values for this element , as provided by Netscape, is <a href=\"https://www.rssboard.org/rss-language-codes\">here</a>." +
            "You may also use <a href=\"https://www.w3.org/TR/REC-html40/struct/dirlang.html#langcodes\">values defined</a> by the W3C.")
    private String language;

    @Specification(description = "Copyright notice for content in the channel.")
    private String copyright;

    @Specification(description = "Email address for person responsible for editorial content .")
    private String managingEditor;

    @Specification(description = "Email address for person responsible for technical issues relating to channel.")
    private String webMaster;

    @Specification(description = "The publication date for the content in the channel." +
            "For example, the New York Times publishes on a daily basis , the publication date flips once every 24 hours." +
            "That's when the pubDate of the channel changes." +
            "All date-times in RSS conform to the Date and Time Specification of <a href=\"https://www.rfc-editor.org/rfc/rfc822\">RFC 822</a>," +
            "with the exception that the year may be expressed with two characters or four characters (four preferred).")
    private String pubDate;

    @Specification(description = "The last time the content of the channel changed.")
    private String lastBuildDate;

    @Specification(description = "Specify one or more categories that the channel belongs to." +
            "Follows the same rules as the < item >-level category element ." +
            "More <a href=\"https://www.rssboard.org/rss-specification#syndic8\">info</a>.")
    private String category;

    @Specification(description = "A string indicating the program used to generate the channel.")
    private String generator;

    @Specification(description = "A URL that points to the <a href=\"https://www.rssboard.org/rss-specification\">documentation</a> for the format used in the RSS file." +
            "It's probably a pointer to this page." +
            "It's for people who might stumble across an RSS file on a Web server 25 years from now and wonder what it is.")
    private String docs;

    @Specification(description = "Allows processes to register with a cloud to be notified of updates to the channel," +
            "implementing a lightweight publish-subscribe protocol for RSS feeds. More info" +
            "<a href=\"https://www.rssboard.org/rss-specification#ltcloudgtSubelementOfLtchannelgt\">here</a>.")
    private String cloud;

    @Specification(description = "ttl stands for time to live ." +
            "It's a number of minutes that indicates how long a channel can be cached before refreshing from the source." +
            "More info <a href=\"https://www.rssboard.org/rss-specification#ltttlgtSubelementOfLtchannelgt\">here</a>.")
    private String ttl;

    @Specification(description = "Specifies a GIF, JPEG or PNG image that can be displayed with the channel." +
            "More info <a href=\"https://www.rssboard.org/rss-specification#ltimagegtSubelementOfLtchannelgt\">here</a>>.")
    private String image;

    @Specification(description = "The <a href=\"https://www.w3.org/PICS/\">PICS</a> rating for the channel.")
    private String rating;

    @Specification(description = "Specifies a text input box that can be displayed with the channel." +
            "More info <a href=\"https://www.rssboard.org/rss-specification#lttextinputgtSubelementOfLtchannelgt\">here</a>.")
    private String textInput;

    @Specification(description = "A hint for aggregators telling them which hours they can skip ." +
            "This element contains up to 24 <hour> sub-elements whose value is a number between 0 and 23," +
            "representing a time in GMT, when aggregators, if they support the feature ," +
            "may not read the channel on hours listed in the <skipHours> element." +
            "The hour beginning at midnight is hour zero.")
    private String skipHours;

    @Specification(description = "A hint for aggregators telling them which days they can skip ." +
            "This element contains up to seven <day> sub-elements whose value is" +
            "Monday, Tuesday, Wednesday, Thursday, Friday, Saturday or Sunday." +
            "Aggregators may not read the channel during days listed in the <skipDays> element.")
    private String skipDays;

    @Specification(description = "子结点")
    private List<Item> itemList;
}
