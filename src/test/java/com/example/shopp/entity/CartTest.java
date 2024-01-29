package com.example.shopp.entity;

import com.example.shopp.constant.Role;
import com.example.shopp.dto.MemberDto;
import com.example.shopp.repository.CartRepository;
import com.example.shopp.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class CartTest {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void memberCartTest() {
        // 멤버 DTO 생성
        MemberDto memberDto = MemberDto.builder()
                .name("홍길동")
                .email("hong@example.com")
                .password("password")
                .address("서울시")
                .build();

        // DTO를 이용하여 Member 엔티티 생성. DTO -> Entity
        Member member = Member.builder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .password(memberDto.getPassword())
                .address(memberDto.getAddress())
                .role(Role.USER) // 예시로 사용자 역할 설정
                .build();

        // 멤버 저장
        memberRepository.save(member);

        // Cart에 멤버 등록 및 저장
        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush();
        em.clear();

        // 멤버 검색
        Cart foundCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(member.getId(), foundCart.getMember().getId(), "멤버 ID가 일치하지 않습니다.");
    }
}