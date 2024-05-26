package application;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class Teacher_DataController implements Initializable{
	@FXML
	private TableView<ObservableList<String>> tableview;
	MysqlModel mysqlmodel = new MysqlModel();
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	private ObservableList<ObservableList<String>> data;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String query = "select * from teacher_details";
		data = FXCollections.observableArrayList();
		try {
			preparedStatement = mysqlmodel.connection.prepareStatement(query);
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
				col.setPrefWidth(110.5);
				col.setResizable(false);
//				col.setStyle("-fx-text-alignment: center");
				tableview.getColumns().addAll(col);
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
			
		} catch (Exception e) {
			System.out.println("Error in building Data");
		} finally {
			try {
				preparedStatement.close();
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
