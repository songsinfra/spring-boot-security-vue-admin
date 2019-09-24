package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.vo.MbrInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MbrServiceTest {

    @Autowired
    private MbrService mbrService;


    @Test
    @Transactional
    public void getMemberList_ALL() {
        String managerYn = "Y";
        String mbrId = "admin1";

        List<MbrInfo> memberList = mbrService.getMemberList(managerYn, mbrId);

        assertThat(memberList).size().isGreaterThan(1);
    }

    @Test
    @Transactional
    public void getMemberList_ID() {
        String managerYn = "";
        String mbrId = "admin1";

        List<MbrInfo> memberList = mbrService.getMemberList(managerYn, mbrId);

        assertThat(memberList).size().isGreaterThan(0);
    }

    @Test
    @Transactional
    public void insertMbr() {
        int insertCnt = mbrService.insertMbr(MbrInfo.builder()
                .mbrId("insertMbr1")
                .mbrNm("홍길동")
                .roleCd("R-001")
                .mbrPw("pwd")
                .mbrCompany("넥스그리드")
                .mbrDptmt("부서")
                .tel("01012341234")
                .email("hong@nexgrid.co.kr")
                .build()
        );

        assertThat(insertCnt).isEqualTo(1);
    }

    @Test
    public void isDuplicateMbrId_검색된_아이디_있음() {
        String mbrId = "admin1";
        String tel = "";
        boolean duplicateMbrId = mbrService.isDuplicateMember(mbrId, tel);

        assertThat(duplicateMbrId).isTrue();
    }

    @Test
    public void isDuplicateMbrId_검색된_휴대폰있음() {
        String mbrId = "";
        String tel = "01012341233";
        boolean duplicateMbrId = mbrService.isDuplicateMember(mbrId, tel);

        assertThat(duplicateMbrId).isTrue();
    }

    @Test
    public void isDuplicateMbrId_검색된_회원_없음() {
        String mbrId = "$$$";
        String tel = "";
        boolean duplicateMbrId = mbrService.isDuplicateMember(mbrId, tel);

        assertThat(duplicateMbrId).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void isDuplicateMbrId_검색_입력값_없음() {
        String mbrId = "";
        String tel = "";
        boolean duplicateMbrId = mbrService.isDuplicateMember(mbrId, tel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateMemberInfo_등록된_멤버가_없음_오류() {
        mbrService.updateMemberInfo(MbrInfo.builder()
                .mbrId("insertMbr1")
                .mbrNm("홍길동")
                .newPw("newPassword")
                .roleCd("R-001")
                .mbrPw("pwd")
                .mbrCompany("넥스그리드")
                .mbrDptmt("부서")
                .useYn("Y")
                .tel("01012341234")
                .email("hong@nexgrid.co.kr")
                .build()
        );
    }

    @Test
    @Transactional
    public void updateMemberInfo() {
        int insertCnt = mbrService.updateMemberInfo(MbrInfo.builder()
                .mbrId("admin1")
                .mbrNm("홍길동")
                .roleCd("R-001")
                .mbrPw("pwd")
                .mbrCompany("넥스그리드")
                .mbrDptmt("부서")
                .useYn("Y")
                .tel("01012341234")
                .email("hong@nexgrid.co.kr")
                .build()
        );

        assertThat(insertCnt).isEqualTo(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updatePwd_등록된_사용자없음() {
        String mbrId = "amin1";
        String newPwd = "test12321";

        int updateCnt = mbrService.updatePwd(mbrId, newPwd);
    }

    @Test
    @Transactional
    public void updatePwd() {
        String mbrId = "admin1";
        String newPwd = "test12321";

        int updateCnt = mbrService.updatePwd(mbrId, newPwd);

        assertThat(updateCnt).isEqualTo(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteMbr_입력데이터_없음() {
        List<String> mbrIdList = new ArrayList<>();

        int deleteCnt = mbrService.deleteMbr(mbrIdList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteMbr_입력데이터_null() {
        List<String> mbrIdList = null;

        int deleteCnt = mbrService.deleteMbr(mbrIdList);
    }

    @Test
    @Transactional
    public void deleteMbr() {
        List<String> mbrIdList = new ArrayList<>();
        mbrIdList.add("admin1");
        mbrIdList.add("admin2");

        int deleteCnt = mbrService.deleteMbr(mbrIdList);

        assertThat(deleteCnt).isEqualTo(mbrIdList.size());
    }

    @Test
    public void getCompanyList() {
        List<String> companyList = mbrService.getCompanyList();

        assertThat(companyList).size().isGreaterThan(0);
    }

}