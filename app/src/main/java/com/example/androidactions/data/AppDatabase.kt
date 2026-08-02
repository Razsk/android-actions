package com.example.androidactions.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        TaskEntity::class,
        RoutineEntity::class,
        TaskExecutionLog::class,
        ChallengeEntity::class,
        SplitTimeEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(ActionTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun routineDao(): RoutineDao
    abstract fun taskExecutionLogDao(): TaskExecutionLogDao
    abstract fun challengeDao(): ChallengeDao
}
