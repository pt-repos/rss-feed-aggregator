package com.feedreader.rssaggregator.util;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * OPMLParser which parses the opml files
 */
public class OPMLParser implements Runnable {
    private static final String TITLE = "title";
    private static final String HTMLURL = "htmlUrl";
    private static final String XMLURL = "xmlUrl";
    private static final String TEXT = "text";
    private static final String TYPE = "type";
    private final URL opmlUrl;
    ConcurrentSkipListSet<String> feedsSet;

    /**
     * Constructor
     * @param opmlUrl The opmlurl to parse
     * @param feedsSet The list in which to aggregated the items processed from this url
     */
    public OPMLParser(String opmlUrl, ConcurrentSkipListSet<String> feedsSet) {
        this.feedsSet = feedsSet;
        try {
            this.opmlUrl = new URL(opmlUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method which parses the url
     * @return The aggregated list which contain a set of urls
     */
    public ConcurrentSkipListSet<String> parseOPML() {
        try {
            String title = "";
            String htmlUrl = "";
            String xmlUrl = "";
            String text = "";
            String type = "";

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                try {
                    if (event.isStartElement() && event.asStartElement().getName() != null) {
                        String eventName = event.asStartElement().getName().toString();
                        if (eventName.equals("outline")) {
                            Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                            while (attributes.hasNext()) {
                                Attribute attribute = attributes.next();
                                if (attribute.getName() != null) {
                                    String name = attribute.getName().toString();
                                    switch (name) {
                                        case XMLURL:
                                            xmlUrl = attribute.getValue();
                                            break;
                                        default:
                                            break;

                                    }
                                }
                            }
                        }
                    }
                } catch (ClassCastException ce) {
                    continue;
                }
                if (!xmlUrl.isEmpty()) {
                    try {
                        feedsSet.add(xmlUrl);
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // throw new RuntimeException(e);
        }
        return feedsSet;
    }

    /**
     * Open an input stream from a url for reading in the elements
     * @return InputStream for url from which to process
     */
    private InputStream read() {
        try {
            return opmlUrl.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        parseOPML();
    }
}
