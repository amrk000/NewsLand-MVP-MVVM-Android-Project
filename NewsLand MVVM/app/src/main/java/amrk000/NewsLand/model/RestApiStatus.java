package amrk000.NewsLand.model;

public class RestApiStatus {
    private boolean loading;
    private int RequestStatus;
    private int loadedDataCount;
    private String responseErrorMessage;
    private Throwable failException;

    //Successful Status
    public static final int DATA_LOADED = 0;
    public static final int ALL_DONE = 1;
    public static final int API_ERROR_MESSAGE = 2;
    public static final int API_REQUEST_FAILED = 3;
    public static final int API_REQUEST_WRONG_KEY = 4;
    public static final int API_REQUEST_NO_RESULTS = 5;

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public int getRequestStatus() {
        return RequestStatus;
    }

    public void setRequestStatus(int requestStatus) {
        RequestStatus = requestStatus;
    }

    public int getLoadedDataCount() {
        return loadedDataCount;
    }

    public void setLoadedDataCount(int loadedDataCount) {
        this.loadedDataCount = loadedDataCount;
    }

    public String getResponseErrorMessage() {
        return responseErrorMessage;
    }

    public void setResponseErrorMessage(String responseErrorMessage) {
        this.responseErrorMessage = responseErrorMessage;
    }

    public Throwable getFailException() {
        return failException;
    }

    public void setFailException(Throwable failException) {
        this.failException = failException;
    }
}
