import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ActorGame {
	
	//VARIABLES
	public HashMap<String, String> actorMap = new HashMap<String, String>();
	public HashMap<String, String> movieMap = new HashMap<String, String>();
	public LabeledGraph<String> connections = new LabeledGraph<String>();
	public Set<String> keywords;
	
	private final int WIDTH = 600, HEIGHT = 800;
	private String DGRAY = "#463F3A", WHITE = "#F4F3EE", MGRAY = "#8A817C", LGRAY = "#BCB8B1", PINK = "#E0AFA0";
	private final int TEXT_HEIGHT = 40;
	
	//CONSTRUCTOR
	public ActorGame() throws IOException {
		
		//create maps and graph
		createActors();
		createMovieMap();
		int counter = setupGraph();
		keywords = actorMap.keySet();
		
		//UI STUFF
			//set up main panel
			JPanel panel = new JPanel();
			BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
			panel.setLayout(boxlayout);
			panel.setBorder(BorderFactory.createEmptyBorder(20,20, 20, 20));
			panel.setBackground(Color.decode(DGRAY));
			
			//instructions panel
			JPanel topPanel = new JPanel(){ //override draw function
				public void paint(Graphics g) {
					g.setColor(Color.decode(MGRAY));
					g.fillRect(0, 0, ActorGame.this.WIDTH, 100);
					g.setColor(Color.decode(WHITE));
					g.drawString("How to Play:", 10, 20);
					g.drawString("Input two actors in the spaces below and use the tools to calculate how far away", 20, 45);
					g.drawString("they are from eachother using other actors. The path used to connect them will be", 20, 60);
					g.drawString("printed below.", 20, 75);
				}
			};
			topPanel.setBackground(Color.decode(WHITE));
			topPanel.setPreferredSize(new Dimension(WIDTH, 100));
			panel.add(topPanel);
			
			//input panel
			JPanel inputPanel = new JPanel();
			inputPanel.setBackground(Color.decode(DGRAY));
			inputPanel.setPreferredSize(new Dimension(WIDTH, 55));
			//actor 1
				JLabel actor1Prompt = new JLabel();
				actor1Prompt.setText("Actor 1:");
				actor1Prompt.setForeground(Color.decode(WHITE));
				actor1Prompt.setPreferredSize(new Dimension(50, TEXT_HEIGHT));
				inputPanel.add(actor1Prompt);
				
				
				JTextPane actor1Input = new JTextPane();
				StyledDocument doc = actor1Input.getStyledDocument();
				Style style1 = actor1Input.addStyle("typed", null);
		        StyleConstants.setForeground(style1, Color.decode(MGRAY));
		        StyleConstants.setBackground(style1, Color.decode(WHITE));
		        Style style2 = actor1Input.addStyle("generated", null);
		        StyleConstants.setForeground(style2, Color.decode(PINK));
		        StyleConstants.setBackground(style2, Color.decode(WHITE));
		       //  actor1Input
				actor1Input.setPreferredSize(new Dimension(120, 18));
				actor1Input.addKeyListener(new KeyListener() {

					public void keyPressed(KeyEvent e) {
						
						String orgText = actor1Input.getText();
						
						for(String key : keywords) {
							if(key.contains(orgText)) {
								
								try {
									doc.insertString(doc.getLength(), orgText, style1);
								} catch (BadLocationException e1) {
									e1.printStackTrace();
								}
								
								String updated = key.substring(orgText.length() - 1);
								
								try {
									doc.insertString(doc.getLength(), updated, style2);
								} catch (BadLocationException e1) {
									e1.printStackTrace();
								}
								
								break;
							}
						}
					}
					public void keyReleased(KeyEvent e) {}
					public void keyTyped(KeyEvent e) {}
					
				});
				inputPanel.add(actor1Input);
			//actor 2
				JLabel actor2Prompt = new JLabel();
				actor2Prompt.setText("Actor 2:");
				actor2Prompt.setForeground(Color.decode(WHITE));
				actor2Prompt.setPreferredSize(new Dimension(50, TEXT_HEIGHT));
				inputPanel.add(actor2Prompt);
				JTextField actor2Input = new JTextField(10);
				actor2Input.setBackground(Color.decode(WHITE));
				actor2Input.setForeground(Color.decode(MGRAY));
				inputPanel.add(actor2Input);	
			panel.add(inputPanel);
			
			//output panel
			JPanel outputPanel = new JPanel();
			outputPanel.setBackground(Color.decode(LGRAY));
			outputPanel.setPreferredSize(new Dimension(WIDTH, 505));
			panel.add(outputPanel);
			
			//button panel
			JPanel buttonPanel = new JPanel();
			buttonPanel.setBackground(Color.decode(DGRAY));
			buttonPanel.setPreferredSize(new Dimension(WIDTH, 100));
				//button 1
				JButton distButton = new JButton("Check Distance");
				distButton.setPreferredSize(new Dimension(150, TEXT_HEIGHT));
				distButton.setBackground(Color.decode(PINK));
				distButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
					}});	
				buttonPanel.add(distButton);
			panel.add(buttonPanel);
			
			
			//main container
			JFrame frame = new JFrame();
			frame.setSize(WIDTH, HEIGHT);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(panel);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			frame.setVisible(true);
			panel.setFocusable(true);
		
		
	}
	
	public void distSearch(String actor1, String actor2) {
		
		if(isValid(actor1) == true && isValid(actor2) == true) {
			
		} else {
			//error
		}
		
	}
	
	public boolean isValid(String temp) {
		
		String[] arr = temp.split(" ");
		
		arr[0].toLowerCase();
		arr[0] = arr[0].substring(0, 1).toUpperCase() + arr[0].substring(1);
		
		arr[1].toLowerCase();
		arr[1] = arr[0].substring(0, 1).toUpperCase() + arr[1].substring(1);
		
		String check = arr[0] + " " + arr[1];
		
		if(keywords.contains(check)) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public int setupGraph() throws IOException {
		
		//setup buffered reader
		BufferedReader sg = new BufferedReader(new FileReader("movie-actors.txt"));
		
		//temp variables
		String currKey = "";
		String prevKey = "";
		int counter = 0;
		ArrayList<String> currMovie = new ArrayList<String>();
		
		//run through each line
		for(String line = sg.readLine(); line != null; line = sg.readLine()) {
			
			//split the line
			String[] arr = line.split("~");

			//set the current key
			currKey = arr[0];
			
			if(currKey.equals(prevKey)) {
				currMovie.add(actorMap.get(arr[1]));
			} else {
				
				//connect everything
				for(int i = 0; i < currMovie.size(); i++) {
					for(int j = i+1; j < currMovie.size(); j++) {
						connections.connect(currMovie.get(i), currMovie.get(j));
						counter++;
					}
				}
				
				//reset for next movie
				prevKey = arr[0];
				currMovie.clear();
				currMovie.add(actorMap.get(arr[1]));
			}
		}
		
		for(int i = 0; i < currMovie.size(); i++) {
			for(int j = i+1; j < currMovie.size(); j++) {
				connections.connect(currMovie.get(i), currMovie.get(j));
				counter++;
			}
		}
		
		
		sg.close();
		
		return counter;
	}
	
	public void createActors() throws IOException {
		
		//setup buffered reader
		BufferedReader am = new BufferedReader(new FileReader("actors2.txt"));
		
		//loop through file
		for (String line = am.readLine(); line != null; line = am.readLine()) {	
			
			//split then add to map
			String[] arr = line.split("~");
			actorMap.put(arr[0], arr[1]);
			connections.addVertex(arr[1]); //this also makes all the vertices
			
		}
		
		am.close();
	}
	
	public void createMovieMap() throws IOException {
		
		//setup buffered reader
		BufferedReader mm = new BufferedReader(new FileReader("movies.txt"));
				
		//loop through file
		for (String line = mm.readLine(); line != null; line = mm.readLine()) {	
					
			//split then add to map
			String[] arr = line.split("~");
			movieMap.put(arr[0], arr[1]);
			}
				
		mm.close();
		}


	public static void main(String args[]) throws IOException {
		
		new ActorGame();
	}
	
}
