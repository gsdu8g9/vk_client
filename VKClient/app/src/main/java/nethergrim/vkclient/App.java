package nethergrim.vkclient;

import android.app.Application;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

/**
 * Created by nethergrim on 06.10.2014.
 */
public class App extends Application {

    public static final String TOKEN_KEY = "nethergrim";

    @Override
    public void onCreate() {
        super.onCreate();
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        for (int i = 0; i < fingerprints.length; i++){
            Log.e("fingerprint", fingerprints[i]);
        }
    }
}
