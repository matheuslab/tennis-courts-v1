package com.tenniscourts.guests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GuestDTO {

    @NotNull
    @NotEmpty
    public String name;
}
