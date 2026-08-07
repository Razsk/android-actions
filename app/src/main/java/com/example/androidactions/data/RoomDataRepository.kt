package com.example.androidactions.data

import com.example.androidactions.domain.OptimizationCardAnalyzer
import com.example.androidactions.domain.SuggestionCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit

class RoomDataRepository(
    private val taskDao: TaskDao,
    private val routineDao: RoutineDao,
    private val executionLogDao: TaskExecutionLogDao,
    private val buddyDao: BuddyDao,
    private val cardAnalyzer: OptimizationCardAnalyzer = OptimizationCardAnalyzer()
) {
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()
    val allRoutines: Flow<List<RoutineEntity>> = routineDao.getAllRoutines()
    val allBuddies: Flow<List<BuddyEntity>> = buddyDao.getAllBuddies()

    suspend fun createNewTask(
        title: String,
        tags: List<String>,
        listName: String,
        frequencyDays: Int,
        isReusable: Boolean = true
    ): Long {
        val newTask = TaskEntity(
            title = title,
            isReusable = isReusable,
            defaultPeriodDays = frequencyDays,
            listName = listName,
            tagsCsv = tags.joinToString(",")
        )
        val taskId = taskDao.insertTask(newTask)

        if (frequencyDays > 0) {
            val dueTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(frequencyDays.toLong())
            val newRoutine = RoutineEntity(
                taskId = taskId,
                frequencyDays = frequencyDays,
                dueTimestamp = dueTime,
                isPostponed = false
            )
            routineDao.insertRoutine(newRoutine)
        }
        return taskId
    }

    suspend fun completeTask(taskId: Long) {
        val log = TaskExecutionLog(
            taskId = taskId,
            actionType = ActionType.COMPLETED,
            timestamp = System.currentTimeMillis()
        )
        executionLogDao.insertLog(log)

        val task = taskDao.getTaskById(taskId)
        if (task != null && task.defaultPeriodDays > 0) {
            val nextDueTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(task.defaultPeriodDays.toLong())
            val routine = RoutineEntity(
                taskId = taskId,
                frequencyDays = task.defaultPeriodDays,
                dueTimestamp = nextDueTime,
                isPostponed = false
            )
            routineDao.insertRoutine(routine)
        }
    }

    suspend fun postponeRoutine(taskId: Long, deferDays: Int) {
        val log = TaskExecutionLog(
            taskId = taskId,
            actionType = ActionType.POSTPONED,
            timestamp = System.currentTimeMillis()
        )
        executionLogDao.insertLog(log)

        val newDueTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(deferDays.toLong())
        val routine = RoutineEntity(
            taskId = taskId,
            frequencyDays = deferDays,
            dueTimestamp = newDueTime,
            isPostponed = true
        )
        routineDao.insertRoutine(routine)
    }

    suspend fun addBuddy(name: String, phoneNumber: String): Long {
        val buddy = BuddyEntity(
            name = name,
            phoneNumber = phoneNumber
        )
        return buddyDao.insertBuddy(buddy)
    }
}
