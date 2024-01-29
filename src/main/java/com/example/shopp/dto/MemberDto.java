package com.example.shopp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberDto {
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    private String email;
    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;
}