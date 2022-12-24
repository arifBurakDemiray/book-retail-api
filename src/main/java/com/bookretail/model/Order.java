package com.bookretail.model;

import com.bookretail.enums.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Setter
    @Column(nullable = false)
    private EOrderStatus status;

    @NotNull
    @Column(nullable = false)
    private Double cost;

    @NotNull
    @Column(nullable = false)
    private Long quantity;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @CreationTimestamp
    private Date createdAt;

    @CreationTimestamp
    @Setter
    private Date updatedAt;

    public Order(Long quantity, Double cost, Book book, User user) {
        status = EOrderStatus.PENDING;
        this.quantity = quantity;
        this.cost = cost;
        this.book = book;
        this.user = user;
    }

}
