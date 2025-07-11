package com.example.trpg.tool.entity;

public enum AccountLevel {
    NORMAL(0),
    SPECIAL(1),
    VIP(2);

    private final int code;
    AccountLevel(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static AccountLevel fromCode(int code) {
        for (AccountLevel level : AccountLevel.values()) {
            if (level.code == code) return level;
        }
        return NORMAL;
    }
}
