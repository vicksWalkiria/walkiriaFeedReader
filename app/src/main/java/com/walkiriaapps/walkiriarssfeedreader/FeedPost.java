package com.walkiriaapps.walkiriarssfeedreader;

public class FeedPost {
    String title;
    String content;
    String link;
    String category;

    public FeedPost(String title, String content, String link, String category)
    {
        this.title = title;
        this.content = content;
        this.link = link;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public String getCategory() {
        return category;
    }

}
