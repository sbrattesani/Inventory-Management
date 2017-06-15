import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Color;

@SuppressWarnings("serial")
public class EditProduct extends JFrame {
	public int id;
	public JTextField idField;
	public JTextField nameField;
	public JTextField priceField;
	public JComboBox<?> offerBox;
	public JTextField stockField;
	public JLabel lblError;

	public EditProduct() {
		// Create and define JFrame attributes
		JFrame EditProduct = new JFrame("E-Store Prototype");
		EditProduct.getContentPane().setBackground(new Color(255, 255, 255));
		EditProduct.setSize(664, 409);
		EditProduct.setLocation(500, 500);
		EditProduct.getContentPane().setLayout(null);

		JLabel lblTitle = new JLabel("Edit product");
		lblTitle.setFont(new Font("Verdana", Font.BOLD, 20));
		lblTitle.setBounds(252, 13, 172, 26);
		EditProduct.getContentPane().add(lblTitle);

		// Create text fields and set text and populate fields based on selected item
		idField = new JTextField();
		idField.setBounds(12, 120, 116, 22);
		idField.setColumns(10);
		idField.setText(String.valueOf(PriceGUI.selected.getID()));
		id = PriceGUI.selected.getID(); // Used in SQL query
		idField.setEditable(false);
		EditProduct.getContentPane().add(idField);

		nameField = new JTextField();
		nameField.setBounds(140, 120, 116, 22);
		nameField.setColumns(10);
		nameField.setText(String.valueOf(PriceGUI.selected.getName()));
		EditProduct.getContentPane().add(nameField);

		priceField = new JTextField();
		priceField.setBounds(268, 120, 116, 22);
		priceField.setColumns(10);
		priceField.setText(String.valueOf(PriceGUI.selected.getPrice()));
		EditProduct.getContentPane().add(priceField);

		String[] offerStrings = { "No Offer", "Half Price", "Buy 1 get 1 Free", "3 for 2" };
		offerBox = new JComboBox<Object>(offerStrings);
		offerBox.setBounds(396, 120, 116, 22);
		offerBox.setSelectedItem(PriceGUI.selected.getOffers().toString());
		EditProduct.getContentPane().add(offerBox);

		stockField = new JTextField();
		stockField.setBounds(522, 120, 116, 22);
		stockField.setColumns(10);
		stockField.setText(String.valueOf(PriceGUI.selected.getStock()));
		stockField.setEditable(false);
		EditProduct.getContentPane().add(stockField);

		// JLables to provide user with relevant information for text fields
		JLabel lblProductId = new JLabel("Product ID");
		lblProductId.setBounds(12, 101, 75, 16);
		EditProduct.getContentPane().add(lblProductId);

		JLabel lblName = new JLabel("Name");
		lblName.setBounds(140, 101, 56, 16);
		EditProduct.getContentPane().add(lblName);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setBounds(268, 101, 56, 16);
		EditProduct.getContentPane().add(lblPrice);

		JLabel lblOffers = new JLabel("Offers");
		lblOffers.setBounds(396, 101, 56, 16);
		EditProduct.getContentPane().add(lblOffers);

		JLabel lblStock = new JLabel("Stock");
		lblStock.setBounds(522, 101, 56, 16);
		EditProduct.getContentPane().add(lblStock);

		// Edit button functionalities, Call Edit() method on action event
		JButton editBtn = new JButton("Edit");
		editBtn.setBackground(new Color(178, 34, 34));
		editBtn.setForeground(new Color(255, 255, 255));
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!nameField.getText().trim().isEmpty()){
				Edit();
				} else {
					lblError.setText("Error: Product name can not be empty");
				}
			}
		});
		editBtn.setBounds(208, 201, 116, 25);
		EditProduct.getContentPane().add(editBtn);

		// Close JFrame and reopen priceGUI
		JButton cancelBtn = new JButton("Close");
		cancelBtn.setForeground(new Color(255, 255, 255));
		cancelBtn.setBackground(new Color(178, 34, 34));
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditProduct.dispose();
				new PriceGUI();
				} 
		});
		cancelBtn.setBounds(336, 201, 116, 25);
		EditProduct.getContentPane().add(cancelBtn);
		
		// JLabel to display error messages i.e. invalid int, string inputs etc
				lblError = new JLabel();
				lblError.setHorizontalAlignment(SwingConstants.CENTER);
				lblError.setForeground(Color.RED);
				lblError.setFont(new Font("Tahoma", Font.PLAIN, 18));
				lblError.setBounds(12, 277, 626, 58);
				EditProduct.getContentPane().add(lblError);
				
		EditProduct.setVisible(true);
	}

	// Populate text fields based on ID received from PriceGUI class
	public void Edit() {
		try {
			// Load the driver and connect to database
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager
					.getConnection("jdbc:mysql://dbinstance.cy0v0cdt3tqa.eu-west-1.rds.amazonaws.com:3306/products?user=usersb&password=password");

			// Create a prepared statement to make the results editable
			String edit = "SELECT * FROM productrecords WHERE ID =" + id;
			PreparedStatement statement = conn.prepareStatement(edit, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet results = statement.executeQuery(edit);

			// Loop through database and find the id, allocate text fields to
			// strings for update, then update the relevant row
			if (results.next()) {
				results.first();

				String name = nameField.getText();
				String price = priceField.getText();
				String offers = offerBox.getSelectedItem().toString();

				results.updateString("Name", name);
				results.updateString("Price", price);
				results.updateString("Offers", offers);
				results.updateRow();

				// User feedback dialog
				lblError.setText("Record updated successfully");
			} else {
				JOptionPane.showMessageDialog(null, "Error: Record not found");
			}

			// Release resources
			statement.close();
			conn.close();
		} catch (ClassNotFoundException cnf) {
			lblError.setText("Error: " + cnf.getMessage());
		} catch (SQLException sqe) {
			lblError.setText("Error: " + sqe.getMessage());
		}
	}
}
