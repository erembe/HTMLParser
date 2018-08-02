package pl.birek.tutorials.jsoup.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pl.birek.tutorials.jsoup.model.WebsitePage;

import java.io.IOException;
import java.net.URL;


public class SinglePageParsingController {
    private WebsitePage websitePage;

    @FXML TextField urlTextField;
    @FXML TextField titleField;
    @FXML TextField metaDescriptionsField;
    @FXML TextField h1HeadersField;
    @FXML TextField imagesField;
    @FXML TextField javascriptField;
    @FXML TextField cssField;

    public void parseButtonAction() {
        Connection connection;
        Document document = null;

        try {
            websitePage = new WebsitePage(new URL(urlTextField.getText()));
            connection = Jsoup.connect(urlTextField.getText());
        } catch (Exception e) {
            // TODO implement exception handling
            // TODO to implement - show information dialog instead of throwing exception
            // TODO when the exception i caught, the button does not work again
            System.err.println("URL you provided (" + urlTextField.getText() + ") is malformed. Please provide proper URL.\n");
            e.printStackTrace();
            return;
        }

        try {
            document = connection.get();
        } catch (IOException e) {
            // TODO implement exception handling
            e.printStackTrace();
        }

        if (document != null){
            websitePage.setTitle(document.title()); // TODO what in case of multiple <title> tags?
            websitePage.parseMetaDescriptions(document);
            websitePage.parseHeaders(document);
            websitePage.parseImages(document);
            websitePage.parseJavaScriptResource(document);
            websitePage.parseCssScriptResource(document);
        }

        fillColumns(websitePage);
    }

    // TODO createseparate methods for each field
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
