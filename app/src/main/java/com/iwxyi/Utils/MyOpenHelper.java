package com.iwxyi.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MyOpenHelper extends SQLiteOpenHelper {

    public MyOpenHelper(Context context) {
        // 创建数据库，上下文、名字、cursor对象（结果集/游标）、数据库版本（从1开始）
        super(context, "poorrow.db", null, 1);
    }

    // 数据库第一次创建的时候用（仅一次），特别适合做表结构的初始化
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table poorrow( _id integer primary key autoincrement, field varchar(20), content text )");
        db.execSQL("INSERT INTO poorrow (field, content) values ('bills', '')");
        db.execSQL("INSERT INTO poorrow (field, content) values ('cards', '')");
        db.execSQL("INSERT INTO poorrow (field, content) values ('kinds_spending', '')");
        db.execSQL("INSERT INTO poorrow (field, content) values ('kinds_income', '')");
        db.execSQL("INSERT INTO poorrow (field, content) values ('kinds_borrowing', '')");
    }

    // 数据库升级的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //例如：升级 info表 添加 phone字段

    }
}