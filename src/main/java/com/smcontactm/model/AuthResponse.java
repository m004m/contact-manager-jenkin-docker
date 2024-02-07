package com.smcontactm.model;

import lombok.*;

import java.util.Collection;

    @Data
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public class AuthResponse {

        private  String userId;
        private  String accessToken;
        private String refreshToken;
        private Long expireAt;
        private Collection<String> authorities;

}
