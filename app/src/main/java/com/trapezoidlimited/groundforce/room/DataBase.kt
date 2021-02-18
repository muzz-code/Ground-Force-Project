package com.trapezoidlimited.groundforce.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RoomAgent::class, RoomAdditionalDetail::class,
        RoomMission::class, RoomOngoingMission::class, RoomSurvey::class,
        RoomHistoryMission::class, RoomHistorySurvey::class, RoomNotification::class],
    version = 20,
    exportSchema = false
)
abstract class DataBase : RoomDatabase() {

    abstract fun dao(): RoomDao

    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null

        fun getDatabase(context: Context): DataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "ground_force_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance

                return instance
            }
        }
    }
}