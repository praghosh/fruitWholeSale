package nmfc.helper;
 
public class Result {
    private final boolean success;
    private final String message;
    public Result(boolean success, String result) {
        this.success = success;
        this.message = result;
    }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}