package com.feedreader.rssaggregator.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedMessage implements Comparable{

    private static final DateFormat PUBDATE_FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

    private String title;
    private String description;
    private String link;
    private String author;
    private String guid;
    private Date pubDate;
    private boolean isSentinel;

    public FeedMessage() {
        this.isSentinel = false;
    }

    public FeedMessage(String title, String description, String link, String author, Date pubDate, String guid) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
        this.pubDate = pubDate;
        this.guid = guid;
        this.isSentinel = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) throws ParseException {
        this.pubDate = PUBDATE_FORMATTER.parse(pubDate);
    }

    @Override
    public String toString() {
        return "FeedMessage [title=" + title + ", description=" + description
                + ", link=" + link + ", author=" + author + ", guid=" + guid
                + "]";
    }

    @Override
    public int compareTo(Object o) {
        FeedMessage message = (FeedMessage)o;
        if (this.equals(message))
            return 0;
        else{
            int compareStatus = this.getPubDate().compareTo(message.getPubDate());
            if(compareStatus == 0){
                return -1;
            }else{
                return compareStatus * (-1);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        FeedMessage message = (FeedMessage)obj;
        if(!this.guid.equals(message.getGuid())){
            return false;
        }else if(!this.link.equals(message.getLink())){
            return false;
        }else if(!this.title.equals(message.getTitle())){
            return false;
        }else if(!this.description.equals(message.getDescription())){
            return false;
        }else{
            return true;
        }
    }

    public boolean isSentinel() {
        return isSentinel;
    }

    public void setSentinel(boolean sentinel) {
        isSentinel = sentinel;
    }
}