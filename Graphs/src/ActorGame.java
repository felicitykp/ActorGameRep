import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
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
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;



public class ActorGame {
	
	/* Here is my "Kevin Bacon Game" that essentially find the degrees of seperation between two actors using
	 * the text files included in the project. I have two additional functionalities that are unique to my project.
	 * The first is an auto fill text feature. I made it so as you typed an actors name it suggested input values. 
	 * If you press the fill button is will accept the auto fill suggestion.
	*/
	
	//VARIABLES
	public HashMap<String, String> actorMap = new HashMap<String, String>();
	public HashMap<String, String> movieMap = new HashMap<String, String>();
	public LabeledGraph<String> connections = new LabeledGraph<String>();
	public Set<String> keywords;
	
	private final int WIDTH = 600, HEIGHT = 800;
	private String DGRAY = "#463F3A", WHITE = "#F4F3EE", MGRAY = "#8A817C", LGRAY = "#BCB8B1", PINK = "#E0AFA0";
	private final int TEXT_HEIGHT = 40;
	private String autofill = "";
	private String autofill2 = "";
	
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
					g.drawString("printed below. Please note that the Autofill function is case sensitive", 20, 75);
	
				}
			};
			topPanel.setBackground(Color.decode(WHITE));
			topPanel.setPreferredSize(new Dimension(WIDTH, 100));
			panel.add(topPanel);
			
			//INPUT PANEL
			JPanel inputPanel = new JPanel();
			inputPanel.setBackground(Color.decode(DGRAY));
			inputPanel.setPreferredSize(new Dimension(WIDTH, 55));
			//ACTOR 1 STUFF
				//PROMPT LABEL
				JLabel actor1Prompt = new JLabel();
				actor1Prompt.setText("Actor 1:");
				actor1Prompt.setForeground(Color.decode(WHITE));
				actor1Prompt.setPreferredSize(new Dimension(50, TEXT_HEIGHT));
				inputPanel.add(actor1Prompt);
				//INPUT AREA
				//setup
				StyleContext sc = new StyleContext();
				final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
				JTextPane actor1Input = new JTextPane(doc);
				actor1Input.setPreferredSize(new Dimension(120, 18));
				inputPanel.add(actor1Input);
				actor1Input.setHighlighter(null);
				//define styles
				Style style1 = sc.addStyle("Heading2", null);
				style1.addAttribute(StyleConstants.Foreground, Color.decode(DGRAY));
				Style style2 = sc.addStyle("Heading2", null);
				style2.addAttribute(StyleConstants.Foreground, Color.decode(MGRAY));
				//key listener
				actor1Input.addKeyListener(new KeyListener() {
					public void keyReleased(KeyEvent e) {
						//find auto fill
						String orgText = actor1Input.getText();
						orgText = orgText.substring(0, orgText.length()-autofill.length());
						for(String key : keywords) {
							String curr = actorMap.get(key);
							if(curr.contains(orgText)) {
								autofill = curr.substring(orgText.length());
								break;
							} 
						}
						//set new text w auto fill
						actor1Input.setText(orgText + autofill);
						doc.setCharacterAttributes(actor1Input.getText().length() - autofill.length(),
								autofill.length(), style2, true);
						doc.setCharacterAttributes(0, actor1Input.getText().length() -  autofill.length(),
								style1, true);
						actor1Input.setCaretPosition(actor1Input.getText().length() - autofill.length());
						
					}
					public void keyPressed(KeyEvent e) {}
					public void keyTyped(KeyEvent e) {}
					
				});
				//AUTOFILL BUTTON
				JButton fill1Button = new JButton("Fill");
				fill1Button.setPreferredSize(new Dimension(65, TEXT_HEIGHT - 20));
				fill1Button.setBackground(Color.decode(PINK));
				fill1Button.setForeground(Color.decode(DGRAY));
				fill1Button.setOpaque(true);
				//fill1Button.setFocusPainted(false);
				fill1Button.setBorderPainted(false);
				fill1Button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//applies the auto fill
						String curr = actor1Input.getText();
						String updated = curr.substring(0, 1).toUpperCase() + curr.substring(1);
						actor1Input.setText(updated);
						doc.setCharacterAttributes(0, curr.length(), style1, true);
					}
				});
				inputPanel.add(fill1Button);
				
			//ACTOR 2 STUFF
				//PROMPT
				JLabel actor2Prompt = new JLabel();
				actor2Prompt.setText("Actor 2:");
				actor2Prompt.setForeground(Color.decode(WHITE));
				actor2Prompt.setPreferredSize(new Dimension(50, TEXT_HEIGHT));
				inputPanel.add(actor2Prompt);
				//INPUT AREA
				//setup
				StyleContext sc2 = new StyleContext();
				final DefaultStyledDocument doc2 = new DefaultStyledDocument(sc2);
				JTextPane actor2Input = new JTextPane(doc2);
				actor2Input.setPreferredSize(new Dimension(120, 18));
				inputPanel.add(actor2Input);
				actor2Input.setHighlighter(null);
				//define styles
				Style style3 = sc2.addStyle("Heading2", null);
				style3.addAttribute(StyleConstants.Foreground, Color.decode(DGRAY));
				Style style4 = sc2.addStyle("Heading2", null);
				style4.addAttribute(StyleConstants.Foreground, Color.decode(MGRAY));
				//key listener
				actor2Input.addKeyListener(new KeyListener() {
					public void keyReleased(KeyEvent e) {
						//find auto fill value
						String orgText = actor2Input.getText();
						orgText = orgText.substring(0, orgText.length()-autofill2.length());
						for(String key : keywords) {
							String curr = actorMap.get(key);
							if(curr.contains(orgText)) {
								autofill2 = curr.substring(orgText.length());
								break;
							} 
						}
						//fill new text area
						actor2Input.setText(orgText + autofill2);
						doc2.setCharacterAttributes(actor2Input.getText().length() - autofill2.length(),
								autofill2.length(), style4, true);
						doc2.setCharacterAttributes(0, actor2Input.getText().length() -  autofill2.length(),
								style3, true);
						actor2Input.setCaretPosition(actor2Input.getText().length() - autofill2.length());
						
					}
					public void keyPressed(KeyEvent e) {}
					public void keyTyped(KeyEvent e) {}
					
				});
				//AUTOFILL BUTTON
				JButton fill2Button = new JButton("Fill");
				fill2Button.setPreferredSize(new Dimension(65, TEXT_HEIGHT - 20));
				fill2Button.setBackground(Color.decode(PINK));
				fill2Button.setForeground(Color.decode(DGRAY));
				fill2Button.setOpaque(true);
				//fill1Button.setFocusPainted(false);
				fill2Button.setBorderPainted(false);
				fill2Button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//applies the auto fill
						String curr = actor2Input.getText();
						String updated = curr.substring(0, 1).toUpperCase() + curr.substring(1);
						actor2Input.setText(updated);
						doc.setCharacterAttributes(0, curr.length(), style3, true);
					}
				});
				inputPanel.add(fill2Button);
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
