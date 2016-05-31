package Numvc;

import java.util.ArrayList;
import java.util.Random;

public class Graph {
	Vertex[] vertices;
	public static int cutOff = 10;
	double rho = 0.3;
	double gamma = 0.5;
	double mean = 1;
	public long startTime;

	public Graph(int[][] ip) {
		vertices = new Vertex[ip.length];

		for (int i = 0; i < ip.length; i++) {
			vertices[i] = new Vertex(i + 1);
			vertices[i].edges = new Edge[ip[i].length];

			for (int j = 0; j < ip[i].length; j++) {
				vertices[i].edges[j] = new Edge(i + 1, ip[i][j]);
			}
		}
		gamma *= vertices.length;
	}

	public Graph(ArrayList<Integer>[] ip) {
		vertices = new Vertex[ip.length];

		for (int i = 0; i < ip.length; i++) {
			vertices[i] = new Vertex(i + 1);
			vertices[i].edges = new Edge[ip[i].size()];

			for (int j = 0; j < ip[i].size(); j++) {
				vertices[i].edges[j] = new Edge(i + 1, ip[i].get(j));
			}
		}
		gamma *= vertices.length;
	}

	@Override
	public String toString() {
		String res = "";
		int cnt = 0;
		for (Vertex vertex : vertices) {
			if (vertex.isInC) {
				res += vertex.id + " ";
				cnt++;
			}
		}
		System.out.println("Count = " + cnt);
		return res;
	}

	public void computeGreedyVC() {
		Edge e;
		while ((e = uncoveredEdgeExists()) != null) {
			if (!e.covered) {
				addToC(e.id);
			}
		}
		// for (int i = 0; i < this.vertices.length; i++)
		// addToC(i + 1);
	}

	private Edge uncoveredEdgeExists() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Vertex vertex : vertices) {
			for (Edge e : vertex.edges) {
				if (!e.covered) {
					edges.add(e);
				}
			}
		}
		if (edges.isEmpty())
			return null;
		Random rand = new Random();
		return edges.get(rand.nextInt(edges.size()));
	}

	@SuppressWarnings("unused")
	private Edge randomUncovered() {
		Random rand = new Random();
		rand.nextInt(100);
		while (true) {
			int i = rand.nextInt(vertices.length);
			Vertex vertex = vertices[i];
			int j = rand.nextInt(vertex.edges.length);
			for (; j < vertex.edges.length; j++) {
				if (!vertex.edges[j].covered) {
					return vertex.edges[j];
				}
			}
		}
	}

	public void setEdge(int n, boolean v) {
		vertices[n - 1].isInC = true;
		for (Edge e : vertices[n - 1].edges) {
			e.covered = v;
			for (Edge e1 : vertices[e.id - 1].edges) {
				if (e1.id == n) {
					e1.covered = v;
					break;
				}
			}
		}
	}

	public void addToC(int n) {
		setEdge(n, true);
	}

	public void removeFromC(int n) {
		setEdge(n, false);
		vertices[n - 1].isInC = false;
		for (Edge e : vertices[n - 1].edges) {
			if (vertices[e.id - 1].isInC)
				addToC(e.id);
		}
	}

	public void computeNuMVC() {
		int elapsedTime = 1;
		computeGreedyVC();
		while (elapsedTime < cutOff) {
			if (elapsedTime % 100 == 0) {
				System.out.println(elapsedTime);
				System.out.println(this);
				System.out.println("Execution time : "
						+ (System.currentTimeMillis() - this.startTime) + " ms");
				System.out.println();
				System.out.println();
			}
			if (uncoveredEdgeExists() == null) {
				Vertex u = getVertexWithHighestDScoreFromC(elapsedTime);
				removeFromC(u.id);
				vertices[u.id - 1].time = elapsedTime;
				elapsedTime++;
				continue;
			}
			Vertex u = getVertexWithHighestDScoreFromC(elapsedTime);
			removeFromC(u.id);
			vertices[u.id - 1].time = elapsedTime;
			u.confChange = 0;
			for (Edge e : u.edges) {
				vertices[e.id - 1].confChange = 1;
			}
			Edge e = uncoveredEdgeExists();
			Vertex v = chooseOneVertex(e, elapsedTime);
			addToC(v.id);
			vertices[v.id - 1].time = elapsedTime;
			for (Edge e1 : v.edges) {
				vertices[e1.id - 1].confChange = 1;
			}
			weightUpdate();
			updateMean();
			if (mean >= gamma)
				updateWeight();
			elapsedTime++;
		}
	}

	private void updateWeight() {
		for (Vertex vertex : vertices) {
			for (Edge e : vertex.edges) {
				e.w = (int) (rho * e.w);
			}
		}
	}

	private void updateMean() {
		double sum = 0, i = 0;
		for (Vertex vertex : vertices) {
			for (Edge e : vertex.edges) {
				sum += e.w;
				i++;
			}
		}
		mean = sum / i;
	}

	private void weightUpdate() {
		for (Vertex vertex : vertices) {
			for (Edge e : vertex.edges) {
				if (!e.covered) {
					e.w++;
				}
			}
		}
	}

	private Vertex chooseOneVertex(Edge e, int k) {
		updateDScores();

		Vertex res = null;
		if (vertices[e.from - 1].confChange == 1) {
			if (vertices[e.id - 1].confChange == 1) {
				if (vertices[e.id - 1].dscore > vertices[e.from - 1].dscore) {
					res = vertices[e.id - 1];
				} else if (vertices[e.id - 1].dscore == vertices[e.from - 1].dscore) {
					if ((k - vertices[e.id - 1].time) > (k - vertices[e.from - 1].time))
						res = vertices[e.id - 1];
					else
						res = vertices[e.from - 1];
				} else
					res = vertices[e.from - 1];
			} else {
				res = vertices[e.from - 1];
			}
		} else if (vertices[e.id - 1].confChange == 1) {
			res = vertices[e.id - 1];
		}
		return res;
	}

	public Vertex getVertexWithHighestDScoreFromC(int k) {
		updateDScores();
		Vertex high_d = null;
		for (Vertex vertex : vertices) {
			if (vertex.isInC) {
				if (high_d == null) {
					high_d = vertex;
					continue;
				}
				if (vertex.dscore > high_d.dscore) {
					high_d = vertex;
				} else if (vertex.dscore == high_d.dscore) {
					if ((k - vertex.time) > (k - high_d.time))
						high_d = vertex;
				}
			}
		}
		return high_d;
	}

	@SuppressWarnings("unused")
	private Vertex getVertexWithHighestDScore(int k) {
		updateDScores();
		Vertex high_d = vertices[0];
		for (Vertex vertex : vertices) {
			if (vertex.dscore > high_d.dscore) {
				high_d = vertex;
			} else if (vertex.dscore == high_d.dscore) {
				if ((k - vertex.time) > (k - high_d.time))
					high_d = vertex;
			}
		}
		return high_d;
	}

	public void updateDScores() {
		int x = getCost();
		for (Vertex vertex : vertices) {
			int y;
			if (vertex.isInC) {
				removeFromC(vertex.id);
				y = getCost();
				addToC(vertex.id);
			} else {
				addToC(vertex.id);
				y = getCost();
				removeFromC(vertex.id);
			}
			vertex.dscore = x - y;
		}
	}

	private int getCost() {
		int cost = 0;
		for (Vertex vertex : vertices) {
			for (Edge e : vertex.edges) {
				if (!e.covered) {
					cost += e.w;
				}
			}
		}
		return cost / 2;
	}

	public static void main(String[] args) {
		Random rand = new Random();
		rand.nextInt(100);
		System.out.println(rand.nextInt(100));
		System.out.println(rand.nextInt(100));
		System.out.println(rand.nextInt(100));
		System.out.println(rand.nextInt(100));
	}
}
