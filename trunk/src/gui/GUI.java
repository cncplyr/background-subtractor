package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

/**
 * ASDJFLASDJFLAKJSDLJ swing sucks
 * @author cncplyr
 * @version 0.1
 *
 */
public class GUI extends JFrame {
	private static final long serialVersionUID = 2941318999657277463L;

	public GUI() {
		setTitle("Background Subtractor v1.0");
		setSize(500, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setJMenuBar(createMenu());

		Container contentPane = new Container();
		
		
		this.add(createGlobalOptions());
		this.add(createBackgroundOptions());
		this.add(createSubtractOptions());
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUI gui = new GUI();
				gui.setVisible(true);
			}
		});
	}


	public static void addComponentsToPane(Container contentPane) {
		// Any number of rows and 2 columns
		contentPane.setLayout(new GridLayout(0, 1));


		JButton runAllButton = new JButton("Run everything");
		runAllButton.setBounds(40, 80, 80, 25);
		JButton runBgButton = new JButton("Background Only");
		runBgButton.setBounds(40, 80, 80, 25);
		JButton runSubButton = new JButton("Subtract Only");
		runSubButton.setBounds(40, 80, 80, 25);

		contentPane.add(runAllButton);
		contentPane.add(runBgButton);
		contentPane.add(runSubButton);
	}

	public JMenuBar createMenu() {
		JMenuBar menuBar = new JMenuBar();

		/* FILE MENU */
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);

		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuItemExit.setMnemonic(KeyEvent.VK_C);
		menuItemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		file.add(menuItemExit);

		/* HELP MENU */
		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);

		JMenuItem menuItemAbout = new JMenuItem("About");
		menuItemAbout.setMnemonic(KeyEvent.VK_A);

		help.add(menuItemAbout);

		/* Add to bar */
		menuBar.add(file);
		menuBar.add(help);

		return menuBar;
	}

	public Container createGlobalOptions() {
		JPanel container = new JPanel();
		SpringLayout layout = new SpringLayout();
		container.setLayout(layout);
		
		

		container.add(new JLabel("Background and Subtraction: "));
		container.add(new JTextField("Text field", 15));

		
		return container;
	}

	public Container createBackgroundOptions() {
		Container container = new Container();

		return container;
	}

	public Container createSubtractOptions() {
		Container container = new Container();

		return container;
	}

}
