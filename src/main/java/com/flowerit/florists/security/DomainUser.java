package com.flowerit.florists.security;

import com.google.common.base.Objects;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class DomainUser extends User {

    private final String id;

    public DomainUser(String id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainUser)) return false;
        if (!super.equals(o)) return false;
        DomainUser that = (DomainUser) o;
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), id);
    }
}
