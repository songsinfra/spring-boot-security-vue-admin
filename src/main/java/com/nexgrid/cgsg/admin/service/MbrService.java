package com.nexgrid.cgsg.admin.service;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.exception.AdminException;
import com.nexgrid.cgsg.admin.mapper.MbrMapper;
import com.nexgrid.cgsg.admin.utils.CommonUtil;
import com.nexgrid.cgsg.admin.utils.StringUtil;
import com.nexgrid.cgsg.admin.vo.MbrInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
        this.checkUserInfoInPwd(mbrInfo.getMbrPw(), mbrInfo);

        mbrInfo.setMbrPw(CommonUtil.convertEncryptPassword(mbrInfo.getMbrPw()));
        mbrInfo.setPwApplyDt(CommonUtil.getToday());

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

        mbrInfo.setMbrPw(""); // 회원수정시 업데이트는 newPw만 가능하게 초기화
        if (!StringUtil.empty(mbrInfo.getNewPw())) {
            this.checkUserInfoInPwd(mbrInfo.getNewPw(), mbrInfo);

            mbrInfo.setNewPw(CommonUtil.convertEncryptPassword(mbrInfo.getNewPw()));
            mbrInfo.setPwApplyDt(CommonUtil.getToday());
            this.setPwdHistoryTo(mbrInfo);
        }

        return mbrMapper.updateMbr(mbrInfo);
    }

    private void setPwdHistoryTo(MbrInfo mbrInfo) {
        MbrInfo mbrOldInfo = mbrMapper.getMbrOldInfo(mbrInfo.getMbrId());
        Assert.notNull(mbrOldInfo, "registered Member is not found");
        this.checkUsedPassword(mbrInfo.getNewPw(), mbrOldInfo);

        mbrInfo.setMbrPw(mbrInfo.getNewPw());
        mbrInfo.setMbrPwOld1(mbrOldInfo.getMbrPw());
        mbrInfo.setMbrPwOld2(mbrOldInfo.getMbrPwOld1());
    }

    private void checkUsedPassword(String encNewPw, MbrInfo mbrInfo) {
        // 이전에 사용했던 비번 확인
        if (StringUtils.equals(encNewPw, mbrInfo.getMbrPw())
                || StringUtils.equals(encNewPw, mbrInfo.getMbrPwOld1())
                || StringUtils.equals(encNewPw, mbrInfo.getMbrPwOld2())
        )
            throw new AdminException(SystemStatusCode.INVALID_PARAMETER, "이전에 사용한 비밀번호 입니다.");
    }

    private void checkUserInfoInPwd(String newPw, MbrInfo mbrInfo) {
        // userid 사용 확인
        if(StringUtils.containsAny(newPw, mbrInfo.getMbrId(), mbrInfo.getTel())) throw new AdminException(SystemStatusCode.INVALID_PARAMETER, "비빌번호에 개인정보가 포함되어 있으면 안됩니다.");
    }

    public int updatePwd(String mbrId, String newPwd) {
        Assert.hasLength(mbrId, "mbrId is null");
        Assert.hasLength(newPwd, "newPwd is null");

        MbrInfo mbrOldInfo = mbrMapper.getMbrOldInfo(mbrId);
        Assert.notNull(mbrOldInfo, "registered Member is not found");

        this.checkUserInfoInPwd(newPwd, mbrOldInfo);

        String encNewPwd = CommonUtil.convertEncryptPassword(newPwd);
        this.checkUsedPassword(encNewPwd, mbrOldInfo);

        MbrInfo mbrInfoParam = MbrInfo.builder()
                .newPw(encNewPwd)
                .mbrId(mbrId)
                .pwApplyDt(CommonUtil.getToday())
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
