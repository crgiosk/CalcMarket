package com.calcmarket.data.local.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.calcmarket.data.local.entities.BuyEntity

object Migrations {
    val MIGRATION_1_2 = Migration(1, 2) { database ->
        database.execSQL(
            "ALTER TABLE '${BuyEntity.NAME_TABLE}' " +
                    "ADD COLUMN '${BuyEntity.COLUMN_IN_PROGRESS}' " +
                    "INTEGER NOT NULL DEFAULT 0"
        )
    }
}