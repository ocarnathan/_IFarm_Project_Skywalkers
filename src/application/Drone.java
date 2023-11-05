package application;

import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.scene.image.ImageView;

public class Drone {
    private static Drone instance = null;
    private ImageView droneImage;
    private double sceneWidth;
    private double sceneHeight;
    private double droneWidth;
    private double droneHight;
    private Animation droneAnimation;

    private Drone(AnchorPane visualizer) {
        // Create the ImageView for the drone image
    	ImageView droneImageView = new ImageView(new Image("drone.png"));
        this.droneImage = droneImageView;

        this.sceneWidth = 800;
        this.sceneHeight = 600;
        this.droneWidth = droneImage.getLayoutBounds().getWidth();
        this.droneHight = droneImage.getLayoutBounds().getHeight();
        
        droneImageView.setLayoutX(sceneWidth - droneWidth);
        droneImageView.setLayoutY(0);

        // Add the drone image to the Visualizer AnchorPane
        visualizer.getChildren().add(droneImage);
      
    }

    public static Drone getInstance(AnchorPane visualizer) {
        if (instance == null) {
            instance = new Drone(visualizer);
        }
        return instance;
    }

    public void startAnimation() {
        double topY = 0;
        double bottomY = sceneHeight - droneHight;
        

        SequentialTransition animation = new SequentialTransition();

        for (int i = 1; i <= 10; i=i+2) {
            // Adjust the X movement value based on the loop index
            double xMove = i * droneWidth;

            Timeline moveDown = moveY(bottomY);
            Timeline rotate90 = rotate(90);
            Timeline moveLeft = moveX(xMove);
            Timeline rotate90_2 = rotate(180);
            Timeline moveUp = moveY(topY);
            Timeline rotate90_3 = rotate(90);
            Timeline moveLeft2 = moveX(xMove + droneWidth);
            Timeline rotate90_4 = rotate(0);

            animation.getChildren().addAll(moveDown, rotate90, moveLeft, rotate90_2, moveUp, rotate90_3, moveLeft2, rotate90_4);
        }
        Timeline moveDown_2 = moveY(bottomY);
        animation.getChildren().add(moveDown_2);

        animation.play();
       // System.out.println("x pos: "+droneImage.getTranslateX()+" y pos: "+ droneImage.getTranslateY());
        animation.setOnFinished(event -> returnHome());
        
        
    }
    

    public void returnHome() {
    	double targetX = 0;
    	double targetY = 0;
        double currentX = -droneImage.getTranslateX();
        double currentY = droneImage.getTranslateY();
        
        // Calculate the angle to rotate to face the target position
        double angle = Math.toDegrees(Math.atan2(targetY - currentY, targetX - currentX));
        
        // Create a timeline for rotation
        Timeline rotateTimeline = new Timeline();
        KeyValue rotateKeyValue = new KeyValue(droneImage.rotateProperty(), angle);
        KeyFrame rotateKeyFrame = new KeyFrame(Duration.seconds(1), rotateKeyValue);
        rotateTimeline.getKeyFrames().add(rotateKeyFrame);
        
        // Create a timeline for translation
        Timeline translateTimeline = new Timeline();
        KeyValue xKeyValue = new KeyValue(droneImage.translateXProperty(), targetX);
        KeyValue yKeyValue = new KeyValue(droneImage.translateYProperty(), targetY);
        KeyFrame translateKeyFrame = new KeyFrame(Duration.seconds(1), xKeyValue, yKeyValue);
        translateTimeline.getKeyFrames().add(translateKeyFrame);
        
        // Create a sequential transition to play both animations
        SequentialTransition returnHomeAnimation = new SequentialTransition(rotateTimeline, translateTimeline);
        
        // Set an event handler to reset the rotation to 0 when the animation is finished
        
        
        // Play the animation
        returnHomeAnimation.play();
        returnHomeAnimation.setOnFinished(event -> 
            droneImage.setRotate(0)
        );
    }
    
    public void goToItem(double targetX, double targetY) {
    	targetX = targetX + droneWidth/2;
    	targetY = targetY - droneHight/2;
        double currentX = -droneWidth;
        double currentY = 0;
        
        // Calculate the angle to rotate to face the target position
        double angle = Math.toDegrees(Math.atan2(targetY - currentY, targetX - currentX));
        
        // Create a timeline for rotation
        Timeline rotateTimeline = new Timeline();
        KeyValue rotateKeyValue = new KeyValue(droneImage.rotateProperty(), angle-90);
        KeyFrame rotateKeyFrame = new KeyFrame(Duration.seconds(1), rotateKeyValue);
        rotateTimeline.getKeyFrames().add(rotateKeyFrame);
        
        // Create a timeline for translation
        Timeline translateTimeline = new Timeline();
        KeyValue xKeyValue = new KeyValue(droneImage.translateXProperty(), targetX);
        KeyValue yKeyValue = new KeyValue(droneImage.translateYProperty(), targetY);
        KeyFrame translateKeyFrame = new KeyFrame(Duration.seconds(1), xKeyValue, yKeyValue);
        translateTimeline.getKeyFrames().add(translateKeyFrame);
        
        // Create a sequential transition to play both animations
        SequentialTransition goToItemAnimation = new SequentialTransition(rotateTimeline, translateTimeline);
        
        // Set an event handler to reset the rotation to 0 when the animation is finished
        
        
        // Play the animation
        goToItemAnimation.play();
        goToItemAnimation.setOnFinished(event -> returnHome());
    }



    private Timeline moveY(double to) {
        Duration duration = Duration.seconds(0.5);
        KeyValue keyValue = new KeyValue(droneImage.translateYProperty(), to);
        KeyFrame keyFrame = new KeyFrame(duration, keyValue);
        return new Timeline(keyFrame);
    }
    
    private Timeline moveX(double dist) {
        Duration duration = Duration.seconds(0.2);
        KeyValue keyValue = new KeyValue(droneImage.translateXProperty(),-dist);
        KeyFrame keyFrame = new KeyFrame(duration, keyValue);
        return new Timeline(keyFrame);
    }
    

    private Timeline rotate(double angle) {
        Duration duration = Duration.seconds(0.2);
        KeyValue keyValue = new KeyValue(droneImage.rotateProperty(), angle);
        KeyFrame keyFrame = new KeyFrame(duration, keyValue);
        return new Timeline(keyFrame);
    }
}
