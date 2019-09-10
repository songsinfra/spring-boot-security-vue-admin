package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.exception.AdminException;
import com.nexgrid.cgsg.admin.mapper.LoginMapper;
import com.nexgrid.cgsg.admin.vo.LoginInfo;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @InjectMocks
    private LoginService loginServiceMock;

    @Mock
    private LoginMapper loginMapper;

    @Test
    public void login() {
    }

    @Test
    public void successLogin() {
    }

    @Test
    public void lockedLogin_잠금상태() {
        String mbrId = "admin";
        String loginFailCnt = "0";
        String loginFailDate = getNowDateTimeMinues(11);

        try {
            loginServiceMock.lockedLoginChecker(mbrId, loginFailCnt, loginFailDate);
        } catch (AdminException e) {
            assertThat(e.getCode()).isEqualByComparingTo(SystemStatusCode.LOGIN_FAIL_LOCK);
        }
    }

    private String getNowDateTimeMinues(int i) {
        return LocalDateTime.now().minusMinutes(i).format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
    }

    @Test
    public void lockedLogin_로그인전_인증_실패() {
        String mbrId = "admin";
        String loginFailCnt = "3";
        String loginFailDate = getNowDateTimeMinues(11);

        try {
            loginServiceMock.lockedLoginChecker(mbrId, loginFailCnt, loginFailDate);
        } catch (AdminException e) {
            assertThat(e.getCode()).isEqualByComparingTo(SystemStatusCode.LOGIN_FAIL_LOCK_LEFT_TIME);
        }
    }

    @Test
    public void lockedLogin_10분_초과_5회_이상_계정_잠금해제() {
        String mbrId = "admin";
        String loginFailCnt = "6";
        String loginFailDate = getNowDateTimeMinues(11);

        when(loginMapper.setUserUnLockAndResetFailCnt(any(String.class))).thenReturn(1);
        loginServiceMock.lockedLoginChecker(mbrId, loginFailCnt, loginFailDate);

        verify(loginMapper, times(1)).setUserUnLockAndResetFailCnt(anyString());

    }

    @Test
    public void unlockedLogin_실패_n회_이상_로그인제한_n분_이하() {
        String mbrId = "admin";
        String loginFailCnt = "6";
        String loginFailDate = getNowDateTimeMinues(10);

        try {
            loginServiceMock.unLockedLoginChecker(mbrId, loginFailCnt, loginFailDate);
        } catch (AdminException e) {
            assertThat(e.getCode()).isEqualByComparingTo(SystemStatusCode.LOGIN_FAIL_LOCK_LEFT_TIME);
        }
    }

    @Test
    public void unlockedLogin_로그인성공() {
        String mbrId = "admin";
        String loginFailCnt = "6";
        String loginFailDate = getNowDateTimeMinues(11);

        when(loginMapper.setUserUnLockAndResetFailCnt(anyString())).thenReturn(1);

        loginServiceMock.unLockedLoginChecker(mbrId, loginFailCnt, loginFailDate);

        verify(loginMapper, times(1)).setUserUnLockAndResetFailCnt(anyString());
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
    public void unlockedLoginForFail_5회_오류나면_아이디_잠금() {
        String mbrId = "admin";
        String loginFailCnt = "5";

        try {
            loginServiceMock.unlockedLoginForFail(mbrId, loginFailCnt);
        } catch (AdminException e) {
            assertThat(e.getCode()).isEqualByComparingTo(SystemStatusCode.LOGIN_FAIL_LOCKED_USER_ID);
            verify(loginMapper, times(1)).setUserLock(anyString());
            verify(loginMapper, times(0)).updateLoginFailCnt(any(LoginInfo.class));
        }
    }

    @Test
    public void unlockedLoginForFail_로그인실패_5회_이하() {
        String mbrId = "admin";
        String loginFailCnt = "4";

        loginServiceMock.unlockedLoginForFail(mbrId, loginFailCnt);

        verify(loginMapper, times(0)).setUserLock(anyString());
        verify(loginMapper, times(1)).updateLoginFailCnt(any(LoginInfo.class));
    }

    @Test
    public void lockedLoginForFail() {
    }

    @Test
    public void isExpirePasswordDuration() {
    }
}