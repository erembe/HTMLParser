package pl.birek.tutorials.jsoup.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import pl.birek.tutorials.jsoup.enums.WebProtocol;
import pl.birek.tutorials.jsoup.model.WebsitePage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;


public class SinglePageParsingController {
    private Document document;
    private URL url;
    private WebsitePage websitePage;

    @FXML TextField urlTextField;
    @FXML TextField titleField;
    @FXML TextField metaDescriptionsField;
    @FXML TextField h1HeadersField;
    @FXML TextField imagesField;
    @FXML TextField javascriptField;
    @FXML TextField cssField;

    // TODO create enums protocols?

    public void parseButtonAction() {
        if (urlTextField.getText().equals(""))
            return;

        handleProtocol();

        try {
            url = new URL(urlTextField.getText());
        } catch (MalformedURLException e) {
            System.err.println("Unknown error. Please try again."); // TODO - dialog message
            return;
        }
        websitePage = new WebsitePage(url);
        Connection connection = Jsoup.connect(websitePage.getUrl().toString()); // TODO - check exceptions?


        try {
            document = connection.get();
        } catch (UnknownHostException | IllegalArgumentException e) {
            System.err.println("Unknown host. Please check website URL."); // TODO - dialog message
            return;
        } catch (HttpStatusException | SocketTimeoutException | MalformedURLException | UnsupportedMimeTypeException e) {
            System.err.println("Server response is not OK. Connection timed out, resource type is unsupported or provided URL is malformed. Please try again."); // TODO - dialog message
            return;
        } catch (IOException e) {
            System.err.println("Unknown error. Please try again."); // TODO - dialog message
            return;
        }

        try {
            handleRedirects(connection);
        } catch (IOException e){
            System.err.println("Error occurred during attempt to request. Please try again."); // TODO - dialog message
            e.printStackTrace();
            return;
        }

        if (document != null){
            websitePage.setTitle(document.title()); // TODO what in case of multiple <title> tags?, empty <title> tag or none
            websitePage.parseMetaDescriptions(document);
            websitePage.parseHeaders(document);
            websitePage.parseImages(document);
            websitePage.parseJavaScriptResource(document);
            websitePage.parseCssScriptResource(document);
            fillColumns(websitePage);
        }
    }

    private void handleProtocol() {
        if (isProtocolProvided()) {
            if (!getProtocol().equals(WebProtocol.HTTP) && !getProtocol().equals(WebProtocol.HTTPS))
                changeProtocol(WebProtocol.HTTP);
        }
        else
            setProtocol(WebProtocol.HTTP);
    }

    private void handleRedirects(Connection connection) throws IOException {
        Connection.Response response;
            response = connection.followRedirects(true).execute();
        if (!websitePage.getUrl().equals(response.url())){
            websitePage.setUrl(response.url());
            urlTextField.setText(websitePage.getUrl().toString());
        }
    }

    private void changeProtocol(WebProtocol protocolToSet) {
        StringBuilder sourceProtocol = new StringBuilder();

        for (int i=0; i< urlTextField.getText().indexOf("://"); i++)
            sourceProtocol.append(urlTextField.getText().charAt(i));

        urlTextField.setText(urlTextField.getText().replace(sourceProtocol, protocolToSet.toString().toLowerCase()));
    }

    private void setProtocol(WebProtocol protocolToSet) {
        urlTextField.setText(protocolToSet.toString().toLowerCase() + "://" + urlTextField.getText());
    }

    private WebProtocol getProtocol() {
        StringBuilder sourceProtocol = new StringBuilder();

        for (int i=0; i< urlTextField.getText().indexOf("://"); i++)
            sourceProtocol.append(urlTextField.getText().charAt(i));

        for (WebProtocol protocol : WebProtocol.values())
            if (WebProtocol.compareToString(protocol, sourceProtocol.toString()))
                return protocol;

        return WebProtocol.UNKNOWN;
    }

    private boolean isProtocolProvided() {
        return urlTextField.getText().contains("://");
    }

    // TODO create separate methods for each field
    // TODO add field about redirection
    // TODO add field about https
    // TODO add other SEO fields ; )
    private void fillColumns(WebsitePage websitePage) {
        titleField.setText(websitePage.getTitle());
        metaDescriptionsField.setText(Integer.toString(websitePage.getMetaDescriptions().size()));
        h1HeadersField.setText(Integer.toString(websitePage.getHeaders().size()));
        imagesField.setText(Integer.toString(websitePage.getImages().size()));
        javascriptField.setText(Integer.toString(websitePage.getJavaScriptResources().size()));
        cssField.setText(Integer.toString(websitePage.getCssStylesheetResources().size()));

        if (websitePage.getMetaDescriptions().size() == 1)
            metaDescriptionsField.setText(websitePage.getMetaDescriptions().first().attr("content"));

        if (websitePage.getHeaders().size() == 1)
            h1HeadersField.setText(websitePage.getHeaders().first().text());

        if (websitePage.getImages().size() == 1)
            imagesField.setText(websitePage.getImages().first().text());

        if (websitePage.getJavaScriptResources().size() == 1)
            javascriptField.setText(websitePage.getJavaScriptResources().first().text());

        if (websitePage.getCssStylesheetResources().size() == 1)
            cssField.setText(websitePage.getCssStylesheetResources().first().text());
    }
}
