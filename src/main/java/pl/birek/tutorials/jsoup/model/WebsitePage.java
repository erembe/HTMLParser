package pl.birek.tutorials.jsoup.model;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

public class WebsitePage {
    private final URL url;
    private String title;
    private Elements metaDescriptions;
    private Elements headers;
    private Elements images;
    private Elements javaScriptResources;
    private Elements cssStylesheetResources;

    public WebsitePage(URL url) {
        this.url = url;
        this.title = "";
        this.metaDescriptions = new Elements();
        this.headers = new Elements();
        this.images = new Elements();
        this.javaScriptResources = new Elements();
        this.cssStylesheetResources = new Elements();
    }

    // TITLE
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // META DESCRIPTIONS
    public Elements getMetaDescriptions() {
        return metaDescriptions;
    }

    public void parseMetaDescriptions(Document doc){
        Elements meta = doc.select("meta");

        for (Element elem : meta)
            if (elem.attr("name").toLowerCase().equals("description"))
                metaDescriptions.add(elem);
    }

    // HEADERS
    public Elements getHeaders() {
        return headers;
    }

    public void parseHeaders(Document doc) {
        headers = doc.select("h1");
    }

    // IMAGES
    public Elements getImages() {
        return images;
    }

    public void parseImages(Document doc) {
        images = doc.select("img");
    }

    // JAVASCRIPT RESOURCES
    public Elements getJavaScriptResources() {
        return javaScriptResources;
    }

    public void parseJavaScriptResource(Document doc) {
        Elements scripts = doc.select("script");

        for (Element elem : scripts)
            if (elem.attr("type").toLowerCase().equals("text/javascript"))
                javaScriptResources.add(elem);
    }

    // CSS RESOURCES
    public Elements getCssStylesheetResources() {
        return cssStylesheetResources;
    }

    public void parseCssScriptResource(Document doc) {
        Elements links = doc.select("link");

        for (Element elem : links)
            if (elem.attr("rel").toLowerCase().equals("stylesheet"))
                cssStylesheetResources.add(elem);
    }
}
