import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class InsertProduct extends JFrame {
	public int id;
	public JTextField idField;
	public JTextField nameField;
	public JTextField priceField;
	public JComboBox<?> offerBox;
	public JTextField stockField;
	public JLabel lblError;
	
	public InsertProduct() {

		// Create and define JFrame attributes
		JFrame InsertProduct = new JFrame("E-Store Prototype");
		InsertProduct.getContentPane().setBackground(new Color(255, 255, 255));
		InsertProduct.setSize(664, 409);
		InsertProduct.setLocation(500, 500);
		InsertProduct.getContentPane().setLayout(null);

		JLabel lblTitle = new JLabel("Add a product");
		lblTitle.setFont(new Font("Verdana", Font.BOLD, 20));
		lblTitle.setBounds(252, 13, 172, 26);
		InsertProduct.getContentPane().add(lblTitle);

		// Create text fields for item insertion
		idField = new JTextField();
		idField.setBackground(new Color(255, 255, 255));
		idField.setBounds(12, 120, 116, 22);
		InsertProduct.getContentPane().add(idField);
		idField.setColumns(10);

		nameField = new JTextField();
		nameField.setBounds(140, 120, 116, 22);
		InsertProduct.getContentPane().add(nameField);
		nameField.setColumns(10);

		priceField = new JTextField();
		priceField.setBounds(268, 120, 116, 22);
		InsertProduct.getContentPane().add(priceField);
		priceField.setColumns(10);

		String[] offerStrings = { "No Offer", "Half Price", "Buy 1 get 1 free", "3 for 2" };
		offerBox = new JComboBox<Object>(offerStrings);
		offerBox.setBounds(396, 120, 116, 22);
		InsertProduct.getContentPane().add(offerBox);

		stockField = new JTextField();
		stockField.setBounds(522, 120, 116, 22);
		InsertProduct.getContentPane().add(stockField);
		stockField.setColumns(10);

		// JLables to provide user with relevant information for text fields
		JLabel lblProductId = new JLabel("Product ID");
		lblProductId.setBounds(12, 101, 75, 16);
		InsertProduct.getContentPane().add(lblProductId);

		JLabel lblName = new JLabel("Name");
		lblName.setBounds(140, 101, 56, 16);
		InsertProduct.getContentPane().add(lblName);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setBounds(268, 101, 56, 16);
		InsertProduct.getContentPane().add(lblPrice);

		JLabel lblOffers = new JLabel("Offers");
		lblOffers.setBounds(396, 101, 56, 16);
		InsertProduct.getContentPane().add(lblOffers);

		JLabel lblStock = new JLabel("Stock");
		lblStock.setBounds(522, 101, 56, 16);
		InsertProduct.getContentPane().add(lblStock);
		
		// JLabel to display error messages i.e. invalid int, string inputs etc
				lblError = new JLabel("Please enter product information");
				lblError.setHorizontalAlignment(SwingConstants.CENTER);
				lblError.setForeground(Color.RED);
				lblError.setFont(new Font("Tahoma", Font.PLAIN, 18));
				lblError.setBounds(12, 277, 626, 58);
				InsertProduct.getContentPane().add(lblError);

		JButton addBtn = new JButton("Add");
		addBtn.setBackground(new Color(178, 34, 34));
		addBtn.setForeground(new Color(255, 255, 255));
		addBtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(!nameField.getText().trim().isEmpty()){
				Add();
				} else {
					lblError.setText("Error: Product name can not be empty");
				}
		}
		});
		addBtn.setBounds(140, 201, 116, 25);
		InsertProduct.getContentPane().add(addBtn);

		// Clear text field items on action event
		JButton clearBtn = new JButton("Clear");
		clearBtn.setBackground(new Color(178, 34, 34));
		clearBtn.setForeground(new Color(255, 255, 255));
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				idField.setText(null);
				nameField.setText(null);
				priceField.setText(null);
				offerBox.setSelectedIndex(0);
				stockField.setText(null);
			}
		});
		clearBtn.setBounds(268, 201, 116, 25);
		InsertProduct.getContentPane().add(clearBtn);

		// Close JFrame and reopen priceGUI
		JButton cancelBtn = new JButton("Close");
		cancelBtn.setBackground(new Color(178, 34, 34));
		cancelBtn.setForeground(new Color(255, 255, 255));
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InsertProduct.dispose();
				new PriceGUI();
			}
		});
		cancelBtn.setBounds(396, 201, 116, 25);
		InsertProduct.getContentPane().add(cancelBtn);

		InsertProduct.setVisible(true);

		// Open price GUI on window close
		InsertProduct.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				new PriceGUI();
			}
		});
	}

		// Add text field values into database on action event
			public void Add() {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection conn = DriverManager
							.getConnection("jdbc:mysql://dbinstance.cy0v0cdt3tqa.eu-west-1.rds.amazonaws.com:3306/products?user=usersb&password=password");

					String id = idField.getText();
					String name = nameField.getText();
					String price = priceField.getText();
					String offer = offerBox.getSelectedItem().toString();
					String stock = stockField.getText();

					// SQL statement to place text field values into database
					Statement statement = conn.createStatement();
					String update = "INSERT INTO productrecords (ID, Name, Price, Offers, Stock) " + "VALUES ('" + id
							+ "', '" + name + "', '" + price + "', '" + offer + "', '" + stock + "')";

					// Execute the statement and release resources
					statement.executeUpdate(update);
					statement.close();
					conn.close();

					// Update error label to provide user with successful update
					// information
					lblError.setText("Insertion of product ID: " + id + " was successful");

				} catch (ClassNotFoundException cnf) {
					lblError.setText("Error: " + cnf.getMessage());
				} catch (SQLException sqe) {
					lblError.setText("Error: " + sqe.getMessage());
				}
			}

	}


