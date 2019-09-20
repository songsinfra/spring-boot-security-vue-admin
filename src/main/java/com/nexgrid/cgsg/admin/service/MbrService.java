package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.mapper.MbrMapper;
import com.nexgrid.cgsg.admin.utils.StringUtil;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MbrService {

    @Autowired
    private MbrMapper mbrMapper;

    public List<MbrInfo> getMemberList(String managerYn, String mbrId) {
        Assert.notNull(managerYn, "managerYn is null");
        Assert.hasLength(mbrId, "mbrId is null");

        return mbrMapper.getMemberList(managerYn, mbrId);
    }

    public int insertMbr(MbrInfo mbrInfo) {
        Assert.notNull(mbrInfo, "mbrInfo is null");

        return mbrMapper.insertMbr(mbrInfo);
    }

    public boolean isDuplicateMember(String mbrId, String tel) {

        int duplicationMbrId = mbrMapper.isDuplicationMember(mbrId, tel);

        return duplicationMbrId > 0;
    }

    public int updateMemberInfo(MbrInfo mbrInfo) {
        Assert.notNull(mbrInfo, "mbrInfo is null");
        Assert.hasLength(mbrInfo.getMbrId(), "mbrId is null");

        if (StringUtil.empty(mbrInfo.getNewPw())) {
            this.setPwdHistoryTo(mbrInfo);
        }

        mbrInfo.setPwApplyDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        return mbrMapper.updateMemberInfo(mbrInfo);
    }

    private void setPwdHistoryTo(MbrInfo mbrInfo) {
        MbrInfo mbrOldInfo = mbrMapper.getMbrOldInfo(mbrInfo.getMbrId());
        Assert.notNull(mbrOldInfo, "registered Member is not found");

        mbrInfo.setMbrPw(mbrInfo.getNewPw());
        mbrInfo.setMbrPwOld1(mbrOldInfo.getMbrPw());
        mbrInfo.setMbrPwOld2(mbrOldInfo.getMbrPwOld1());
    }

    public int updatePwd(String mbrId, String newPwd) {
        Assert.hasLength(mbrId, "mbrId is null");
        Assert.hasLength(newPwd, "newPwd is null");

        MbrInfo mbrOldInfo = mbrMapper.getMbrOldInfo(mbrId);

        MbrInfo mbrInfoParam = MbrInfo.builder()
                        .newPw(newPwd)
                        .pwApplyDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                        .mbrPw(mbrOldInfo.getMbrPw())
                        .mbrPwOld1(mbrOldInfo.getMbrPwOld1())
                        .build()
                ;

        return mbrMapper.setMemberPwdInit(mbrInfoParam);
    }

    @Transactional
    public void deleteMbr(List<String> mbrIdList){
        for (String mbrId: mbrIdList) {
            mbrMapper.deleteMbr(mbrId);
        }
    }

    public List<String> getCompanyList() {
        return mbrMapper.getCompanyList();
    }





}
