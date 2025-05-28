package What2Do.service;


import What2Do.domain.Board;
import What2Do.domain.BoardFile;
import What2Do.domain.LikeIt;
import What2Do.repository.BoardRepository;
import What2Do.repository.FileRepository;
import What2Do.repository.LikeRepository;
import jakarta.servlet.http.HttpSession;
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
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class BoardService {

    @Autowired
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;
    private final LikeRepository likeRepository;


    public BoardService(BoardRepository boardRepository, FileRepository fileRepository, LikeRepository likeRepository) {
        this.boardRepository = boardRepository;

        this.fileRepository = fileRepository;
        this.likeRepository = likeRepository;
    }
    private final String uploadDir = "D:/upload/";



    public void register(String title, String content, String writer, String area, List<MultipartFile> files)throws IOException {
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
    public Integer likeMinus(Integer num){
        Integer likeCnt=boardRepository.updateLikeMinus(num);
        return likeCnt;
    }
    //게시글에 좋아요를 했는지 판별
    public Integer likeB(Integer num, String id){
        Integer likeCnt=likeRepository.findByNumAndId(num,id);
        return likeCnt;
    }
    public void likeI(Integer num,String id){
        likeRepository.insertNumAndID(num,id);
    }

    public void likeD(Integer num,String id){
        likeRepository.deleteByNumAndId(num,id);
    }
    //게시글 삭제
    public void del(Integer num){
        boardRepository.deleteById(num);
    }
    //수정할 게시글 불러오기
    public Board update(Integer num) {
        Board board = boardRepository.findById(num).orElseThrow();
        return board;
    }
    //수정한 게시글 저장
    public void modify(Board board) {
        Board b = boardRepository.findById(board.getNum()).orElseThrow();
        b.setTitle(board.getTitle());
        b.setContent(board.getContent());
        boardRepository.save(b);
    }









}

