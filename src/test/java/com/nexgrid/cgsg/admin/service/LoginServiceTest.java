package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.mapper.LoginMapper;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @InjectMocks
    private LoginService loginServiceMock;

    @Mock
    private LoginMapper loginMapper;

    @Before
    public void before() {

    }
    @Test
    public void login() {
    }

    @Test
    public void successLogin() {
    }

    @Test
    public void lockedLogin() {
    }

    @Test
    public void unLockedLogin() {
    }

    @Test
    public void getLeftMinuteBetweenNowDateAnd() {
    }

    @Test
    public void 비밀번호변경확인_날짜_지남_MOCK() {
        when(loginMapper.getApplyDate(any(MbrInfo.class))).thenReturn("2019-06-09 00:00:00.0");
        LocalDate nowDate = LocalDate.of(2019, 9, 8);
        boolean expirePasswordDuration = loginServiceMock.isExpirePasswordDuration(new MbrInfo(), nowDate);

        assertThat(expirePasswordDuration).isTrue();
    }

    @Test
    public void 비밀번호변경확인_날짜_안지남_MOCK() {
        when(loginMapper.getApplyDate(any(MbrInfo.class))).thenReturn("2019-06-08 00:00:00.0");
        LocalDate nowDate = LocalDate.of(2019, 9, 8);
        boolean expirePasswordDuration = loginServiceMock.isExpirePasswordDuration(new MbrInfo(), nowDate);

        assertThat(expirePasswordDuration).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void 비밀번호변경확인_Mbrinfo_없음() {
        when(loginMapper.getApplyDate(any(MbrInfo.class))).thenReturn("");
        LocalDate nowDate = LocalDate.of(2019, 9, 8);
        boolean expirePasswordDuration = loginServiceMock.isExpirePasswordDuration(new MbrInfo(), nowDate);
    }

    @Test
    public void failLogin() {
        MbrInfo mbrInfo = new MbrInfo();
        mbrInfo.setMbrId("admin");

        LocalDate nowDate = LocalDate.of(2019, 9, 8);

        boolean expirePasswordDuration = loginService.isExpirePasswordDuration(mbrInfo, nowDate);

        assertThat(expirePasswordDuration).isTrue();
    }

    @Test
    public void unlockedLoginForFail() {
    }

    @Test
    public void lockedLoginForFail() {
    }

    @Test
    public void isExpirePasswordDuration() {
    }
}