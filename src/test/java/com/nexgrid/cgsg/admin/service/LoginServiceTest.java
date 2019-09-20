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
import org.springframework.transaction.annotation.Transactional;

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
    public static final String MBR_ID = "admin";

    @Test
    public void login() {
    }

    @Test
    public void successLogin() {
    }

    private String getNowDateTimeMinues(int i) {
        return LocalDateTime.now().minusMinutes(i).format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
    }

    @Test
    public void lockedLogin_잠금상태() {
        String loginFailCnt = "0";
        String loginFailDate = getNowDateTimeMinues(11);

        try {
            loginServiceMock.lockedLoginChecker(this.MBR_ID, loginFailCnt, loginFailDate);
        } catch (AdminException e) {
            assertThat(e.getCode()).isEqualByComparingTo(SystemStatusCode.LOGIN_FAIL_LOCK);
        }
    }

    @Test
    public void lockedLogin_로그인전_인증_실패() {
        String loginFailCnt = "3";
        String loginFailDate = getNowDateTimeMinues(11);

        try {
            loginServiceMock.lockedLoginChecker(this.MBR_ID, loginFailCnt, loginFailDate);
        } catch (AdminException e) {
            assertThat(e.getCode()).isEqualByComparingTo(SystemStatusCode.LOGIN_FAIL_LOCK_LEFT_TIME);
        }
    }

    @Test
    public void lockedLogin_10분_초과_5회_이상_계정_잠금해제() {
        String loginFailCnt = "6";
        String loginFailDate = getNowDateTimeMinues(11);

        when(loginMapper.setUserUnLockAndResetFailCnt(any(String.class))).thenReturn(1);
        loginServiceMock.lockedLoginChecker(this.MBR_ID, loginFailCnt, loginFailDate);

        verify(loginMapper, times(1)).setUserUnLockAndResetFailCnt(anyString());

    }

    @Test
    @Transactional
    public void lockedLogin_10분_초과_5회_이상_계정_잠금해제_DB() {
        String loginFailCnt = "6";
        String loginFailDate = getNowDateTimeMinues(11);

        loginService.lockedLoginChecker(this.MBR_ID, loginFailCnt, loginFailDate);
    }

    @Test
    public void unlockedLogin_실패_n회_이상_로그인제한_n분_이하() {
        String loginFailCnt = "6";
        String loginFailDate = getNowDateTimeMinues(10);

        try {
            loginServiceMock.unLockedLoginChecker(this.MBR_ID, loginFailCnt, loginFailDate);
        } catch (AdminException e) {
            assertThat(e.getCode()).isEqualByComparingTo(SystemStatusCode.LOGIN_FAIL_LOCK_LEFT_TIME);
        }
    }

    @Test
    public void unlockedLogin_로그인성공() {
        String loginFailCnt = "6";
        String loginFailDate = getNowDateTimeMinues(11);

        when(loginMapper.setUserUnLockAndResetFailCnt(anyString())).thenReturn(1);

        loginServiceMock.unLockedLoginChecker(this.MBR_ID, loginFailCnt, loginFailDate);

        verify(loginMapper, times(1)).setUserUnLockAndResetFailCnt(anyString());
    }

    @Test
    @Transactional
    public void unlockedLogin_로그인성공_DB() {
        String loginFailCnt = "6";
        String loginFailDate = getNowDateTimeMinues(11);

        loginService.unLockedLoginChecker(this.MBR_ID, loginFailCnt, loginFailDate);
    }

    @Test
    public void 비밀번호변경확인_날짜_지남_MOCK() {
        when(loginMapper.getApplyDate(any(MbrInfo.class))).thenReturn("2019-06-09 00:00:00.0");
        LocalDate nowDate = LocalDate.of(2019, 9, 8);
        boolean expirePasswordDuration = loginServiceMock.isExpirePasswordDuration(new MbrInfo(), nowDate);

        assertThat(expirePasswordDuration).isTrue();
    }

    @Test
    public void 비밀번호변경확인_날짜_지남_DB() {
        MbrInfo mbrInfo = new MbrInfo();
        mbrInfo.setMbrId(MBR_ID);

        LocalDate nowDate = LocalDate.of(2019, 9, 8);
        boolean expirePasswordDuration = loginService.isExpirePasswordDuration(mbrInfo, nowDate);

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
    public void unlockedLoginForFail_5회_오류나면_아이디_잠금() {
        String loginFailCnt = "5";

        try {
            loginServiceMock.unlockedLoginForFail(this.MBR_ID, loginFailCnt);
        } catch (AdminException e) {
            assertThat(e.getCode()).isEqualByComparingTo(SystemStatusCode.LOGIN_FAIL_LOCKED_USER_ID);
            verify(loginMapper, times(1)).setUserLock(anyString());
            verify(loginMapper, times(0)).updateLoginFailCnt(any(LoginInfo.class));
        }
    }

    @Test
    @Transactional
    public void unlockedLoginForFail_5회_오류나면_아이디_잠금_DB() {
        String loginFailCnt = "5";

        try {
            loginService.unlockedLoginForFail(this.MBR_ID, loginFailCnt);
        } catch (AdminException e) {
            assertThat(e.getCode()).isEqualByComparingTo(SystemStatusCode.LOGIN_FAIL_LOCKED_USER_ID);
        }
    }

    @Test
    public void unlockedLoginForFail_로그인실패_5회_이하() {
        String loginFailCnt = "4";

        loginServiceMock.unlockedLoginForFail(this.MBR_ID, loginFailCnt);

        verify(loginMapper, times(0)).setUserLock(anyString());
        verify(loginMapper, times(1)).updateLoginFailCnt(any(LoginInfo.class));
    }

    @Test
    @Transactional
    public void unlockedLoginForFail_로그인실패_5회_이하_DB() {
        String loginFailCnt = "4";

        loginService.unlockedLoginForFail(this.MBR_ID, loginFailCnt);
    }

    @Test
    public void lockedLoginForFail_10분경과_아이디_잠금해제() {
        String loginFailCnt = "5";
        String loginFailDate = getNowDateTimeMinues(11);
        loginServiceMock.lockedLoginForFail(this.MBR_ID, loginFailCnt, loginFailDate);

        verify(loginMapper, times(1)).setUserUnLockAndResetFailCnt(anyString());
        verify(loginMapper, times(1)).updateLoginFailCnt(any());
    }

    @Test
    @Transactional
    public void lockedLoginForFail_10분경과_아이디_잠금해제_DB() {
        String loginFailCnt = "5";
        String loginFailDate = getNowDateTimeMinues(11);
        loginService.lockedLoginForFail(this.MBR_ID, loginFailCnt, loginFailDate);
    }

    @Test
    public void lockedLoginForFail_10분경과_아이디_잠금() {
        String loginFailCnt = "5";
        String loginFailDate = getNowDateTimeMinues(9);

        try {
            loginServiceMock.lockedLoginForFail(this.MBR_ID, loginFailCnt, loginFailDate);
        } catch (AdminException e) {
            assertThat(e.getCode()).isEqualByComparingTo(SystemStatusCode.LOGIN_FAIL_LOCK);
        }
    }

    @Test
    public void isExpirePasswordDuration() {
    }
}