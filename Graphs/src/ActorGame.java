import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ActorGame {
	
	//VARIABLES
	public HashMap<String, String> actorMap = new HashMap<String, String>();
	public HashMap<String, String> movieMap = new HashMap<String, String>();
	public LabeledGraph<String> connections = new LabeledGraph<String>();
	
	//CONSTRUCTOR
	public ActorGame() throws IOException {
		
		//create maps
		createActors();
		createMovieMap();
		
		int counter = setupGraph();
		
		System.out.println(counter);
		
		System.out.println(connections.BFS("65731", "15853"));
		
		
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
		BufferedReader am = new BufferedReader(new FileReader("actors.txt"));
		
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
			actorMap.put(arr[0], arr[1]);
			}
				
		mm.close();
		}


	public static void main(String args[]) throws IOException {
		
		new ActorGame();
	}
	
}
