package cl.puntito.simple_pip_mode

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import cl.puntito.simple_pip_mode.PipCallbackHelper
import io.flutter.embedding.android.FlutterFragmentActivity;

open class PipCallbackHelperActivityWrapper : FlutterFragmentActivity() {
    protected var callbackHelper = PipCallbackHelper()

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        callbackHelper.configureFlutterEngine(flutterEngine)
    }

//    override fun onPictureInPictureModeChanged(active: Boolean, newConfig: Configuration?) {
//        super.onPictureInPictureModeChanged(active, newConfig)
//        callbackHelper.onPictureInPictureModeChanged(active)
//        Log.e("PIP_MODE", "onPictureInPictureModeChanged: $active")
//    }


    override fun onUserLeaveHint() {
        Log.e("PIP_MODE", "callbackHelper.shouldEnterPip ${callbackHelper.shouldEnterPip}");
        if (SimplePipModePlugin.callbackHelper.shouldEnterPip) {
//            super.onUserLeaveHint();

            enterPictureInPictureMode(SimplePipModePlugin.callbackHelper.buildPipParams())
        }
    }
}