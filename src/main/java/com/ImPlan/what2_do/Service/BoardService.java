package com.ImPlan.what2_do.Service;

import com.ImPlan.what2_do.Domain.Board;
import com.ImPlan.what2_do.Domain.BoardFile;
import com.ImPlan.what2_do.Repository.BoardRepository;
import com.ImPlan.what2_do.Repository.FileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BoardService {

    @Autowired
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;


    public BoardService(BoardRepository boardRepository, FileRepository fileRepository) {
        this.boardRepository = boardRepository;

        this.fileRepository = fileRepository;
    }
    private final String uploadDir = "D:/upload/";



    public void register(String title, String content, String writer,String area, List<MultipartFile> files)throws IOException {
        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        board.setWriter(writer);
        board.setArea(area);
        for(MultipartFile file:files){
            if(!file.isEmpty()){
                String orignalName = file.getOriginalFilename();
                UUID uuid = UUID.randomUUID(); //파일이름 랜덤으로 생성
                String storedName= uuid+"_"+ file.getOriginalFilename();//저장될 파일이름 생성
                String path = uploadDir + storedName;
                file.transferTo(new File(path));

                BoardFile boardFile= new BoardFile();
                boardFile.setFilename(orignalName);
                boardFile.setOrignFileName(storedName);
                boardFile.setFilePath(path);

                board.addFile(boardFile);

            }
        }
        boardRepository.save(board);
    }
    //게시글 리스트
    public Page<Board> listV(Pageable pageable){
        Page<Board> list=boardRepository.findAll(pageable);
        return list;
    }

    //검색된 게시글 리스트
    public Page<Board> listSearch(Pageable pageable,String searchKeyword){
        Page<Board> list=boardRepository.findByAreaContaining(searchKeyword,pageable);
        return list;
    }

    public Board view(Integer num){
        Board board = boardRepository.findById(num).orElseThrow();
        return board;
    }
    public List<BoardFile> viewF(Integer num){
        List<BoardFile> boardFiles = fileRepository.findByPostnum_Num(num);
        return boardFiles;
    }

    public void updateCount(Integer num){
        boardRepository.updateCount(num);
    }

    public Integer likePlus(Integer num){
        Integer likeCnt=boardRepository.updateLikePlus(num);
        return likeCnt;
    }

    public void likeMinus(Integer num){
        boardRepository.updateLikeMinus(num);
    }







}
