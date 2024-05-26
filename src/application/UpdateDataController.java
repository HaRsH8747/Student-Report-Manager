package application;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.RadioButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.DatePicker;

public class UpdateDataController implements Initializable{
	@FXML private TextField txtname;
	@FXML private TextField txtsurname;
	@FXML private RadioButton rbmale;
	@FXML private RadioButton rbfemale;
	@FXML private ToggleGroup rbgender;
	@FXML private DatePicker dpdob;
	@FXML private ComboBox<String> cbdept;
	@FXML private ComboBox<Integer> cbyear;
	@FXML private TextField txtphoneno;
	@FXML private Label lblstatus;
	@FXML private TextArea txtaddress;
	@FXML private TextField txtusername;
	@FXML private PasswordField pfpass;
	@FXML private PasswordField pfconfpass;
	public Integer id;
	MysqlModel mysqlmodel = new MysqlModel();
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private String select = "select * from student_details where id = ?";
	private String update = "UPDATE `student_details` SET `name`=?, `surname`=?, `gender`=?, `dob`=?, `department`=?, `year`=?, `phoneno`=?, `address`=?, `username`=?, `password`=? WHERE (`id`=?)";
	ObservableList<String> dept = FXCollections.observableArrayList("Computer","Mechanical","Civil","Electrical","I.T","E.C","Chemical");
	ObservableList<Integer> year = FXCollections.observableArrayList(1,2,3,4);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbdept.setItems(dept);
		cbyear.setItems(year);
	}
	
	public void getData() {
		try {
			preparedStatement = mysqlmodel.connection.prepareStatement(select);
			preparedStatement.setString(1, id.toString());
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next())
			{
				txtname.setText(resultSet.getString("name"));
				txtsurname.setText(resultSet.getString("surname"));
				if(resultSet.getString("gender").equals("Male")) {
					rbmale.setSelected(true);
					
				}else {
					rbfemale.setSelected(true);
				}
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate date = LocalDate.parse(resultSet.getString("dob"), formatter);
				dpdob.setValue(date);
				cbdept.setValue(resultSet.getString("department"));
				cbyear.setValue(Integer.parseInt(resultSet.getString("year")));
				txtphoneno.setText(resultSet.getString("phoneno"));
				txtaddress.setText(resultSet.getString("address"));
				txtusername.setText(resultSet.getString("username"));
				pfpass.setText(resultSet.getString("password"));
				pfconfpass.setText(resultSet.getString("password"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 	
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
	
	public boolean isConfirmed() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("CONFIRMATION");
		alert.setHeaderText("Data Was Changed!!");
		alert.setContentText("Do You Really Want to Update Selected Data");
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
	
	public void Update() throws InterruptedException {		
		String gender = RadioSelect();
		if(!pfpass.getText().equals(pfconfpass.getText()) || IsEmpty())
		{
			if(IsEmpty())
			{
				lblstatus.setText("Please Enter all Information!!!");
				lblstatus.setTextFill(Color.RED);
			}else {
				lblstatus.setText("Password Mismatch!!");
				lblstatus.setTextFill(Color.RED);
			}
		}else
		{
			try {
				preparedStatement = mysqlmodel.connection.prepareStatement(update);
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
				preparedStatement.setString(11, id.toString());
				if(isConfirmed())
				{
					preparedStatement.executeUpdate();				
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("UPDATION SUCCESSFUL");
					alert.setHeaderText("Data Updated Successfully");
					alert.show();
					ClearAll();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
}
