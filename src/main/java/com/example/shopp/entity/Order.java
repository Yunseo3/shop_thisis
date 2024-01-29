package com.example.shopp.entity;

import com.example.shopp.constant.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @NotNull
    @Column(name = "reg_time")
    private LocalDateTime regTime;

    @NotNull
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Builder
    public Order(Long id, Member member, LocalDateTime orderDate, OrderStatus orderStatus, LocalDateTime regTime, LocalDateTime updateTime) {
        this.id = id;
        this.member = member;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.regTime = regTime;
        this.updateTime = updateTime;
    }

}