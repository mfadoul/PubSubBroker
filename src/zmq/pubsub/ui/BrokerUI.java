package zmq.pubsub.ui;

import java.awt.EventQueue;
import java.awt.FileDialog;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import zmq.pubsub.Broker;
import zmq.pubsub.configuration.BrokerEndpointPair;
import zmq.pubsub.configuration.PubSubBrokerConfiguration;
import zmq.pubsub.configuration.PubSubBrokerConfigurationJson;
import zmq.pubsub.configuration.PubSubBrokerConfigurationXml;

import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import java.awt.FlowLayout;

public class BrokerUI {

	// Data
	private PubSubBrokerConfiguration pubSubBrokerConfiguration = null;
	private Broker broker = null;

	private Thread brokerProxyThread = null;

	private JFrame frmBroker;
	private final Action swingActionLoadJson = new SwingActionLoadJson();
	private final Action swingActionLoadXml = new SwingActionLoadXml();
	private final Action swingActionClear = new SwingActionClearConfiguration();

	private DefaultListModel<BrokerEndpointPair> brokerEndpointPairListModel = null;
	private JList<BrokerEndpointPair> brokerEndpointPairList = null;

	FileDialog loadJsonFileDialog;
	FileDialog loadXmlFileDialog;
	JToggleButton tglbtnBrokerstate;
	JLabel brokerStateLabel;
	
	// Icons
	private final ImageIcon playIcon = new ImageIcon("images/ic_play_circle_outline_black_24dp_1x.png");
	private final ImageIcon pauseIcon = new ImageIcon("images/ic_pause_circle_outline_black_24dp_1x.png");
	private final ImageIcon stopIcon = new ImageIcon("images/ic_stop_black_24dp_1x.png");
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BrokerUI window = new BrokerUI();
					window.frmBroker.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BrokerUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBroker = new JFrame();
		frmBroker.setTitle("Broker");
		frmBroker.setBounds(100, 100, 450, 300);
		frmBroker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmBroker.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmLoadJson = new JMenuItem("Load JSON");
		mntmLoadJson.setAction(swingActionLoadJson);
		mnFile.add(mntmLoadJson);
		
		JMenuItem mntmLoadXml = new JMenuItem("Load XML");
		mntmLoadXml.setAction(swingActionLoadXml);
		mnFile.add(mntmLoadXml);
		
		JMenuItem mntmClear = new JMenuItem("Clear");
		mntmClear.setAction(swingActionClear);
		mnFile.add(mntmClear);
		
		this.loadJsonFileDialog = new FileDialog(frmBroker, "Load JSON Broker config file");
		this.loadXmlFileDialog = new FileDialog(frmBroker, "Load XML Broker config file");
		
		// Initialize the list model for endpoints
		brokerEndpointPairListModel = new DefaultListModel<BrokerEndpointPair>();
		brokerEndpointPairList = new JList<BrokerEndpointPair>(brokerEndpointPairListModel);

		frmBroker.getContentPane().add(brokerEndpointPairList, BorderLayout.WEST);
		
		JPanel panel = new JPanel();
		frmBroker.getContentPane().add(panel, BorderLayout.NORTH);
		
		//tglbtnBrokerstate = new JToggleButton("Broker (Inactive)");
		tglbtnBrokerstate = new JToggleButton("");
		
		tglbtnBrokerstate.setIcon(stopIcon);

		tglbtnBrokerstate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tglbtnBrokerstate.isSelected()) {
					startBroker();
					
				} else {
					stopBroker();
				}
			}
		});
		panel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		tglbtnBrokerstate.setEnabled(false);
		panel.add(tglbtnBrokerstate);
		
		brokerStateLabel = new JLabel("No broker is loaded.");

		panel.add(brokerStateLabel);
	}

	private class SwingActionLoadXml extends AbstractAction {
		private static final long serialVersionUID = 4126236482906807621L;
		
		public SwingActionLoadXml() {
			putValue(NAME, "Load XML");
			putValue(SHORT_DESCRIPTION, "Load Broker Configuration in XML format");
		}
		public void actionPerformed(ActionEvent e) {
			loadXmlFileDialog.setVisible(true);
			System.out.println("Directory: " + loadXmlFileDialog.getDirectory());
			System.out.println("File: " + loadXmlFileDialog.getFile());
			// Directory
			String fullXmlFilePath = loadXmlFileDialog.getDirectory() + loadXmlFileDialog.getFile();
			try {
				pubSubBrokerConfiguration = new PubSubBrokerConfigurationXml(fullXmlFilePath);
				populateBrokerEndpointPairList();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
		
	private class SwingActionLoadJson extends AbstractAction {
		private static final long serialVersionUID = 5409621294243958038L;
		public SwingActionLoadJson() {
			putValue(NAME, "Load JSON");
			putValue(SHORT_DESCRIPTION, "Load Broker Configuration in JSON format");
		}
		public void actionPerformed(ActionEvent e) {
			loadJsonFileDialog.setVisible(true);
			System.out.println("Directory: " + loadJsonFileDialog.getDirectory());
			System.out.println("File: " + loadJsonFileDialog.getFile());
			
			// Directory
			String fullJsonFilePath = loadJsonFileDialog.getDirectory() + loadJsonFileDialog.getFile();
			
			File initialFile = new File(fullJsonFilePath);
			InputStream jsonInputStream;
			try {
				jsonInputStream = new FileInputStream(initialFile);
				pubSubBrokerConfiguration = new PubSubBrokerConfigurationJson(jsonInputStream);
				populateBrokerEndpointPairList();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
	private class SwingActionClearConfiguration extends AbstractAction {
		private static final long serialVersionUID = 6275103832903180793L;
		public SwingActionClearConfiguration() {
			putValue(NAME, "Clear");
			putValue(SHORT_DESCRIPTION, "Clear Broker Data");
		}
		public void actionPerformed(ActionEvent e) {
			pubSubBrokerConfiguration = null;
			broker = null;
			populateBrokerEndpointPairList();
			tglbtnBrokerstate.setEnabled(false);
			tglbtnBrokerstate.setSelected(false);
			//tglbtnBrokerstate.setText("Broker ()");
			tglbtnBrokerstate.setIcon(stopIcon);
			
			brokerStateLabel.setText("No broker is loaded.");
		}
	}

	private void populateBrokerEndpointPairList() {
		brokerEndpointPairListModel.clear();
		
		if (pubSubBrokerConfiguration != null) {
			for (int i = 0; i < pubSubBrokerConfiguration.getBrokerBindingsCount(); i++) {
				brokerEndpointPairListModel.addElement(pubSubBrokerConfiguration.getBrokerBinding(i));				
			}
			if (pubSubBrokerConfiguration.getBrokerBindingsCount()>0) {
				broker = new Broker(pubSubBrokerConfiguration);
				tglbtnBrokerstate.setEnabled(true);
				tglbtnBrokerstate.setIcon(playIcon);
				brokerStateLabel.setText("Broker is ready to run.");
			}
		}
	}
	
	private void startBroker() {
		System.out.println("Run if the button is pressed");
		//tglbtnBrokerstate.setText("Broker (Active)");
		tglbtnBrokerstate.setIcon(pauseIcon);
		brokerStateLabel.setText("Broker is active.");
		
		// Try to start the broker.
		if ((broker!=null) && (brokerProxyThread==null)) {
			brokerProxyThread = new Thread(broker);
			brokerProxyThread.start();
		}
	}
	
	private void stopBroker() {
		System.out.println("Stop if the button is unpressed");
		//tglbtnBrokerstate.setText("Broker (Inactive)");
		tglbtnBrokerstate.setIcon(playIcon);
		brokerStateLabel.setText("Broker is inactive, ready to run.");

		// Try to stop the broker.
		if ((broker!=null) && (brokerProxyThread!=null)) {
			brokerProxyThread.interrupt();
			while (!brokerProxyThread.isInterrupted()) {
				System.out.println("Trying to interrupt the broker proxy.");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			System.out.println("Cleaning up the broker proxy.");
			broker.freeResources();
			brokerProxyThread = null;
		}
	}
	
}
