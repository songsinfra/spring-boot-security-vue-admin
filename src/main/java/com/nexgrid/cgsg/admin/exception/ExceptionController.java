package com.nexgrid.cgsg.admin.exception;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.vo.ResultInfo;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLDataException;
import java.util.List;

@ControllerAdvice
@RestController
public class ExceptionController implements ErrorController {

    private static final String PATH = "/error"; // configure 에서 Redirect 될 path

    @RequestMapping(value = PATH)
    public String error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (String.valueOf(status).equalsIgnoreCase(String.valueOf(HttpStatus.NOT_FOUND.value()))) {
            response.sendRedirect("/");
            return "errors/404"; // /WEB-INF/errors/404.jsp
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    @ExceptionHandler(AdminException.class)
    public ResponseEntity adminException(AdminException exception) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(exception.getCode().getCode());
        resultInfo.setMessage(exception.getMessage());

        return new ResponseEntity(resultInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity IllegalArgumentException(IllegalArgumentException exception) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(SystemStatusCode.INVALID_PARAMETER.getCode());
        resultInfo.setMessage(exception.getMessage());

        return new ResponseEntity(resultInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SQLDataException.class})
    public ResponseEntity SQLDataException(SQLDataException exception) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(SystemStatusCode.DB_ERROR.getCode());
        resultInfo.setMessage(exception.getMessage());

        return new ResponseEntity(resultInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ResultInfo resultInfo = new ResultInfo();
        BindingResult result = exception.getBindingResult();
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            String message = allErrors != null ? allErrors.get(0).getDefaultMessage() : "";
            resultInfo.setMessage(message);
        }

        resultInfo.setCode(SystemStatusCode.INVALID_PARAMETER.getCode());

        return new ResponseEntity(resultInfo, HttpStatus.BAD_REQUEST);
    }
}
