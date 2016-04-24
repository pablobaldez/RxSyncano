package rx.syncano;

import com.syncano.library.api.Response;

/**
 * Created by pablobaldez on 24/04/16.
 */
public class RxSyncanoException extends Exception {

    private int resultCode;
    private int httpCode;
    private String httpMessage;

    public RxSyncanoException(Response<?> response) {
        super(response.getError());
        resultCode = response.getResultCode();
        httpCode = response.getHttpResultCode();
        httpMessage = response.getHttpReasonPhrase();
    }

    public int getResultCode() {
        return resultCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getHttpMessage() {
        return httpMessage;
    }
}
