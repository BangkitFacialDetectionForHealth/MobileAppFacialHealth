package com.example.lastprojectbangkit.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.lastprojectbangkit.R


class DefaultEditText : AppCompatEditText {
    constructor(context : Context): super(context){
        init()
    }
    constructor(context: Context,attrs:AttributeSet) : super(context,attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet,defStyleAttr:Int): super(context,attrs,defStyleAttr){
        init()
    }
    private fun init(){
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val name = s.toString()
                when {
                    name.isBlank() -> error = context.getString(R.string.message_login_page)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // do nothing
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}