package sample.Controller;

import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.Node;
import java.util.Map;
import java.util.HashMap;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.*;
import java.util.logging.Handler;
import javax.swing.*;
import javax.swing.SpringLayout.Constraints;

public class ServerC implements Initializable{


    // hostInfo로 부터 받아올 정보를 저장할 변수들
    private String nick;
    private String lang;
    private int port;

    together to = new together();


    //스레드
    public class chatThread extends Thread{

        // 상대방이 전달한 메시지가 nick과 lang을 담은 정보인지 아닌지 판별하는 정규 표현식
        Pattern pattern = Pattern.compile("(^\\{(\"nick\") : \"[0-9A-Za-zㄱ-ㅎㅏ-ㅣ가-힣]*\", (\"lang\") : ((\"Korean\")|(\"Japanese\")|(\"Chinese\")|(\"English\"))\\}$)");
        Matcher m;


        // 난 생성자 ㅎ
        public chatThread(){
        }


        // 스레드 내부에서 반복적으로 실행될 녀석
        public void run(){
            try {

                socket = server.accept();   // 클라이언트 접속
                System.out.println("접속 대따");    // 접속 됨을 출력
                sMsg = new DataOutputStream(socket.getOutputStream());  // 상대방에게 메시지를 전달할 용도의 스트림
                sMsg.writeUTF("{\"nick\" : \""+nick+"\", \"lang\" : \""+lang+"\"}"); // 상대방에게 내 정보를 전달함 with nick and lang
                gMsg = new DataInputStream(socket.getInputStream());    // 상대방으로 부터 오는 메시지를 받을 용도의 스트림

                while((margin=gMsg.readUTF()) != "/exit"){  // 상대방이 보낸 메시지 읽어오고 조건에 따른 while문


                    // 정규표현식과 margin과 일치하면 실행
                    if((m = pattern.matcher(margin)).find()){
                        Cnick = to.getNick(margin);
                        Clang = to.getLang(margin);
                        System.out.println("여기는 스레드!"+Cnick+" "+Clang);    // 파싱하고 데이터 저장이 잘 됐는지 확인
                        System.out.println("pattern correct");  // 정규표현식과 일치하는지 출력
                        continue;   // 여기서 실패했다고 멈추면 안되지! 계속 데이터를 받아야해 ㅎㅎ
                    }

                    System.out.println("(테스트 구간) 내 언어: "+lang+" 상대방: "+Clang);  // 잘넘어 왔니

                    lang = to.whatMyLang(lang); // 번역기에 사용할 수 있는 걸로 변환
                    System.out.println("What's my lang? : "+lang);  // 확인

                    Clang = to.whatMyLang(Clang);   // 번역기에 사용할 수 있는 걸로 변환
                    System.out.println("What's my Clang? : "+Clang);    // 확인



                    System.out.println("(테스트 구간2) 내 언어: "+lang+" 상대방: "+Clang); // 총 확인

                    if(lang != Clang){  // 내언어하고 상대방 언어하고 다르면 번역기 실행
                        margin = (Clang == "ko") ? to.translator(lang, Clang, margin) : to.toKorean(lang, Clang, margin);
                    }
                    // 스레드에서 UI 구현을 하기위한 Platform.runLater()
                    Platform.runLater(()->{

                        vbox = new VBox();

                        // 상대방이 보낸 메시지의 라벨은 오른쪽 상단에
                        vbox.setAlignment(Pos.TOP_RIGHT);
                        vbox.setPrefWidth(900.0);
                        vbox.setPrefHeight(30.0);
                        vbox.setStyle("-fx-background-color: rgba(255,255,255,1);");

                        // 추가할 라벨 설정
                        label = new Label("Not your Nick: "+Cnick);
                        label.setPadding(new Insets(0,5,0,0));
                        label.setStyle("-fx-background-color: rgba(255,255,255,0);-fx-text-fill: #fec923;");

                        // 추가할 텍스트에리아 설정
                        textfield = new TextField(margin);
                        textfield.setEditable(false);
                        textfield.setPrefHeight(904);
                        textfield.setPrefWidth(38);
                        textfield.setStyle("-fx-background-color: #fc6767;-fx-text-fill:#ffffff;");
                        //

                        // vbox에 추가
                        vbox.getChildren().add(label);
                        vbox.getChildren().add(textfield);

                        // UI에 추가
                        append.getChildren().add(vbox);

                        // 스크롤 밑으로
                        scroll.setVvalue(1.0);
                    });
                    System.out.println("From :"+margin); // 받은 메시지 확인용 출력
                    try {
                        sleep(500L);    // 잠들어랏
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    // UI에 추가할때 사용할 fxml용 변수들
    Label label;
    TextField textfield;
    VBox vbox;


    // 소켓으로 데이터를 주고받기 위한 변수들
    private DataInputStream gMsg;
    private DataOutputStream sMsg;
    //

    // 소켓 변수들
    private ServerSocket server;
    private Socket socket;

    // 받은 데이터를 저장해서 쓸꺼임
    private String margin="";

    // 스레드 객체용 변수
    chatThread chat ;


    // You guy's from Schat.fxml :D
    @FXML private Button send;
    @FXML private TextArea msg;
    @FXML private VBox append;
    @FXML private ScrollPane scroll;

    // 상대방 정보를 담을 변수들
    private String Cnick;
    private String Clang;


    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources){ // 1빠따로 실행

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


        // 내가 어딘지, 값을 제대로 받았는지는 알아야지 ㅎ
        System.out.println("Successfully come to ServerC in initialize : "+nick+" "+lang+port);

        try {
            // 내가 어딧는지 알기 2
            System.out.println("Successfully come to ServerC with : "+nick+" "+lang+port);
            try {
                server = new ServerSocket(port);  // 서버 만들기!
            } catch(BindException e){
                System.out.println("Already using Port");
            }

            // 서버 확인
            System.out.println("Here is your server"+server);

            // 스레드 객체 생성
            chat = new chatThread();

            // 스레드 실행~~~
            chat.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // fx:id가 send라는 녀석이 눌리면 실행되는 이벤트
        send.setOnMouseClicked(event->{
            try {

                // UI용 Vbox
                vbox = new VBox();
                vbox.setPrefWidth(900.0);
                vbox.setPrefHeight(30.0);
                vbox.setStyle("-fx-background-color: rgba(255,255,255,1);");

                // UI용 라벨
                label = new Label("Your Nick: "+nick);
                label.setPadding(new Insets(0,0,0,5));
                label.setStyle("-fx-background-color: rgba(255,255,255,0);-fx-text-fill: #3985c3;");


                // UI용 TextArea
                textfield = new TextField(msg.getText());
                textfield.setEditable(false);
                textfield.setPrefHeight(200);
                textfield.setPrefWidth(200);
                textfield.setStyle("-fx-background-color: #cbf6b7;-fx-text-fill:#000000;");

                // vbox에 추가
                vbox.getChildren().add(label);
                vbox.getChildren().add(textfield);

                // UI에 추가
                append.getChildren().add(vbox);

                // 내가 뭘보내는지는 알아야지
                System.out.println("to :"+msg.getText());

                // 메시지 인풋 값을 받아서 상대방에게 메시지 전송
                sMsg.writeUTF(msg.getText());

                // 메시지 인풋 필드를 깔끔하게~
                msg.clear();

                // 스크롤 밑으로
                scroll.setVvalue(1.0);
            } catch(NullPointerException e){    // 클라이언트 없이 메시지를 보내겨 시도할때 소켓 객체가 없어서 NullPointerExceptio 발생 -> 예외 처리
                System.out.println("Maybe no Client");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    // hostInfo컨트롤러로 부터 값을 받아오기 위한 메소드
    public void getInfos(String nick,String lang,int port){
        this.nick = nick;
        this.lang = lang;
        this.port = port;

        // Get Infos Well?
        System.out.println("Successfully come to ServerC with : "+this.nick+" "+this.lang+" "+this.port);
    }
}

