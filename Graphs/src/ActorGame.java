import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;



public class ActorGame {
	
	
	/* Hello! This is my "Kevin Bacon Game" project that finds the degrees of seperation between two actors using
	 * the three text files included in the project. I tried my best to comment this to explain everything I did!!
	 
	 * Extra Functionality 1: AUTOFILL !!
	 		* When you input actors names it will suggest names based on your input
	 		* I accomplish this using a Set of the keywords from the actorMap
	 		* The fill button both applies the current suggestion and reset the auto fill
	 		* this is basically like 5 functionalities in one so you should just marvel in how this works
	 		* and ignore my second functionality :)
	 		
	 * Extra Functionality 2: All movies
	 		* This functionality just lists all the movies that an actor is in. It works even if only 1 actor
	 		* is entered and it is pretty simple (but auto fill was hard so shush)
	 */
	
	
	//VARIABLES
		//major components
			public HashMap<String, String> actorMap = new HashMap<String, String>();
			public HashMap<String, String> movieMap = new HashMap<String, String>();
			public LabeledGraph<String, String> connections = new LabeledGraph<String, String>();
			public Set<String> keywords;
		
		//UI components
			private final int WIDTH = 600, HEIGHT = 800;
			private String DGRAY = "#463F3A", WHITE = "#F4F3EE", MGRAY = "#8A817C", LGRAY = "#BCB8B1", PINK = "#E0AFA0";
			private final int TEXT_HEIGHT = 20;
			
		//string components
			private String autofill = "";
			private String autofill2 = "";
			private String displayed = "";
			
			
			public ArrayList<String> allMov;
	
	//CONSTRUCTOR
	public ActorGame() throws IOException {
		
		//create maps and graph
		createActors();
		createMovieMap();
		int counter = setupGraph();
		
		//set the keywords for autofill
		keywords = actorMap.keySet();

		
		//UI STUFF (sorry it's a lot)

			//MAIN PANEL
			JPanel panel = new JPanel();
			BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
			panel.setLayout(boxlayout);
			panel.setBorder(BorderFactory.createEmptyBorder(20,20, 20, 20));
			panel.setBackground(Color.decode(DGRAY));
			
			
			//INSTRUCTIONS PANEL
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
			panel.add(topPanel); //add to panel **
			
			
			//INPUT PANEL
			JPanel inputPanel = new JPanel();
			inputPanel.setBackground(Color.decode(DGRAY));
			inputPanel.setPreferredSize(new Dimension(WIDTH, 60));
			
			//ACTOR 1 STUFF
			
				//PROMPT LABEL
				JLabel actor1Prompt = new JLabel();
				actor1Prompt.setText("Actor 1:");
				actor1Prompt.setForeground(Color.decode(WHITE));
				actor1Prompt.setPreferredSize(new Dimension(50, TEXT_HEIGHT));
				inputPanel.add(Box.createRigidArea(new Dimension(73, TEXT_HEIGHT))); //SPACING
				inputPanel.add(actor1Prompt); //add to panel
				inputPanel.add(Box.createRigidArea(new Dimension(5, TEXT_HEIGHT))); //SPACING
				
				//INPUT AREA
					//setup
					StyleContext sc = new StyleContext();
					final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
					JTextPane actor1Input = new JTextPane(doc);
					actor1Input.setPreferredSize(new Dimension(200, 18));
					actor1Input.setBackground(Color.decode(WHITE));
					inputPanel.add(actor1Input); //add to panel
					inputPanel.add(Box.createRigidArea(new Dimension(5, TEXT_HEIGHT))); //SPACING
					actor1Input.setHighlighter(null);
					
					//define styles
					Style style1 = sc.addStyle("Heading2", null);
					style1.addAttribute(StyleConstants.Foreground, Color.decode(DGRAY));
					Style style2 = sc.addStyle("Heading2", null);
					style2.addAttribute(StyleConstants.Foreground, Color.decode(MGRAY));
					
					//key listener
					actor1Input.addKeyListener(new KeyAdapter() {
						public void keyReleased(KeyEvent e) {
							
							//run auto fill
							String orgText = actor1Input.getText().trim();
							orgText = orgText.substring(0, orgText.length()-autofill.length());
							for(String key : keywords) {
								String curr = actorMap.get(key);
								if(curr.length() >= orgText.length() && 
										curr.substring(0,orgText.length()).equalsIgnoreCase(orgText)) {
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
				});
				
				//AUTOFILL BUTTON
					//setup
					JButton fill1Button = new JButton("Fill");
					fill1Button.setPreferredSize(new Dimension(65, TEXT_HEIGHT));
					fill1Button.setBackground(Color.decode(PINK));
					fill1Button.setForeground(Color.decode(DGRAY));
					fill1Button.setOpaque(true);
					fill1Button.setBorderPainted(false);
					
					//apply autofill
					fill1Button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {  
							String curr = actor1Input.getText();
							String updated = curr.substring(0, 1).toUpperCase() + curr.substring(1);
							actor1Input.setText(updated);
							doc.setCharacterAttributes(0, curr.length(), style1, true);
						}
					});
				inputPanel.add(fill1Button); //add to panel
				inputPanel.add(Box.createRigidArea(new Dimension(73, TEXT_HEIGHT))); //SPACING
				
			//ACTOR 2 STUFF
				
				//PROMPT LABEL
				JLabel actor2Prompt = new JLabel();
				actor2Prompt.setText("Actor 2:");
				actor2Prompt.setForeground(Color.decode(WHITE));
				actor2Prompt.setPreferredSize(new Dimension(50, TEXT_HEIGHT));
				inputPanel.add(actor2Prompt); //add to panel
				inputPanel.add(Box.createRigidArea(new Dimension(5, TEXT_HEIGHT))); //SPACING
				
				
				//INPUT AREA
					//setup
					StyleContext sc2 = new StyleContext();
					final DefaultStyledDocument doc2 = new DefaultStyledDocument(sc2);
					JTextPane actor2Input = new JTextPane(doc2);
					actor2Input.setPreferredSize(new Dimension(200, 18));
					actor2Input.setBackground(Color.decode(WHITE));
					inputPanel.add(actor2Input);
					inputPanel.add(Box.createRigidArea(new Dimension(5, TEXT_HEIGHT))); //SPACING
					actor2Input.setHighlighter(null);
					
					//define styles
					Style style3 = sc2.addStyle("Heading2", null);
					style3.addAttribute(StyleConstants.Foreground, Color.decode(DGRAY));
					Style style4 = sc2.addStyle("Heading2", null);
					style4.addAttribute(StyleConstants.Foreground, Color.decode(MGRAY));
				
					//key listener
					actor2Input.addKeyListener(new KeyAdapter() {
						public void keyReleased(KeyEvent e) {
							
							//run autofill
							String orgText = actor2Input.getText().trim();
							orgText = orgText.substring(0, orgText.length()-autofill2.length());
							for(String key : keywords) {
								String curr = actorMap.get(key);
								if(curr.length() >= orgText.length() && 
										curr.substring(0,orgText.length()).equalsIgnoreCase(orgText)) {
									autofill2 = curr.substring(orgText.length());
									break;
								} 
							}
							//set new text w auto fill
							actor2Input.setText(orgText + autofill2);
							doc2.setCharacterAttributes(actor2Input.getText().length() - autofill2.length(),
									autofill2.length(), style4, true);
							doc2.setCharacterAttributes(0, actor2Input.getText().length() -  autofill2.length(),
									style3, true);
							actor2Input.setCaretPosition(actor2Input.getText().length() - autofill2.length());
						}
						
					});
					
				//AUTOFILL BUTTON
					//setup
					JButton fill2Button = new JButton("Fill");
					fill2Button.setPreferredSize(new Dimension(65, TEXT_HEIGHT));
					fill2Button.setBackground(Color.decode(PINK));
					fill2Button.setForeground(Color.decode(DGRAY));
					fill2Button.setOpaque(true);
					fill2Button.setBorderPainted(false);
					
					//apply auto fill
					fill2Button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							//applies the auto fill
							String curr = actor2Input.getText();
							String updated = curr.substring(0, 1).toUpperCase() + curr.substring(1);
							actor2Input.setText(updated);
							doc2.setCharacterAttributes(0, curr.length(), style3, true);
							autofill2 = "";
						}
					});
				inputPanel.add(fill2Button); //add to panel
				panel.add(inputPanel); //add to panel  **
			
			//OUTPUT PANEL
				//setup
				JPanel outputPanel = new JPanel();
				outputPanel.setBackground(Color.decode(LGRAY));
				outputPanel.setPreferredSize(new Dimension(WIDTH, 505));
				
				//display text for panel
				JTextArea displayText = new JTextArea();
				displayText.setForeground(Color.decode(DGRAY));
				displayText.setBackground(Color.decode(LGRAY));
				displayText.setPreferredSize(new Dimension(WIDTH - 150, 500));
				displayText.setEditable(false);
				displayText.setText(displayed);        				//this line is where i set the output initially
				outputPanel.add(displayText);
				panel.add(outputPanel); //add to panel **
			
			//BUTTON PANEL
				//setup
				JPanel buttonPanel = new JPanel();
				buttonPanel.setBackground(Color.decode(DGRAY));
				buttonPanel.setPreferredSize(new Dimension(WIDTH, 50));
				buttonPanel.add(Box.createRigidArea(new Dimension(WIDTH, TEXT_HEIGHT / 2))); //SPACING
				
					//BFS Button
					JButton distButton = new JButton("Calculate Distance");
					distButton.setPreferredSize(new Dimension(180, TEXT_HEIGHT));
					distButton.setBackground(Color.decode(PINK));
					distButton.setForeground(Color.decode(DGRAY));
					distButton.setOpaque(true);
					distButton.setBorderPainted(false);
					distButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							distSearch(actor1Input.getText().trim(), actor2Input.getText().trim());
							displayText.setText("\n\n\n" + displayed);
						}});
					
				buttonPanel.add(distButton); //add to panel
				buttonPanel.add(Box.createRigidArea(new Dimension(15, TEXT_HEIGHT / 2))); //SPACING
			
					//Second button
					JButton numButton = new JButton("List All Movies");
					numButton.setPreferredSize(new Dimension(150, TEXT_HEIGHT));
					numButton.setBackground(Color.decode(PINK));
					numButton.setForeground(Color.decode(DGRAY));
					numButton.setOpaque(true);
					numButton.setBorderPainted(false);
					numButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							
							listAllMovies(actor1Input.getText().trim(), actor2Input.getText().trim());
							displayText.setText("\n\n\n" + displayed);
							
							
					}});
					buttonPanel.add(numButton); //add to panel
				panel.add(buttonPanel); //add to panel **
			
			//MAIN CONTAINER
			JFrame frame = new JFrame();
			frame.setSize(WIDTH, HEIGHT);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(panel);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			frame.setVisible(true);
			panel.setFocusable(true);
		
	}
	
	
	//This is where I run my BFS search to find the distance between the two actors
	public void distSearch(String actor1, String actor2) {
		
		//checks if they are valid inputs
		if(isValid(actor1) == true && isValid(actor2) == true) {
			
			//runs the search
			ArrayList<Object> path = connections.BFS(actor1, actor2);
			System.out.println(path);
			
			//if directly connected
			if(path.size() == 3) { 
				displayed = "Whoa " + path.get(0) + " and " + path.get(2) + " were both in " + path.get(1);
			} 
			
			//if connected through others
			else {
				displayed = "Let's see... \n\n";
				for(int i = 0; i < path.size() - 1; i = i+2) {
					displayed += path.get(i) + " was in " + path.get(i+1) + " with " + path.get(i+2) + "\n\n";
				}	
			}
			
		// if not valid inputs
		} else {
			displayed = "Please enter two valid actors for search";
			System.out.println("Error: Not Valid Inputs");
		}
	}
	
	
	public void listAllMovies(String actor1, String actor2) {
		
		//if both valid
		if(isValid(actor1) == true && isValid(actor2) == true) {
			
			//gets list of all movies from graph class
			ArrayList<String> act1Movies = connections.getVertex(actor1).getEdges();
			ArrayList<String> act2Movies = connections.getVertex(actor2).getEdges();
			
			//formats the movies
			String act1String = "";
			for(int i = 0; i < act1Movies.size(); i++) {
				act1String += "\n" + act1Movies.get(i);
			}
			String act2String = "";
			for(int i = 0; i < act2Movies.size(); i++) {
				act2String += "\n" + act2Movies.get(i);
			}
			
			
			//sets the display text
			displayed = actor1 + " was in:\n" + act1String + "\n\n";
			displayed += actor2 + " was in:\n" + act2String;
			
		} else if(isValid(actor1) == true) {
			
			//same as above but just actor 1
			ArrayList<String> act1Movies = connections.getVertex(actor1).getEdges();
			String act1String = "";
			for(int i = 0; i < act1Movies.size(); i++) {
				act1String += "\n" + act1Movies.get(i);
			}
			displayed = actor1 + " was in:\n" + act1String;
		} else if (isValid(actor2) == true){
			
			//same as above just actor 2
			ArrayList<String> act2Movies = connections.getVertex(actor2).getEdges();
			String act2String = "";
			for(int i = 0; i < act2Movies.size(); i++) {
				act2String += "\n" + act2Movies.get(i);
			}
			displayed = actor2 + " was in:\n" + act2String;
		} else {
			displayed = "Please enter at least one valid Actor Name";
			System.out.println("Error: Not Valid Inputs");
		}
		
		
		
		
	}
	
	
	
	//checks if actors names are valid
	public boolean isValid(String temp) {
		
		//converts to lower case
		temp.toLowerCase();
		
		//check if actors in map
		if(actorMap.containsValue(temp)) {
			return true;
		} else {
			return false;
		}
	}
	
	//builds the actual graph
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
			
			//checks if match 
			if(currKey.equals(prevKey)) {
				currMovie.add(actorMap.get(arr[1]));
			} else {
				
				//connect everything
				for(int i = 0; i < currMovie.size(); i++) {
					for(int j = i+1; j < currMovie.size(); j++) {
						connections.connect(currMovie.get(i), currMovie.get(j), movieMap.get(currKey));
						counter++;
					}
				}
				
				//reset for next movie
				prevKey = arr[0];
				currMovie.clear();
				currMovie.add(actorMap.get(arr[1]));
			}
		}	
		
		sg.close();
		
		return counter;
	}
	
	//builds actor map
	public void createActors() throws IOException {
		
		//setup buffered reader
		BufferedReader am = new BufferedReader(new FileReader("actors2.txt"));
		
		//loop through file
		for (String line = am.readLine(); line != null; line = am.readLine()) {	
			
			//split then add to map
			String[] arr = line.split("~");
			arr[1].toLowerCase();
			
			actorMap.put(arr[0], arr[1]);
			connections.addVertex(arr[1]);
			
		}
		
		am.close();
	}
	
	//builds movie map
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


	//MAIN METHOD
	public static void main(String args[]) throws IOException {
		
		new ActorGame(); 
		//whoa look how nice and clean this is :)
	}
	
}
