package albabe.albabe.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
    Description : ApiResponse를 정의하는 소스파일
 */

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean result;
    private String message;
    private T data;
}
