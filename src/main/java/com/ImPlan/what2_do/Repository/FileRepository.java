package com.ImPlan.what2_do.Repository;


import com.ImPlan.what2_do.Domain.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FileRepository extends JpaRepository<BoardFile, Integer> {
    List<BoardFile> findByPostnum_Num(Integer num);
}
