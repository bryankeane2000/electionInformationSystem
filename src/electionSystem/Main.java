package electionSystem;

import javafx.application.Application;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.layout.Priority;
        import javafx.scene.layout.VBox;
        import javafx.stage.Stage;

public class Main extends Application {

    /**
     *
     */
    static List<Politician> politicianList = new List<>();
    static List<Election> electionsList = new List<>();





    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("electionSystem.fxml"));
        primaryStage.setTitle("Election Information System");
        primaryStage.setScene(new Scene(root, 700, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
