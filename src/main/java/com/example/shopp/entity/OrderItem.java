package com.example.shopp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    @Builder
    public OrderItem(Long id, Item item, Order order, int orderPrice, int count, LocalDateTime regTime, LocalDateTime updateTime) {
        this.id = id;
        this.item = item;
        this.order = order;
        this.orderPrice = orderPrice;
        this.count = count;
        this.regTime = regTime;
        this.updateTime = updateTime;
    }

}