package com.jsw.app.snackbat.vo;

import java.security.Principal;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnonymousPrincipal implements Principal {

    private String name;

    @Override
    public boolean equals (Object another) {
        if (!(another instanceof Principal)) {
            return false;
        }

        Principal principal = (Principal) another;
        return principal.getName() == this.name;
    }

    @Override
    public int hashCode () {
        return Objects.hash(this.name);
    }

}
