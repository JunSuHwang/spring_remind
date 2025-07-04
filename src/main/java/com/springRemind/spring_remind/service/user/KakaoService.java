package com.springRemind.spring_remind.service.user;

import com.google.gson.Gson;
import com.springRemind.spring_remind.advice.exception.CComunicationException;
import com.springRemind.spring_remind.model.social.KakaoProfile;
import com.springRemind.spring_remind.model.social.RetKakaoAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class KakaoService {

    private final RestTemplate restTemplate;
    private final Environment env;
    private final Gson gson;

    @Value("${spring.url.base}")
    private String baseUrl;

    @Value("${spring.social.kakao.client_id}")
    private String kakaoClientId;

    @Value("${spring.social.kakao.redirect}")
    private String kakaoRedirect;

    public KakaoProfile getKakaoProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("spring.social.kakao.url.profile"), request, String.class);
            if(response.getStatusCode() == HttpStatus.OK)
                return gson.fromJson(response.getBody(), KakaoProfile.class);
        } catch (Exception e) {
            throw new CComunicationException();
        }
        throw new CComunicationException();
    }

    public RetKakaoAuth getKakaoTokenInfo(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", baseUrl + kakaoRedirect);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("spring.social.kakao.url.token"), request, String.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            return gson.fromJson(response.getBody(), RetKakaoAuth.class);
        }
        return null;
    }
}
