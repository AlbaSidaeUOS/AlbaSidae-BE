package albabe.albabe.domain.dto;

public class ApiResponse<T> {
    private boolean result;
    private String message;
    private T data;

    // 성공 응답 생성
    public ApiResponse(boolean result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    // 실패 응답 생성
    public ApiResponse(boolean result, String message) {
        this.result = result;
        this.message = message;
        this.data = null; // 실패 시 데이터 없음
    }

    // Getter & Setter
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
