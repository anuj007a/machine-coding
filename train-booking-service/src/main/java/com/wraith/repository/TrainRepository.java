package com.wraith.repository;

import com.wraith.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainRepository  extends JpaRepository<Train, Long> {
}
