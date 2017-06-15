
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

// This class retrieves items in the product database and populates them within a
// printable JTable for user reports
public class ReportGen {

	public ReportGen() throws Exception {
		JFrame reportGen = new JFrame("Print");
		reportGen.getContentPane().setBackground(new Color(255, 255, 255));
		reportGen.setResizable(false);
		reportGen.setSize(733, 510);
		reportGen.setLocation(300, 200);
		reportGen.getContentPane().setLayout(null);

		// Populate JTable and add to JFrame
		JTable table = new JTable(buildTableModel(Print()));
		JScrollPane pane = new JScrollPane(table); 
		pane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		pane.setBackground(Color.WHITE);
		pane.setBounds(58, 56, 597, 350);
		reportGen.getContentPane().add(pane);

		// Print button, sets JTable as printable
		JButton printBtn = new JButton("Print");
		printBtn.setBackground(new Color(178, 34, 34));
		printBtn.setForeground(new Color(255, 255, 255));
		printBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					table.print(JTable.PrintMode.FIT_WIDTH, null, null);
				} catch (PrinterException e1) {
					e1.printStackTrace();
				}
			}
		});
		printBtn.setBounds(234, 425, 97, 25);
		reportGen.getContentPane().add(printBtn);

		// Close JFrame and reopen priceGUI
		JButton cancelBtn = new JButton("Close");
		cancelBtn.setBackground(new Color(178, 34, 34));
		cancelBtn.setForeground(new Color(255, 255, 255));
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reportGen.dispose();
			}
		});
		cancelBtn.setBounds(357, 425, 116, 25);
		reportGen.getContentPane().add(cancelBtn);

		JLabel lblNewLabel = new JLabel("Store Report");
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 16));
		lblNewLabel.setBounds(300, 27, 130, 16);
		reportGen.getContentPane().add(lblNewLabel);

		reportGen.setVisible(true);
	}

	// Create table model
	public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

		ResultSetMetaData metaData = rs.getMetaData();

		// names of columns
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column));
		}

		// data of the table
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}

		return new DefaultTableModel(data, columnNames);

	}

	// Create a default result set which can be called to populate the JTable
	public static ResultSet Print() throws SQLException {

			// Connect to the database
			Connection conn = DriverManager
					.getConnection("jdb" + "c:mysql://dbinstance.cy0v0cdt3tqa.eu-west-1.rds.amazonaws.com:3306/products?user=usersb&password=password");
			Statement statement = conn.createStatement();

			// SQL Query used to select required information from product
			// records
			String query = "SELECT * FROM productrecords";
			ResultSet results = statement.executeQuery(query);

			return results;
		
	}

}
