package application;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainController implements Initializable{
	
	@FXML private TextField adminusername;
	@FXML private PasswordField adminpassword;
	@FXML private Label lblstatus;
	@FXML public AnchorPane anchorpane;
	public MysqlModel mysqlmodel = new MysqlModel();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		adminpassword.requestFocus();
	}
	
	private boolean isEmpty() {
		if(adminusername.getText().isEmpty())
		{
			adminusername.requestFocus();
			lblstatus.setText("Please Enter all Information!!");
			lblstatus.setTextFill(Color.RED);
			return true;
		}else if(adminpassword.getText().isEmpty())
		{
			adminpassword.requestFocus();
			lblstatus.setText("Please Enter all Information!!");
			lblstatus.setTextFill(Color.RED);
			return true;
		}
		return false;
	}
	
	public void btnadmin(ActionEvent event) {
		if(!isEmpty())
		{
			try {
				if(mysqlmodel.isAdmin(adminusername.getText(), adminpassword.getText()))
				{
					((Node)event.getSource()).getScene().getWindow().hide();
					Stage primaryStage = new Stage();
					FXMLLoader loader = new FXMLLoader();
					Pane root = loader.load(getClass().getResource("/application/Login.fxml").openStream());
					LoginController login = (LoginController)loader.getController();
					Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("/css/Loginapplication.css").toExternalForm());
					primaryStage.setResizable(false);
					primaryStage.setTitle("Login Page");
					primaryStage.setScene(scene);
					primaryStage.show();
					login.anchorpane.requestFocus();
				}else
				{
					lblstatus.setText("Invalid Username or Password");
					lblstatus.setTextFill(Color.RED);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		}
}
