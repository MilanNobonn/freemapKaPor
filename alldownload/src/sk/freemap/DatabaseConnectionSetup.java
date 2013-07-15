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
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DatabaseConnectionSetup extends JFrame implements ActionListener {

	private JTextField jdbcUrl;
	private JTextField jdbcUser;
	private JPasswordField jdbcPassword;
	Exporter exporter;

	DatabaseConnectionSetup(Exporter _exporter) {
		super("Set database connection:");

		exporter = _exporter;

		setSize(850, 200);

		Container c = getContentPane();

		c.setLayout(new GridLayout(0, 2));

		jdbcUrl = new JTextField(70);
		jdbcUser = new JTextField(70);
		jdbcPassword = new JPasswordField(70);

		c.add(new JLabel("JDBC connection URL:"));
		jdbcUrl.setText("jdbc:postgresql://localhost:5432/kataster");
		jdbcUrl.setToolTipText("jdbc:postgresql:params\n" + "See:\n"
				+ "http://jdbc.postgresql.org/documentation/head/connect.html");
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

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBorder(BorderFactory.createLineBorder(Color.black));
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
		c.add(cancelButton);

		JTextArea description = new JTextArea(
				"For connection parameters see:\n"
						+ "http://jdbc.postgresql.org/documentation/head/connect.html");
		c.add(description);

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("OK".equals(e.getActionCommand())) {
			try {
				exporter.setConnection(DriverManager.getConnection(jdbcUrl
						.getText().trim(), jdbcUser.getText().trim(),
						jdbcPassword.getText().trim()));
				this.dispose();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, e1.toString(),
						"Database connection error", JOptionPane.ERROR_MESSAGE);
			}
		}
		if ("Cancel".equals(e.getActionCommand())) {
			this.dispose();
		}
	}

}
