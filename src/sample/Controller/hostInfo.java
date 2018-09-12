package sample.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.lang.Integer;

public class hostInfo implements Initializable{

    // hostInfo.fxml 에서 가져옴
    @FXML private Button hosting;
    @FXML private TextField port_Number;

    @FXML
    public void onEnter(ActionEvent event){ // 엔터키 이벤트!
        // 여기로도 MainController에서 값이 제대로 왔는지 확인
        System.out.println("Successfully come to initailize : "+nick+" "+lang);
        try {
            port = Integer.parseInt(port_Number.getText());  // 포트 번호 int형으로 형변환
        } catch(NumberFormatException e){
            System.out.println("No Input");
        }
        if(port < 1500 || port > 491515){   // 가용 포트 번호 X
            try {
                Parent root;
                root = FXMLLoader.load(getClass().getResource("../fxml/Eport.fxml"));   // Eport.fxml 로드

                Stage scene = new Stage();
                scene.setTitle("Invalid Port");
                scene.setScene(new Scene(root,300,100));
                scene.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {  // 가용 포트 번호 O
            ((Node) event.getSource()).getScene().getWindow().hide(); // 이전 창 닫기!

            try {

                // initialize 실행 전에 값 넘기는 과정
                ServerC sc = new ServerC(); // 객체 생성
                sc.getInfos(nick, lang, port);  // 생성한 객체로 값을 전달하는 함수 실행 (값 전달) 아직 initialize 시작 전
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("../fxml/Schat.fxml")); // Schat.fxml의 컨트롤러는 ServerC.java
                fxml.setController(sc); // Schat.fxml에 대한 컨트롤러 설정
                Parent root = fxml.load();  // 로드 -> initialize 실행

                // 화면 설정 및 보여주기
                Stage scene = new Stage();
                scene.setTitle("Schatting");
                scene.setScene(new Scene(root, 900, 500));
                scene.show();
                //


            } catch (NumberFormatException e) {   // 포트 번호를 이상하게 입력했을때
                System.out.println("Invalid Port!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 받은 포트 번호를 int 형으로 저장해줄 변수 선언
    private int port;

    // MainController에서 넘겨준 값을 저장하기 위한 변수
    private String nick;
    private String lang;

    public void initialize(URL fxmlFileLocation,ResourceBundle resources){  // hostInfo.java가 로드 될때 가장 먼저 실행 됨

        // 내가 어딘지 출력(확인용)
        System.out.println("HostInfo.java");

        // fx:id가 hosting인 버튼을 눌렀을 때 일어나는 이벤트
        hosting.setOnMouseClicked(event->{
            // 여기로도 MainController에서 값이 제대로 왔는지 확인
            System.out.println("Successfully come to initailize : "+nick+" "+lang);
            try {
                port = Integer.parseInt(port_Number.getText()); // 포트 번호 int형으로 형변환
            } catch(NumberFormatException e){
               e.printStackTrace();
            }
            if(port<1234 || port > 20000){  // 가용 포트 번호 X
                try {
                    Parent root;
                    root = FXMLLoader.load(getClass().getResource("../fxml/Eport.fxml"));   // Eport.fxml 로드

                    Stage scene = new Stage();
                    scene.setTitle("Invalid Port");
                    scene.setScene(new Scene(root,300,100));
                    scene.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {  // 가용 포트 번호 O
                ((Node) event.getSource()).getScene().getWindow().hide();   // 전에 있던 창 닫아주구요

                try {

                    // initialize 실행 전에 값 넘기는 과정
                    ServerC sc = new ServerC(); // 객체 생성
                    sc.getInfos(nick, lang, port);  // 생성한 객체로 값을 전달하는 함수 실행 (값 전달) 아직 initialize 시작 전
                    FXMLLoader fxml = new FXMLLoader(getClass().getResource("../fxml/Schat.fxml")); // Schat.fxml의 컨트롤러는 ServerC.java
                    fxml.setController(sc); // Schat.fxml에 대한 컨트롤러 설정
                    Parent root = fxml.load();  // 로드 -> initialize 실행

                    // 화면 설정 및 보여주기
                    Stage scene = new Stage();
                    scene.setTitle("Schatting");
                    scene.setScene(new Scene(root, 900, 500));
                    scene.show();
                    //


                } catch (NumberFormatException e) {   // 포트 번호를 이상하게 입력했을때
                    System.out.println("Invalid Port!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // MainController에서 값을 받아오기 위한 함수
    public void getInfo(String nick,String lang){
        this.nick = nick;
        this.lang = lang;
        System.out.println("Successfully saved with : "+this.nick+" "+this.lang);
    }

}
