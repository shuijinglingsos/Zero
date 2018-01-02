package com.elf.zero.net;

/**
 * 键值对
 * Created by Lidong on 2017/12/27.
 */
public class KeyValuePair<Value> {

    private String mKey;
    private Value mValue;

    public KeyValuePair(String key, Value value) {
        mKey = key;
        mValue = value;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public Value getValue() {
        return mValue;
    }

    public void setValue(Value value) {
        this.mValue = value;
    }
}
