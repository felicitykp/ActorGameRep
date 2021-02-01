import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class UnlabeledGraph<E> {
	
	HashMap<E, Vertex> vertices;
	
	//CONSTRUCTOR
	public UnlabeledGraph() {
		vertices = new HashMap<E, Vertex>();
	}
	
	public void addVertex(E info) {
		vertices.put(info, new Vertex(info));
	}
	
	public void connect(E info1, E info2) {
		Vertex v1 = vertices.get(info1);
		Vertex v2 = vertices.get(info2);
		
		v1.neighbors.add(v2);
		v2.neighbors.add(v1);
	}
	
	public ArrayList<E> BFS(E start, E target) {
		
		//setup array list
		ArrayList<Vertex> toVisit = new ArrayList<Vertex>();
		toVisit.add(vertices.get(start));
		
		//keep track of what we have already visited
		HashSet<Vertex> visited = new HashSet<Vertex>();
		
		//keep track of path
		HashMap<Vertex, Vertex> leadsTo = new HashMap<Vertex, Vertex>();
		
		
		
		//SEARCH
		while(!toVisit.isEmpty()) {
			
			Vertex curr = toVisit.remove(0);
			
			for(Vertex neighbor : curr.neighbors) {
				
				//check if visited
				if(visited.contains(neighbor)) {
					continue;
				}
				
				//establish connection
				leadsTo.put(neighbor, curr);
				
				//check if target
				if(neighbor.info.equals(target)) {
					return backTrace(neighbor, leadsTo);
				}
				
				else {
					toVisit.add(neighbor);
					visited.add(curr);
				}
			}	
		}	
		
		return null;
	}
	
	public ArrayList<E> backTrace(Vertex target, HashMap<Vertex, Vertex> leadsTo) { //WNODHFSIUGH
		
		Vertex curr = target;
		ArrayList<E> path = new ArrayList<E>();
		
		while(curr != null) {
			path.add(0, curr.info);
			curr = leadsTo.get(curr);
		}
		
		return path;
		
	}
	
	
	//VERTEX CLASS
	private class Vertex {
		E info;
		HashSet<Vertex> neighbors;
		
		public Vertex(E info) {
			this.info = info;
			neighbors = new HashSet<Vertex>();
		}
		
		//fix equals method
		public boolean equals(Vertex other) {
			return info.equals(other.info);
		}
		
	}
	
	//MAIN METHOD
	public static void main(String[] args) {
		
		UnlabeledGraph<String> g = new UnlabeledGraph<String>();
		
		g.addVertex("Reina");
		g.addVertex("Felicity");
		g.addVertex("Andria");
		g.addVertex("Elgin");
		g.addVertex("Veronika");
		
		g.connect("Reina", "Veronika");
		g.connect("Reina", "Felicity");
		g.connect("Felicity", "Andria");
		g.connect("Felicity", "Elgin");
		
		g.BFS("Reina", "Elgin");
		
	
	}
	
	
	
}
