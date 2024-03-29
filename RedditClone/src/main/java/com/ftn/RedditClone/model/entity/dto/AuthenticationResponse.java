package com.ftn.RedditClone.model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AuthenticationResponse {
    private String authenticationToken;
    private String username;
    private Date expiresAt;
}
