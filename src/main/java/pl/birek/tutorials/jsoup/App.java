package pl.birek.tutorials.jsoup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// TODO - implement JavaDoc annotations
// TODO - implement JUnit tests
public class App extends Application
{
    public static void main( String[] args )
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource("gui/single-page-parsing.fxml"));
            primaryStage.setTitle("Homepage - HTML Parser");
            primaryStage.setMinWidth(600.0);
            primaryStage.setMinHeight(280.0);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        catch (Exception exception){
            // TODO implement error handling
            System.err.println("Error occured during attempt to load FXML resource.");
            exception.printStackTrace();
            System.exit(-1);
        }
    }
}
