package pl.birek.tutorials.jsoup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
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
//            primaryStage.setMaxWidth(700.0);
            primaryStage.setMinHeight(280.0);
//            primaryStage.setMaxHeight(280.0);
//            primaryStage.resizableProperty().setValue(false);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        catch (Exception exception){
            System.err.println("Error occured during attempt to load FXML resource.");
            exception.printStackTrace();
            System.exit(-1);
        }
    }
}
