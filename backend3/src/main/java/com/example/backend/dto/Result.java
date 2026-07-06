package com.example.backend.dto;
//泛型类，T表示 data 字段的类型，可以是 List<BookResponse>、BookResponse或 Void
public class Result<T> {
    private int code;
    private String message;
    private T data;

    // 成功（带数据）
    //静态工厂方法，创建一个成功的响应，包含数据
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = "success";
        result.data = data;
        return result;
    }

    // 成功（不带数据，用于删除等操作）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败
    //静态工厂方法，创建一个失败的响应
    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        return result;
    }

    // Getter 方法
    //Getter 方法，Spring 自动转 JSON 时需要
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}