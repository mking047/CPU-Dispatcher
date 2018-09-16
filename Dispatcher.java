package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;



import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.Color;

/**
 * This GUI simulates a CPU dispatcher, CS471 Project #1
 * @author Matthew
 *
 */
public class Dispatcher extends JFrame {
	
	private JPanel contentPane;
	private JTextField textField;
	List<Process> ready = new ArrayList<Process>();
	List<Process> blocked = new ArrayList<Process>();
	List<Process> running = new ArrayList<Process>();
	int pid = 0;
	private JButton btnAdd;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dispatcher frame = new Dispatcher();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public Dispatcher() {
		
		////////////////////////////////////////////////
		//				Create GUI 					  //
		////////////////////////////////////////////////
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 736, 461);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPriority = new JLabel("Priority #");
		lblPriority.setBounds(12, 13, 56, 16);
		contentPane.add(lblPriority);
		
		textField = new JTextField();
		textField.setBounds(68, 10, 56, 22);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(12, 86, 215, 326);
		contentPane.add(panel);
		
		JTextArea textArea = new JTextArea();
		panel.add(textArea);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(252, 86, 215, 326);
		contentPane.add(panel_1);
		
		JTextArea textArea_1 = new JTextArea();
		panel_1.add(textArea_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel_2.setBounds(490, 86, 215, 326);
		contentPane.add(panel_2);
		
		JTextArea textArea_2 = new JTextArea();
		panel_2.add(textArea_2);
		
		JLabel lblReady = new JLabel("Ready");
		lblReady.setBounds(68, 71, 56, 16);
		contentPane.add(lblReady);
		
		JLabel lblRunning = new JLabel("Running");
		lblRunning.setBounds(322, 71, 56, 16);
		contentPane.add(lblRunning);
		
		JLabel lblBlocked = new JLabel("Blocked");
		lblBlocked.setBounds(568, 71, 56, 16);
		contentPane.add(lblBlocked);
		
		
//////////////////////////////////////////////////////////////
//				Terminate button event proceedings			//
//////////////////////////////////////////////////////////////	
		
		JButton btnTerminate = new JButton("Terminate");
		btnTerminate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!blocked.isEmpty()){
					for (Process p:blocked)
					{
						if(Integer.parseInt(textField.getText()) == p.priority)
						{
							blocked.remove(p);
							Collections.sort(blocked, new CustomComparator());
							printTextFields(textArea,textArea_1,textArea_2);
							return;
						}
					}
				}
				if(!ready.isEmpty()){
					for (Process p:ready)
					{
						if(Integer.parseInt(textField.getText()) == p.priority)
						{
							ready.remove(p);
							Collections.sort(ready, new CustomComparator());
							printTextFields(textArea,textArea_1,textArea_2);
							return;
						}
					}
				}
				if(!running.isEmpty()){
					for (Process p:running)
					{
						if(Integer.parseInt(textField.getText()) == p.priority)
						{
							running.remove(p);
							addRunningProcess();
							printTextFields(textArea,textArea_1,textArea_2);
							return;
						}
					}
				}
			}
		});
		btnTerminate.setBounds(564, 9, 97, 25);
		contentPane.add(btnTerminate);
		
//////////////////////////////////////////////////////////////
//				Add button event proceedings				//
//////////////////////////////////////////////////////////////	
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			pid++;
			Process temp = new Process(Integer.parseInt(textField.getText()), pid);
			if(running.isEmpty())
				running.add(temp);
			else{
				ready.add(temp);
				Collections.sort(ready, new CustomComparator());
			}
			printTextFields(textArea,textArea_1,textArea_2);
			}
		});

		btnAdd.setBounds(131, 9, 97, 25);
		contentPane.add(btnAdd);
		
		
//////////////////////////////////////////////////////////////
//				Block button event proceedings				//
//////////////////////////////////////////////////////////////	
		JButton btnBlock = new JButton("Block");
		btnBlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ready.isEmpty()){
					for (Process p:ready)
					{
						if(Integer.parseInt(textField.getText()) == p.priority)
						{
							blocked.add(p);
							ready.remove(p);
							Collections.sort(blocked, new CustomComparator());
							Collections.sort(ready, new CustomComparator());
							printTextFields(textArea,textArea_1,textArea_2);
							return;
						}
					}
				}
				if(!running.isEmpty()){
					for (Process p:running)
					{
						if(Integer.parseInt(textField.getText()) == p.priority)
						{
							blocked.add(p);
							running.remove(0);
							addRunningProcess();
							Collections.sort(blocked, new CustomComparator());
							printTextFields(textArea,textArea_1,textArea_2);
							return;
						}
					}
				}
			}
		});
		btnBlock.setBounds(236, 9, 97, 25);
		contentPane.add(btnBlock);
		
//////////////////////////////////////////////////////////////
//				Ready button event proceedings				//
//////////////////////////////////////////////////////////////
		
		JButton btnReady = new JButton("Ready");
		btnReady.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!blocked.isEmpty())
				{
					for (Process p:blocked)
					{
						if(Integer.parseInt(textField.getText()) == p.priority)
						{
							ready.add(p);
							blocked.remove(p);
							Collections.sort(blocked, new CustomComparator());
							Collections.sort(ready, new CustomComparator());
							printTextFields(textArea,textArea_1,textArea_2);
							return;
						}
					}
				}
				if(!running.isEmpty())
				{
					for (Process p:running)
					{
						if(Integer.parseInt(textField.getText()) == p.priority)
						{
							ready.add(p);
							running.remove(p);
							addRunningProcess();
							Collections.sort(ready, new CustomComparator());
							printTextFields(textArea,textArea_1,textArea_2);
							return;
						}
					}
				}
				
			}
		});
		btnReady.setBounds(345, 9, 97, 25);
		contentPane.add(btnReady);

//////////////////////////////////////////////////////////////
//				Run button event proceedings				//
//////////////////////////////////////////////////////////////
		JButton btnRunning = new JButton("Run");
		btnRunning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ready.isEmpty()){
					for (Process p:ready)
					{
						if(Integer.parseInt(textField.getText()) == p.priority)
						{
							ready.add(running.get(0));
							running.remove(0);
							running.add(p);
							ready.remove(p);
							Collections.sort(ready, new CustomComparator());
							printTextFields(textArea,textArea_1,textArea_2);
							return;
						}
					}
				}
				if(!blocked.isEmpty()){
					for (Process p:blocked)
					{
						if(Integer.parseInt(textField.getText()) == p.priority)
						{
							ready.add(running.get(0));
							running.remove(0);
							running.add(p);
							blocked.remove(p);
							Collections.sort(ready, new CustomComparator());
							Collections.sort(blocked, new CustomComparator());
							printTextFields(textArea,textArea_1,textArea_2);
							return;
						}
					}
				}
			
			}
		});
		btnRunning.setBounds(455, 9, 97, 25);
		contentPane.add(btnRunning);
		
	
	}
	
	/**
	 * override for compare for sorting algorithm    
	 * 
	 */
	public class CustomComparator implements Comparator<Process> {
	    @Override
	    public int compare(Process o1, Process o2) {
	        return o1.priority - o2.priority;
	    }
	}
	
	/**
	 * takes a process from ready or blocked queue and puts it in running queue    
	 */
	public void addRunningProcess(){
		if(!ready.isEmpty())
		{
			running.add(ready.get(0));
			ready.remove(0);
			return;
		}
		if(!blocked.isEmpty())
		{
			running.add(blocked.get(0));
			blocked.remove(0);
			return;
		}
	}
	
	/**
	 * prints queues to their corresponding text area fields
	 * 
	 * @param textArea
	 * @param textArea_1
	 * @param textArea_2
	 */
	public void printTextFields(JTextArea textArea, JTextArea textArea_1, JTextArea textArea_2)
	{
		textArea.setText("");
		textArea_1.setText("");
		textArea_2.setText("");
		
		if(!ready.isEmpty())
		for(Process it: ready){
			textArea.append("Priority:" + ready.get(ready.indexOf(it)).priority + "    PID:" + ready.get(ready.indexOf(it)).pid + "\n");
		}
		if(!blocked.isEmpty())
		for(Process it: blocked){
			textArea_2.append("Priority:" + blocked.get(blocked.indexOf(it)).priority + "    PID:" + blocked.get(blocked.indexOf(it)).pid + "\n");
		}
		if(!running.isEmpty())
		for(Process it: running){
			textArea_1.append("Priority:" + running.get(running.indexOf(it)).priority + "    PID:" + running.get(running.indexOf(it)).pid + "\n");
		}
		
	}

}

