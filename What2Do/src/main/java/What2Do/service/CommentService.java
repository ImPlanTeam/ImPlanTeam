package What2Do.service;

import What2Do.domain.Bcomment;
import What2Do.domain.Comment;
import What2Do.repository.BcommentRepository;
import What2Do.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository;
    private final BcommentRepository bcommentRepository;


    public CommentService(CommentRepository commentRepository, BcommentRepository bcommentRepository) {
        this.commentRepository = commentRepository;
        this.bcommentRepository = bcommentRepository;
    }

    public void commentD(Long no){
        commentRepository.deleteById(no);
    }

    public void commentDelete(Integer no){
        bcommentRepository.deleteById(no);
    }

    public Comment commentM(Long no){
        Comment comment = commentRepository.findByNo(no);
        return comment;
    }
    public Bcomment commentModify(Integer no){
        Bcomment comment = bcommentRepository.findByNo(no);
        return comment;
    }

    @Transactional
    public void commentU(Long no,String content){
        commentRepository.updateContent(no,content);
    }
    @Transactional
    public void commentUpdate(Integer no,String content){
        bcommentRepository.updateContent(no,content);
    }


    public Integer counting(Long id){
        return commentRepository.countByTour_id(id);
    }

    public void commentR(Comment comment){
        commentRepository.save(comment);
    }

    public void commentB(Bcomment bcomment){
        bcommentRepository.save(bcomment);
    }

    public List<Bcomment> bcommentV(Integer board){
        return bcommentRepository.findAllByBoard_num(board);
    }
    public List<Comment> commentV(Long tour){
        return commentRepository.findAllByTour_id(tour);
    }



}