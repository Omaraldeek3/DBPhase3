package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InfoEmployee {
	private int emp_id;
	private SimpleStringProperty name;
	private int branch_id;
	private double salary;
	private SimpleStringProperty contactInfo;
	private SimpleStringProperty position;

	public InfoEmployee(int emp_id, String name, int branch_id, double salary, String contactInfo, String position) {
		super();
		this.emp_id = emp_id;
		this.name = new SimpleStringProperty(name);
		this.branch_id = branch_id;
		this.salary = salary;
		this.contactInfo = new SimpleStringProperty(contactInfo);
		this.position = new SimpleStringProperty(position);
	}

	public int getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}

	public SimpleStringProperty getName() {
		return name;
	}

	public void setName(SimpleStringProperty name) {
		this.name = name;
	}

	public int getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(int branch_id) {
		this.branch_id = branch_id;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public SimpleStringProperty getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(SimpleStringProperty contactInfo) {
		this.contactInfo = contactInfo;
	}

	public SimpleStringProperty getPosition() {
		return position;
	}

	public void setPosition(SimpleStringProperty position) {
		this.position = position;
	}

}
