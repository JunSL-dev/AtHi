package sample.Controller;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import java.util.Map;
import java.util.HashMap;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.lang.Integer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClientC implements Initializable{

    together to = new together();   // 공통적으로 사용할 기능들을 담아놓은 together 객체 생성

    // 스레드
    public class chatThread extends Thread{

        // 받을 데이터중 상대방 닉네임과 언어권을 받기 위해 소켓으로 받는 데이터 판별하는 정규식
        Pattern pattern = Pattern.compile("(^\\{(\"nick\") : \"[0-9A-Za-zㄱ-ㅎㅏ-ㅣ가-힣]*\", (\"lang\") : ((\"Korean\")|(\"Japanese\")|(\"Chinese\")|(\"English\"))\\}$)");
        Matcher m;


        // 생성자 입니다 ㅎㅎ
        public chatThread(){

        }

        // 스레드 내부에서 반복적으로 실행 될 녀석이라는데
        public void run(){
            try {

                // 상대방으로 부터 값을 받아오고 그 값이 "/exit" 가아니면 쭉 실행
                while((margin=gMsg.readUTF()) != "/exit"){

                    // 데이터 판별( 상대방 데이터를 받기 위함. )
                    if((m = pattern.matcher(margin)).find()){   // 요녀석은 상대방 데이터를 화면으로 출력하면 안되고, 그렇다고 해서 멈춰도 안됨 -> continue
                        Cnick = to.getNick(margin);
                        Clang = to.getLang(margin);
                        System.out.println("여기는 스레드!"+Cnick+" "+Clang);    // 파싱후에 저장이 잘 되었는지 확인
                        System.out.println("Pattern Correct");  // 패턴이 알맞으면 뜨도록 함
                        continue;   // 과정이 끝나고 다시 채팅으로 돌아가야지 ㅎㅎ
                    }
                    System.out.println("(테스트 구간) 내 언어: "+lang+" 상대방: "+Clang);  // 테스트

                    lang = to.whatMyLang(lang); // 번역기에 사용할 수 있는 언어로 변환
                    System.out.println("What's my lang? : "+lang);  // 확인

                    Clang = to.whatMyLang(Clang); // 번역기에 사용할 수 있는 언어로 변환
                    System.out.println("What's my Clang? : "+Clang);    // 확인


                    System.out.println("(테스트 구간2) 내 언어: "+lang+" 상대방: "+Clang); // 최종 확인

                    if(lang != Clang){  // 내 언어하고 상대방 언어하고 다를때만 번역기 실행
                        margin = (Clang == "ko") ? to.translator(lang, Clang, margin) : to.toKorean(lang, Clang, margin);
                    }

                    // 스레드에서 UI 구현하기 위해 Platform.runLater
                    Platform.runLater(()->{

                        vbox = new VBox();

                        // 상대방이 보낸 메시지의 라벨은 오른쪽 상단에
                        vbox.setAlignment(Pos.TOP_RIGHT);
                        vbox.setPrefWidth(900.0);
                        vbox.setPrefHeight(30.0);
                        vbox.setStyle("-fx-background-color: rgba(255,255,255,1);");

                        // 추가할 라벨 :: 상대방의 정보를 받아와서 Cnick 이라는 변수를 넣을 예정
                        label = new Label("Not your Nick: "+Cnick);

                        // 패딩 설정
                        label.setPadding(new Insets(0,5,0,0));
                        label.setStyle("-fx-background-color: rgba(255,255,255,0);-fx-text-fill: #fec923;");

                        // 상대방이 보낼 메시지를 띄울 ui 설정들
                        textfield = new TextField(margin);
                        textfield.setEditable(false);
                        textfield.setPrefHeight(904);
                        textfield.setPrefWidth(38);
                        textfield.setStyle("-fx-background-color: #fc6767;-fx-text-fill:#ffffff;");
                        //

                        // vbox에 추가!
                        vbox.getChildren().add(label);
                        vbox.getChildren().add(textfield);

                        // UI에 추가
                        append.getChildren().add(vbox);

                        // 채팅 될때 마다 화면 스크롤을 맨아래로 맞추고싶어
                        scroll.setVvalue(1.0);
                    });
                    System.out.println("From :"+margin); // 받는 메시지 출력
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Cchat.fxml에서 가져옴 어노테이션들
    @FXML private TextArea msg;
    @FXML private Button send;
    @FXML private Pane show;
    @FXML private VBox append;
    @FXML private ScrollPane scroll;

    // ip 와 port 정보를 받을 변수 From joinInfo.java
    private String ip;
    private int port;

    // 소켓 데이터 주고 받을 변수
    private DataInputStream gMsg;
    private DataOutputStream sMsg;

    // 소켓 변수
    private Socket socket;

    // 받는 데이터 저장용 변수
    private String margin="";

    // 스레드 객체용 변수
    chatThread chat;


    // UI에 추가할때 사용할 fxml용 변수들
    private Label label;
    private TextField textfield;
    private VBox vbox;

    // 내 닉네임과 언어권
    private String nick;
    private String lang;

    // 상대방 닉네임과 언어권
    private String Cnick;
    private String Clang;



    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){ // 1삐띠로 시작


        msg.setOnKeyPressed(new EventHandler<KeyEvent>(){

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER))
                {
                    // UI용 Vbox
                    vbox = new VBox();
                    vbox.setPrefWidth(900.0);
                    vbox.setPrefHeight(30.0);
                    vbox.setStyle("-fx-background-color: rgba(255,255,255,1);");

                    // UI용 label 설정
                    label = new Label("Your Nick: "+nick);
                    label.setPadding(new Insets(0,0,0,5));
                    label.setStyle("-fx-background-color: rgba(255,255,255,0);-fx-text-fill: #3985c3;");

                    // UI용 textarea 설정
                    textfield = new TextField(msg.getText());
                    textfield.setEditable(false);
                    textfield.setPrefHeight(200);
                    textfield.setPrefWidth(200);
                    textfield.setStyle("-fx-background-color: #cbf6b7;-fx-text-fill:#000000;");
                    //

                    // vbox에 추가
                    vbox.getChildren().add(label);
                    vbox.getChildren().add(textfield);

                    // UI에 추가
                    append.getChildren().add(vbox);

                    // 메시지 입력창에서 받아온 값을 소켓을 통해 전달
                    try {
                        sMsg.writeUTF(msg.getText());
                    } catch(NullPointerException s){
                        System.out.println("No Client");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("to :"+msg.getText());  // 메시지 입력창에서 값이 제대로 왔는지 확인
                    scroll.setVvalue(1.0);  // 스크롤!
                    msg.clear();    // 메시지 창 비우기 ㅎㅎ
                }
            }
        });




        // 내가 잘 왔니? 확인 해보장
        System.out.println("Successfully come to Cclient'Initialize with : "+nick+" "+lang+" "+ip+" "+port);

        try {

            socket = new Socket(ip,port); // Ip가 ip변수, Port가 port변수인 서버 접속
            gMsg = new DataInputStream(socket.getInputStream());    // 상대방에게서 받을 메시지 스트림
            sMsg = new DataOutputStream(socket.getOutputStream());  // 상대방에게 보낼 메시지 스트림
            sMsg.writeUTF("{\"nick\" : \""+nick+"\", \"lang\" : \""+lang+"\"}"); // 내 nick, lang에 대한 정보 전달

            chat=new chatThread();  // 스레드 객체 생성
            chat.start();   // 스레드 실행~~~

        } catch(ConnectException e){    // 서버가 존재 하지 않을 때
            System.out.println("서버가 없습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // fx:id가 send인 녀석이 눌렸을때 작동
        send.setOnMouseClicked(event->{
            try {
                // UI용 Vbox
                vbox = new VBox();
                vbox.setPrefWidth(900.0);
                vbox.setPrefHeight(30.0);
                vbox.setStyle("-fx-background-color: rgba(255,255,255,1);");

                // UI용 label 설정
                label = new Label("Your Nick: "+nick);
                label.setPadding(new Insets(0,0,0,5));
                label.setStyle("-fx-background-color: rgba(255,255,255,0);-fx-text-fill: #3985c3;");

                // UI용 textarea 설정
                textfield = new TextField(msg.getText());
                textfield.setEditable(false);
                textfield.setPrefHeight(200);
                textfield.setPrefWidth(200);
                textfield.setStyle("-fx-background-color: #cbf6b7;-fx-text-fill:#000000;");
                //

                // vbox에 추가
                vbox.getChildren().add(label);
                vbox.getChildren().add(textfield);

                // UI에 추가
                append.getChildren().add(vbox);

                // 메시지 입력창에서 받아온 값을 소켓을 통해 전달
                sMsg.writeUTF(msg.getText());

                System.out.println("to :"+msg.getText());  // 메시지 입력창에서 값이 제대로 왔는지 확인
                scroll.setVvalue(1.0);  // 스크롤!
                msg.clear();    // 메시지 창 비우기 ㅎㅎ
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    // hostInfo로부터 받는 정보들 저장하기 위한 함수
    public void getInfo(String nick,String lang,String ip,int port){
        this.nick = nick;
        this.lang = lang;
        this.port = port;
        this.ip = ip;

        // 제대로 왔니?
        System.out.println("Successfully come to Cclient with : "+this.nick+" "+this.lang+" "+this.ip+" "+this.port);
    }
}
