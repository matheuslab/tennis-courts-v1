package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RequestMapping("/reservation")
@RestController
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @ApiOperation("Create a book reservation")
    @PostMapping("/")
    public ResponseEntity<Void> bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @ApiOperation("Find a reservation")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> findReservation(Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @ApiOperation("Cancel a reservation")
    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationDTO> cancelReservation(Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @ApiOperation("Reschedule a reservation")
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> rescheduleReservation(Long reservationId, Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
