package android.os;

import androidx.annotation.NonNull;

public class Handler {

    public Handler(@NonNull final Looper looper) {
    }

    public boolean postAtFrontOfQueue(@NonNull final Runnable r) {
        r.run();
        return true;
    }

}
