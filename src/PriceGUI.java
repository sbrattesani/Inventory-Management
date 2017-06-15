import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.SwingConstants;

// This class provides the user with price controller functionalities, that allow the addition, changing, and 
// removal of items in the product database
@SuppressWarnings("serial")
public class PriceGUI extends JFrame {
	public int index = -1;
	public static Product selected;

	public PriceGUI() {

		// Create and define JFrame attributes
		JFrame priceGUI = new JFrame("E-Store Prototype");
		priceGUI.getContentPane().setBackground(Color.WHITE);
		priceGUI.setSize(733, 510);
		priceGUI.setLocation(300, 200);
		priceGUI.getContentPane().setLayout(null);

		/// Create JList to hold database information and set model
		JList<Product> productList = new JList<Product>(); 
		productList.setModel(Query()); // Call Query() method to get model from database

		// JLables to provide user with relevant information for text fields
		JLabel lblNewLabel = new JLabel("Price Controller");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(304, 13, 126, 16);
		priceGUI.getContentPane().add(lblNewLabel);

		JLabel lblError = new JLabel("");
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblError.setForeground(Color.RED);
		lblError.setBounds(12, 367, 691, 25);
		priceGUI.getContentPane().add(lblError);

		// JList selection listener. Sets index, selected item, and clears user feedback label
		ListSelectionListener listSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					index = productList.getSelectedValue().getID();
					selected = productList.getSelectedValue();
					lblError.setText("");
				}
			}
		};
		productList.addListSelectionListener(listSelectionListener);
	    productList.setSelectedIndex(index);
		JScrollPane pane = new JScrollPane(productList); // Allows items to be scrolled through
		pane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		pane.setBackground(Color.WHITE);
		pane.setBounds(58, 56, 597, 182);
		priceGUI.getContentPane().add(pane);

		JButton addBtn = new JButton("Add"); // Add products to database
		addBtn.setForeground(new Color(255, 255, 255));
		addBtn.setBackground(new Color(178, 34, 34));
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				priceGUI.dispose();
				Add(); // Call add function
			}
		});
		addBtn.setBounds(136, 297, 97, 25);
		priceGUI.getContentPane().add(addBtn);

		JButton editBtn = new JButton("Edit"); // Edit product values in the database
		editBtn.setBackground(new Color(178, 34, 34));
		editBtn.setForeground(new Color(255, 255, 255));
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!productList.isSelectionEmpty()) {
					priceGUI.dispose();
					Edit();
				} else {
					lblError.setText("Please select an item to edit");
				}
			}
		});
		editBtn.setBounds(245, 297, 97, 25);
		priceGUI.getContentPane().add(editBtn);

		// Remove a product from the database
		JButton removeBtn = new JButton("Remove");
		removeBtn.setForeground(new Color(255, 255, 255));
		removeBtn.setBackground(new Color(178, 34, 34));
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get ID of selected item and call the remove method
				if (index >= 0) {
					index = productList.getSelectedValue().getID();
					Remove();
					productList.setModel(Query()); // Refresh JList
				} else {
					lblError.setText("Please select an item to remove");
				}
			}
		});
		removeBtn.setBounds(354, 297, 97, 25);
		priceGUI.getContentPane().add(removeBtn);

		// Close JFrame and reopen priceGUI
		JButton cancelBtn = new JButton("Close");
		cancelBtn.setBackground(new Color(178, 34, 34));
		cancelBtn.setForeground(new Color(255, 255, 255));
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				priceGUI.dispose();
			}
		});
		cancelBtn.setBounds(463, 297, 116, 25);
		priceGUI.getContentPane().add(cancelBtn);

		priceGUI.setVisible(true);
	}

	// Query method that returns a product model from database for use in the JList
	public DefaultListModel<Product> Query() {
		DefaultListModel<Product> productModel = new DefaultListModel<Product>(); 
		try {
			// Load the driver and connect to the database
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager
					.getConnection("jdb" + "c:mysql://dbinstance.cy0v0cdt3tqa.eu-west-1.rds.amazonaws.com:3306/products?user=usersb&password=password");
			Statement statement = conn.createStatement();

			// SQL Query used to select required information from product
			// records
			String query = "SELECT * FROM productrecords";
			ResultSet results = statement.executeQuery(query);

			// Loop through all records and add them to the product model
			while (results.next()) {
				int ID = results.getInt("ID");
				String name = results.getString("Name");
				String price = results.getString("Price");
				String offers = results.getString("Offers");
				int stock = results.getInt("Stock");

				// Populate model with each product within the database
				Product product = new Product(ID, name, price, offers, stock);
				productModel.addElement(product);
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
		return productModel;

	}

	// Add method to open new GUI for inserting a new product
	public void Add() {
		new InsertProduct();

	}

	// Remove method to delete items from database
	public void Remove() {
		try {
			// Load the driver and connect to database
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager
					.getConnection("jdbc:mysql://dbinstance.cy0v0cdt3tqa.eu-west-1.rds.amazonaws.com:3306/products?user=usersb&password=password");

			// Get selected product ID from and remove from database
			Statement statement = conn.createStatement();
			String remove = "DELETE FROM productrecords WHERE ID =" + index;

			// Execute the statement and release resources
			statement.executeUpdate(remove);
			statement.close();
			conn.close();

		} catch (ClassNotFoundException cnf) {
			System.err.println(cnf.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Edit method to open the EditProduct class
	public void Edit() {
		new EditProduct();
	}
}
