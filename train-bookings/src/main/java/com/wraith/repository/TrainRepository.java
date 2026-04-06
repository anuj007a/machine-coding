package com.wraith.repository;

import com.itsrdb.bookmytrain.BookMyTrain.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainRepository  extends JpaRepository<Train, Long> {
}
