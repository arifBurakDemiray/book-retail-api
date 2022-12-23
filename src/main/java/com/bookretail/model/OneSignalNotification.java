package com.bookretail.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "onesignal_notifications")
public class OneSignalNotification extends Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String heading;

    @Column
    private String content;

    @Column(nullable = false)
    @Setter
    private String oneSignalId = "";

    public OneSignalNotification(@NotNull User user, String heading, String content) {
        this.user = user;
        this.heading = heading;
        this.content = content;
    }

    @Getter
    @Column(nullable = false)
    @CreationTimestamp
    private Date createdOn;
}
