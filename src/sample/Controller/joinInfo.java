package sample.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.lang.Integer;

public class joinInfo implements Initializable{


    // join.fxml에서 가져옴
    @FXML private TextField port_Number;
    @FXML private Button join;
    @FXML private TextField ip;

    // 포트 번호를 int 형으로 저장하기위한 변수
    private int port;

    // MainController에서 가져온 값을 저장하기 위한 변수들
    private String nick;
    private String lang;


    @Override
    public void initialize(URL fxmlFileLocation,ResourceBundle resources){ // 로드 되면 젤 먼저 실행

        port_Number.setOnKeyPressed(new EventHandler<KeyEvent>() {  // 엔터키로 페이지 넘기기
            @Override
            public void handle(KeyEvent event) {
                // 여기로도 MainController에서 값이 제대로 왔는지 확인
                if (event.getCode().equals(KeyCode.ENTER)) {    // Pressed Key가 Enter키이면 실행
                    try {
                        port = Integer.parseInt(port_Number.getText());  // 포트 번호 int형으로 형변환
                    } catch (NumberFormatException e) {
                        System.out.println("No Input"); // 포트번호에 아무 값도 입력하지 않고 Enter키를 눌렀을때
                    }
                    if (port < 1500 || port > 491515) {     // 가용 포트 번호 X
                        try {
                            Parent root;
                            root = FXMLLoader.load(getClass().getResource("../fxml/Eport.fxml"));   //Eport.fxml 로드

                            Stage scene = new Stage();
                            scene.setTitle("Invalid Port");
                            scene.setScene(new Scene(root, 300, 100));
                            scene.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {    // 가용 포트 번호 O
                        ((Node) event.getSource()).getScene().getWindow().hide();   //전페이지 닫혀랏

                        try {

                            // initialize 실행 전에 값 넘기는 과정
                            ClientC cc = new ClientC(); // 객체 생성
                            cc.getInfo(nick, lang, ip.getText(),port);  // 생성한 객체로 값을 전달하는 함수 실행 (값 전달) 아직 initialize 시작 전
                            FXMLLoader fxml = new FXMLLoader(getClass().getResource("../fxml/Cchat.fxml")); // Schat.fxml의 컨트롤러는 ServerC.java
                            fxml.setController(cc); // Schat.fxml에 대한 컨트롤러 설정
                            Parent root = fxml.load();  // 로드 -> initialize 실행

                            // 화면 설정 및 보여주기
                            Stage scene = new Stage();
                            scene.setTitle("Cchatting");
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
            }
        });

        ip.setOnKeyPressed(new EventHandler<KeyEvent>() {   // ip주소 입력창에서 Enter key 이벤트
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    if (!ip.getText().equals("127.0.0.1")) {  // 온리 127.0.0.1 Ip 주소만 가능
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("../fxml/Eip.fxml"));  // Eip.fxml 로드

                            Stage scene = new Stage();
                            scene.setTitle("Only 127.0.0.1");
                            scene.setScene(new Scene(root, 300, 100));
                            scene.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        // 내가 initialize 부분으로 제대로 된 변수를 갖고 왔는지 테스트
        System.out.println("SuccessFully come to joinInfo's initialize with : "+nick+" "+lang);

        // fx:id가 join 인 버튼을 누르면 일어나는 이벤트
        join.setOnMouseClicked(event->{
            try {
                // 포트번호 int형으로 형변환
                port = Integer.parseInt(port_Number.getText());
                // 전에 창 숨기기
                ((Node) event.getSource()).getScene().getWindow().hide();

                // ClientC컨트롤러로 initialize 실행전 변수 전달
                ClientC cc = new ClientC(); // 객체 생성
                System.out.println("헤헤헤헤헤헤ㅔㅎ"+port);
                cc.getInfo(nick,lang,ip.getText(),port);  // 정보를 전달 할 수 있는 함수를 객채를 통해 전달
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("../fxml/Cchat.fxml")); // 추후 객채의 fxml이 될 녀석 컨트롤러는 ClientC
                fxml.setController(cc); // 컨트롤러 설정
                Parent root = fxml.load();  // 로드
                //

//                Parent root = FXMLLoader.load(getClass().getResource("Cchat.fxml"));

                // 화면 설정 및 보여주기
                Stage scene = new Stage();
                scene.setTitle("Cchatting");
                scene.setScene(new Scene(root,900,500));
                scene.show();
                //

            }catch(NumberFormatException e){    // 포트 번호가 유효하지 않을때
                System.out.println("Invalid port!");
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
    }


    // MainController에서 값을 받아오기 위한 함수
    public void getInfo(String nick,String lang){
        this.nick = nick;
        this.lang = lang;
        System.out.println("SuccessFully come join Info with : "+this.nick+" "+this.lang);
    }

}
