package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import biblioteka.Biblioteka;

import javax.swing.JList;

public class AplicationWind implements ActionListener {
	Biblioteka b = new Biblioteka();
	private JFrame frame;
	JButton btnNewButton;
	private JList list;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AplicationWind window = new AplicationWind();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AplicationWind() {
		JButton btnNewButton;
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		btnNewButton = new JButton("pokaz");
		// btnNewButton.setPreferredSize(new Dimension(50,100));
		btnNewButton.addActionListener(this);
		// btnNewButton.setPreferredSize(new Dimension(,200));

		frame.getContentPane().add(btnNewButton, BorderLayout.NORTH);

		list = new JList();
		frame.getContentPane().add(list, BorderLayout.CENTER);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == btnNewButton) {
			// Vector<Czytelnik> vector = new
			// Vector<Czytelnik>(b.selectCzytelnik());
			Vector<String> vector = new Vector<String>(
					b.pokazMojeWypozyczoneKsiazki(1));
			list.setListData(vector);

		}

	}
}
