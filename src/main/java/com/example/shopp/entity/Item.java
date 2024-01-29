package com.example.shopp.entity;

import com.example.shopp.constant.ItemSellStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "item_name", length = 50)
    private String itemNm;

    @NotNull
    @Column(name = "price")
    private int price;

    @NotNull
    @Column(name = "stock_number")
    private int stockNumber;

    @NotNull
    @Column(name = "item_detail", columnDefinition="TEXT")
    private String itemDetail;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "item_sell_status")
    private ItemSellStatus itemSellStatus;

    @NotNull
    @Column(name = "reg_time")
    private LocalDateTime regTime;

    @NotNull
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Builder
    public Item(Long id, String itemNm, int price, int stockNumber, String itemDetail, ItemSellStatus itemSellStatus, LocalDateTime regTime, LocalDateTime updateTime) {
        this.id = id;
        this.itemNm = itemNm;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
        this.regTime = regTime;
        this.updateTime = updateTime;
    }
}