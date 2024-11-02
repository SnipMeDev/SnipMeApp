package dev.snipme.snipmeapp.infrastructure.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface SnippetDao {
    @Query("""
    SELECT s.*, u.login as ownerName, 
    CASE WHEN s.ownerId = :userId THEN 1 ELSE 0 END as isOwner 
    FROM snippets as s 
    INNER JOIN users as u ON s.ownerId = u.id 
    WHERE s.id = :uuid
    """)
    fun snippet(uuid: Int, userId: Int) : Single<SnippetWithOwner>

    @Query("SELECT s.*, u.login as ownerName, CASE WHEN s.ownerId = :userId THEN 1 ELSE 0 END as isOwner FROM snippets as s INNER JOIN users as u ON s.ownerId = u.id")
    fun snippets(userId: Int) : Single<List<SnippetWithOwner>>

    @Query("SELECT COUNT(*) FROM snippets")
    fun count() : Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(snippet: SnippetEntry): Single<Long>

    @Query("""
        UPDATE snippets
        SET title = :title,
            code = :code,
            modifiedAt = current_timestamp,
            visibility = :visibility,
            language = :language
        WHERE id = :uuid
    """)
    fun update(
        uuid: Int,
        title: String,
        code: String,
        visibility: String,
        language: String,
    ) : Completable

    @Query("DELETE FROM snippets WHERE id = :uuid")
    fun delete(uuid: Int): Completable

}