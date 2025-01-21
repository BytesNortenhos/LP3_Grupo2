package Utils;

import java.util.List;
import java.util.Map;

public class ErrorHandler {
    private boolean isSuccessful;
    private String message;
    private List<Map<String, Object>> opoData;

    public ErrorHandler(boolean isSuccessful, String message) {
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public ErrorHandler(boolean isSuccessful, String message, List<Map<String, Object>> opoData) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        this.opoData = opoData;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, Object>> getOpoData() {
        return opoData;
    }

    public void setOpoData(List<Map<String, Object>> opoData) {
        this.opoData = opoData;
    }
}
