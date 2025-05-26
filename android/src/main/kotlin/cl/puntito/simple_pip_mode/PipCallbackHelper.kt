package cl.puntito.simple_pip_mode

import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.util.Rational
import androidx.annotation.NonNull
import cl.puntito.simple_pip_mode.actions.PipAction
import io.flutter.plugin.common.MethodChannel
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.Log

open class PipCallbackHelper {
    private val CHANNEL = "puntito.simple_pip_mode"
    var shouldEnterPip: Boolean = false

    var aspectRatio: Rational = Rational(9, 16)
    var autoEnter: Boolean = false
    var seamlessResize: Boolean = false
    var actions: List<RemoteAction> = emptyList()

    fun buildPipParams(): PictureInPictureParams {
        val builder = PictureInPictureParams.Builder()
            .setAspectRatio(Rational(9, 16))
            .setActions(actions)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            builder.setAutoEnterEnabled(autoEnter)
            builder.setSeamlessResizeEnabled(seamlessResize)
        }

        return builder.build()
    }

    private lateinit var channel: MethodChannel


    fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
    }

    fun setChannel(channel: MethodChannel) {
        this.channel = channel
    }

    fun onPictureInPictureModeChanged(active: Boolean) {
        if (active) {
            channel.invokeMethod("onPipEntered", null)
        } else {
            channel.invokeMethod("onPipExited", null)
        }
    }

    fun onPipAction(action: PipAction) {
        when (action) {
            PipAction.CUSTOM -> {
                channel.invokeMethod("onCustomPipAction", "custom_action")
            }
            PipAction.MIC_ON -> {
                Log.d("PIP_MODE", "Action onPipAction: mic_on")
                channel.invokeMethod("onMicAction", "mic_on")
            }
            PipAction.MIC_OFF -> {
                Log.d("PIP_MODE", "Action onPipAction: mic_off")
                channel.invokeMethod("onMicAction", "mic_off")
            }
            PipAction.CAMERA_ON -> {
                Log.d("PIP_MODE", "Action onPipAction: camera_on")
                channel.invokeMethod("onCameraAction", "camera_on")
            }
            PipAction.CAMERA_OFF -> {
                Log.d("PIP_MODE", "Action onPipAction: camera_off")
                channel.invokeMethod("onCameraAction", "camera_off")
            }
            else -> {
                channel.invokeMethod("onPipAction", action.name.lowercase())
            }
        }
    }
    
    // Additional method to send custom messages from the PIP action
    fun sendCustomActionMessage(message: String) {
        channel.invokeMethod("onCustomPipAction", message)
    }
    
    // Additional method to send mic state messages from the PIP action
    fun sendMicStateMessage(isMicOn: Boolean) {
        channel.invokeMethod("onMicAction", if (isMicOn) "mic_on" else "mic_off")
    }
    
    // Additional method to send camera state messages from the PIP action
    fun sendCameraStateMessage(isCameraOn: Boolean) {
        channel.invokeMethod("onCameraAction", if (isCameraOn) "camera_on" else "camera_off")
    }
}