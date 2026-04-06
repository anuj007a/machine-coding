package com.wraith.model;

import com.itsrdb.bookmytrain.BookMyTrain.model.Train;
import com.itsrdb.bookmytrain.BookMyTrain.model.User;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "t_bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "train_id", referencedColumnName = "id")
    private Train train;

    private String source;

    private String destination;

    private Long seatNumber;

}
