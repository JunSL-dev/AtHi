package sample.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.Node;
import java.util.Map;
import java.util.HashMap;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class MainController implements Initializable{


    // main.fxml에서 가져옴 (어노테이션?)
    @FXML private TextField nick;
    @FXML private ComboBox<String> lang;
    @FXML private Button host;
    @FXML private Button join;

    // 처음엔 다른 컨트롤러로 넘길때 객체 만들고 받을 함수의 매개변수로 Map 전달 예정 But 다양한 문제점과 함께 다른 방법 성공 그래도 귀찮으니 안바꿈
    private Map<String,String> infos = new HashMap<String,String>();

    @Override // main.fxml이 로드 되고 컨트롤러에서 가장 먼저 실행됨
    public void initialize(URL fxmlFileLocation,ResourceBundle resources){


        join.setOnMouseClicked(event ->{ // fx:id가 join이라는 버튼이 눌리면 실행됨

            if(lang.getValue() == null){    // 선택된 국가가 없을 때
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("../fxml/Elang.fxml"));    // Elang.fxml로드

                    Stage scene = new Stage();
                    scene.setTitle("Plz Select Country");
                    scene.setScene(new Scene(root,300,100));
                    scene.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else {  // 국가가 선택이 되었을 때
                try {
                    ((Node) event.getSource()).getScene().getWindow().hide(); // join을 누르면 화면전환 됨 -> 그 전 화면을 닫기 위함

                    // Map에 저장할 당시에 join을 눌러야 값을 받도록 함 -> 버튼을 눌렀을때 Map에 값 저장
                    infos.put("nick", nick.getText());
                    infos.put("lang", lang.getValue());

                    // initialize 부분에서도 변수를 받을 수 있도록 함!! ****
                    joinInfo jI = new joinInfo();   // fxml이 로드되기전에 객체 생성
                    jI.getInfo(nick.getText(), lang.getValue());    // 객체로 함수 호출하고 전달하고픈 정보 전달
                    FXMLLoader fxml = new FXMLLoader(getClass().getResource("../fxml/join.fxml")); // fxml파일 얻어옴 join.fxml파일의 컨트롤러는 joinInfo.java
                    fxml.setController(jI); // 로드한 fxml파일에 대한 컨트롤러 설정
                    Parent root = fxml.load();  // 후에 로드 initialize 실행 -> initialize 전에 값을 이미 받은 상태
                    // 요기까지 ****

//                Parent root;
//                root = FXMLLoader.load(getClass().getResource("join.fxml"));

                    // 화면 설정 및 보여주기
                    Stage scene = new Stage();
                    scene.setTitle("Join");
                    scene.setScene(new Scene(root, 600, 400));
                    scene.show();
                    //

                    // 확인 차 출력
                    for (String key : infos.keySet()) {
                        System.out.println("MainController :" + key + " " + infos.get(key));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        host.setOnMouseClicked(event->{ // fx:id가 host라는 버튼이 눌리면 실행됨

            if(lang.getValue() == null){    // 선택된 국가가 없을 때
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("../fxml/Elang.fxml"));    // Elang.fxml 로드

                    Stage scene = new Stage();
                    scene.setTitle("Plz Select Country");
                    scene.setScene(new Scene(root,300,100));
                    scene.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {  // 국가 선택이 되었을때
                try {
                    ((Node) event.getSource()).getScene().getWindow().hide();    // join을 누르면 화면전환 됨 -> 그 전 화면을 닫기 위함

                    FXMLLoader fxml = new FXMLLoader(getClass().getResource("../fxml/hostInfo.fxml"));  // fxml파일 얻어옴 hostInfo.fxml파일의 컨트롤러는 hostInfo.java
                    Parent root = fxml.load();  //fxml 로듸

                    // 화면 설정 및 보여주기
                    Stage scene = new Stage();
                    scene.setTitle("Hosting Information");
                    scene.setScene(new Scene(root, 600, 400));
                    scene.show();
                    //

                    // Map에 저장할 당시에 join을 눌러야 값을 받도록 함 -> 버튼을 눌렀을때 Map에 값 저장
                    infos.put("nick", nick.getText());
                    infos.put("lang", lang.getValue());


                    // 값 확인 차 출력
                    for (String key : infos.keySet()) {
                        System.out.println("MainController :" + key + " " + infos.get(key));
                    }

                    // 값을 넘겨줄 컨트로러의 컨트롤러를 받아옴
                    hostInfo ctrl = fxml.getController();

                    // 값을 넘겨줄 컨트롤러에 값을 넘기기 위한 함수 실행
                    ctrl.getInfo(infos.get("nick"), infos.get("lang"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
