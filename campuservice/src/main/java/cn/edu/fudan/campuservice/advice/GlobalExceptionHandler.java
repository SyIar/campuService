package cn.edu.fudan.campuservice.advice;

import cn.edu.fudan.campuservice.entity.Response;
import cn.edu.fudan.campuservice.exception.ParamsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ParamsException.class)
    @ResponseBody
    public Response paramsExcetion(ParamsException e) {
        return new Response<>("400", "parameters error", e.getMessage());
    }
}
