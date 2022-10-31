package com.example.alfa_bank_android_app_parent.ui.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alfa_bank_android_app_parent.MainActivity
import com.example.alfa_bank_android_app_parent.R
import com.example.alfa_bank_android_app_parent.domain.entiies.AuthenticationItemsForAdapter
import com.example.alfa_bank_android_app_parent.domain.entiies.PinClass
import com.example.alfa_bank_android_app_parent.ui.adapters.AuthenticationCardAdapter

import com.example.alfa_bank_android_app_parent.ui.uihelper.InitializeRecyclerViewClass
import java.util.concurrent.Executor

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var buttonsRecyclerView: RecyclerView

    // private lateinit var lengthPinRecyclerView: RecyclerView
    private lateinit var viewModel: AuthenticationViewModel

    private lateinit var authenticationCardAdapter: AuthenticationCardAdapter
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var text: TextView
    private lateinit var mode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        this.supportActionBar?.hide()
        viewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]
        viewModel.pinClass = PinClass(
            listOf(
                findViewById(R.id.button_uncheked1),
                findViewById(R.id.button_uncheked2),
                findViewById(R.id.button_uncheked3),
                findViewById(R.id.button_uncheked4)
            ),

            )
        idInitialize()
        initializeMode()

        initializeBiometricAuthentication()
        initializeRecyclerView()
        viewModel.pinClass?.adapter = {
            changeAuthenticationDataAdapter()
        }

        if(mode == AUTHENTICATION_MODE){
            biometricPrompt.authenticate(promptInfo)
        }
    }


    private fun initializeMode() {
        mode = intent?.getStringExtra(MODE) ?: ""
        when (mode) {
            INPUT_FIRST_TIME_MODE -> {
                startFirstTimeMode()
            }
            INPUT_SECOND_TIME_MODE -> {
                startSecondTimeMode()
            }
            AUTHENTICATION_MODE -> {
                startAuthenticationMode()
            }
        }
    }

    private fun startFirstTimeMode() {
        text.text = intent.getStringExtra(TEXT)
        viewModel.funAfterPinWasEntered = {
            val intent = it?.let { it1 -> newIntentInputSecondTimePinCode(this, it1) }
            this.startActivity(intent)
            finish()
        }
    }

    private fun startSecondTimeMode() {
        text.text = intent.getStringExtra(TEXT)
        viewModel.funAfterPinWasEntered = {
            if (intent.getStringExtra(PIN) == it) {
                it?.let { it1 -> viewModel.preferences.userPinCode = it1.toInt() }
                val intent = Intent(this, MainActivity::class.java)
                viewModel.preferences.isUserLogged = true
                this.startActivity(intent)
                finish()
            } else {
                for (circle in viewModel.pinClass?.circles ?: listOf()) {
                    animateIncorrectPasswordView(circle, 6, 140f, 20f)
                }
            }
        }
    }

    private fun animateIncorrectPasswordView(
        view: View,
        countAction: Int,
        value: Float,
        stepValue: Float,
        nowDeep: Int = 0
    ) {
        if (nowDeep == countAction) {
            ViewCompat.animate(view)
                .translationX(0f)
                .setDuration(100L)
                .withEndAction {
                    viewModel.pinClass?.removePin()
                }
                .start()

            return
        }
        ViewCompat.animate(view)
            .translationX(value)
            .setDuration(100L)
            .withEndAction {
                animateIncorrectPasswordView(
                    view,
                    countAction,
                    (value - stepValue) * -1,
                    stepValue * -1,
                    nowDeep + 1
                )
            }
            .start()
    }

    private fun animateCorrectPasswordView(
        views: List<ImageView>,
        scaleX: Float,
        scaleY: Float,
        alpha: Float,
        duration: Long,
        deep: Int,
        count: Int
    ) {

        if (views.size < 4  )
            return
        if(deep - 1 == count){
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            finish()
        }
        ViewCompat.animate(views[0])
            .scaleY(scaleY)
            .scaleX(scaleX)
            .alpha(alpha)
            .setDuration(duration)
            .withEndAction {
                ViewCompat.animate(views[1])
                    .scaleY(scaleY)
                    .scaleX(scaleX)
                    .alpha(alpha)
                    .setDuration(duration)
                    .withEndAction {
                        ViewCompat.animate(views[2])
                            .scaleY(scaleY)
                            .scaleX(scaleX)
                            .alpha(alpha)
                            .setDuration(duration)
                            .withEndAction {
                                ViewCompat.animate(views[3])
                                    .scaleY(scaleY)
                                    .scaleX(scaleX)
                                    .alpha(alpha)
                                    .setDuration(duration)
                                    .withEndAction {
                                        if (count % 2 == 0) {
                                            animateCorrectPasswordView(
                                                viewModel.pinClass?.circles ?: listOf(),
                                                1F, 1F, 1F, duration, deep, count + 1
                                            )
                                        }else{
                                            animateCorrectPasswordView(
                                                viewModel.pinClass?.circles ?: listOf(),
                                                0.7F, 0.7F, 0.4F, duration, deep, count + 1
                                            )
                                        }
                                    }
                            }
                    }
            }
    }

    private fun startAuthenticationMode() {
        text.visibility = View.INVISIBLE

        viewModel.funAfterPinWasEntered = {
            if (viewModel.preferences.userPinCode.toString() == it) {
                animateCorrectPasswordView(
                    viewModel.pinClass?.circles ?: listOf(),
                    0.7F, 0.7F, 0.4F, 70L, 4, 0
                )
            } else {
                for (circle in viewModel.pinClass?.circles ?: listOf()) {
                    animateIncorrectPasswordView(circle, 6, 140f, 20f)
                }
            }
        }
    }

    private fun initializeBiometricAuthentication() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    viewModel.pinClass?.addAllPinCode()
                    animateCorrectPasswordView(
                        viewModel.pinClass?.circles ?: listOf(),
                        0.7F, 0.7F, 0.4F, 70L, 4, 0
                    )
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setTitle("Вход с отпечатком")
            .setSubtitle("Прикоснитесь к сенсору для входа по отпечатку")
            .setNegativeButtonText("Отмена")
            .build()



    }

    private fun initializeRecyclerViewAdapter() {
        //pinCodeAdapter = PinCodeAdapter(0)
        val authenticationItemsForAdapter = AuthenticationItemsForAdapter()
        viewModel.loadItemsForAdapter(
            authenticationItemsForAdapter,
            biometricPrompt,
            promptInfo,
            mode,
        )
        authenticationCardAdapter =
            AuthenticationCardAdapter(authenticationItemsForAdapter, mode)

    }

    private fun idInitialize() {
        buttonsRecyclerView = findViewById(R.id.buttonsRecyclerView)
        //lengthPinRecyclerView = findViewById(R.id.lengthPinRecyclerView)
        text = findViewById(R.id.text)
    }

    private fun initializeRecyclerView() {
        initializeRecyclerViewAdapter()
        initializeButtonsRecyclerView()

    }


    private fun changePinCodeDataAdapter() {
        val authenticationItemsForAdapter = AuthenticationItemsForAdapter()
        viewModel.loadItemsForAdapter(
            authenticationItemsForAdapter,
            biometricPrompt,
            promptInfo,
            mode,
        )
        authenticationCardAdapter.authenticationItemsForAdapter = authenticationItemsForAdapter

        //pinCodeAdapter.pinLength = length
        //pinCodeAdapter.notifyDataSetChanged()
    }

    fun changeAuthenticationDataAdapter() {
        val authenticationItemsForAdapter = AuthenticationItemsForAdapter()
        viewModel.loadItemsForAdapter(
            authenticationItemsForAdapter,
            biometricPrompt,
            promptInfo,
            mode
        )
        authenticationCardAdapter.authenticationItemsForAdapter = authenticationItemsForAdapter
        authenticationCardAdapter.notifyItemChanged(11)
    }

    private fun initializeButtonsRecyclerView() {
        val gridLayoutManager = GridLayoutManager(applicationContext, 3)
        InitializeRecyclerViewClass<AuthenticationCardAdapter.ItemHolder>()
            .setParametersToRecyclerView(
                buttonsRecyclerView,
                gridLayoutManager,
                authenticationCardAdapter
            )
    }

    fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        finish()
    }


    companion object {
        const val PIN_LENGTH = 4
        private const val TEXT = "TEXT"
        private const val PIN = "PIN"
        private const val MODE = "MODE"
        const val INPUT_FIRST_TIME_MODE = "INPUT_FIRST_TIME_MODE"
        const val INPUT_SECOND_TIME_MODE = "INPUT_SECOND_TIME_MODE"
        const val AUTHENTICATION_MODE = "AUTHENTICATION_MODE"

        fun newIntentInputFirstTimePinCode(context: Context): Intent {
            return Intent(context, AuthenticationActivity::class.java)
                .putExtra(TEXT, "Введите pin-code")
                .putExtra(MODE, INPUT_FIRST_TIME_MODE)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        }

        fun newIntentInputSecondTimePinCode(context: Context, pin: String): Intent {
            return Intent(context, AuthenticationActivity::class.java)
                .putExtra(TEXT, "Повторите pin-code")
                .putExtra(PIN, pin)
                .putExtra(MODE, INPUT_SECOND_TIME_MODE)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        fun newIntentAuthentication(context: Context): Intent {
            return Intent(context, AuthenticationActivity::class.java)
                .putExtra(MODE, AUTHENTICATION_MODE)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }
}