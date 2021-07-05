//----------------------------------------------------------------------------------------------------
//LightChange.java
//Author: Evan Conway
//Creates a window displaying a traffic light. This light can be altered by pressing a button.
//----------------------------------------------------------------------------------------------------

import javafx.application.Application;
import javafx.event.*;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class LightChange extends Application {
	//enum to track the current light state
	private enum LightState {RED, YELLOW, GREEN};
	private LightState state = LightState.GREEN;
	
	//----------------------------------------------------------------------------------------------------
	//creates the light display
	//changes the light when the orange button is clicked
	//----------------------------------------------------------------------------------------------------
	public void start(Stage primaryStage) {
		//light circles
		Circle redLight, yellowLight, greenLight;
		
		//button and button space
		Rectangle buttonSpace;
		Circle button;
		
		//these are the standard measurements for U.S. stop lights (in inches)
		//magnifyBy changes the window size
		final double magnifyBy = 10.0;
		final double lightWidth = 13.5 * magnifyBy;
		final double lightHeight = 42.0 * magnifyBy;
		final double signalRadius = 6.0 * magnifyBy;
		
		//set up the traffic lights
		redLight = new Circle((lightWidth/2.0), (lightHeight/6.0), signalRadius);
        redLight.setFill(Color.BLACK);
        
        yellowLight = new Circle((lightWidth/2.0), (lightHeight/6.0 + lightHeight * 1.0/3.0), signalRadius);
        yellowLight.setFill(Color.BLACK);
        
        greenLight = new Circle((lightWidth/2.0), (lightHeight/6.0 + lightHeight * 2.0/3.0), signalRadius);
        greenLight.setFill(Color.GREEN);
        
        //set up the button to advance the light
        //+5 to lightWidth is because some black was showing at the edges
        buttonSpace = new Rectangle(0, lightHeight, lightWidth+5, lightWidth+5);
        buttonSpace.setFill(Color.GRAY);
        
        button = new Circle((lightWidth/2.0), (lightHeight/6.0 + lightHeight), signalRadius);
        button.setFill(Color.ORANGE);
        button.setStyle("-fx-cursor: hand;");
        
        //change the light when the button is clicked
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				switch(state) {
					case GREEN:
						greenLight.setFill(Color.BLACK);
						yellowLight.setFill(Color.YELLOW);
						state = LightState.YELLOW;
						break;
					case YELLOW:
						yellowLight.setFill(Color.BLACK);
						redLight.setFill(Color.RED);
						state = LightState.RED;
						break;
					case RED:
						redLight.setFill(Color.BLACK);
						greenLight.setFill(Color.GREEN);
						state = LightState.GREEN;
						break;
				}
			}
		};
		button.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
		
		//display traffic light as window of set size
		Group root = new Group(redLight, yellowLight, greenLight, buttonSpace, button);
        Scene scene = new Scene(root, lightWidth, (lightHeight + lightWidth), Color.BLACK);
        primaryStage.setTitle("Light");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }
	
	//----------------------------------------------------------------------------------------------------
	//starts the program
	//----------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
    	launch(args);
    }
}