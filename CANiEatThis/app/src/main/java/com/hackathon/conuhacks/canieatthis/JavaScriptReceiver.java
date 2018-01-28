package com.hackathon.conuhacks.canieatthis;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

/**
 * Created by ericvuong on 2018-01-27.
 */

    public class JavaScriptReceiver
    {
        Context mContext;

        /** Instantiate the receiver and set the context */
        JavaScriptReceiver(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void showFood(){
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
        }

    }

