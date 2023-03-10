package com.noti.noti.auth.adapter.in.web.controller;

import static com.noti.noti.error.ErrorCode.EXPIRED_JWT;
import static com.noti.noti.error.ErrorCode.ILLEGAL_ARGUMENT_JWT;
import static com.noti.noti.error.ErrorCode.INVALID_SIGNATURE_JWT;
import static com.noti.noti.error.ErrorCode.MALFORMED_JWT;
import static com.noti.noti.error.ErrorCode.TEACHER_NOT_FOUND;
import static com.noti.noti.error.ErrorCode.UNSUPPORTED_JWT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.noti.noti.auth.application.port.in.ReissueTokenUsecace;
import com.noti.noti.auth.domain.JwtToken;
import com.noti.noti.error.GlobalExceptionHandler;
import com.noti.noti.error.exception.CustomExpiredJwtException;
import com.noti.noti.error.exception.CustomIllegalArgumentException;
import com.noti.noti.error.exception.CustomMalformedJwtException;
import com.noti.noti.error.exception.CustomSignatureException;
import com.noti.noti.error.exception.CustomUnsupportedJwtException;
import com.noti.noti.teacher.application.exception.TeacherNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReissueTokenControllerTest ?????????")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ReissueTokenControllerTest {

  @InjectMocks
  ReissueTokenController reissueTokenController;

  @Mock
  ReissueTokenUsecace reissueTokenUsecace;

  MockMvc mockMvc;

  // MockMvc ????????? ????????? ??? ????????? ???????????? ????????????.
  @BeforeEach
  void prepare() {
    mockMvc = MockMvcBuilders.standaloneSetup(reissueTokenController)
        .setControllerAdvice(GlobalExceptionHandler.class)
        .addFilter(new CharacterEncodingFilter("UTF-8", true)).build();
  }

  @Nested
  class reissueTokne_???????????? {

    final String TOKEN = "token";
    final String AUTHORIZATION_HEADER = "Authorization";
    final String BEARER_PREFIX = "Bearer ";

    @Nested
    class ??????_?????????_?????????_?????????_???????????? {

      final String ACCESS_TOKEN = "access token";
      final String REFRESH_TOKEN = "refresh token";
      final JwtToken jwtToken = new JwtToken(ACCESS_TOKEN, REFRESH_TOKEN);

      @Test
      void ???????????????_?????????_????????????_?????????_??????_??????_?????????_????????????() throws Exception {
        when(reissueTokenUsecace.reissueToken(TOKEN)).thenReturn(jwtToken);

        mockMvc.perform(
                post("/api/auth/reissue").header(AUTHORIZATION_HEADER, BEARER_PREFIX + TOKEN))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.accessToken").value(ACCESS_TOKEN),
                jsonPath("$.refreshToken").value(REFRESH_TOKEN));
      }
    }

    @Nested
    class ??????_?????????_?????????_?????????_???????????? {

      @Test
      void CustomExpiredJwtException_?????????_????????????_401_????????????_?????????_????????????() throws Exception {
        when(reissueTokenUsecace.reissueToken(TOKEN))
            .thenThrow(new CustomExpiredJwtException("????????? ???????????????"));

        mockMvc.perform(
                post("/api/auth/reissue")
                    .header(AUTHORIZATION_HEADER, BEARER_PREFIX + TOKEN))
            .andExpectAll(
                status().isUnauthorized(),
                result -> assertThat(result.getResolvedException())
                    .isInstanceOf(CustomExpiredJwtException.class),
                jsonPath("$.message").value(EXPIRED_JWT.getMessage()),
                jsonPath("$.status").value(EXPIRED_JWT.getStatus()),
                jsonPath("$.errors").isEmpty(),
                jsonPath("$.code").value(EXPIRED_JWT.getCode())
            );
      }
    }

    @Nested
    class ??????_?????????_????????????_??????_?????????_???????????? {

      @Test
      void CustomUnsupportedJwtException_?????????_????????????_401_????????????_?????????_????????????() throws Exception {
        when(reissueTokenUsecace.reissueToken(TOKEN))
            .thenThrow(new CustomUnsupportedJwtException("???????????? ?????? ???????????????"));

        mockMvc.perform(
                post("/api/auth/reissue")
                    .header(AUTHORIZATION_HEADER, BEARER_PREFIX + TOKEN))
            .andExpectAll(
                status().isUnauthorized(),
                result -> assertThat(result.getResolvedException())
                    .isInstanceOf(CustomUnsupportedJwtException.class),
                jsonPath("$.message").value(UNSUPPORTED_JWT.getMessage()),
                jsonPath("$.status").value(UNSUPPORTED_JWT.getStatus()),
                jsonPath("$.errors").isEmpty(),
                jsonPath("$.code").value(UNSUPPORTED_JWT.getCode())
            );
      }
    }

    @Nested
    class ??????_?????????_?????????_?????????_???????????? {

      @Test
      void CustomMalformedJwtException_?????????_????????????_401_????????????_?????????_????????????() throws Exception {
        when(reissueTokenUsecace.reissueToken(TOKEN))
            .thenThrow(new CustomMalformedJwtException("????????? ???????????????"));

        mockMvc.perform(
                post("/api/auth/reissue")
                    .header(AUTHORIZATION_HEADER, BEARER_PREFIX + TOKEN))
            .andExpectAll(
                status().isUnauthorized(),
                result -> assertThat(result.getResolvedException())
                    .isInstanceOf(CustomMalformedJwtException.class),
                jsonPath("$.message").value(MALFORMED_JWT.getMessage()),
                jsonPath("$.status").value(MALFORMED_JWT.getStatus()),
                jsonPath("$.errors").isEmpty(),
                jsonPath("$.code").value(MALFORMED_JWT.getCode())
            );
      }
    }

    @Nested
    class ??????_?????????_?????????_signature_?????????_???????????? {

      @Test
      void CustomSignatureException_?????????_????????????_401_????????????_?????????_????????????() throws Exception {
        when(reissueTokenUsecace.reissueToken(TOKEN))
            .thenThrow(new CustomSignatureException("????????? ???????????????"));

        mockMvc.perform(
                post("/api/auth/reissue")
                    .header(AUTHORIZATION_HEADER, BEARER_PREFIX + TOKEN))
            .andExpectAll(
                status().isUnauthorized(),
                result -> assertThat(result.getResolvedException())
                    .isInstanceOf(CustomSignatureException.class),
                jsonPath("$.message").value(INVALID_SIGNATURE_JWT.getMessage()),
                jsonPath("$.status").value(INVALID_SIGNATURE_JWT.getStatus()),
                jsonPath("$.errors").isEmpty(),
                jsonPath("$.code").value(INVALID_SIGNATURE_JWT.getCode())
            );
      }
    }

    @Nested
    class ??????_?????????_???????????????_?????????_??????_???????????? {

      @Test
      void CustomIllegalArgumentException_?????????_????????????_401_????????????_?????????_????????????() throws Exception {
        when(reissueTokenUsecace.reissueToken(TOKEN))
            .thenThrow(new CustomIllegalArgumentException("????????? ??? ?????????"));

        mockMvc.perform(
                post("/api/auth/reissue")
                    .header(AUTHORIZATION_HEADER, BEARER_PREFIX + TOKEN))
            .andExpectAll(
                status().isUnauthorized(),
                result -> assertThat(result.getResolvedException())
                    .isInstanceOf(CustomIllegalArgumentException.class),
                jsonPath("$.message").value(ILLEGAL_ARGUMENT_JWT.getMessage()),
                jsonPath("$.status").value(ILLEGAL_ARGUMENT_JWT.getStatus()),
                jsonPath("$.errors").isEmpty(),
                jsonPath("$.code").value(ILLEGAL_ARGUMENT_JWT.getCode())
            );
      }
    }

    @Nested
    class ??????_?????????_?????????_????????????_?????????_????????????_????????? {

      @Test
      void TeacherNotFoundException_?????????_????????????_404_????????????_?????????_????????????() throws Exception {
        when(reissueTokenUsecace.reissueToken(TOKEN))
            .thenThrow(new TeacherNotFoundException());

        mockMvc.perform(
                post("/api/auth/reissue")
                    .header(AUTHORIZATION_HEADER, BEARER_PREFIX + TOKEN))
            .andExpectAll(
                status().isNotFound(),
                result -> assertThat(result.getResolvedException())
                    .isInstanceOf(TeacherNotFoundException.class),
                jsonPath("$.message").value(TEACHER_NOT_FOUND.getMessage()),
                jsonPath("$.status").value(TEACHER_NOT_FOUND.getStatus()),
                jsonPath("$.errors").isEmpty(),
                jsonPath("$.code").value(TEACHER_NOT_FOUND.getCode())
            );
      }
    }
  }
}