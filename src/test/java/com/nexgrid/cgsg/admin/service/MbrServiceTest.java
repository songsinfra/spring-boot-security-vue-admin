package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.base.BaseServiceTest;
import com.nexgrid.cgsg.admin.utils.TestObjectFactory;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import org.junit.Before;
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
@Transactional
public class MbrServiceTest extends BaseServiceTest {

    private final String MBR_ID1 = "TESTID2";
    private final String MBR_ID2 = "TESTID3";
    @Autowired
    private MbrService mbrService;

    @Autowired
    private TestObjectFactory testObjectFactory;

    @Before
    public void setUp() {
        mbrService.insertMbr(testObjectFactory.getMbrInfo()
                                              .mbrId(MBR_ID1)
                                              .build());
        mbrService.insertMbr(testObjectFactory.getMbrInfo()
                                              .mbrId(MBR_ID2)
                                              .build());
    }

    @Test
    public void getMemberList_ALL() {
        String managerYn = "Y";
        String mbrId = MBR_ID1;

        List<MbrInfo> memberList = mbrService.getMemberList(managerYn, mbrId);

        assertThat(memberList).size().isGreaterThan(1);
    }

    @Test
    public void getMemberList_ID() {
        String managerYn = "";
        String mbrId = MBR_ID1;

        List<MbrInfo> memberList = mbrService.getMemberList(managerYn, mbrId);

        assertThat(memberList).size().isGreaterThan(0);
    }

    @Test
    public void insertMbr() {
        int insertCnt = mbrService.insertMbr(testObjectFactory.getMbrInfo()
                                                              .build()
        );

        assertThat(insertCnt).isEqualTo(1);
    }

    @Test
    public void isDuplicateMbrId_검색된_아이디_있음() {
        String mbrId = MBR_ID1;
        String tel = "";
        boolean duplicateMbrId = mbrService.isDuplicateMember(mbrId, tel);

        assertThat(duplicateMbrId).isTrue();
    }

    @Test
    public void isDuplicateMbrId_검색된_휴대폰있음() {
        String mbrId = "";
        String tel = testObjectFactory.getMbrInfo().build().getTel();
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

    @Test
    public void updateMbr_등록된_멤버가_없음_오류() {
        this.assertException(IllegalArgumentException.class, "mbrId is null");

        mbrService.updateMbr(testObjectFactory.getMbrInfo()
                                              .mbrId(null)
                                              .build()
        );
    }

    @Test
    public void updateMbr() {
        int insertCnt = mbrService.updateMbr(testObjectFactory.getMbrInfo()
                                                              .mbrId(MBR_ID1)
                                                              .build()
        );

        assertThat(insertCnt).isEqualTo(1);
    }

    @Test
    public void updatePwd_등록된_사용자없음() {
        assertException(IllegalArgumentException.class, "registered Member is not found");
        String mbrId = "amin0";
        String newPwd = "test12320";

        int updateCnt = mbrService.updatePwd(mbrId, newPwd);
    }

    @Test
    public void updatePwd() {
        String mbrId = MBR_ID1;
        String newPwd = "test12320";

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
    public void deleteMbr() {
        List<String> mbrIdList = new ArrayList<>();
        mbrIdList.add(MBR_ID1);
        mbrIdList.add(MBR_ID2);

        int deleteCnt = mbrService.deleteMbr(mbrIdList);

        assertThat(deleteCnt).isEqualTo(mbrIdList.size());
    }

    @Test
    public void getCompanyList() {
        List<String> companyList = mbrService.getCompanyList();

        assertThat(companyList).size().isGreaterThan(0);
    }

}
