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
    SELECT s.*, u.login as ownerName,
    CASE WHEN s.ownerId = :userId THEN 1 ELSE 0 END as isOwner,
    CASE WHEN r.reaction = 0 THEN 'DISLIKE' ELSE CASE WHEN r.reaction = 2 THEN 'LIKE' ELSE 'NONE' END END as userReaction,
    (Select Count(*) FROM reactions as r where r.snippetId = :uuid and reaction = 2) as numberOfLikes,
    (Select Count(*) FROM reactions as r where r.snippetId = :uuid and reaction = 0) as numberOfDislikes
    FROM snippets as s 
    INNER JOIN users as u ON s.ownerId = u.id 
    LEFT JOIN reactions as r ON r.userId = :userId and r.snippetId = :uuid
    WHERE s.id = :uuid
    """
    )
    fun snippet(uuid: Int, userId: Int): Single<SnippetExtended>

    @Query(
        """ 
            SELECT s.*, u.login as ownerName,
            CASE WHEN s.ownerId = :userId THEN 1 ELSE 0 END as isOwner,
            CASE WHEN r.reaction = 0 THEN "DISLIKE" ELSE CASE WHEN r.reaction = 2 THEN "LIKE" ELSE "NONE" END END as userReaction,
            (Select Count(*) FROM reactions as r where r.snippetId = s.id and reaction = 2) as numberOfLikes,
            (Select Count(*) FROM reactions as r where r.snippetId = s.id and reaction = 0) as numberOfDislikes
            FROM snippets as s
            INNER JOIN users as u ON s.ownerId = u.id
            LEFT JOIN reactions as r ON r.userId = :userId and r.snippetId = s.id
        """
    )
    fun snippets(userId: Int): Single<List<SnippetExtended>>

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
            language = :language
        WHERE id = :uuid
    """
    )
    fun update(
        uuid: Int,
        title: String,
        code: String,
        visibility: String,
        language: String,
    ): Completable

    @Query("DELETE FROM snippets WHERE id = :uuid")
    fun delete(uuid: Int): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun reaction(reaction: ReactionEntry): Completable

}