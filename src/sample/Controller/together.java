package sample.Controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class together {

    public String translator(String mylang/* 내 언어 */, String Olang/* 쟤 언어 */, String string /* 쟤가 말한거 */) throws IOException {
        System.out.println("내 언어 체크: "+mylang);
        System.out.println("상대방 언어 체크: "+Olang);
        System.out.println("상대방 말한거 체크: "+string);
        String clientId = "M3vZbmBPA6Qn6WBs4qyu";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "7wyC7bbDZO";//애플리케이션 클라이언트 시크릿값";
        try {
            String text = URLEncoder.encode(string, "UTF-8");   // string 자리에 번역할 언어를 넣음
            String apiURL = "https://openapi.naver.com/v1/language/translate";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            //일본어 :ja   중국어 : zh-CN

            String postParams = "source="+Olang+"&target="+mylang+"&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(parseLang(response.toString()));
            return parseLang(response.toString());  //  Json을 직접 파싱 해서 리턴해줌
        } catch (Exception e) {
            throw e;
        }
    }

    public String toKorean(String mylang,String Olang,String string) throws IOException {
        System.out.println("상대방 언어 체크　in together Controller: "+Olang);
        String clientId = "M3vZbmBPA6Qn6WBs4qyu";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "7wyC7bbDZO";//애플리케이션 클라이언트 시크릿값";
        try {
            String text = URLEncoder.encode(string, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/language/translate";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            //일본어 :ja   중국어 : zh-CN

            String postParams = "source="+Olang+"&target=ko&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            if(mylang == "ko"){ // 같은 언어일때, 번역기에서 오류가 남
                return parseLang(response.toString());  // No problem
            }else {
                return translator(mylang, "ko", parseLang(response.toString()));   // 내 언어가 ko가 아니면 내 언어에 맞게 번역
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public String parseLang(String json) throws IOException {   //Json을 파싱 하겠습니다
        try {
            Map<String,Object> map = new ObjectMapper().readValue(json,Map.class);
            Map<String,Object> message = (Map<String, Object>) map.get("message");  // key값 : message
            Map<String,String> result = (Map<String, String>) message.get("result");    // key값 : result
            String trans = result.get("translatedText");    // key값 : translatedText
            System.out.println(trans);
            return trans;   // 파싱 완료, 번역된 문장 리턴
        } catch (JsonParseException e) {
            throw e;
        } catch (JsonMappingException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    public String getNick(String json) throws IOException { // 닉네임 얻기 From 상대방이 보내준 맵 형식의 정보
        try {
            Map<String,String> map = new ObjectMapper().readValue(json,Map.class);
            return map.get("nick"); // key값 : nick
        } catch (IOException e) {
            throw e;
        }

    }

    public String getLang(String json) throws IOException { // 언어 얻기 From 상대방이 보내준 맵 형식의 정보
        try {
            Map<String,String> map = new ObjectMapper().readValue(json,Map.class);
            return map.get("lang"); // key값 : lang
        } catch (IOException e) {
            throw e;
        }

    }

    public String whatMyLang(String lang){  // 내 언어는? :: 번역기에서 언어 판별할 때 사용하는 단어 ko, ja, en, zh-CN 으로 변환
        System.out.println("Do you have a question with 'What's my Lang'?"+lang);
        if(lang.equals("Korean") || lang.equals("ko")){ // 한국어
            return "ko";
        } else if(lang.equals("English") || lang.equals("en")){ // 영어
            return "en";
        } else if(lang.equals("Japanese") || lang.equals("ja")){  // 일본어
            return"ja";
        } else{
            return "zh-CN"; // 중국어
        }
    }
}
