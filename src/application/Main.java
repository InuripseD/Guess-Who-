
package application;

import java.nio.file.Paths;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	//MediaPlayer mediaPlayer;
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			//primaryStage.setFullScreen(true);
			Image icone = new Image("/resources/images/icone.png");
			//Image icone = new Image(getClass().getResourceAsStream("icone.png"));
			primaryStage.getIcons().add(icone);
			primaryStage.setTitle("Qui est ce?");
			primaryStage.setResizable(true);
			primaryStage.show();
			//music();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** public void music(){
		String s = "music.mp3";
		Media h= new Media(Paths.get(s).toUri().toString());
		mediaPlayer = new MediaPlayer(h);
		mediaPlayer.play(); 
		
	}**/
	
	public static void main(String[] args) {
		launch(args);
	}
}
