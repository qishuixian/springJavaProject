package com.example.backend.exception;

import com.example.backend.dto.Result;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

// 组合了 @ControllerAdvice+ @ResponseBody，表示该类用于全局异常处理，返回值直接写入响应体
// 处理顺序:当异常发生时，Spring 会按照 @ExceptionHandler的匹配精度从高到低查找，优先匹配最具体的异常类型。如果找不到匹配的，最后由 handleException(Exception.class)兜底
@RestControllerAdvice
// 全局异常处理器
public class GlobalExceptionHandler {

    // 处理自定义业务异常  指定该方法处理哪种类型的异常
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    // 处理资源不存在异常（IllegalArgumentException）
    @ExceptionHandler(IllegalArgumentException.class)
    //设置 HTTP 响应状态码
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleNotFound(IllegalArgumentException e) {
        return Result.error(404, e.getMessage());
    }

    // 处理数据库访问异常
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleDataAccessException(DataAccessException e) {
        return Result.error(500, "数据库操作失败，请稍后重试");
    }

    // 处理空指针异常
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleNullPointerException(NullPointerException e) {
        return Result.error(500, "服务器内部错误");
    }

    // 处理所有未捕获的异常（兜底）
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        // 生产环境中建议记录日志，不返回具体错误信息
        return Result.error(500, "服务器繁忙，请稍后重试");
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return Result.error(400, message);
    }
}