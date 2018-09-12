package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    // 메인 시작!
    @Override
    public void start(Stage primaryStage){
        try{
            // 로드할 fxml 경로 설정
            Parent root = FXMLLoader.load(getClass().getResource("fxml/main.fxml")); // main.fxml의 컨트롤러 : MainController
            Scene scene = new Scene(root,1200,740);
            scene.getStylesheets().add(getClass().getResource("Css/main.css").toString());
            primaryStage.setTitle("Give Your Information"); // 앱 화면 타이틀
            primaryStage.setScene(scene); // 로드 될 화면과 크기
            primaryStage.show(); // 화면 보여랏
        }catch(Exception e){ //예외
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);   // Start!
    }
}
