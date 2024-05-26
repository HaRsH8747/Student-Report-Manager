package application;

import java.io.IOException;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SignupController implements Initializable{
	
	@FXML private TextField txtname;
	@FXML private TextField txtsurname;
	@FXML private RadioButton rbmale;
	@FXML private RadioButton rbfemale;
	@FXML private DatePicker dpdoj;
	@FXML private ComboBox<String> cbdept;
	@FXML private TextField txtphoneno;
	@FXML private TextField txtusername;
	@FXML private PasswordField pfpass;
	@FXML private PasswordField pfconfpass;
	@FXML private Label lblstatus;
	private String gender;
	PreparedStatement preparedStatement = null;
	ResultSet resultset = null;
	MysqlModel mysqlmodel = new MysqlModel();
	String query = "INSERT INTO `sms`.`teacher_details` (`name`, `surname`, `gender`, `doj`, `department`, `phoneno`, `username`, `password`) VALUES (?,?,?,?,?,?,?,?)";
	
	ObservableList<String> list = FXCollections.observableArrayList("Computer","Mechanical","Civil","Electrical","I.T","E.C","Chemical"); 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbdept.setItems(list);
	}
	
	public String RadioSelect()
	{
		if(rbmale.isSelected())
			return "Male";
		else
			return "Female";
	}
	
	public boolean IsEmpty() {
		String gender = RadioSelect();
		if(txtname.getText().isEmpty() || txtsurname.getText().isEmpty() || gender=="" || dpdoj.getValue()==null || cbdept.getValue()=="" || txtphoneno.getText().isEmpty() || txtusername.getText().isEmpty() || pfpass.getText().isEmpty() || pfconfpass.getText().isEmpty())
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isAvailable() throws SQLException {
		String query = "select username from teacher_details";
		preparedStatement = mysqlmodel.connection.prepareStatement(query);
		resultset = preparedStatement.executeQuery();
		ArrayList<String> list = new ArrayList<String>();
		while(resultset.next())
		{
			list.add(resultset.getString("username"));
		}
		for (int i = 0; i < list.size(); i++) {
			if(txtusername.getText().equals(list.get(i)))
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("USERNAME INVALID");
				alert.setHeaderText("Enter a Valid Username!!");
				alert.setContentText("Username '"+txtusername.getText()+"' Already Exixts");
				Optional<ButtonType> option = alert.showAndWait();
				
				if(option.get()==null) {
					txtusername.clear();
					txtusername.requestFocus();
					return false;
				}					
				else if(option.get()==ButtonType.OK) {
					txtusername.clear();
					txtusername.requestFocus();
					return false;
				}
				else
					return false;
			}
		}
		return true;
	}
	
	public boolean isConfirmed() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("CONFIRMATION");
		alert.setHeaderText("New Entry!!!");
		alert.setContentText("Do You Really Want to Insert this Data");
		Optional<ButtonType> option = alert.showAndWait();
		
		if(option.get()==null)
			return false;
		else if(option.get()==ButtonType.OK)
			return true;
		else if(option.get()==ButtonType.CANCEL)
			return false;
		else
			return false;
	}
	
	public void ClearAll() {
		txtname.clear();
		txtsurname.clear();
		rbmale.setSelected(false);
		rbfemale.setSelected(false);
		cbdept.setValue(null);
		txtphoneno.clear();
		txtusername.clear();
		pfpass.clear();
		pfconfpass.clear();		
	}

	public void Submit(ActionEvent event) throws SQLException {
		if(!IsEmpty())
		{
			if(isAvailable())
			{
				try {
					if(pfpass.getText().equals(pfconfpass.getText()))
					{
						gender = RadioSelect();
						preparedStatement = mysqlmodel.connection.prepareStatement(query);
						preparedStatement.setString(1, txtname.getText());
						preparedStatement.setString(2, txtsurname.getText());
						preparedStatement.setString(3, gender);
						preparedStatement.setString(4, dpdoj.getValue().toString());
						preparedStatement.setString(5, cbdept.getValue());
						preparedStatement.setString(6, txtphoneno.getText());
						preparedStatement.setString(7, pfpass.getText());
						preparedStatement.setString(8, pfconfpass.getText());
						if(isConfirmed())
						{
							preparedStatement.executeUpdate();
							lblstatus.setText("");
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setTitle("SIGNUP SUCCESSFUL");
							alert.setHeaderText("New Data Added Successfully");
							alert.setContentText("Now You Can Login With this Id:\n"+"Username: "+txtsurname.getText()+"\n Password: "+pfpass.getText());
							alert.show();
							ClearAll();
						}
					}
					else
					{
						Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
						alert.setTitle("PASSWORD MISMATCH");
						alert.setHeaderText("Password Confirmation Failed!!!");
						alert.setContentText("Please Enter Same Password as 'Confirm Password' Field");
						alert.show();
						pfconfpass.clear();
						pfconfpass.requestFocus();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		else
		{
			lblstatus.setText("Please Enter all Information!!!");
			lblstatus.setTextFill(Color.RED);
		}
	}
	
	public void Back(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage  = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/application/Login.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Login Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
