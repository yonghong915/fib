package com.fib.ecny.trans.comp;

import com.fib.common.bus.base.BizException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // 处理自定义业务异常，返回ProblemDetail而非Result
    @ExceptionHandler(BizException.class)
    public ProblemDetail handleBusiness(BizException e) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, e.getMessage());
        // 自定义扩展字段：带上业务错误码
        pd.setProperty("bizCode", e.getCode());
        pd.setProperty("timestamp", Instant.now());
        return pd; // 注意：直接返回ProblemDetail，不用包在ResponseEntity里
    }

    // 处理参数校验异常——这个以前要写一堆代码
    // 继承ResponseEntityExceptionHandler后，框架自动处理MethodArgumentNotValidException
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status,
                "请求参数校验失败");
        // 将字段校验错误注入到errors扩展属性中
        List<String> errors = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        pd.setProperty("errors", errors);
        return ResponseEntity.status(status).body(pd);
    }
    @ExceptionHandler(IdempotentException.class)
    public Object handleIdem(IdempotentException e){
        return Map.of("code",400,"msg",e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleArg(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return Result.fail(msg); // 返回所有错误信息，分号分隔
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraint(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream()
                .map(v -> v.getMessage())
                .collect(Collectors.joining("; "));
        return Result.fail(msg);
    }
}
