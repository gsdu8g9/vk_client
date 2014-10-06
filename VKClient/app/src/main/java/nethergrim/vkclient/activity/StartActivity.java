package nethergrim.vkclient.activity;

import android.app.AlertDialog;
import android.os.Bundle;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;

import nethergrim.vkclient.App;
import nethergrim.vkclient.fragment.MainFragment;
import nethergrim.vkclient.R;

/**
 * Created by nethergrim on 06.10.2014.
 */
public class StartActivity extends AbstractActivity {

    private static final String[] sMyScope = new String[] {   VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.AUDIO,
            VKScope.DIRECT,
            VKScope.DOCS,
            VKScope.GROUPS,
            VKScope.MESSAGES,
            VKScope.NOTES,
            VKScope.NOTIFICATIONS,
            VKScope.NOTIFY,
            VKScope.PAGES, VKScope.STATS, VKScope.STATUS, VKScope.VIDEO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (VKAccessToken.tokenFromSharedPreferences(this, App.TOKEN_KEY) != null){
            VKSdk.initialize(sdkListener, "4579411", VKAccessToken.tokenFromSharedPreferences(this, App.TOKEN_KEY));
        } else {
            VKSdk.initialize(sdkListener, "4579411");
        }
        if (VKSdk.isLoggedIn()) {
            goToMainFragment();
        }
    }

    private void goToMainFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!VKSdk.isLoggedIn()){
            VKSdk.authorize(sMyScope);
        }
    }

    private final VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
        }
        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(sMyScope);
        }
        @Override
        public void onAccessDenied(final VKError authorizationError) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(authorizationError.toString())
                    .show();
        }
        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            newToken.saveTokenToSharedPreferences(StartActivity.this, App.TOKEN_KEY);
            goToMainFragment();
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
        }
    };
}
