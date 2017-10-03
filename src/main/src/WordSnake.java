package main.src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class WordSnake extends Application {

	
	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		Generator generator;
		try {
			generator = new Generator("./test.txt");
		} catch (IOException e) {
			System.out.print("File is not readable");
			return;
		}
		while(true){
			System.out.println();
			
			String start, end;
			
			do{
				System.out.println("Provide the start and end words");
				start = in.next();
				end = in.next();
				if(!validWords(end,start, generator.getWordList())){
					System.err.println("These words are not valid");
				} else {
					break;
				}
			}while(true);
			
			
			
			
			List<String> out = generator.generate(end, start);
			
			if(out != null){
				Iterator<String> itr = out.iterator();
				while(itr.hasNext()){
					System.out.print(itr.next());
					System.out.print(" -> ");
				}
				System.out.println();
				
			} else {
				System.err.print("Nothing is found");
			}
			generator.printStats();
			generator.cleanUp();
		}
		
	} 
	
	private static boolean validWords(String start, String end, List<String> list){
		if(start.length() != end.length()){
			return false;
		}
		return (list.contains(start) && list.contains(end));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hey!");
		
		Button btn = new Button();
		btn.setText("Say nothing");
		
		StackPane root = new StackPane();
		root.getChildren().add(btn);
		
		primaryStage.setScene(new Scene(root, 300,300));
		primaryStage.show();
		
	}

}
