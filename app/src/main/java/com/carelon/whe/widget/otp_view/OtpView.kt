package com.carelon.whe.widget.otp_view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import com.carelon.whe.R

class OtpView : LinearLayout {

    private var length: Int = 1

    private var edtOtp1: EditText? = null
    private var edtOtp2: EditText? = null
    private var edtOtp3: EditText? = null
    private var edtOtp4: EditText? = null
    private var edtOtp5: EditText? = null
    private var edtOtp6: EditText? = null

    var otpListener: OTPListener? = null
    var isErrorShown = false

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.otp_view, this, true)
        val styles = context.obtainStyledAttributes(attrs, R.styleable.OtpTextView)
        styleEditTexts(styles, attrs)
        styles.recycle()
    }

    private fun styleEditTexts(styles: TypedArray, attrs: AttributeSet?) {
        length = styles.getInt(R.styleable.OtpTextView_length, DEFAULT_LENGTH)
        generateViews(styles, attrs)
    }

    private fun generateViews(styles: TypedArray, attrs: AttributeSet?) {

        edtOtp1 = rootView.findViewById(R.id.edt_otp1)
        edtOtp2 = rootView.findViewById(R.id.edt_otp2)
        edtOtp3 = rootView.findViewById(R.id.edt_otp3)
        edtOtp4 = rootView.findViewById(R.id.edt_otp4)
        edtOtp5 = rootView.findViewById(R.id.edt_otp5)
        edtOtp6 = rootView.findViewById(R.id.edt_otp6)
        setTextWatcher()
        setFocusListener(edtOtp1)
        setFocusListener(edtOtp2)
        setFocusListener(edtOtp3)
        setFocusListener(edtOtp4)
        setFocusListener(edtOtp5)
        setFocusListener(edtOtp6)
        inputDoneListener(edtOtp1)
        inputDoneListener(edtOtp2)
        inputDoneListener(edtOtp3)
        inputDoneListener(edtOtp4)
        inputDoneListener(edtOtp5)
        inputDoneListener(edtOtp6)

    }

    private fun setTextWatcher() {

        edtOtp1?.addTextChangedListener {
            isErrorShown = false
            val length = edtOtp1?.text.toString().length
            if (length == 1) {
                edtOtp2?.requestFocus()
            }
            if (isValidOtp()){
                otpListener?.onOTPComplete(getOtp())
            }else {
                otpListener?.onInteractionListener()
            }
        }
        edtOtp2?.addTextChangedListener {
            isErrorShown = false
            val length = edtOtp2?.text.toString().length
            if (length == 1) {
                edtOtp3?.requestFocus()
            }
            if (isValidOtp()){
                otpListener?.onOTPComplete(getOtp())
            }else {
                otpListener?.onInteractionListener()
            }
        }
        edtOtp3?.addTextChangedListener {
            isErrorShown = false
            val length = edtOtp3?.text.toString().length
            if (length == 1) {
                edtOtp4?.requestFocus()
            }
            if (isValidOtp()){
                otpListener?.onOTPComplete(getOtp())
            }else {
                otpListener?.onInteractionListener()
            }
        }
        edtOtp4?.addTextChangedListener {
            isErrorShown = false
            val length = edtOtp4?.text.toString().length
            if (length == 1) {
                edtOtp5?.requestFocus()
            }
            if (isValidOtp()){
                otpListener?.onOTPComplete(getOtp())
            }else {
                otpListener?.onInteractionListener()
            }
        }
        edtOtp5?.addTextChangedListener {
            isErrorShown = false
            val length = edtOtp5?.text.toString().length
            if (length == 1) {
                edtOtp6?.requestFocus()
            }
            if (isValidOtp()){
                otpListener?.onOTPComplete(getOtp())
            }else {
                otpListener?.onInteractionListener()
            }
        }

        edtOtp6?.addTextChangedListener {
            isErrorShown = false
            val length = edtOtp6?.text.toString().length
            if (length == 1) {
                if (isValidOtp()){
                    otpListener?.onOTPComplete(getOtp())
                }else {
                    otpListener?.onInteractionListener()
                }
            }
        }

        edtOtp6?.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edtOtp6?.text?.clear()
                    showInActive()
                    edtOtp5?.requestFocus()
                }
            }
            false
        }
        edtOtp5?.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edtOtp5?.text?.clear()
                    showInActive()
                    edtOtp4?.requestFocus()
                }
            }
            false
        }
        edtOtp4?.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edtOtp4?.text?.clear()
                    showInActive()
                    edtOtp3?.requestFocus()
                }
            }
            false
        }
        edtOtp3?.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edtOtp3?.text?.clear()
                    showInActive()
                    edtOtp2?.requestFocus()
                }
            }
            false
        }
        edtOtp2?.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edtOtp2?.text?.clear()
                    showInActive()
                    edtOtp1?.requestFocus()
                }
            }
            false
        }
        edtOtp1?.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    showInActive()
                }
            }
            false
        }

    }

    private fun inputDoneListener(input: EditText?) {
        input?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (isValidOtp()) otpListener?.submitOTP(getOtp())
                true
            } else
                false
        }
    }

    private fun setFocusListener(input: EditText?) {
        input?.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!isErrorShown){
                if (hasFocus) {
                    input?.setTextColor(context.getColor(R.color.purple))
                    v.setBackgroundResource(R.drawable.bg_otp_purple)
                } else {
                    input?.setTextColor(context.getColor(R.color.dark_gray))
                    v.setBackgroundResource(R.drawable.bg_otp_inactive)
                }
            }
        }
    }

    fun setFocus() {
        edtOtp1?.requestFocus()
    }

    fun showError() {
        isErrorShown = true
        setViewState(ERROR, edtOtp1)
        setViewState(ERROR, edtOtp2)
        setViewState(ERROR, edtOtp3)
        setViewState(ERROR, edtOtp4)
        setViewState(ERROR, edtOtp5)
        setViewState(ERROR, edtOtp6)
    }

    private fun showInActive() {
        isErrorShown = false
        if (isValidOtp()){
            otpListener?.onOTPComplete(getOtp())
        }else {
            otpListener?.onInteractionListener()
        }
        setViewState(INACTIVE, edtOtp1)
        setViewState(INACTIVE, edtOtp2)
        setViewState(INACTIVE, edtOtp3)
        setViewState(INACTIVE, edtOtp4)
        setViewState(INACTIVE, edtOtp5)
        setViewState(INACTIVE, edtOtp6)
    }

    fun isValidOtp(): Boolean {
        return edtOtp1?.text.toString().trim().isNotEmpty() &&
                edtOtp2?.text.toString().trim().isNotEmpty() &&
                edtOtp3?.text.toString().trim().isNotEmpty() &&
                edtOtp4?.text.toString().trim().isNotEmpty() &&
                edtOtp5?.text.toString().trim().isNotEmpty() &&
                edtOtp6?.text.toString().trim().isNotEmpty()
    }

    fun getOtp(): String {
        val otp1 = edtOtp1?.text.toString().trim()
        val otp2 = edtOtp1?.text.toString().trim()
        val otp3 = edtOtp1?.text.toString().trim()
        val otp4 = edtOtp1?.text.toString().trim()
        val otp5 = edtOtp1?.text.toString().trim()
        val otp6 = edtOtp1?.text.toString().trim()
        return StringBuilder().append(otp1)
            .append(otp2)
            .append(otp3)
            .append(otp4)
            .append(otp5)
            .append(otp6).toString()
    }

    private fun setViewState(state: Int, input: EditText?) {
        when (state) {
            ACTIVE -> {
                input?.setTextColor(context.resources.getColor(R.color.purple))
                input?.setBackgroundResource(R.drawable.bg_otp_purple)
            }
            INACTIVE -> {
                input?.setTextColor(context.resources.getColor(R.color.dark_gray))
                input?.setBackgroundResource(R.drawable.bg_otp_inactive)
            }
            ERROR -> {
                input?.setTextColor(context.resources.getColor(R.color.red))
                input?.setBackgroundResource(R.drawable.bg_otp_red)
            }
            SUCCESS -> {
                input?.setTextColor(context.resources.getColor(R.color.dark_gray))
                input?.setBackgroundResource(R.drawable.bg_otp_purple)
            }
            else -> {
            }
        }
    }

    companion object {
        private const val DEFAULT_LENGTH = 4
        private const val DEFAULT_HEIGHT = 48
        private const val DEFAULT_WIDTH = 48
        private const val DEFAULT_SPACE = -1
        private const val DEFAULT_SPACE_LEFT = 4
        private const val DEFAULT_SPACE_RIGHT = 4
        private const val DEFAULT_SPACE_TOP = 4
        private const val DEFAULT_SPACE_BOTTOM = 4

        const val ACTIVE = 1
        const val INACTIVE = 0
        const val ERROR = -1
        const val SUCCESS = 2

        private const val PATTERN = "[1234567890]*"
    }
}