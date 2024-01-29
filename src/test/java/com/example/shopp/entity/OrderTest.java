package com.example.shopp.entity;

import com.example.shopp.constant.ItemSellStatus;
import com.example.shopp.constant.OrderStatus;
import com.example.shopp.constant.Role;
import com.example.shopp.repository.ItemRepository;
import com.example.shopp.repository.MemberRepository;
import com.example.shopp.repository.OrderItemRepository;
import com.example.shopp.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        return Item.builder()
                .itemNm("테스트 상품")
                .price(10000)
                .itemDetail("상세설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .regTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).build();
    }

    public OrderItem createOrderItem(Order order, Item item) {
        return OrderItem.builder()
                .item(item)
                .order(order)
                .count(10)
                .orderPrice(1000)
                .regTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    void cascadeTest() {

        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.ORDER)
                .regTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();


        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = createOrderItem(order, item);
            order.getOrderItems().add(orderItem);
        }
        orderRepository.saveAndFlush(order); // order를 저장하면서 flush를 호출하여 영속성 컨텍스트에 있는 객체들을 DB에 반영
        em.clear(); // 영속성 컨텍스트를 초기화 한 후 다시 find를 하여 DB에서 주문 엔티티와 주문 아이템 엔티티의 정보가 확실히 저장되었는지 확인하기 위함입니다.

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());
    }

    public Order createOrder() {
        Member member = Member.builder()
                .name("민지훈")
                .role(Role.USER)
                .email("astar5327z@gmail.com")
                .address("부산 동래구 어딘가")
                .password("1234")
                .build();
        memberRepository.save(member);

        Order order = Order.builder()
                .member(member)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.ORDER)
                .regTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = createOrderItem(order, item);
            order.getOrderItems().add(orderItem);
        }

        return order;
    }

    @Test
    @DisplayName("고아 객체 제거 테스트")
    void orphanRemovalTest() {
        Order order = createOrder();
        em.flush(); // DB에 order 데이터 반영되도록 하기

        // order에서 관리하는 orderItem 3개 묶음을 제거하고 그 아이디 가져오기
        Long removedItemId = order.getOrderItems().get(0).getId();

        // orderItem 3개 묶음을 제거하고 DB에 반영
        order.getOrderItems().remove(0);
        em.flush();

        // DB의 OrderItem 엔티티에서 삭제했던 orderItem 묶음의 인덱스에 대한 값이 남아있는지 확인
        // 잘 삭제 되었으면 Null이 떠야함
        OrderItem removedItem = em.find(OrderItem.class, removedItemId);

        assertNull(removedItem, "The removed OrderItem should be null if it has been deleted");
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = this.createOrder();

        em.flush(); // 영속성 컨텍스트에 있는 내용을 DB에 반영
        em.clear(); // 영속성 컨텍스트 초기화

        Long orderItemId = order.getOrderItems().get(0).getId();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("Order class: " + orderItem.getOrder().getClass());
        System.out.println("======================");
        orderItem.getOrder().getOrderDate();
        System.out.println("======================");
    }
}
