package sample.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class E implements Initializable{    // 오류 창이 떴을때, close 버튼을 누르면 그 창을 닫아주는 역할을 하는 컨트롤러 입니다.

    @FXML private Button btn;   // 여러 에러 전용 fxml 파일에서 가져온 버튼

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn.setOnMouseClicked(event->{  // 버튼이 눌리면
            ((Node)event.getSource()).getScene().getWindow().hide();    // 창이 닫힙니다. ㅎㅎ
        });
    }
}
