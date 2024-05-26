package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.PasswordField;
import javafx.scene.control.DatePicker;

public class InsertDataController implements Initializable{
	@FXML private TextField txtname;
	@FXML private TextField txtsurname;
	@FXML private RadioButton rbmale;
	@FXML private ToggleGroup rbgender;
	@FXML private RadioButton rbfemale;
	@FXML private DatePicker dpdob;
	@FXML private ComboBox<String> cbdept;
	@FXML private TextField txtphoneno;
	@FXML private TextArea txtaddress;
	@FXML private Label lblstatus;
	@FXML private TextField txtusername;
	@FXML private PasswordField pfpass;
	@FXML private PasswordField pfconfpass;
	@FXML private ComboBox<Integer> cbyear;
	private String gender;
	MysqlModel mysqlmodel = new MysqlModel();
	PreparedStatement preparedStatement = null;
	ResultSet resultset = null;
	String query = "INSERT INTO `sms`.`student_details` (`name`, `surname`, `gender`, `dob`, `department`, `year`, `phoneno`, `address`, `username`, `password`) VALUES (?,?,?,?,?,?,?,?,?,?)";
	ObservableList<String> dept = FXCollections.observableArrayList("Computer","Mechanical","Civil","Electrical","I.T","E.C","Chemical");
	ObservableList<Integer> year = FXCollections.observableArrayList(1,2,3,4);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbdept.setItems(dept);
		cbyear.setItems(year);
	}	
	
	public String RadioSelect() {
		if(rbmale.isSelected())
			return "Male";
		else
			return "Female";
	}
	
	public boolean IsEmpty() {
		String gender = RadioSelect();
		if(txtname.getText().isEmpty() || txtsurname.getText().isEmpty() || gender=="" || dpdob.getValue()==null || cbdept.getValue()=="" || cbyear.getValue()==null || txtphoneno.getText().isEmpty() || txtaddress.getText().isEmpty() || txtusername.getText().isEmpty() || pfpass.getText().isEmpty() || pfconfpass.getText().isEmpty())
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isAvailable() throws SQLException {
		String query = "select username from student_details";
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
		
	
	public void ClearAll() {
		txtname.clear();
		txtsurname.clear();
		rbmale.setSelected(false);
		rbfemale.setSelected(false);
		dpdob.setValue(null);
		cbdept.setValue(null);
		cbyear.setValue(null);
		txtphoneno.clear();
		txtaddress.clear();
		txtusername.clear();
		pfpass.clear();
		pfconfpass.clear();		
	}
	
	public void Clear(ActionEvent event) {
		ClearAll();
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
						preparedStatement.setString(4, dpdob.getValue().toString());
						preparedStatement.setString(5, cbdept.getValue());
						preparedStatement.setString(6, cbyear.getValue().toString());
						preparedStatement.setString(7, txtphoneno.getText());
						preparedStatement.setString(8, txtaddress.getText());
						preparedStatement.setString(9, txtusername.getText());
						preparedStatement.setString(10, pfpass.getText());
						if(isConfirmed())
						{
							preparedStatement.executeUpdate();
							ClearAll();
							lblstatus.setText("");
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setTitle("INSERTION SUCCESSFULL");
							alert.setHeaderText("Data Inserted Successfully");
							alert.show();
							txtname.requestFocus();
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
		}else
		{
			lblstatus.setText("Please Enter all Infomation!!!");
			lblstatus.setTextFill(Color.RED);
		}
		}
}

