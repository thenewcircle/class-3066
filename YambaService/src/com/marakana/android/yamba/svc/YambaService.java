package com.marakana.android.yamba.svc;

import java.util.List;
import java.util.UUID;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


public class YambaService extends IntentService {
    private static final String TAG = "SVC";

    public static enum Op {
        NOOP(9000),
        POLL(-9001),
        POST(-9002),
        START_POLL(YambaContract.SVC_OP_POLLING_ON),
        STOP_POLL(YambaContract.SVC_OP_POLLING_OFF);

        private static final SparseArray<Op> codes;
        static {
            SparseArray<Op> a = new SparseArray<Op>();
            for (Op op: Op.values()) { a.put(op.getCode(), op); }
            codes = a;
        }

        static Op fromCode(int code) {
            Op op = codes.get(code);
            return (null != op) ? op : NOOP;
        }

        private final int code;
        private Op(int code) { this.code = code; }
        int getCode() { return code; }
    }

    public static String getTransactionId() { return UUID.randomUUID().toString(); }


    public YambaService() { super(TAG); }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle args = intent.getExtras();
        int op = args.getInt(YambaContract.SVC_PARAM_OP);
        if (BuildConfig.DEBUG) { Log.d(TAG, "handle op: " + op); }

        switch (Op.fromCode(op)) {

        	//!! Implement me!
        
            default:
                throw new IllegalArgumentException("Unrecognized op: " + op);
        }
    }
}