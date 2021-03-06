package zmq.pubsub.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PublisherClientUI {

	private JFrame frmPublisherClient;
	private final JButton messageButton1 = new JButton("Message 1");
	private final JButton messageButton2 = new JButton("Message 2");
	private final JButton messageButton3 = new JButton("Message 3");
	private final JButton messageButton4 = new JButton("Message 4");
	private final JButton messageButton5 = new JButton("Message 5");
	private final JButton messageButton6 = new JButton("Message 6");
	private final JTextArea txtrMessages = new JTextArea();
	private final JPanel panel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PublisherClientUI window = new PublisherClientUI();
					window.frmPublisherClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PublisherClientUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPublisherClient = new JFrame();
		frmPublisherClient.setTitle("Publisher Client");
		frmPublisherClient.setBounds(100, 100, 450, 300);
		frmPublisherClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPublisherClient.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		messageButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frmPublisherClient.getContentPane().add(messageButton1);
		
		messageButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frmPublisherClient.getContentPane().add(messageButton2);
		
		messageButton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frmPublisherClient.getContentPane().add(messageButton3);
		
		messageButton4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frmPublisherClient.getContentPane().add(messageButton4);
		
		messageButton5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frmPublisherClient.getContentPane().add(messageButton5);
		
		messageButton6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frmPublisherClient.getContentPane().add(messageButton6);
		
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(10);
		
		frmPublisherClient.getContentPane().add(panel);
		txtrMessages.setColumns(30);
		panel.add(txtrMessages);
		txtrMessages.setRows(10);
	}

}
