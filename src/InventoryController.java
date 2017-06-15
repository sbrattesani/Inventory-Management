import java.awt.Color;
import java.awt.Font;
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class InventoryController {
	public int index;
	public JTextField stockField;
	public JLabel lblError;
	public JList<Product> warningList;

	public InventoryController() {
		// Define JFrame attributes and contents
		JFrame InventoryCon = new JFrame("E-Store Prototype");
		InventoryCon.getContentPane().setBackground(new Color(255, 255, 255));
		InventoryCon.setSize(664, 428);
		InventoryCon.setLocation(500, 500);
		InventoryCon.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Inventory Controller");
		lblNewLabel.setBounds(213, 0, 283, 26);
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 20));
		InventoryCon.getContentPane().add(lblNewLabel);

		JTextField idField = new JTextField();
		idField.setBounds(145, 265, 116, 22);
		idField.setColumns(10);
		idField.setEditable(false);
		InventoryCon.getContentPane().add(idField);

		JTextField nameField = new JTextField();
		nameField.setBounds(273, 265, 116, 22);
		nameField.setColumns(10);
		// nameField.setText(String.valueOf(PriceGUI.selected.getName()));
		nameField.setEditable(false);
		InventoryCon.getContentPane().add(nameField);

		stockField = new JTextField();
		stockField.setBounds(401, 265, 116, 22);
		stockField.setColumns(10);
		// stockField.setText(String.valueOf(PriceGUI.selected.getStock()));
		InventoryCon.getContentPane().add(stockField);

		// Call query method to populate JSCroll with database items
		JList<Product> productList = new JList<Product>();
		productList.setModel(Query()); // Call query to get productmodel
		// Set product index on JList click event
		ListSelectionListener listSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					idField.setText(String.valueOf(productList.getSelectedValue().getID()));
					nameField.setText(String.valueOf(productList.getSelectedValue().getName()));
					stockField.setText(String.valueOf(productList.getSelectedValue().getStock()));
					index = productList.getSelectedValue().getID();
					lblError.setText("");
				}
			}
		};
		productList.addListSelectionListener(listSelectionListener);
		productList.setSelectedIndex(index);
		JScrollPane pane = new JScrollPane(productList); // Allows items to be
															// scrolled through
		pane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		pane.setBackground(Color.WHITE);
		pane.setBounds(20, 40, 450, 182);
		InventoryCon.getContentPane().add(pane);

		// Populate stock warning list
		warningList = new JList<Product>(); // JList that contains products from
											// database
		warningList.setModel(Warning()); // Call alert method to set product
											// model
		warningList.setForeground(Color.RED);
		JScrollPane wpane = new JScrollPane(warningList); // Allows items to be
															// scrolled through
		wpane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		wpane.setBackground(Color.WHITE);
		wpane.setBounds(480, 40, 150, 182);
		InventoryCon.getContentPane().add(wpane);

		JLabel lblProductId = new JLabel("Product ID");
		lblProductId.setBounds(145, 236, 75, 16);
		InventoryCon.getContentPane().add(lblProductId);

		JLabel lblName = new JLabel("Name");
		lblName.setBounds(273, 236, 56, 16);
		InventoryCon.getContentPane().add(lblName);

		JLabel lblStock = new JLabel("Stock");
		lblStock.setBounds(401, 236, 56, 16);
		InventoryCon.getContentPane().add(lblStock);
		
		JLabel lblwarn = new JLabel("Low Stock");
		lblwarn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblwarn.setForeground(Color.RED);
		lblwarn.setBounds(480, 13, 150, 16);
		InventoryCon.getContentPane().add(lblwarn);
		
		// JLabel to display error messages i.e. invalid int, string inputs etc
		lblError = new JLabel();
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setForeground(Color.RED);
		lblError.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblError.setBounds(12, 323, 626, 58);
		InventoryCon.getContentPane().add(lblError);


		JButton orderBtn = new JButton("Order");
		orderBtn .setBackground(new Color(178, 34, 34));
		orderBtn .setForeground(new Color(255, 255, 255));
		orderBtn .setBounds(208, 308, 116, 25);
		orderBtn .addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!(stockField.getText() == null)){
				Edit();
				productList.setModel(Query()); // Refresh productList
				warningList.setModel(Warning());// Refresh warningList
			} else {
				lblError.setText("Error: Stock field can not be empty");
			}
			}
		});
		InventoryCon.getContentPane().add(orderBtn);

		// Close JFrame and reopen priceGUI
		JButton cancelBtn = new JButton("Close");
		cancelBtn.setBackground(new Color(178, 34, 34));
		cancelBtn.setForeground(new Color(255, 255, 255));
		cancelBtn.setBounds(336, 308, 116, 25);
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Alert(); // Warning message trigger on close
				InventoryCon.dispose();
			}
		});
		InventoryCon.getContentPane().add(cancelBtn);
		
		InventoryCon.setVisible(true);
	}

	public void Edit() {
		try {
			// Load the driver and connect to database
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager
					.getConnection("jdbc:mysql://dbinstance.cy0v0cdt3tqa.eu-west-1.rds.amazonaws.com:3306/products?user=usersb&password=password");

			// Create a new SQL statement
			String edit = "SELECT * FROM productrecords WHERE ID =" + index;
			// Prepared statement to make the results editable
			PreparedStatement statement = conn.prepareStatement(edit, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet results = statement.executeQuery(edit);

			// Loop through database and find the id
			if (results.next()) {
				results.first();

				// Allocate stockfield to strings for update
				String stocks = stockField.getText();

				// Update the relevant column in the DB
				results.updateString("Stock", stocks);
				results.updateRow();
				
				lblError.setText("Record successfully updated");
			} 

			// Free statement resources
			statement.close();
			// Free connection resources and commit updates
			conn.close();
		} catch (ClassNotFoundException cnf) {
			lblError.setText("Error: " + cnf.getMessage());
		} catch (SQLException sqe) {
			lblError.setText("Error: " + sqe.getMessage());
		}
	}

	// Method to alert management when stock is low
	public DefaultListModel<Product> Warning() {
		DefaultListModel<Product> warningModel = new DefaultListModel<Product>(); 

		// Database functionalities e.g. connection and database queries
		try {
			// Load the driver and connect to the database
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager
					.getConnection("jdbc:mysql://dbinstance.cy0v0cdt3tqa.eu-west-1.rds.amazonaws.com:3306/products?user=usersb&password=password");
			Statement statement = conn.createStatement();

			// SQL Query used to select required information from product
			// records
			String query = "SELECT * FROM productrecords WHERE Stock < 50";
			ResultSet results = statement.executeQuery(query);

			// Loop through all records and add them to JList
			while (results.next()) {
				// Retrieve each field of the currently selected record
				int ID = results.getInt("ID");
				String name = results.getString("Name");
				String price = results.getString("Price");
				String offers = results.getString("Offers");
				int stock = results.getInt("Stock");

				// Populate model with each product within the database
				Product product = new Product(ID, name, price, offers, stock);
				warningModel.addElement(product);
			}

			// Release resources held by statement
			statement.close();
			// Release resources held by DB connection
			conn.close();
		}
		// Error handling to catch J driver and SQL statement issues
		catch (ClassNotFoundException cnf) {
			System.err.println("Could not load driver");
		} catch (SQLException sqe) {
			System.out.println("Error performing SQL Query");
		}
		return warningModel;

	}

	// Populate JList
	public DefaultListModel<Product> Query() {
		DefaultListModel<Product> productModel = new DefaultListModel<Product>(); // Declare
												
		// Database functionalities e.g. connection and database queries
		try {
			// Load the driver and connect to the database
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager
					.getConnection("jdbc:mysql://dbinstance.cy0v0cdt3tqa.eu-west-1.rds.amazonaws.com:3306/products?user=usersb&password=password");
			Statement statement = conn.createStatement();

			// SQL Query used to select required information from product
			// records
			String query = "SELECT * FROM productrecords";
			ResultSet results = statement.executeQuery(query);

			// Loop through all records and add them to JList
			while (results.next()) {
				// Retrieve each field of the currently selected record
				int ID = results.getInt("ID");
				String name = results.getString("Name");
				String price = results.getString("Price");
				String offers = results.getString("Offers");
				int stock = results.getInt("Stock");

				// Populate model with each product within the database
				Product product = new Product(ID, name, price, offers, stock);
				productModel.addElement(product);
			}

			// Release resources held by statement
			statement.close();
			// Release resources held by DB connection
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
		return productModel;
	}

	// Alert call to inform user and manager of low stock levels
	public void Alert() {
		if (warningList.getModel().getSize() != 0) {
			JOptionPane.showMessageDialog(null, "Manager Alerted to low stock levels", null, JOptionPane.PLAIN_MESSAGE);
		} 
	}
}
