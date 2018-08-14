package pl.birek.htmlparser.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import pl.birek.util.web.WebProtocol;
import pl.birek.util.web.WebsitePage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public class SinglePageParsingController {
    private Connection connection;
    private StringBuilder redirectsChain;

    @FXML Text messageBox;
    @FXML ProgressIndicator progressCircle;
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
        messageBox.setText("");
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
            messageBox.setText("Unknown host. Please check website URL.");
            return;
        } catch (HttpStatusException | SocketTimeoutException | MalformedURLException | UnsupportedMimeTypeException e) {
            messageBox.setText("Server response is not OK. Connection timed out, resource type is unsupported, authorization is required or provided URL is malformed. Please try again.");
            return;
        } catch (IOException e) {
            messageBox.setText("Unknown error. Please try again.");
            return;
        }

        websitePage.parseDocument(document);
        fillColumns(websitePage);
        progressCircle.setProgress(1);
    }

    private void handleProtocol() {
        if (isProtocolProvided()) {
            if (!getProtocol().equals(WebProtocol.HTTP) && !getProtocol().equals(WebProtocol.HTTPS))
                changeProtocol(WebProtocol.HTTP);
        }
        else
            setProtocol(WebProtocol.HTTP);
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

    private boolean isProtocolProvided() {
        return urlTextField.getText().contains("://");
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

    // TODO create something to show more than one f.e. meta description, header etc
    // TODO add external linking field
    private void fillColumns(WebsitePage websitePage) {
        titleField.setText(!websitePage.getTitle().equals("") ? websitePage.getTitle() : "<none>");
        langField.setText(!websitePage.getLanguage().equals("") ? websitePage.getLanguage() : "<none>");
        redirectsField.setText(redirectsChain.toString());
        sslField.setText(websitePage.getProtocol().equals(WebProtocol.HTTPS) ? "SSL found" : "SSL not found");
        h1HeadersField.setText(fillHeaders(websitePage.getHeaders("h1")));
        h2HeadersField.setText(fillHeaders(websitePage.getHeaders("h2")));
        h3HeadersField.setText(fillHeaders(websitePage.getHeaders("h3")));
        h4HeadersField.setText(fillHeaders(websitePage.getHeaders("h4")));
        h5HeadersField.setText(fillHeaders(websitePage.getHeaders("h5")));
        h6HeadersField.setText(fillHeaders(websitePage.getHeaders("h6")));

        switch (websitePage.getMetaDescriptions().size()){
            case 0: metaDescriptionsField.setText("<none>"); break;
            case 1: metaDescriptionsField.setText(websitePage.getMetaDescriptions().first().attr("content")); break;
            default: metaDescriptionsField.setText(Integer.toString(websitePage.getMetaDescriptions().size()));
        }

        switch (websitePage.getCanonicals().size()){
            case 0: canonicalsField.setText("<none>"); break;
            case 1: canonicalsField.setText(websitePage.getCanonicals().first().attr("href")); break;
            default: canonicalsField.setText(Integer.toString(websitePage.getCanonicals().size()));
        }

        switch (websitePage.getImages().size()){
            case 1: imagesField.setText(websitePage.getImages().first().attr("src")); break;
            default: imagesField.setText(Integer.toString(websitePage.getImages().size()));
        }

        switch (websitePage.getJavaScriptResources().size()){
            case 1: javascriptField.setText(websitePage.getJavaScriptResources().first().attr("src")); break;
            default: javascriptField.setText(Integer.toString(websitePage.getJavaScriptResources().size()));
        }

        switch (websitePage.getCssStylesheetResources().size()){
            case 1: cssField.setText(websitePage.getCssStylesheetResources().first().attr("href")); break;
            default: cssField.setText(Integer.toString(websitePage.getCssStylesheetResources().size()));
        }
    }

    private String fillHeaders(Elements headers) {
        if (headers.size() == 1)
            return headers.first().text();
        return Integer.toString(headers.size());
    }

    public void enterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            parseButtonAction();
    }
}
