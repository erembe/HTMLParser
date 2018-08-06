package pl.birek.htmlparser.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import pl.birek.htmlparser.enums.WebProtocol;
import pl.birek.htmlparser.model.WebsitePage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public class SinglePageParsingController {
    private Connection connection;
    private StringBuilder redirectsChain;

    @FXML TextField urlTextField;
    @FXML TextField titleField;
    @FXML TextField metaDescriptionsField;
    @FXML TextField canonicalsField;
    @FXML TextField h1HeadersField;
    @FXML TextField h2HeadersField;
    @FXML TextField h3HeadersField;
    @FXML TextField h4HeadersField;
    @FXML TextField h5HeadersField;
    @FXML TextField h6HeadersField;
    @FXML TextField langField;
    @FXML TextField imagesField;
    @FXML TextField javascriptField;
    @FXML TextField cssField;
    @FXML TextField redirectsField;
    @FXML TextField sslField;

    // TODO - add progress indicator

    public void parseButtonAction() {

        // TODO - disable parseButton until anything is written in text field
        if (urlTextField.getText().equals(""))
            return;
        handleProtocol();

        Document document;
        WebsitePage websitePage;

        try {
            handleRedirects();
            document = connection.get();
            websitePage = new WebsitePage(urlTextField.getText());
        } catch (UnknownHostException | IllegalArgumentException e) {
            System.err.println("Unknown host. Please check website URL."); // TODO - dialog message
            return;
        } catch (HttpStatusException | SocketTimeoutException | MalformedURLException | UnsupportedMimeTypeException e) {
            System.err.println("Server response is not OK. Connection timed out, resource type is unsupported, authorization is required or provided URL is malformed. Please try again."); // TODO - dialog message
            return;
        } catch (IOException e) {
            System.err.println("Unknown error. Please try again."); // TODO - dialog message (send logs?)
            return;
        }

        websitePage.parseDocument(document);
        fillColumns(websitePage);
    }

    private void handleProtocol() {
        if (isProtocolProvided()) {
            if (!getProtocol().equals(WebProtocol.HTTP) && !getProtocol().equals(WebProtocol.HTTPS))
                changeProtocol(WebProtocol.HTTP);
        }
        else
            setProtocol(WebProtocol.HTTP);
    }

    private boolean isProtocolProvided() {
        return urlTextField.getText().contains("://");
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


    private void handleRedirects() throws IOException {
        connection = Jsoup.connect(urlTextField.getText());
        Connection.Response response = connection.followRedirects(false).execute();

        String currentUrl = response.url().toString();
        redirectsChain = new StringBuilder(response.statusCode()==200 ? "<none>" : currentUrl);

        while (response.statusCode() != 200){
            currentUrl = response.header("location");
            redirectsChain.append(" > (").append(response.statusCode()).append(") > ").append(currentUrl);
            connection = Jsoup.connect(currentUrl);
            response = connection.followRedirects(false).execute();
        }
        connection = Jsoup.connect(currentUrl);
        response = connection.execute();

        if (!urlTextField.getText().equals(response.url().toString())){
            urlTextField.setText(response.url().toString());
        }
    }

    // TODO create something to show more than one f.e. meta description, header etc
    // TODO add external linking field
    // TODO move to separate methods
    private void fillColumns(WebsitePage websitePage) {
        titleField.setText(!websitePage.getTitle().equals("") ? websitePage.getTitle() : "<none>");
        metaDescriptionsField.setText(Integer.toString(websitePage.getMetaDescriptions().size()));
        canonicalsField.setText(Integer.toString(websitePage.getCanonicals().size()));
        h1HeadersField.setText(Integer.toString(websitePage.getHeaders("h1").size()));
        h2HeadersField.setText(Integer.toString(websitePage.getHeaders("h2").size()));
        h3HeadersField.setText(Integer.toString(websitePage.getHeaders("h3").size()));
        h4HeadersField.setText(Integer.toString(websitePage.getHeaders("h4").size()));
        h5HeadersField.setText(Integer.toString(websitePage.getHeaders("h5").size()));
        h6HeadersField.setText(Integer.toString(websitePage.getHeaders("h6").size()));
        langField.setText(!websitePage.getLanguage().equals("") ? websitePage.getLanguage() : "<none>");
        imagesField.setText(Integer.toString(websitePage.getImages().size()));
        javascriptField.setText(Integer.toString(websitePage.getJavaScriptResources().size()));
        cssField.setText(Integer.toString(websitePage.getCssStylesheetResources().size()));
        redirectsField.setText(redirectsChain.toString());
        sslField.setText(websitePage.getProtocol().equals(WebProtocol.HTTPS) ? "SSL found" : "SSL not found");

        if (websitePage.getMetaDescriptions().size() == 1)
            metaDescriptionsField.setText(websitePage.getMetaDescriptions().first().attr("content"));
        else if (websitePage.getMetaDescriptions().size() == 0)
            metaDescriptionsField.setText("<none>");

        if (websitePage.getCanonicals().size() == 1)
            canonicalsField.setText(websitePage.getCanonicals().first().attr("href"));

        if (websitePage.getHeaders("h1").size() == 1)
            h1HeadersField.setText(websitePage.getHeaders("h1").first().text());

        if (websitePage.getHeaders("h2").size() == 1)
            h2HeadersField.setText(websitePage.getHeaders("h2").first().text());

        if (websitePage.getHeaders("h3").size() == 1)
            h3HeadersField.setText(websitePage.getHeaders("h3").first().text());

        if (websitePage.getHeaders("h4").size() == 1)
            h4HeadersField.setText(websitePage.getHeaders("h4").first().text());

        if (websitePage.getHeaders("h5").size() == 1)
            h5HeadersField.setText(websitePage.getHeaders("h5").first().text());

        if (websitePage.getHeaders("h6").size() == 1)
            h6HeadersField.setText(websitePage.getHeaders("h6").first().text());

        if (websitePage.getImages().size() == 1)
            imagesField.setText(websitePage.getImages().first().text());

        if (websitePage.getJavaScriptResources().size() == 1)
            javascriptField.setText(websitePage.getJavaScriptResources().first().text());

        if (websitePage.getCssStylesheetResources().size() == 1)
            cssField.setText(websitePage.getCssStylesheetResources().first().text());
    }

    public void enterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            parseButtonAction();
    }
}
