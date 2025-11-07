package com.project4.histories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project4.histories.model.History;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
  List<History> findByUserId(Long userId);
}
