package com.example.androidactions.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RoomDataRepositoryTest {

    private lateinit var fakeTaskDao: FakeTaskDao
    private lateinit var fakeRoutineDao: FakeRoutineDao
    private lateinit var fakeExecutionLogDao: FakeTaskExecutionLogDao
    private lateinit var fakeBuddyDao: FakeBuddyDao
    private lateinit var repository: RoomDataRepository

    @Before
    fun setup() {
        fakeTaskDao = FakeTaskDao()
        fakeRoutineDao = FakeRoutineDao()
        fakeExecutionLogDao = FakeTaskExecutionLogDao()
        fakeBuddyDao = FakeBuddyDao()

        repository = RoomDataRepository(
            taskDao = fakeTaskDao,
            routineDao = fakeRoutineDao,
            executionLogDao = fakeExecutionLogDao,
            buddyDao = fakeBuddyDao
        )
    }

    @Test
    fun createNewTask_insertsTaskAndRoutineIntoDaos() = runTest {
        val taskId = repository.createNewTask(
            title = "Descaling Coffee Machine",
            tags = listOf("Home", "Maintenance"),
            listName = "Home",
            frequencyDays = 90
        )

        assertTrue(taskId > 0)
        assertEquals(1, fakeTaskDao.tasks.value.size)
        assertEquals("Descaling Coffee Machine", fakeTaskDao.tasks.value.first().title)
        assertEquals(90, fakeTaskDao.tasks.value.first().defaultPeriodDays)

        assertEquals(1, fakeRoutineDao.routines.value.size)
        assertEquals(taskId, fakeRoutineDao.routines.value.first().taskId)
    }

    @Test
    fun addBuddy_insertsBuddyIntoDao() = runTest {
        val buddyId = repository.addBuddy("Alex", "+1234567890")
        assertTrue(buddyId > 0)
        assertEquals(1, fakeBuddyDao.buddies.value.size)
        assertEquals("Alex", fakeBuddyDao.buddies.value.first().name)
        assertEquals("+1234567890", fakeBuddyDao.buddies.value.first().phoneNumber)
    }

    @Test
    fun completeTask_recordsExecutionLogAndReschedulesRoutine() = runTest {
        val taskId = repository.createNewTask("Clean Countertop", listOf("Home"), "Home", 1)
        repository.completeTask(taskId)

        assertEquals(1, fakeExecutionLogDao.logs.size)
        assertEquals(ActionType.COMPLETED, fakeExecutionLogDao.logs.first().actionType)
    }
}

// In-Memory Test Doubles for Room DAOs
private class FakeTaskDao : TaskDao {
    val tasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    private var nextId = 1L

    override fun getAllTasks(): Flow<List<TaskEntity>> = tasks.asStateFlow()

    override suspend fun getTaskById(id: Long): TaskEntity? = tasks.value.find { it.id == id }

    override suspend fun insertTask(task: TaskEntity): Long {
        val assignedId = if (task.id == 0L) nextId++ else task.id
        val entity = task.copy(id = assignedId)
        tasks.value = tasks.value + entity
        return assignedId
    }

    override suspend fun updateTask(task: TaskEntity) {
        tasks.value = tasks.value.map { if (it.id == task.id) task else it }
    }

    override suspend fun deleteTask(task: TaskEntity) {
        tasks.value = tasks.value.filterNot { it.id == task.id }
    }
}

private class FakeRoutineDao : RoutineDao {
    val routines = MutableStateFlow<List<RoutineEntity>>(emptyList())
    private var nextId = 1L

    override fun getAllRoutines(): Flow<List<RoutineEntity>> = routines.asStateFlow()

    override suspend fun insertRoutine(routine: RoutineEntity): Long {
        val assignedId = if (routine.id == 0L) nextId++ else routine.id
        val entity = routine.copy(id = assignedId)
        routines.value = routines.value + entity
        return assignedId
    }

    override suspend fun updateRoutine(routine: RoutineEntity) {
        routines.value = routines.value.map { if (it.id == routine.id) routine else it }
    }
}

private class FakeTaskExecutionLogDao : TaskExecutionLogDao {
    val logs = mutableListOf<TaskExecutionLog>()
    private var nextId = 1L

    override fun getLogsForTask(taskId: Long): Flow<List<TaskExecutionLog>> =
        MutableStateFlow(logs.filter { it.taskId == taskId })

    override suspend fun insertLog(log: TaskExecutionLog): Long {
        val assignedId = if (log.id == 0L) nextId++ else log.id
        logs.add(log.copy(id = assignedId))
        return assignedId
    }
}

private class FakeBuddyDao : BuddyDao {
    val buddies = MutableStateFlow<List<BuddyEntity>>(emptyList())
    private var nextId = 1L

    override fun getAllBuddies(): Flow<List<BuddyEntity>> = buddies.asStateFlow()

    override suspend fun insertBuddy(buddy: BuddyEntity): Long {
        val assignedId = if (buddy.id == 0L) nextId++ else buddy.id
        val entity = buddy.copy(id = assignedId)
        buddies.value = buddies.value + entity
        return assignedId
    }
}
