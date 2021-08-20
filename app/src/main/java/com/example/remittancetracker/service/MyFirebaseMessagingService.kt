package com.example.remittancetracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import com.example.android.eggtimernotifications.util.sendNotification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import com.example.remittancetracker.repo.Result
import com.example.remittancetracker.repo.remote.RemoteDataSourceImpl


class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        remoteMessage.data.isNotEmpty().let {
            Timber.i("remote_msg_data : data-> ${remoteMessage.data.toString()}")
            Log.i("new_noti","remote_msg_data :data-> ${remoteMessage.data}")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Timber.i("remote_msg_data :body-> ${it.body}")
            Log.i("new_noti","remote_msg_data :body-> ${it.body}")
            //  sendNotification(it.title.toString(),it.body.toString(),it.clickAction.toString())
            sendNotification(it.body)
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Timber.i("firebase_token: new token ${token}")
        Log.i("token", "firebase_token: new token ${token}")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        Timber.i("sendRegistrationTokenToServer($token)")
        //update user with token on firestore
        if (FirebaseAuth.getInstance().currentUser != null) {
            addTokenToFirestore(token!!)
        }

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(body: String?) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.channel_transaction),
                getString(R.string.channel_order_confirmed_name),
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.description = "New Transaction is recorded. Tap to view details."

            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.sendNotification(body.toString(), applicationContext)
    }


    companion object {
        private const val TAG = "MyFirebaseMsgService"
        fun addTokenToFirestore(token: String) {
            if (token == null) throw NullPointerException("FCM token is null")

            Timber.i("c : ${token}")

            val remote = RemoteDataSourceImpl()
            CoroutineScope(Dispatchers.IO).launch {
                val tokens = remote.getRegistrationToken()
                when (tokens) {
                    is Result.Success -> {
                        if (tokens.data.contains(token))
                            return@launch
                        tokens.data.add(token)

                        val set_token = remote.setFCMRegistrationToken(tokens.data)
                        when (set_token) {
                            is Result.Success -> {
                                Timber.i("firebase_token : Success")
                            }

                            is Result.Error -> {
                                Timber.i("firebase_token : Error ${set_token.exception}")
                            }
                        }
                    }
                    is Result.Error -> {
                        Timber.i("firebase_token : Error ${tokens.exception}")
                    }
                }
            }

        }
    }
}
