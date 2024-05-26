package application;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class StudentDashboardController implements Initializable{
	
	@FXML private AnchorPane outeranchorpane;
	@FXML public AnchorPane ineranchorpane;
	@FXML private VBox vboxmenu;
	@FXML private Label lblteacher;
	@FXML private Button btninsert;
	@FXML private Button btnupdate;
	@FXML private Button btndelete;
	@FXML private Button btnteacher;
	@FXML private Label lblstudentdash;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void InsertData(ActionEvent event) throws IOException {
		ineranchorpane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/application/InsertData.fxml").openStream());
		ineranchorpane.getChildren().add(root);
		ineranchorpane.requestFocus();
	}
	
	public void UpdateData(ActionEvent event) throws IOException {
		ineranchorpane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/application/UpdateDataTable.fxml").openStream());
		ineranchorpane.getChildren().add(root);
		ineranchorpane.requestFocus();	
	}
	
	public void DeleteData(ActionEvent event) throws IOException {
		ineranchorpane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/application/DeleteDataTable.fxml").openStream());
		ineranchorpane.getChildren().add(root);
		ineranchorpane.requestFocus();
	}
	
	public void TeacherData(ActionEvent event) throws IOException {
		ineranchorpane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/application/Teacher_Data.fxml").openStream());
		ineranchorpane.getChildren().add(root);
		ineranchorpane.requestFocus();
	}
	
	public void getName(String name) {
		lblteacher.setText("Teacher Name :"+name);
	}
	
	public void clearPane() {
		ineranchorpane.getChildren().clear();
		ineranchorpane.getChildren().addAll(lblstudentdash,lblteacher);
	}
}