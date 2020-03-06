package ru.levap.cms.persistance.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "app_user", indexes = {
        @javax.persistence.Index(name = "USER_IDX1", columnList = "login,password")
})
public class User implements UserDetails {
	
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotNull
    @Getter @Setter
    private Boolean active;

    @NotNull
    @Column(unique = true)
    @Getter @Setter
    private String login;

    @NotNull
    @Setter
    private String password;
    
    @Getter @Setter
    private String forgotPasswordCode;
    
    @Getter @Setter
    private Long forgotPasswordCodeDate;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private Integer timezone;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String emailConfirmCode;

    @Getter @Setter
    private Boolean emailConfirmed;
    
    @Getter @Setter
    private Boolean privacyConfirmed;
    
    @Getter @Setter
    private Boolean ageConfirmed;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
    @Setter
    private List<UserAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void addAuthority(UserAuthority authority) {
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        this.authorities.add(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder() //
                .appendSuper(super.hashCode()) //
                .append(this.login) //
                .append(this.password) //
                .append(this.active) //
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return new EqualsBuilder() //
                .appendSuper(super.equals(obj)) //
                .append(this.login, other.login) //
                .append(this.password, other.password) //
                .append(this.active, other.active) //
                .isEquals();
    }
}
