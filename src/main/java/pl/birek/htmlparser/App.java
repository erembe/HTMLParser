package pl.birek.htmlparser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Paths;

// TODO - implement JavaDoc annotations
// TODO - implement JUnit tests
// TODO - move WebsitePage & WebProtocol to another package
public class App extends Application
{
    public static void main( String[] args )
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root;
        URL url = null;

        try {
            url = Paths.get("src/main/resources/fxml/single-page-parsing.fxml").toUri().toURL();
            root = FXMLLoader.load(url);

            primaryStage.setTitle("Homepage - HTML Parser");
            primaryStage.setMinWidth(840.0);
            primaryStage.setMinHeight(550.0);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        catch (Exception exception){
            System.err.println("Error occured during attempt to load FXML resource. URL = " + url);
            exception.printStackTrace();
            System.exit(-1);
        }
    }
}
