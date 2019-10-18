package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.mapper.MbrMapper;
import com.nexgrid.cgsg.admin.utils.CommonUtil;
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
        Assert.notNull(mbrInfo.getRoleCd(), "roleCd is null");
        Assert.notNull(mbrInfo.getMbrPw(), "mbrPw is null");
        Assert.notNull(mbrInfo.getMbrNm(), "mbrNm is null");

        mbrInfo.setMbrPw(CommonUtil.convertEncryptPassword(mbrInfo.getMbrPw()));

        return mbrMapper.insertMbr(mbrInfo);
    }

    public boolean isDuplicateMember(String mbrId, String tel) {
        if(StringUtil.empty(mbrId) && StringUtil.empty(tel)) throw new IllegalArgumentException("mbrId, tel is null");

        int duplicationMbrId = mbrMapper.isDuplicateMember(mbrId, tel);

        return duplicationMbrId > 0;
    }

    public int updateMbr(MbrInfo mbrInfo) {
        Assert.notNull(mbrInfo, "mbrInfo is null");
        Assert.hasLength(mbrInfo.getMbrId(), "mbrId is null");

        if (!StringUtil.empty(mbrInfo.getNewPw())) {
            mbrInfo.setNewPw(CommonUtil.convertEncryptPassword(mbrInfo.getNewPw()));
            this.setPwdHistoryTo(mbrInfo);
        }

        mbrInfo.setPwApplyDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        return mbrMapper.updateMbr(mbrInfo);
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
        Assert.notNull(mbrOldInfo, "registered Member is not found");

        MbrInfo mbrInfoParam = MbrInfo.builder()
                .newPw(newPwd)
                .mbrId(mbrId)
                .pwApplyDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")))
                .mbrPw(mbrOldInfo.getMbrPw())
                .mbrPwOld1(StringUtil.nvl(mbrOldInfo.getMbrPwOld1(), ""))
                .build();

        return mbrMapper.setMemberPwdInit(mbrInfoParam);
    }

    @Transactional
    public int deleteMbr(List<String> mbrIdList){
        Assert.notNull(mbrIdList, "mbrIdList is null");
        Assert.isTrue(mbrIdList.size() > 0, "mbrIdList is empty");

        return mbrMapper.deleteMbr(mbrIdList);
    }

    public List<String> getCompanyList() {
        return mbrMapper.getCompanyList();
    }





}
