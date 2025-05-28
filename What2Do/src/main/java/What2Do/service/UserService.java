package What2Do.service;


import What2Do.domain.User;
import What2Do.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class
UserService {




    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void save(User u) {
        System.out.println(u.getId());
        userRepository.save(u);
    }

    @Transactional
    public Map<String, String> validateHandling(Errors errors){
        Map<String, String> result = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()){
            String validKeyName = String.format("valid_%s", error.getField());
            result.put(validKeyName, error.getDefaultMessage());
        }
        return result;
    }


    public List<User> findAll() {
        List<User> list= userRepository.findAll();
        return list;
    }

    public User findById(String id){
        User u = userRepository.findById(id);
        return u;
    }

    @Transactional
    public boolean checkId(String id){
        return userRepository.existsById(id);
    }
    //회원관리 삭제기능
    @Transactional
    public void deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }
    //회원관리 수정기능
    @Transactional
    public void updateUser(String id, User updatedUser) {
        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        existingUser.setName(updatedUser.getName());
        existingUser.setMail(updatedUser.getMail());
        existingUser.setTel(updatedUser.getTel());
    }



}
