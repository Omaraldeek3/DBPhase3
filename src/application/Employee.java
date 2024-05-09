package application;

import static javafx.stage.Modality.NONE;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class Employee extends Application {

	private ArrayList<InfoEmployee> data;

	private ObservableList<InfoEmployee> dataList;

	private String dbURL;
	private String dbUsername = "root";
	private String dbPassword = "2003";
	private String URL = "127.0.0.1";
	private String port = "3306";
	private String dbName = "phase3";
	private Connection con;
	

	
	public static void main(String[] args) {

		Application.launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		data = new ArrayList<>();

		try {

			getData();

			dataList = FXCollections.observableArrayList(data);

			tableView(stage);
			stage.show();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")

	private void tableView(Stage stage) throws ClassNotFoundException, SQLException {

		TableView<InfoEmployee> myDataTable = new TableView<InfoEmployee>();

		Scene scene = new Scene(new Group());
		stage.setTitle("Employee Table");
		stage.setWidth(600);
		stage.setHeight(500);
		
		Label label = new Label("Employee Table");
		label.setFont(new Font("Arial", 20));
		label.setTextFill(Color.RED);

		myDataTable.setEditable(true);
		myDataTable.setMaxHeight(300);
		myDataTable.setMaxWidth(420);

		TableColumn<InfoEmployee, Integer> emp_idCol = new TableColumn<InfoEmployee, Integer>("emp_id");
		emp_idCol.setMinWidth(50);

		emp_idCol.setCellValueFactory(new PropertyValueFactory<InfoEmployee, Integer>("emp_id"));

		TableColumn<InfoEmployee, String> eNameCol = new TableColumn<>("Name");
		eNameCol.setMinWidth(50);
		eNameCol.setCellValueFactory(cellData -> cellData.getValue().getName());

		TableColumn<InfoEmployee, Integer> branch_idCol = new TableColumn<InfoEmployee, Integer>("branch_id");
		branch_idCol.setMinWidth(50);

		branch_idCol.setCellValueFactory(new PropertyValueFactory<InfoEmployee, Integer>("branch_id"));

		TableColumn<InfoEmployee, Double> salaryCol = new TableColumn<InfoEmployee, Double>("salary");
		salaryCol.setMinWidth(50);
		salaryCol.setCellValueFactory(new PropertyValueFactory<InfoEmployee, Double>("salary"));

		salaryCol.setCellFactory(TextFieldTableCell.<InfoEmployee, Double>forTableColumn(new DoubleStringConverter()));

		TableColumn<InfoEmployee, String> contactInfoCol = new TableColumn<InfoEmployee, String>("contactInfo");
		contactInfoCol.setMinWidth(100);
		contactInfoCol.setCellValueFactory(cellData -> cellData.getValue().getContactInfo());

		TableColumn<InfoEmployee, String> positionCol = new TableColumn<InfoEmployee, String>("position");
		positionCol.setMinWidth(100);
		positionCol.setCellValueFactory(cellData -> cellData.getValue().getPosition());

		myDataTable.setItems(dataList);

		myDataTable.getColumns().addAll(emp_idCol, eNameCol, branch_idCol, salaryCol, contactInfoCol, positionCol);

		final TextField addId = new TextField();
		addId.setPromptText("id");
		addId.setMaxWidth(emp_idCol.getPrefWidth());

		final TextField addName = new TextField();
		addName.setMaxWidth(eNameCol.getPrefWidth());
		addName.setPromptText("name");

		final TextField addBranch = new TextField();
		addBranch.setMaxWidth(branch_idCol.getPrefWidth());
		addBranch.setPromptText("branch");

		final TextField addSalary = new TextField();
		addSalary.setMaxWidth(salaryCol.getPrefWidth());
		addSalary.setPromptText("salary");

		final TextField addContactInfo = new TextField();
		addContactInfo.setMaxWidth(contactInfoCol.getPrefWidth());
		addContactInfo.setPromptText("contactInfo");

		final TextField addPosition = new TextField();
		addPosition.setMaxWidth(contactInfoCol.getPrefWidth());
		addPosition.setPromptText("position");

		final Button addButton = new Button("Add");
		addButton.setOnAction((ActionEvent e) -> {
			InfoEmployee rc;
			rc = new InfoEmployee(Integer.valueOf(addId.getText()), addName.getText(), Integer.valueOf(addBranch.getText()),
					Double.valueOf(addSalary.getText()), addContactInfo.getText(), addPosition.getText());
			dataList.add(rc);
			insertData(rc);
			addId.clear();
			addSalary.clear();
			addName.clear();
			addContactInfo.clear();
			addBranch.clear();
			addPosition.clear();

		});

		final HBox hb = new HBox();

		final Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((ActionEvent e) -> {
			ObservableList<InfoEmployee> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<InfoEmployee> rows = new ArrayList<>(selectedRows);
			rows.forEach(row -> {
				myDataTable.getItems().remove(row);
				deleteRow(row);
				myDataTable.refresh();
			});
		});

		hb.getChildren().addAll(addId, addSalary, addName, addContactInfo, addBranch, addPosition);
		hb.setSpacing(3);

		final Button refreshButton = new Button("Refresh");
		refreshButton.setOnAction((ActionEvent e) -> {
			myDataTable.refresh();
		});

		final Button clearButton = new Button("Clear All");
		clearButton.setOnAction((ActionEvent e) -> {
			showDialog(stage, NONE, myDataTable);

		});

		final HBox hb2 = new HBox();
		hb2.getChildren().addAll( clearButton,  addButton, deleteButton);
		hb2.setAlignment(Pos.CENTER_RIGHT);
		hb2.setSpacing(5);
		hb2.setAlignment(Pos.CENTER);

		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 5, 5, 10));
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(label, myDataTable, hb, hb2);
		((Group) scene.getRoot()).getChildren().addAll(vbox);
		stage.setScene(scene);

	}

	private void insertData(InfoEmployee rc) {

		try {
			System.out.println("Insert into Employee (emp_id, ename, branch_id, salary, contactInfo, position) values(" + rc.getEmp_id() + ",'"
					+ rc.getName().getValue() + "'," + rc.getBranch_id() + "," + rc.getSalary() + ",'" + rc.getContactInfo().getValue() + "','" + rc.getPosition().getValue() +"')");

			connectDB();
			ExecuteStatement("INSERT INTO Employee values (" + rc.getEmp_id() + ",'"
					+ rc.getName().getValue() + "'," + rc.getBranch_id() + "," + rc.getSalary() + ",'" + rc.getContactInfo().getValue() + "','" + rc.getPosition().getValue() +"')");
			con.close();
			System.out.println("Connection closed  " + data.size());

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void getData() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select emp_id, name, branch_id, salary, contactInfo, position  from Employee order by emp_id";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		while (rs.next()) {
			data.add(new InfoEmployee(Integer.parseInt(rs.getString(1)), rs.getString(2), Integer.parseInt(rs.getString(3)),
					Double.parseDouble(rs.getString(4)), rs.getString(5),rs.getString(6)));
			
		}

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed  " + data.size());

	}
	private double getTotalSalary() throws SQLException, ClassNotFoundException {
		double totalSalary= 0;
		String SQL;

		connectDB();

		SQL = "select salary  from Employee order by id";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		while (rs.next()) {
			totalSalary += Double.parseDouble(rs.getString(1)) ;

		}

		rs.close();
		stmt.close();

		con.close();
		return totalSalary;

	}

	private void connectDB() throws ClassNotFoundException, SQLException {

		dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
		Properties p = new Properties();
		p.setProperty("user", dbUsername);
		p.setProperty("password", dbPassword);
		p.setProperty("useSSL", "false");
		p.setProperty("autoReconnect", "true");
		Class.forName("com.mysql.cj.jdbc.Driver");

		con = DriverManager.getConnection(dbURL, p);

	}

	public void ExecuteStatement(String SQL) throws SQLException {

		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(SQL);
			stmt.close();

		} catch (SQLException s) {
			s.printStackTrace();
			System.out.println("SQL statement is not executed!");

		}

	}

	public void updateName(int id, String name) {

		try {
			System.out.println("update  employee set name = '" + name + "' where emp_id = " + id);
			connectDB();
			ExecuteStatement("update  employee set name = '" + name + "' where emp_id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateSalary(int id, double salary) {

		try {
			System.out.println("update  employee set salary = " + salary + " where emp_id = " + id);
			connectDB();
			ExecuteStatement("update  employee set salary = " + salary + " where emp_id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateContactInfo(int id, String contactInfo) {

		try {
			System.out.println("update  employee set contactInfo = '" + contactInfo + "' where emp_id = " + id);
			connectDB();
			ExecuteStatement("update  employee set contactInfo = '" + contactInfo + "' where emp_id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateBranchId(int id, String branch_id) {

		try {
			System.out.println("update  employee set branch_id = '" + branch_id + "' where emp_id = " + id);
			connectDB();
			ExecuteStatement("update  employee set branch_id = '" + branch_id + "' where emp_id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void updatePosition(int id, String position) {

		try {
			System.out.println("update  employee set position = '" + position + "' where emp_id = " + id);
			connectDB();
			ExecuteStatement("update  employee set position = '" + position + "' where emp_id = " + id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void deleteRow(InfoEmployee row) {
		// TODO Auto-generated method stub

		try {
			System.out.println("delete from  Employee where emp_id=" + row.getEmp_id() + ";");
			connectDB();
			ExecuteStatement("delete from  Employee where emp_id=" + row.getEmp_id() + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void showDialog(Window owner, Modality modality, TableView<InfoEmployee> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (InfoEmployee row : dataList) {
				deleteRow(row);
				table.refresh();
			}
			table.getItems().removeAll(dataList);
			stage.close();

		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(20, 15, 15, 2));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 250, 140);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}
}