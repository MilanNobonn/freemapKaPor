package sk.freemap;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DatabaseConnectionSetup extends JFrame implements ActionListener {

	private JTextField jdbcUrl;
	private JTextField jdbcUser;
	private JPasswordField jdbcPassword;

	DatabaseConnectionSetup() {
		super("Set database connection:");

		setSize(650, 160);

		Container c = getContentPane();

		c.setLayout(new GridLayout(0, 2));

		jdbcUrl = new JTextField(70);
		jdbcUser = new JTextField(70);
		jdbcPassword = new JPasswordField(70);

		// "jdbc:postgresql://localhost:5433/kataster2"
		// nobonn

		c.add(new JLabel("JDBC connection URL:"));
		jdbcUrl.setText("jdbc:postgresql://localhost:5432/kataster");
		jdbcUrl.setToolTipText("jdbc:postgresql:params");
		c.add(jdbcUrl);
		c.add(new JLabel("user:"));
		c.add(jdbcUser);
		c.add(new JLabel("password:"));
		c.add(jdbcPassword);

		JButton okButton = new JButton("OK");
		okButton.setBorder(BorderFactory.createLineBorder(Color.black));
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		c.add(okButton);

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("OK".equals(e.getActionCommand())) {
			try {
				DriverManager.getConnection(jdbcUrl.getText().trim(), jdbcUser
						.getText().trim(), jdbcPassword.getText().trim());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
