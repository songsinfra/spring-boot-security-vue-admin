package com.nexgrid.cgsg.admin.exception;

import com.nexgrid.cgsg.admin.vo.ResultInfo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(AdminException.class)
    @ResponseBody
    public ResultInfo adminException(AdminException exception) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(exception.getCode().getCode());
        resultInfo.setMessage(exception.getMessage());

        return resultInfo;
    }

}
