package pl.birek.util.web;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;

public class WebsitePage {
    private URL url;
    private WebProtocol protocol;
    private String title;
    private String language;
    private Elements metaDescriptions;
    private Elements canonicals;
    private Elements headers_h1;
    private Elements headers_h2;
    private Elements headers_h3;
    private Elements headers_h4;
    private Elements headers_h5;
    private Elements headers_h6;
    private Elements images;
    private Elements javaScriptResources;
    private Elements cssStylesheetResources;

    public WebsitePage(String url) throws MalformedURLException {
        this.url = new URL(url);
        this.title = "";
        this.language = "";
        this.metaDescriptions = new Elements();
        this.canonicals = new Elements();
        this.headers_h1 = new Elements();
        this.headers_h2 = new Elements();
        this.headers_h3 = new Elements();
        this.headers_h4 = new Elements();
        this.headers_h5 = new Elements();
        this.headers_h6 = new Elements();
        this.images = new Elements();
        this.javaScriptResources = new Elements();
        this.cssStylesheetResources = new Elements();

        if (WebProtocol.compareToString(WebProtocol.HTTP, this.url.getProtocol()))
            this.protocol = WebProtocol.HTTP;
        else if (WebProtocol.compareToString(WebProtocol.HTTPS, this.url.getProtocol()))
            this.protocol = WebProtocol.HTTPS;
        else
            this.protocol = WebProtocol.UNKNOWN;
    }

    public String getTitle() {
        return title;
    }

    public WebProtocol getProtocol(){
        return protocol;
    }

    public Elements getMetaDescriptions() {
        return metaDescriptions;
    }

    public Elements getCanonicals() {
        return canonicals;
    }

    public Elements getHeaders(String headerLevel) {
        switch (headerLevel.toLowerCase()){
            case "h1": return headers_h1;
            case "h2": return headers_h2;
            case "h3": return headers_h3;
            case "h4": return headers_h4;
            case "h5": return headers_h5;
            case "h6": return headers_h6;
        }
        return null;
    }

    public String getLanguage() {
        return language;
    }

    public Elements getImages() {
        return images;
    }

    public Elements getJavaScriptResources() {
        return javaScriptResources;
    }

    public Elements getCssStylesheetResources() {
        return cssStylesheetResources;
    }

    public void parseDocument(Document document) {
        this.title = document.title();
        parseLanguage(document);
        parseMetaDescriptions(document);
        parseLinkTags(document);
        parseHeaders(document);
        parseImages(document);
        parseJavaScriptResource(document);
    }

    private void parseMetaDescriptions(Document doc){
        Elements meta = doc.select("meta");

        for (Element elem : meta)
            if (elem.attr("name").toLowerCase().equals("description"))
                metaDescriptions.add(elem);
    }
    private void parseLinkTags(Document doc){
        Elements links = doc.select("link");

        for (Element elem : links){
            if (elem.attr("rel").toLowerCase().equals("stylesheet"))
                cssStylesheetResources.add(elem);
            else if (elem.attr("rel").equals("canonical"))
                canonicals.add(elem);
        }
    }
    private void parseHeaders(Document doc) {
        headers_h1 = doc.select("h1");
        headers_h2 = doc.select("h2");
        headers_h3 = doc.select("h3");
        headers_h4 = doc.select("h4");
        headers_h5 = doc.select("h5");
        headers_h6 = doc.select("h6");

    }
    private void parseLanguage(Document doc){
        this.language = doc.select("html").attr("lang");
    }
    private void parseImages(Document doc) {
        images = doc.select("img");
    }
    private void parseJavaScriptResource(Document doc) {
        Elements scripts = doc.select("script");

        for (Element elem : scripts)
            if (elem.attr("type").toLowerCase().equals("text/javascript"))
                javaScriptResources.add(elem);
    }
}