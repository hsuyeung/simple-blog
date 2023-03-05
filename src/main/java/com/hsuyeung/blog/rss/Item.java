package com.hsuyeung.blog.rss;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RSS 2.0 item 结点定义
 *
 * <p>
 * 参考链接：
 * <a href="https://www.rssboard.org/rss-specification">RSS 2.0 Specification</a>
 * </p>
 * <p>
 * A channel may contain any number of < item >s.
 * An item may represent a "story" -- much like a story in a newspaper or magazine;
 * if so its description is a synopsis of the story, and the link points to the full story.
 * An item may also be complete in itself,
 * if so, the description contains the text (entity-encoded HTML is allowed ; see examples),
 * and the link and title may be omitted . All elements of an item are optional ,
 * however at least one of title or description must be present.
 *
 * @author hsuyeung
 * @date 2023/03/04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Serializable {
    private static final long serialVersionUID = -1036495625800522431L;

    @Specification(description = "The title of the item .")
    private String title;

    @Specification(description = "The URL of the item .")
    private String link;

    @Specification(description = "The item synopsis.")
    private String description;

    @Specification(description = "Email address of the author of the item ." +
            "<a href=\"https://www.rssboard.org/rss-specification#ltauthorgtSubelementOfLtitemgt\">More</a>.")
    private String author;

    @Specification(description = "Includes the item in one or more categories ." +
            "<a href=\"https://www.rssboard.org/rss-specification#ltcategorygtSubelementOfLtitemgt\">More</a>.")
    private String category;

    @Specification(description = "URL of a page for comments relating to the item ." +
            "<a href=\"https://www.rssboard.org/rss-specification#ltcommentsgtSubelementOfLtitemgt\">More</a>.")
    private String comments;

    @Specification(description = "Describes a media object that is attached to the item ." +
            "<a href=\"https://www.rssboard.org/rss-specification#ltenclosuregtSubelementOfLtitemgt\">More</a>.")
    private String enclosure;

    @Specification(description = "A string that uniquely identifies the item ." +
            "<a href=\"https://www.rssboard.org/rss-specification#ltguidgtSubelementOfLtitemgt\">More</a>.")
    private String guid;

    @Specification(description = "Indicates when the item was published ." +
            "<a href=\"https://www.rssboard.org/rss-specification#ltpubdategtSubelementOfLtitemgt\">More</a>.")
    private String pubDate;

    @Specification(description = "The RSS channel that the item came from." +
            "<a href=\"https://www.rssboard.org/rss-specification#ltsourcegtSubelementOfLtitemgt\">More</a>.")
    private Source source;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Source {
        @Specification(description = "source 标签的 url 属性值")
        private String url;

        @Specification(description = "source 标签之间的内容")
        private String value;
    }
}
