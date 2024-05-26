package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LoginController implements Initializable{
	
	@FXML private TextField teacheruname;
	@FXML private PasswordField teacherpass;
	@FXML private Label lblstatus;
	@FXML public AnchorPane anchorpane;
	MysqlModel mysqlmodel = new MysqlModel();
	private Stage primaryStage1;
	private Stage primaryStage2;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		anchorpane.requestFocus();		
	}
	
	private boolean isEmpty() {
		if(teacheruname.getText().isEmpty())
		{
			teacheruname.requestFocus();
			lblstatus.setText("Please Enter all Information!!");
			lblstatus.setTextFill(Color.RED);
			return true;
		}else if(teacherpass.getText().isEmpty())
		{
			teacherpass.requestFocus();
			lblstatus.setText("Please Enter all Information!!");
			lblstatus.setTextFill(Color.RED);
			return true;
		}
		return false;
	}
	
	public void Login(ActionEvent event) throws IOException, SQLException {
		if(!isEmpty())
		{
			if(mysqlmodel.isTeacher(teacheruname.getText(), teacherpass.getText()))
			{
				if(primaryStage1 == null)
				{
					Screen screen = Screen.getPrimary();
					Rectangle2D bounds = screen.getVisualBounds();
					FXMLLoader loader = new FXMLLoader();
					Pane root = loader.load(getClass().getResource("/application/StudentDashboard.fxml").openStream());
					StudentDashboardController studentdc = (StudentDashboardController)loader.getController();
					studentdc.getName(teacheruname.getText());
					Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("/css/Dashboardapplication.css").toExternalForm());
					primaryStage1 = new Stage();
					primaryStage1.setScene(scene);
					primaryStage1.setX(bounds.getMinX());
					primaryStage1.setY(bounds.getMinY());
					primaryStage1.setWidth(bounds.getWidth());
					primaryStage1.setHeight(bounds.getHeight());
					primaryStage1.setResizable(false);
					primaryStage1.setOnCloseRequest(new EventHandler<WindowEvent>() {
						@Override
						public void handle(WindowEvent event) {
							studentdc.clearPane();
						}
					});
					primaryStage1.setTitle("Student Dashboard");
					primaryStage1.show();
					studentdc.ineranchorpane.requestFocus();
				}
				else if(primaryStage1.isShowing())
					primaryStage1.toFront();
				else
					primaryStage1.show();
				lblstatus.setText("");
			}
			else {
				lblstatus.setText("Invalid Username or Password");
				lblstatus.setTextFill(Color.RED);
			}

		}
		}
	
	public void Signup(ActionEvent event) throws IOException {
		if(primaryStage2 == null)
		{
			Screen screen = Screen.getPrimary();
			Rectangle2D bounds = screen.getVisualBounds();
			primaryStage2 = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root = loader.load(getClass().getResource("/application/Signup.fxml").openStream());
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/Signupapplication.css").toExternalForm());
			primaryStage2.setX(bounds.getMinX());
			primaryStage2.setY(bounds.getMinY());
			primaryStage2.setWidth(bounds.getWidth());
			primaryStage2.setHeight(bounds.getHeight());
			primaryStage2.setResizable(false);
			primaryStage2.setScene(scene);
			primaryStage2.show();
			primaryStage2.setTitle("Signup Page");
		}else if(primaryStage2.isShowing()) {
			primaryStage2.toFront();
		}else {
			primaryStage2.show();
		}
	}
}
