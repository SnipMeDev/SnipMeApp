package dev.snipme.snipmeapp.infrastructure.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface SnippetDao {
    @Query(
        """
        SELECT s.*
        FROM snippets as s 
        WHERE s.id = :uuid
        """
    )
    fun snippet(uuid: Int): Single<SnippetEntry>

    @Query("""SELECT s.* FROM snippets as s""")
    fun snippets(): Single<List<SnippetEntry>>

    @Query("SELECT COUNT(*) FROM snippets")
    fun count(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(snippet: SnippetEntry): Single<Long>

    @Query(
        """
        UPDATE snippets
        SET title = :title,
            code = :code,
            modifiedAt = current_timestamp,
            visibility = :visibility,
            language = :language,
            favorite = :favorite
        WHERE id = :uuid
    """
    )
    fun update(
        uuid: Int,
        title: String,
        code: String,
        visibility: String,
        language: String,
        favorite: Boolean
    ): Completable

    @Query("DELETE FROM snippets WHERE id = :uuid")
    fun delete(uuid: Int): Completable
}