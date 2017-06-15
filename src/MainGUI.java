import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.Color;

// This simple class provides users a GUI to open the various prototype systems including the
// Price controller, Inventory controller, Report generator, and finance approval system
public class MainGUI {

	public static void main(String[] args) {

		// Create and define JFrame attributes
		JFrame mainGUI = new JFrame("Main GUI");
		mainGUI.setResizable(false);
		mainGUI.getContentPane().setBackground(Color.WHITE);
		mainGUI.setSize(710, 510);
		mainGUI.setLocation(300, 200);
		mainGUI.getContentPane().setLayout(null);

		// Create JLabels and JButtons for each prototype system
		JLabel lblMain = new JLabel("E-Store Prototype");
		lblMain.setFont(new Font("Verdana", Font.BOLD, 20));
		lblMain.setBounds(244, 0, 284, 58);
		mainGUI.getContentPane().add(lblMain);

		JLabel lblTitle = new JLabel("Price Controller");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTitle.setBounds(93, 65, 111, 16);
		mainGUI.getContentPane().add(lblTitle);
		
		ImageIcon pImage = new ImageIcon(new ImageIcon("src/priceimage.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
		JLabel priceImage = new JLabel(pImage, JLabel.CENTER);
		priceImage.setBounds(93, 93, 111, 69);
		mainGUI.getContentPane().add(priceImage);
		
		JButton openproductBtn = new JButton("Open");
		openproductBtn.setForeground(Color.WHITE);
		openproductBtn.setFont(new Font("Tahoma", Font.BOLD, 13));
		openproductBtn.setBackground(new Color(178, 34, 34));
		openproductBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new PriceGUI();
			}
		});
		openproductBtn.setBounds(93, 175, 97, 25);
		mainGUI.getContentPane().add(openproductBtn);

		JLabel lblTitle1 = new JLabel("Inventory Controller");
		lblTitle1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTitle1.setBounds(461, 63, 150, 20);
		mainGUI.getContentPane().add(lblTitle1);
		
		ImageIcon iImage = new ImageIcon(new ImageIcon("src/inventoryimage.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
		JLabel invImage = new JLabel(iImage, JLabel.CENTER);
		invImage.setBounds(471, 93, 111, 69);
		mainGUI.getContentPane().add(invImage);
		
		JButton openinventoryBtn = new JButton("Open");
		openinventoryBtn.setFont(new Font("Tahoma", Font.BOLD, 13));
		openinventoryBtn.setForeground(Color.WHITE);
		openinventoryBtn.setBackground(new Color(178, 34, 34));
		openinventoryBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new InventoryController();
			}
		});
		openinventoryBtn.setBounds(471, 175, 97, 25);
		mainGUI.getContentPane().add(openinventoryBtn);

		JLabel lblTitle2 = new JLabel("Report Generator");
		lblTitle2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTitle2.setBounds(93, 255, 138, 16);
		mainGUI.getContentPane().add(lblTitle2);
		
		ImageIcon rImage = new ImageIcon(new ImageIcon("src/reportimage.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
		JLabel repImage = new JLabel(rImage, JLabel.CENTER);
		repImage.setBounds(93, 284, 111, 69);
		mainGUI.getContentPane().add(repImage);
		
		JButton openreportBtn = new JButton("Open");
		openreportBtn.setFont(new Font("Tahoma", Font.BOLD, 13));
		openreportBtn.setForeground(Color.WHITE);
		openreportBtn.setBackground(new Color(178, 34, 34));
		openreportBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new ReportGen();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		openreportBtn.setBounds(93, 369, 97, 25);
		mainGUI.getContentPane().add(openreportBtn);

		JLabel lblTitle3 = new JLabel("Finanace Approval");
		lblTitle3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTitle3.setBounds(461, 255, 150, 16);
		mainGUI.getContentPane().add(lblTitle3);
		
		ImageIcon fImage = new ImageIcon(new ImageIcon("src/financeimage.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
		JLabel finImage = new JLabel(fImage, JLabel.CENTER);
		finImage.setBounds(471, 284, 111, 69);
		mainGUI.getContentPane().add(finImage);
		mainGUI.setVisible(true);

		JButton openfinanceBtn = new JButton("Open");
		openfinanceBtn.setFont(new Font("Tahoma", Font.BOLD, 13));
		openfinanceBtn.setForeground(Color.WHITE);
		openfinanceBtn.setBackground(new Color(178, 34, 34));
		openfinanceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CustomerRecords();
			}
		});
		openfinanceBtn.setBounds(471, 369, 97, 25);
		mainGUI.getContentPane().add(openfinanceBtn);

		// WindowListener, close system when main GUI is closed
		mainGUI.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});

	}
}