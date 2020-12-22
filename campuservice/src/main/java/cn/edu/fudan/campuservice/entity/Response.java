package cn.edu.fudan.campuservice.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Response<T> {
    private String code;
    private String message;
    private T data;

    public static <T> Response success(T data) {
        return new Response<>("200", "OK", data);
    }

    public static <T> Response success(String message, T data) {
        return new Response<>("200", message, data);
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\": \"" + code + '\"' +
                ", \"message\": \"" + message + '\"' +
                ", \"data\": \"" + data + '\"' +
                '}';
    }
}
