package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/application/Main.fxml").openStream());
			MainController main = (MainController)loader.getController();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/Mainapplication.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Admin Page");
			primaryStage.show();
			main.anchorpane.requestFocus();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
