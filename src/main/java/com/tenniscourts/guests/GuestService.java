package com.tenniscourts.guests;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GuestService {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private GuestMapper guestMapper;

    public Guest createGuest(GuestDTO guestDTO) {

        Guest guest = guestMapper.map(guestDTO);
        return guestRepository.save(guest);
    }

    public List<Guest> listAllGuests() {
        return guestRepository.findAll();
    }

    public List<Guest> findByName(String name) {
        return guestRepository.findByName(name);
    }

    public Optional<Guest> findById(Long id) {
        return guestRepository.findById(id);
    }

    public void deleteGuest(Long id){
        guestRepository.deleteById(id);
    }

    public Optional<Guest> putGuest(GuestDTO guestDTO, Long id) {
        Optional<Guest> guest = guestRepository.findById(id);
        return guest.map(value -> {
            Guest newGuest = guestMapper.map(guestDTO);
            newGuest.setId(id);
            return guestRepository.save(newGuest);
        });
    }
}
