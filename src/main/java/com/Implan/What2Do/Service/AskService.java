package com.Implan.What2Do.Service;

import com.Implan.What2Do.Repository.AnswerRepository;
import com.Implan.What2Do.Repository.AskFileRepository;
import com.Implan.What2Do.Repository.AskRepository;
import com.Implan.What2Do.domain.Answer;
import com.Implan.What2Do.domain.Ask;
import com.Implan.What2Do.domain.AskFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AskService {

    private final AskRepository askRepository;
    private final AskFileRepository askFileRepository;
    private final AnswerRepository answerRepository;

    private final String uploadDir = "D:/boottmp/";

    //1:1문의 저장
    public void saveAsk(String title, String content, String vicibility, List<MultipartFile> files) throws IOException {
        Ask ask = new Ask();
        ask.setTitle(title);
        ask.setContents(content);
        ask.setVicibility(vicibility);

        // 파일 저장
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String originalName = file.getOriginalFilename();
                String storedName = UUID.randomUUID() + "_" + originalName;
                String path = uploadDir + storedName;

                // 저장
                file.transferTo(new File(path));

                AskFile askFile = new AskFile();
                askFile.setFilename(originalName);
                askFile.setStoredFilename(storedName);
                askFile.setFilepath(path);

                ask.addFile(askFile);
            }
        }

        askRepository.save(ask);
    }
    //1:1문의 리스트

    public Page<Ask> findAsk(Pageable pageable){
        return askRepository.findAll(pageable);
    }

    //1:1문의 자세히보기
    public Ask viewDetail(Integer no){
        return askRepository.findById(no)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));
    }

    //1:1문의 사진파일 가져오기
    public List<AskFile> getimg(Integer askNo){

        return askFileRepository.findByAsk_no(askNo);

    }
    //1:1문의 답변달기
    public void saveAnswer(Integer askNo, String content) {
        Ask ask = askRepository.findById(askNo)
                .orElseThrow(() -> new IllegalArgumentException("문의글을 찾을 수 없습니다."));
        Answer answer = new Answer();
        answer.setAsk(ask);
        answer.setContent(content);
        answer.setAnsweredAt(LocalDateTime.now());
        answerRepository.save(answer);
    }
    //1:1답변 출력
    public Answer viewAnswer(Integer no){
        return answerRepository.findById(no)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));
    }


}