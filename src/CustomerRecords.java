import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import java.awt.Font;

public class CustomerRecords {
	public int index = -1;
	//public static Customer selected;

	public CustomerRecords() {

		// Create and define JFrame attributes 
		JFrame cusGUI = new JFrame("E-Store Prototype");
		cusGUI.getContentPane().setBackground(new Color(255, 255, 255));
		cusGUI.setResizable(false);
		cusGUI.setSize(733, 510);
		cusGUI.setLocation(300, 200);
		cusGUI.getContentPane().setLayout(null);
		
		// JLabel to provide user with feedback
		JLabel lblUpdate = new JLabel("");
		lblUpdate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblUpdate.setForeground(Color.RED);
		lblUpdate.setBounds(241, 376, 474, 16);
		cusGUI.getContentPane().add(lblUpdate);

		// Create JList to hold database information and set model
		JList<Customer> customerList = new JList<Customer>();															
		customerList.setModel(Query()); // Call Query() method to get model from database
		
		// JList selection listener. Sets index, selected item, and clears user feedback label
		ListSelectionListener listSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					index = customerList.getSelectedValue().getAccount(); // Set selected primary key as the index
				//	selected = customerList.getSelectedValue(); 
					lblUpdate.setText("");
				}
			}
		};
		customerList.addListSelectionListener(listSelectionListener); // Add listener to JList
		
		// Create scroll pane and populate with JList items
		JScrollPane pane = new JScrollPane(customerList);
		pane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		pane.setBackground(Color.WHITE);
		pane.setBounds(58, 56, 597, 182);
		cusGUI.getContentPane().add(pane);

		// Approve Finance Button. Update database item, refresh the JList, and update user feedback label
		// If an item has been selected call approve method, update JList and user feedback label 
		JButton appBtn = new JButton("Approve"); 
		appBtn.setBackground(new Color(178, 34, 34));
		appBtn.setForeground(new Color(255, 255, 255));
		appBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				if (!customerList.isSelectionEmpty()) { 
					Approve();
					customerList.setModel(Query()); // Refresh JList
					lblUpdate.setText("Customer Approved");
				} else {
					lblUpdate.setText("Please select a customer for aprroval");
				}
			}
		});
		appBtn.setBounds(249, 297, 97, 25);
		cusGUI.getContentPane().add(appBtn);

		// Decline Finance Button
		JButton decBtn = new JButton("Decline"); // Add products to database
		decBtn.setBackground(new Color(178, 34, 34));
		decBtn.setForeground(new Color(255, 255, 255));
		decBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!customerList.isSelectionEmpty()) {
					Decline();
					customerList.setModel(Query()); // Refresh JList
					lblUpdate.setText("Customer Declined");
				} else {
					lblUpdate.setText("Please select a customer to decline");
				}
			}
		});
		decBtn.setBounds(375, 297, 97, 25);
		cusGUI.getContentPane().add(decBtn);

		// Cancel button, dispose current GUI and reopen the previous
		JButton cancelBtn = new JButton("Close");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cusGUI.dispose();
				new PriceGUI();
			}
		});

		cusGUI.setVisible(true);
	}

	// Query method creates a default list model that can be called to populate JList with database items
	public DefaultListModel<Customer> Query() {
		DefaultListModel<Customer> customerModel = new DefaultListModel<Customer>(); 

		// Database functionalities e.g. connection and database queries
		try {
			// Load the driver and connect to the database
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager
					.getConnection("jdb" + "c:mysql://dbinstance.cy0v0cdt3tqa.eu-west-1.rds.amazonaws.com:3306/customers?user=usersb&password=password");
			Statement statement = conn.createStatement();

			// SQL Query used to select required information from database
			String query = "SELECT * FROM customerrecords";
			ResultSet results = statement.executeQuery(query);

			// Loop through found records and add them to customer model
			while (results.next()) {
				int account = results.getInt("Account");
				String name = results.getString("Name");
				String address = results.getString("Address");
				String email = results.getString("Email");
				String status = results.getString("Status");

				Customer customer = new Customer(account, name, address, email, status);
				customerModel.addElement(customer);
			}

			// Release resources
			statement.close();
			conn.close();
		}
		// Error handling to catch J driver and SQL statement issues
		catch (ClassNotFoundException cnf) {
			System.err.println("Could not load driver");
			System.err.println(cnf.getMessage());
			System.exit(-1);
		} catch (SQLException sqe) {
			System.out.println("Error performing SQL Query");
			System.out.println(sqe.getMessage());
			System.exit(-1);
		}
		return customerModel;

	}

	// Approve method that is called to update selected database item as approved
	public void Approve() {
		try {
			// Load the driver and connect to database
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager
					.getConnection("jdbc:mysql://dbinstance.cy0v0cdt3tqa.eu-west-1.rds.amazonaws.com:3306/customers?user=usersb&password=password");

			// Create a prepared SQL statement to make the results editable
			String approve = "SELECT * FROM customerrecords WHERE Account =" + index; 
			PreparedStatement statement = conn.prepareStatement(approve, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet results = statement.executeQuery(approve);

			// Loop through database and find the id then update the relevant row, else print error
			if (results.next()) {
				results.first();
				results.updateString("Status", "Approved");
				results.updateRow();
			} 
			
			// Release resources
			statement.close();
			conn.close();
			
			// Error handling to catch J driver and SQL statement issues
		} catch (ClassNotFoundException cnf) {
			System.err.println("Could not load driver");
		} catch (SQLException sqe) {
			System.err.println("Error in SQL Update");
		}
	}

	public void Decline() {
		try {
			// Load the driver and connect to database
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager
					.getConnection("jdbc:mysql://dbinstance.cy0v0cdt3tqa.eu-west-1.rds.amazonaws.com:3306/customers?user=usersb&password=password");

			// Create a prepared statement to make the results editable
			String approve = "SELECT * FROM customerrecords WHERE Account =" + index;
			PreparedStatement statement = conn.prepareStatement(approve, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet results = statement.executeQuery(approve);

			// Loop through database and find the id then update the relevant row
			if (results.next()) {
				results.first();
				results.updateString("Status", "Declined");
				results.updateRow();
				System.out.println("Record updated");
			} else {
				System.out.println("Record does not exist");
			}

			// Release resources
			statement.close();
			conn.close();
			
			// Error handling to catch J driver and SQL statement issues
		} catch (ClassNotFoundException cnf) {
			System.err.println("Could not load driver");
			System.err.println(cnf.getMessage());
			System.exit(-1);
		} catch (SQLException sqe) {
			System.err.println("Error in SQL Update");
			System.err.println(sqe.getMessage());
			System.exit(-1);
		}
	}
}
