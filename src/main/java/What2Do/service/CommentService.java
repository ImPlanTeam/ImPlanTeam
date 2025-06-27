package What2Do.service;

import What2Do.domain.Comment;
import What2Do.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void commentR(Comment comment){
        commentRepository.save(comment);
    }
    public List<Comment> commentV(Long tour){
        return commentRepository.findAllByTour_id(tour);
    }
    public void commentD(Long no){
        commentRepository.deleteById(no);
    }
    public Comment commentM(Long no){
        Comment comment = commentRepository.findByNo(no);
        return comment;
    }
    @Transactional
    public void commentU(Long no,String content){
        commentRepository.updateContent(no,content);

    }
    public Integer counting(Long id){
        return commentRepository.countByTour_id(id);

    }

}
