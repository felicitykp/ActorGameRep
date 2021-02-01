import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LabeledGraph<E> {
	
	HashMap<E, Vertex> vertices;
	
	public LabeledGraph() {
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

	
	
	private class Vertex {
		E info;
		HashSet<Vertex> neighbors;
		
		public Vertex(E info) {
			this.info = info;
			neighbors = new HashSet<Vertex>();
		}
	}
	
	
	public ArrayList<E> BFS(E start, E target) {
		
		ArrayList<Vertex> toVisit = new ArrayList<Vertex>();
		toVisit.add(vertices.get(start));
		HashSet<Vertex> visited = new HashSet<Vertex>();
		visited.add(vertices.get(start));
		
		HashMap<Vertex, Vertex> leadsTo = new HashMap<Vertex, Vertex>();
		
		while (!toVisit.isEmpty()) {
			
			Vertex curr = toVisit.remove(0);
			
			for (Vertex neighbor : curr.neighbors) {
				
				if (visited.contains(neighbor)) continue;
				

				leadsTo.put(neighbor, curr);
				
				if (neighbor.info.equals(target)) {
					
					return backtrace(neighbor, leadsTo);
				}
				
				else {
					toVisit.add(neighbor);
					visited.add(neighbor);
				}
			}
		}
		return null;
	}
	
	public ArrayList<E> backtrace(Vertex target, HashMap<Vertex, Vertex> leadsTo) {
		
		Vertex curr = target;
		ArrayList<E> path = new ArrayList<E>();
		
		while (curr != null) {
			path.add(0, curr.info);
			curr = leadsTo.get(curr);
		}
		return path;
		
	}
	
	public static void main(String[] args) {
		
		LabeledGraph<String> g = new LabeledGraph<String>();
		
	}
}
