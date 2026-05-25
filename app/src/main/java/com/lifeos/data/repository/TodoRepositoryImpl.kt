package com.lifeos.data.repository

import android.content.Context
import com.lifeos.core.util.NetworkUtils
import com.lifeos.data.local.dao.TodoDao
import com.lifeos.data.mapper.TodoMapper
import com.lifeos.data.remote.firebase.FirebaseAuthManager
import com.lifeos.data.remote.firebase.FirestoreService
import com.lifeos.domain.model.SyncStatus
import com.lifeos.domain.model.Todo
import com.lifeos.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val context: Context,
    private val todoDao: TodoDao,
    private val authManager: FirebaseAuthManager,
    private val firestoreService: FirestoreService
) : TodoRepository {

    override fun getAllTodos(): Flow<List<Todo>> {
        return todoDao.getAllTodos().map { list -> list.map(TodoMapper::toDomain) }
    }

    override suspend fun upsertTodo(todo: Todo) {
        todoDao.upsert(TodoMapper.toEntity(todo))

        val uid = authManager.currentUserId() ?: return
        if (!NetworkUtils.isOnline(context)) return

        firestoreService
            .todosCollection(uid)
            .document(todo.id)
            .set(todo.copy(syncStatus = SyncStatus.SYNCED))
            .await()
    }

    override suspend fun deleteTodo(id: String) {
        todoDao.deleteById(id)

        val uid = authManager.currentUserId() ?: return
        if (!NetworkUtils.isOnline(context)) return

        firestoreService.todosCollection(uid).document(id).delete().await()
    }

    override suspend fun syncTodos() {
        val uid = authManager.currentUserId() ?: return
        if (!NetworkUtils.isOnline(context)) return

        val snapshot = firestoreService.todosCollection(uid).get().await()
        val remoteTodos = snapshot.toObjects(Todo::class.java)

        // Naive last-write-wins placeholder: store remote as-is (LLD conflict resolution can be expanded later).
        remoteTodos.forEach { remote ->
            val local = todoDao.getById(remote.id)
            if (local == null || remote.updatedAt > local.updatedAt) {
                todoDao.upsert(TodoMapper.toEntity(remote.copy(syncStatus = SyncStatus.SYNCED)))
            }
        }
    }
}
