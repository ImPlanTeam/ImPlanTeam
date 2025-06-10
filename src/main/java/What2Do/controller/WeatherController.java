package What2Do.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@RestController
@RequiredArgsConstructor
    public class WeatherController {
        public static void main(String[] args) {

            String url = "https://apihub.kma.go.kr/api/typ01/url/fct_shrt_reg.php?tmfc=0&authKey=7MyyBJBvQWeMsgSQb0Fn0w"; // 다운로드할 파일의 URL 문자열 변수
            String saveFilePath = "output_file.zip"; // 저장할 파일 경로 문자열 변수

            try {
                downloadFile(url, saveFilePath);
                System.out.println("파일 다운로드가 완료되었습니다.");
            } catch (IOException e) {
                System.out.println("파일 다운로드 중 오류가 발생했습니다: " + e.getMessage());
            }
        }

        public static void downloadFile(String url, String saveFilePath) throws IOException {
            URL fileUrl = new URL(url); // 다운로드할 파일의 URL 생성
            URLConnection connection = fileUrl.openConnection(); // URL에 대한 연결 열기 및 URLConnection 객체 생성

            InputStream inputStream = connection.getInputStream(); // 연결된 URL로부터 데이터를 읽어오기 위한 입력 스트림 생성
            FileOutputStream outputStream = new FileOutputStream(saveFilePath); // 지정된 파일 경로에 파일을 쓰기 위한 출력 스트림 생성

            byte[] buffer = new byte[1024]; // 데이터를 읽어올 버퍼 생성
            int bytesRead; // 읽어온 바이트 수를 저장하는 변수

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead); // 버퍼에 있는 데이터를 파일에 기록
            }

            outputStream.close(); // 출력 스트림 닫기
            inputStream.close(); // 입력 스트림 닫기
        }
    }

