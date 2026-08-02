package com.example.androidactions.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routines")
    fun getAllRoutines(): Flow<List<RoutineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: RoutineEntity): Long

    @Update
    suspend fun updateRoutine(routine: RoutineEntity)
}

@Dao
interface TaskExecutionLogDao {
    @Query("SELECT * FROM task_execution_logs WHERE taskId = :taskId ORDER BY timestamp DESC")
    fun getLogsForTask(taskId: Long): Flow<List<TaskExecutionLog>>

    @Insert
    suspend fun insertLog(log: TaskExecutionLog): Long
}

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges")
    fun getAllChallenges(): Flow<List<ChallengeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: ChallengeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplitTime(splitTime: SplitTimeEntity): Long

    @Query("SELECT * FROM split_times WHERE challengeId = :challengeId AND taskId = :taskId ORDER BY splitDurationSeconds ASC LIMIT 1")
    suspend fun getPersonalBestSplit(challengeId: Long, taskId: Long): SplitTimeEntity?
}
