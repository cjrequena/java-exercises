package com.sample.algorithms.dijkstra;

/**
 * Dijkstra's algorithm,is a dijkstra search algorithm that solves the single-source
 * shortest path problem for a dijkstra with nonnegative edge path costs, producing
 * a shortest path tree.
 * <p>
 * NOTE:  The inputs to Dijkstra's algorithm are a directed and weighted dijkstra consisting
 * of 2 or more nodes, generally represented by an adjacency matrix or list, and a start node.
 * <p>
 * Original source of code: https://rosettacode.org/wiki/Dijkstra%27s_algorithm#Java
 * Also most of the comments are from RosettaCode.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Dijkstra {

  private static final Graph.Edge[] GRAPH = {
    // Distance from node "a" to node "b" is 7.
    // In the current Graph there is no way to move the other way (e,g, from "b" to "a"),
    // a new edge would be needed for that
    new Graph.Edge("a", "b", 7),
    new Graph.Edge("a", "c", 9),
    new Graph.Edge("a", "f", 14),
    new Graph.Edge("b", "c", 10),
    new Graph.Edge("b", "d", 15),
    new Graph.Edge("c", "d", 11),
    new Graph.Edge("c", "f", 2),
    new Graph.Edge("d", "e", 6),
    new Graph.Edge("e", "f", 9),
  };
  private static final String START = "a";
  private static final String END = "e";

  /**
   * main function
   * Will run the code with "GRAPH" that was defined above.
   */
  public static void main(String[] args) {
    Graph g = new Graph(GRAPH);
    g.dijkstra(START);
    g.printPath(END);
    //g.printAllPaths();
  }
}

class Graph {

  // mapping of vertex names to Vertex objects, built from a set of Edges
  private final Map<String, Vertex> graph;

  /**
   * Builds a dijkstra from a set of edges
   */
  public Graph(Edge[] edges) {
    graph = new HashMap<>(edges.length);

    // one pass to find all vertices
    for (Edge e : edges) {
      if (!graph.containsKey(e.vertex1)) graph.put(e.vertex1, new Vertex(e.vertex1));
      if (!graph.containsKey(e.vertex2)) graph.put(e.vertex2, new Vertex(e.vertex2));
    }

    // another pass to set neighbouring vertices
    for (Edge e : edges) {
      graph.get(e.vertex1).neighbours.put(graph.get(e.vertex2), e.distance);
      // dijkstra.get(e.vertex2).neighbours.put(dijkstra.get(e.vertex1), e.distance); // also do this for an undirected dijkstra
    }
  }

  /**
   * One edge of the dijkstra (only used by Graph constructor)
   */
  public static class Edge {
    public final String vertex1, vertex2;
    public final int distance;

    public Edge(String vertex1, String vertex2, int distance) {
      this.vertex1 = vertex1;
      this.vertex2 = vertex2;
      this.distance = distance;
    }
  }

  /**
   * One vertex of the dijkstra, complete with mappings to neighbouring vertices
   */
  public static class Vertex implements Comparable<Vertex> {
    public final String name;
    // MAX_VALUE assumed to be infinity
    public int distance = Integer.MAX_VALUE;
    public Vertex previous = null;
    public final Map<Vertex, Integer> neighbours = new HashMap<>();

    public Vertex(String name) {
      this.name = name;
    }

    private void printPath() {
      if (this == this.previous) {
        System.out.printf("%s", this.name);
      } else if (this.previous == null) {
        System.out.printf("%s(unreached)", this.name);
      } else {
        this.previous.printPath();
        System.out.printf(" -> %s(%d)", this.name, this.distance);
      }
    }

    public int compareTo(Vertex other) {
      if (distance == other.distance)
        return name.compareTo(other.name);

      return Integer.compare(distance, other.distance);
    }

    @Override
    public String toString() {
      return "(" + name + ", " + distance + ")";
    }
  }

  /**
   * Runs dijkstra using a specified source vertex
   */
  public void dijkstra(String startName) {
    if (!graph.containsKey(startName)) {
      System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
      return;
    }
    final Vertex source = graph.get(startName);
    NavigableSet<Vertex> q = new TreeSet<>();

    // set-up vertices
    for (Vertex v : graph.values()) {
      v.previous = v == source ? source : null;
      v.distance = v == source ? 0 : Integer.MAX_VALUE;
      q.add(v);
    }

    dijkstra(q);
  }

  /**
   * Implementation of dijkstra's algorithm using a binary heap.
   */
  private void dijkstra(final NavigableSet<Vertex> q) {
    Vertex u, v;
    while (!q.isEmpty()) {
      // vertex with shortest distance (first iteration will return source)
      u = q.pollFirst();
      if (u.distance == Integer.MAX_VALUE)
        break; // we can ignore u (and any other remaining vertices) since they are unreachable

      // look at distances to each neighbour
      for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {
        v = a.getKey(); // the neighbour in this iteration

        final int alternateDist = u.distance + a.getValue();
        if (alternateDist < v.distance) { // shorter path to neighbour found
          q.remove(v);
          v.distance = alternateDist;
          v.previous = u;
          q.add(v);
        }
      }
    }
  }

  /**
   * Prints a path from the source to the specified vertex
   */
  public void printPath(String endName) {
    if (!graph.containsKey(endName)) {
      System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
      return;
    }

    graph.get(endName).printPath();
    System.out.println();
  }

  /**
   * Prints the path from the source to every vertex (output order is not guaranteed)
   */
  public void printAllPaths() {
    for (Vertex v : graph.values()) {
      v.printPath();
      System.out.println();
    }
  }
}
