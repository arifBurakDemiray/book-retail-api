package com.bookretail.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@DynamicUpdate
@NoArgsConstructor
@Table(name = "user_notifications")
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "onesignal_notification_id", nullable = false)
    private OneSignalNotification oneSignalNotification;

    @Column(nullable = false, name = "is_read")
    @Setter
    private boolean read = false;

    @Column
    private Date readAt;

    public UserNotification(@NotNull User user, @NotNull OneSignalNotification oneSignalNotification) {
        this.user = user;
        this.oneSignalNotification = oneSignalNotification;
    }

}
