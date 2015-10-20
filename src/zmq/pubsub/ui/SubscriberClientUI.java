package zmq.pubsub.ui;

import java.awt.EventQueue;
//import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;
import java.awt.BorderLayout;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import zmq.pubsub.MessageUtils;
import zmq.pubsub.message.MessageMap;
import zmq.pubsub.subscriber.SubscriberClient;
import zmq.pubsub.subscriber.SubscriberClientJson;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class SubscriberClientUI implements Runnable {

	private JFrame frmSubscriberClient;
	private SubscriberClient subscriberClient = null;

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
		subscriberClient = new SubscriberClientJsonUi("data/SubscriberConfig.json");
		initialize();
		
		// Start the listener thread
		ZMQ.Context context = ZMQ.context (1);
		this.subscriberSocket = context.socket(ZMQ.SUB);
		this.subscriberSocket.connect(subscriberClient.getSubscriberEndpoint());

		this.startSubscriberThread();
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
		
		JToolBar toolBar = new JToolBar();
		toolBar.setOrientation(SwingConstants.VERTICAL);
		
		messageScrollPane = new JScrollPane(toolBar);
		
		lblSubscriptions = new JLabel("Subscriptions");
		toolBar.add(lblSubscriptions);

		frmSubscriberClient.getContentPane().add(messageScrollPane, BorderLayout.WEST);

		ArrayList<String> messageNames = new ArrayList<String>();
		
		MessageMap messageMap = subscriberClient.getMessageMap();
		for (Integer messageId: messageMap.getAllMessageIds()) {
			String label;
			try {
				label = messageMap.getMessageName(messageId) + ": " + messageId.toString();
				messageNames.add(label);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		frmSubscriberClient.getContentPane().add(textArea, BorderLayout.CENTER);
		
		textConsoleArea = new JTextArea();
		frmSubscriberClient.getContentPane().add(textConsoleArea, BorderLayout.SOUTH);
				
		for (final Integer messageId: subscriberClient.getMessageMap().getAllMessageIds()) {
			String messageName;
			try {
				messageName = subscriberClient.getMessageMap().getMessageName(messageId);
				MessageCheckBox messageCheckBox = new MessageCheckBox(messageId, messageName);
				//messageSubscriptionListScrollPane.add(messageCheckBox);
				
				messageCheckBox.setSelected(subscriberClient.isSubscribed(messageId));
				
				messageCheckBox.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							MessageCheckBox messageCheckBox = (MessageCheckBox) e.getSource();
							if (messageCheckBox.isSelected()) {
								
								System.out.println("Action: Subscribed to " + messageCheckBox.getMessageName());
								textArea.append("Action: Subscribed to " + messageCheckBox.getMessageName() + "\n");
								subscriberClient.subscribe(messageId);

							} else {
								//message
								System.out.println("Action: Unsubscribed from " + messageCheckBox.getMessageName());
								textArea.append("Action: Unsubscribed from " + messageCheckBox.getMessageName() + "\n");
								subscriberClient.unsubscribe(messageId);
							}
						}
					});
				
				toolBar.add(messageCheckBox);				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		menuBar = new JMenuBar();
		frmSubscriberClient.setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmLoadJson = new JMenuItem("Load JSON");
		mntmLoadJson.setAction(swingActionLoadJson);
		mnFile.add(mntmLoadJson);
		
		mntmClear = new JMenuItem("Clear");
		mntmClear.setAction(swingActionClear);
		mnFile.add(mntmClear);
	}


	
	private class SwingActionClearConfiguration extends AbstractAction {
		private static final long serialVersionUID = -7200508333403727798L;
		public SwingActionClearConfiguration() {
			putValue(NAME, "Clear");
			putValue(SHORT_DESCRIPTION, "Clear Subscriber Data (TODO)");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	
	private class SubscriberClientJsonUi extends SubscriberClientJson {

		public SubscriberClientJsonUi(String subscriberConfigFilename) {
			super(subscriberConfigFilename);
		}
		
		@Override
		protected boolean receiveMessage(Socket subscriberSocket) {
			//textArea.append("Waiting for next message...");
			int messageId = MessageUtils.byteArrayToInt(subscriberSocket.recv());
			String messageContents = subscriberSocket.recvStr();
			
			System.out.println("Receive (ID=" + messageId + ").  Contents=" + messageContents);
			textArea.append("Receive (ID=" + messageId + ").  Contents=" + messageContents + "\n");
			return true;
		}

		
	}
	
	private class SwingActionLoadJson extends AbstractAction {
		private static final long serialVersionUID = 6575589615318707331L;
		public SwingActionLoadJson() {
			putValue(NAME, "Load JSON");
			putValue(SHORT_DESCRIPTION, "Load a JSON subscriber config file (TODO)");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	
	private class MessageCheckBox extends JCheckBox {
		private static final long serialVersionUID = 6178752106473007155L;
		public MessageCheckBox(final int messageId, final String messageName) {
			this.messageId = messageId;
			this.messageName = messageName;
			this.setText(messageName + "(" + Integer.toString(messageId) + ")");
			System.out.println("MessageCheckBox created: " + this.getText());
		}

		public int getMessageId() {
			return messageId;
		}

		public String getMessageName() {
			return messageName;
		}

		private final int messageId;
		private final String messageName;
	}
	
	private void startSubscriberThread() {
		if ((subscriberSocket != null) && (this.subscriberThread==null)) {
			subscriberThread = new Thread(this);
			subscriberThread.start();
		} else {
			System.err.println("ERROR: Could not initialize the subscriber thread!");
		}
	}
	
	// Member variables
	JList<String> list;
	JTextArea textArea;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmLoadJson;
	private JMenuItem mntmClear;
	private final Action swingActionClear = new SwingActionClearConfiguration();
	private final Action swingActionLoadJson = new SwingActionLoadJson();
	private JScrollPane messageScrollPane;	
	private JLabel lblSubscriptions;
	private JTextArea textConsoleArea;

	Socket subscriberSocket = null;
	private Thread subscriberThread = null;

	@Override
	public void run() {
		this.textArea.append("Starting run();");
		subscriberClient.subscriberLoop();		
	}
}
