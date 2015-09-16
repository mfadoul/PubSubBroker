package zmq.pubsub.ui;

import java.awt.EventQueue;
//import java.awt.List;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;
import java.awt.BorderLayout;

import org.zeromq.ZMQ.Socket;

import zmq.pubsub.message.MessageMap;
import zmq.pubsub.subscriber.SubscriberClientJson;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class SubscriberClientUI extends SubscriberClientJson {

	private JFrame frmSubscriberClient;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SubscriberClientUI window = new SubscriberClientUI();
					window.frmSubscriberClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SubscriberClientUI() {
		// Load the subscriber configuration file.
		super("data/SubscriberConfig.json");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() {
		frmSubscriberClient = new JFrame();
		frmSubscriberClient.setTitle("Subscriber Client");
		frmSubscriberClient.setBounds(100, 100, 450, 300);
		frmSubscriberClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ArrayList<String> messageNames = new ArrayList<String>();
		
		MessageMap messageMap = this.getMessageMap();
		for (Integer messageId: messageMap.getAllMessageIds()) {
			String label;
			try {
				label = messageMap.getMessageName(messageId) + ": " + messageId.toString();
				messageNames.add(label);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		list = new JList(messageNames.toArray());
		
		// Is this the correct way to initialize the list?
		//list = new JList<String>((String[]) messageNames.toArray());
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				List<String> selectedValuesList = list.getSelectedValuesList();
				String selectionString = selectedValuesList.toString();
				textArea.setText(selectionString);
			}
		});

		frmSubscriberClient.getContentPane().add(list, BorderLayout.WEST);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		frmSubscriberClient.getContentPane().add(textArea, BorderLayout.CENTER);
	}

	@Override
	protected boolean receiveMessage(Socket subscriberSocket) {
		// TODO Auto-generated method stub
		return false;
	}

	// Member variables
	JList<String> list;
	JTextArea textArea;
}
