package application;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class UpdateDataTableController implements Initializable{
	@FXML private ComboBox<String> cbdept;
	@FXML private ComboBox<Integer> cbyear;
	@FXML private Label lblstatus;
	@FXML private Pane updatepane;
	@FXML private Button btnselect;
	@FXML private TableView<ObservableList<String>> tableview;
	private Pane root;
	private ObservableList<String> dept = FXCollections.observableArrayList("Computer","Mechanical","Civil","Electrical","I.T","E.C","Chemical");
	private ObservableList<Integer> year = FXCollections.observableArrayList(1,2,3,4);
	private ObservableList<ObservableList<String>> data;
	MysqlModel mysqlmodel = new MysqlModel();
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	String query1 = "SELECT * FROM sms.student_details where department=? and year=?";
	String query2 = "select id from student_details where username=?";
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbdept.setItems(dept);
		cbyear.setItems(year);
	}
	
	private boolean isEmpty() {
		if(cbdept.getValue()==null)
		{
			cbdept.requestFocus();
			lblstatus.setText("Please Enter all Information!!!");
			lblstatus.setTextFill(Color.RED);
			return true;
		}else if(cbyear.getValue()==null)
		{
			cbyear.requestFocus();
			lblstatus.setText("Please Enter all Information!!!");
			lblstatus.setTextFill(Color.RED);
			return true;
		}
		return false;
	}
	
	public void Search(ActionEvent event){
		tableview.getColumns().clear();
		if(!isEmpty())
		{
			data = FXCollections.observableArrayList();
			try {
				
				preparedStatement  = mysqlmodel.connection.prepareStatement(query1);
				preparedStatement.setString(1, cbdept.getValue().toString());
				preparedStatement.setString(2, cbyear.getValue().toString());
				resultSet = preparedStatement.executeQuery();
				
				for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
					final int j = i;
					TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
					col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

						@Override
						public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
							return new SimpleStringProperty(param.getValue().get(j).toString());
						}
					});
					col.setPrefWidth(90.1);
					col.setResizable(false);
					tableview.getColumns().addAll(col);
				}
					updatepane.getChildren().remove(root);
					if(tableview.isVisible() == false && btnselect.isVisible() == false )
					{
						tableview.setVisible(true);
						btnselect.setVisible(true);
					}
				while(resultSet.next())
				{
					ObservableList<String> row = FXCollections.observableArrayList();
					for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
						row.add(resultSet.getString(i));
					}
					data.add(row);
				}
				tableview.setItems(data);
				if(tableview.getItems().isEmpty()) {
					lblstatus.setText("No Such Entry Exists!!!");
					lblstatus.setTextFill(Color.RED);
				}else {
					lblstatus.setText("Select Entry From Below Table to Update Data");
					lblstatus.setTextFill(Color.GREEN);
				}
				} catch (Exception e) {
				lblstatus.setText("No Such Entry Exists!!!");
				lblstatus.setTextFill(Color.RED);
				e.printStackTrace();
				}
		}
	}
	
	public void Select(ActionEvent event) throws IOException {
		if(tableview.getSelectionModel().getSelectedItem()==null)
		{
			if(tableview.getItems().isEmpty())
			{
					if(cbdept.getValue() == null)
						cbdept.requestFocus();
					else
						cbyear.requestFocus();
					lblstatus.setText("Please Enter all Information!!!");
					lblstatus.setTextFill(Color.RED);
			}
			else
			{
				lblstatus.setText("Please Select a Entry to Update it's Data");
				lblstatus.setTextFill(Color.RED);
			}
		}
		else
		{
			try {
				String uname = tableview.getSelectionModel().getSelectedItem().get(9);
				tableview.setVisible(false);
				btnselect.setVisible(false);
				FXMLLoader loader = new FXMLLoader();
				root = loader.load(getClass().getResource("/application/UpdateData.fxml").openStream());
				UpdateDataController updatedata = (UpdateDataController)loader.getController();
				preparedStatement = mysqlmodel.connection.prepareStatement(query2);
				preparedStatement.setString(1, uname);
				resultSet = preparedStatement.executeQuery();
				while(resultSet.next())
				{
					updatedata.id = Integer.parseInt(resultSet.getString("id"));
				}
				updatedata.getData();
				updatepane.getChildren().add(root);
				lblstatus.setText("");

			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		}	
	}
	
