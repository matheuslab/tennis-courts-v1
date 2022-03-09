package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequestMapping("/guests")
@RestController
@AllArgsConstructor
public class GuestController extends BaseRestController {

    @Autowired
    private GuestService guestService;

    @ApiOperation("Find Guests by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Guest>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(guestService.findById(id));
    }

    @ApiOperation("Retrieve Guests List, with support to filter by name")
    @GetMapping("/")
    public ResponseEntity<List<Guest>> listGuests(@RequestParam(value = "name", required = false) String name) {
        if (name == null) {
            return ResponseEntity.ok(guestService.listAllGuests());
        }
        return ResponseEntity.ok(guestService.findByName(name));
    }

    @ApiOperation("Create a new Guest")
    @PostMapping("/")
    public ResponseEntity<Guest> createGuest(@RequestBody @Valid GuestDTO guestDTO) {
        return new ResponseEntity<Guest>(guestService.createGuest(guestDTO), HttpStatus.CREATED);
    }

    @ApiOperation("Update a Guest")
    @PutMapping("/{id}")
    public ResponseEntity<Optional<Guest>> putGuest(@RequestBody @Valid GuestDTO guestDTO, @PathVariable Long id) {
        Optional<Guest> guest = guestService.putGuest(guestDTO, id);
        if (guest.isPresent()) {
            return ResponseEntity.ok(guest);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation("Delete a Guest")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
