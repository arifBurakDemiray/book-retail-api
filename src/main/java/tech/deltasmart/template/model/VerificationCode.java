package com.bookretail.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.bookretail.enums.EVerificationKeyType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "verification_codes")
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String code;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiration;

    @NotNull
    @Column()
    private EVerificationKeyType type;

    public VerificationCode(String code, User user, Date expiration, EVerificationKeyType type) {
        this.code = code;
        this.user = user;
        this.type = type;
        this.expiration = expiration;
    }

    public boolean isExpired() {
        return expiration.compareTo(new Date()) <= 0;
    }
}
