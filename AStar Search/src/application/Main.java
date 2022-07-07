package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Main extends Application {
	//Array list for vilgs with their H values
	static ArrayList<Node> vilgH = new ArrayList<Node>();
	//Array List for vilgs when read from file
	static ArrayList<Node> vilg = new ArrayList<Node>();
	//array list for the final path
	static List<Node> path = new ArrayList<Node>();
	//printable string for GUI
	static String printPath;
	public static int click = 0;
	public static boolean isclicksUsed = false;
	public static Node source, goal;
	//Array list to print lines on map
	 ArrayList<Node> lineList= new ArrayList<Node>();
	 //Array list to delete lines on map
	 ArrayList<Line> lines = new ArrayList<Line>();
	public void start(Stage primaryStage) throws FileNotFoundException {
		Button run = new Button();
		run.setText("Run");
		run.setLayoutX(1160 + 40);
		run.setLayoutY(200);
		run.setPrefWidth(40);

		ComboBox combo1 = new ComboBox();
		combo1.setLayoutX(1100 + 40);
		combo1.setLayoutY(70);
		combo1.setPrefWidth(200);
		combo1.setEditable(true);

		Label combo1Label = new Label();
		combo1Label.setText("Source:");
		combo1Label.setLayoutX(1050 + 40);
		combo1Label.setLayoutY(70);

		ComboBox combo2 = new ComboBox();
		combo2.setLayoutX(1100 + 40);
		combo2.setLayoutY(140);
		combo2.setPrefWidth(200);
		combo2.setEditable(true);

		Label combo2Label = new Label();

		combo2Label.setText("Target:");
		combo2Label.setLayoutX(1050 + 40);
		combo2Label.setLayoutY(140);

		TextArea textArea = new TextArea();
		textArea.setLayoutX(1080 + 40);
		textArea.setLayoutY(250);
		textArea.setPrefHeight(230);
		textArea.setPrefWidth(250);

		Label textAreaLabel = new Label();
		textAreaLabel.setText("Path:");
		textAreaLabel.setLayoutX(1050 + 40);
		textAreaLabel.setLayoutY(230);

		TextField textField = new TextField();
		textField.setLayoutX(1120 + 40);
		textField.setLayoutY(500);

		Label textFieldLabel = new Label();
		textFieldLabel.setText("Distance:");
		textFieldLabel.setLayoutX(1060 + 40);
		textFieldLabel.setLayoutY(500);

		Line line = new Line();
		line.setStartX(1020 + 40);
		line.setStartY(0);
		line.setEndX(1020 + 40);
		line.setEndY(1000);

		FileInputStream input = new FileInputStream("map.JPG");
		Image image = new Image(input);
		ImageView mv = new ImageView(image);

		mv.setFitHeight(800);
		mv.setFitWidth(1020 + 40);

		mv.setLayoutX(0);
		Pane pane = new Pane();

		pane.getChildren().add(mv);

		for (int i = 0; i < vilg.size(); i++) {

			vilg.get(i).c.setFill(Color.RED);
			pane.getChildren().add(vilg.get(i).c);
		}

		pane.addEventHandler(MouseEvent.ANY, event -> {
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				isclicksUsed = true;
				for (int i = 0; i < vilg.size(); i++) {
					if (vilg.get(i).x + 6 > event.getX() && vilg.get(i).x - 6 < event.getX()
							&& vilg.get(i).y + 6 > event.getY() && vilg.get(i).y - 6 < event.getY()) {
						if (vilg.get(i).c.getFill().equals(Color.RED)) {
							click++;

							if (click == 1) {
								vilg.get(i).c.setFill(Color.BLUE);
								source = vilg.get(i);
								combo1.setValue(source.Name);

							}
							if (click == 2) {
								vilg.get(i).c.setFill(Color.BLUE);
								goal = vilg.get(i);
								combo2.setValue(goal.Name);

							}

						}
						break;
					}
				}
			}

		});

		Button reset = new Button();
		reset.setText("Reset");
		reset.setLayoutX(1200 + 40);
		reset.setLayoutY(200);
		reset.setPrefWidth(60);

		ObservableList<Node> observableList = FXCollections.observableList(vilg);
		combo1.setItems(observableList);
		combo2.setItems(observableList);
		pane.getChildren().addAll(run, combo1, combo2, textArea, combo1Label, combo2Label, textAreaLabel, textField,
				textFieldLabel, line, reset);

		Scene scene = new Scene(pane, 850, 750);

		pane.setOnMouseClicked(e -> {
			System.out.println("x" + "is :" + e.getX());
			System.out.println("y" + "is :" + e.getY());
			System.out.println("----------");
		});

		primaryStage.setTitle("AI");
		primaryStage.setScene(scene);
		primaryStage.show();

		run.setOnAction((ActionEvent event) -> {

			if (combo1.getValue() == null || combo2.getValue() == null) {
				textArea.setText("you should choose two countries");
				return;
			}
			String sourceName = combo1.getValue().toString();

			String goalName = combo2.getValue().toString();

			if (goalName == sourceName) {
				textArea.setText("Please enter two different countries");
				return;
			}

			source = vilg.get(findIndex(sourceName, vilg));
			goal = vilg.get(findIndex(goalName, vilg));
			try {
				makeList(goal);
			} catch (NumberFormatException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			source = vilgH.get(findIndex(sourceName, vilgH));
			goal = vilgH.get(findIndex(goalName, vilgH));

			aStar(source, goal);
			path = printPath(goal);
			
			textArea.setText(printPath);
            textField.setText(goal.g /5 +"KM");
            for (int i = 0; i < path.size() - 1; i++) {
                Line CityLine = new Line();
                CityLine.setStartX(path.get(i).x);
                CityLine.setStartY(path.get(i).y);
                CityLine.setEndX(path.get(i + 1).x);
                CityLine.setEndY(path.get(i + 1).y);
                pane.getChildren().add(CityLine);
                lines.add(CityLine);

		}});

		reset.setOnAction((ActionEvent event) -> {
			 click = 0;
	            for (int i = 0; i < vilg.size(); i++) {
	                vilg.get(i).c.setFill(Color.RED);
	            }
	            for (int i = 0; i < lines.size(); i++) {
	                pane.getChildren().remove(lines.get(i));
	            }

	            lineList.clear();
	            lines.clear();

	            combo1.setValue(null);
	            combo2.setValue(null);
	            textArea.setText("");
	            textField.setText("");
	        });
		

		primaryStage.setFullScreen(true);
	}

	public static void main(String[] args) throws IOException {

		File file = new File("places.txt");
		// define a buffer reader to read text
		BufferedReader br = new BufferedReader(new FileReader(file));
		try {
			String line;

			while ((line = br.readLine()) != null) {
				String[] str = line.split(" ");

				Circle point = new Circle(Double.parseDouble(str[1]), Double.parseDouble(str[2]), 3);
				Node vilgTemp = new Node(str[0], Double.parseDouble(str[1]), Double.parseDouble(str[2]), point);

				vilg.add(vilgTemp);

			}
		}

		finally {
			br.close();
		}

		for (int i = 0; i < vilgH.size(); i++) {

			System.out.print(vilgH.get(i).Name + " ");
			System.out.print(vilgH.get(i).h);
			System.out.println();
		}

		for (int i = 0; i < vilgH.size(); i++) {
			Node p = vilgH.get(i);
			System.out.println(p.Name);
			System.out.print(p.adj.toString());
			System.out.println();
		}

		System.out.println("Path: " + path);

		launch(args);

	}
	//method that takes arraylist and string and return the index of the element that is equal to the string
	public static int findIndex(String key, ArrayList<Node> list) {
		int index = 0;
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).Name.compareTo(key) == 0)
				index = i;

		}
		return index;
	}

	//make list method creates the VilgH method with H values and adj vilgs from roads file
	public static void makeList(Node goal) throws NumberFormatException, IOException {
		for (int i = 0; i < vilg.size(); i++) {
			double ac = Math.abs(goal.y - vilg.get(i).y);
			double cb = Math.abs(goal.x - vilg.get(i).x);

			double h = Math.hypot(ac, cb);

			Node temp = new Node(vilg.get(i).Name, h, vilg.get(i).c);
			temp.x = vilg.get(i).x;
			temp.y = vilg.get(i).y;
			
			vilgH.add(temp);

		}
		File file2 = new File("roads.txt");
		// define a buffer reader to read text
		BufferedReader br2 = new BufferedReader(new FileReader(file2));
		try {
			String line;

			while ((line = br2.readLine()) != null) {
				String[] str = line.split(" ");
				for (int i = 0; i < vilgH.size(); i++) {
					if (vilgH.get(i).Name.compareTo(str[0]) == 0) {
						vilgH.get(i).addBranch(Double.parseDouble(str[2]), vilgH.get(findIndex(str[1], vilgH)));
					}

					else if (vilgH.get(i).Name.compareTo(str[1]) == 0) {
						vilgH.get(i).addBranch(Double.parseDouble(str[2]), vilgH.get(findIndex(str[0], vilgH)));
					}

				}

			}
		}

		finally {
			br2.close();
		}

	}

	public static Node aStar(Node start, Node target) {
		PriorityQueue<Node> closedList = new PriorityQueue<>();
		PriorityQueue<Node> openList = new PriorityQueue<>();

		start.f = start.g + start.calculateH(target);
		openList.add(start);

		while (!openList.isEmpty()) {
			Node n = openList.peek();
			if (n == target) {
				return n;
			}

			for (Node.Edge edge : n.adj) {
				Node m = edge.node;
				double totalWeight = n.g + edge.weight;

				if (!openList.contains(m) && !closedList.contains(m)) {
					m.parent = n;
					m.g = totalWeight;
					m.f = m.g + m.calculateH(target);
					openList.add(m);
				} else {
					if (totalWeight < m.g) {
						m.parent = n;
						m.g = totalWeight;
						m.f = m.g + m.calculateH(target);

						if (closedList.contains(m)) {
							closedList.remove(m);
							openList.add(m);
						}
					}
				}
			}

			openList.remove(n);
			closedList.add(n);
		}
		return null;
	}

	public static List<Node> printPath(Node target) {
		List<Node> path = new ArrayList<Node>();

		for (Node node = target; node != null; node = node.parent) {
			path.add(node);
		}

		Collections.reverse(path);
		
		 printPath = path.get(0).Name+"\n";
		for(int i=1;i < path.size();i++)
		{
			printPath += " --->" + path.get(i).Name +"\n";
		}
		
		if(path.size() < 2)
			printPath = "No Path Found";
		return path;
	}
}
