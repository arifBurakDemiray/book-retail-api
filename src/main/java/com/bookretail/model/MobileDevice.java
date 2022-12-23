package com.bookretail.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mobile_devices")
public class MobileDevice {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NonNull
    @Column(unique = true, nullable = false, length = 36)
    private String deviceId;

    @NonNull
    @CreationTimestamp
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @NonNull
    @CreationTimestamp
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @NonNull
    @Column(unique = true, nullable = false, length = 36)
    private String refreshToken;

    public MobileDevice(@NonNull User user, @NotNull String deviceId, @NotNull String refreshToken) {
        this.user = user;
        this.deviceId = deviceId;
        this.refreshToken = refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        updated();
    }

    private void updated() {
        updatedAt = Date.from(Instant.now());
    }
}
