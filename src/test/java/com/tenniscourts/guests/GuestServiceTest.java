package com.tenniscourts.guests;

import com.tenniscourts.reservations.ReservationService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private GuestMapper guestMapper;

    @InjectMocks
    private GuestService guestService;

    @Test
    public void functionCreateGuestShouldAddANewGuest() {
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setName("Matheus");
        Guest guest = Guest.builder().name("Matheus").build();
        when(guestRepository.save(any())).thenReturn(guest);

        assertEquals(guestService.createGuest(guestDTO), guest);

    }

    @Test
    public void functionPutGuestShouldUpdateGuestByGivenId() {
        Guest guest = Guest.builder().name("Matheus").build();
        guest.setId(1L);
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setName("Ana");
        Guest expectedGuest = Guest.builder().name("Ana").build();
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(guestMapper.map(guestDTO)).thenReturn(expectedGuest);
        when(guestRepository.save(expectedGuest)).thenReturn(expectedGuest);

        assertEquals(guestService.putGuest(guestDTO, 1L), Optional.of(expectedGuest));
    }

    @Test
    public void functionDeleteGuestShouldDeleteGuestByGivenId() {
        assertDoesNotThrow(() -> guestService.deleteGuest(1L));
    }

    @Test
    public void functionFindAllGuestShouldRetrieveAllGuests() {
        List<Guest> guestList = List.of(Guest.builder().name("Maria").build(), Guest.builder().name("Matheus").build());
        Mockito.when(guestRepository.findAll()).thenReturn(guestList);

        assertEquals(guestService.listAllGuests(), guestList);
    }

    @Test
    public void functionFindByIdGuestShouldRetrieveGuestWithGivenId() {
        Guest expectedGuest = Guest.builder().name("Laura").build();
        Mockito.when(guestRepository.findById(1L)).thenReturn(Optional.of(expectedGuest));

        assertEquals(guestService.findById(1L), Optional.of(expectedGuest));
    }

    @Test
    public void functionFindByNameGuestShouldRetrieveGuestWithGivenName() {
        Guest expectedGuest = Guest.builder().name("Laura").build();
        Mockito.when(guestRepository.findByName("Laura")).thenReturn(List.of(expectedGuest));

        assertEquals(guestService.findByName("Laura"), List.of(expectedGuest));
    }
}
