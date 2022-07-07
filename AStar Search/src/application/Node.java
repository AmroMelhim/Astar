package application;

import java.util.*;

import javafx.scene.shape.Circle;

public class Node implements Comparable<Node> {
      // Id for readability of result purposes
      
      public String Name;
      public double x;
      public double y;

      // Parent in the path
      public Node parent = null;

      public List<Edge> adj;

      // Evaluation functions
      public double f = 0;
      public double g ;
      public double h; 
      Circle c;

      Node(String Name,double h){
    	  	this.Name = Name;
            this.h = h;
           
            this.adj = new ArrayList<>();
      }
      
      Node(String Name,double h,Circle c){
  	  	this.Name = Name;
          this.h = h;
          this.c=c;
         
          this.adj = new ArrayList<>();
    }
      
      Node(String Name,double x, double y )
      {
    	 this.Name= Name;
    	 this.x=x;
    	 this.y=y;
      }
      
      Node(String Name,double x, double y, Circle c )
      {
    	 this.Name= Name;
    	 this.x=x;
    	 this.y=y;
    	 this.c=c;
      }

      @Override
      public int compareTo(Node n) {
            return Double.compare(this.f, n.f);
      }

      public static class Edge {
            Edge(Node node, double weight){
                  this.weight = weight;
                  this.node = node;
            }

            public double weight;
            public Node node;
            
            @Override
        	public String toString() {
        		return node.Name;
        	}
      }

      public void addBranch(double weight, Node node){
            Edge newEdge = new Edge(node, weight);
            adj.add(newEdge);
      }

      @Override
	public String toString() {
		return Name;
	}

	public double calculateH(Node target){
            return this.h;
      }
}