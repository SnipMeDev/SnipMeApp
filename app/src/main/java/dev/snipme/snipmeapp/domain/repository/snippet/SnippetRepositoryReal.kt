package dev.snipme.snipmeapp.domain.repository.snippet

import dev.snipme.snipmeapp.domain.error.ErrorHandler
import dev.snipme.snipmeapp.domain.snippets.Snippet
import dev.snipme.snipmeapp.domain.snippets.SnippetResponseMapper
import dev.snipme.snipmeapp.domain.snippets.SnippetVisibility
import dev.snipme.snipmeapp.infrastructure.local.SnippetDao
import dev.snipme.snipmeapp.infrastructure.local.SnippetEntry
import dev.snipme.snipmeapp.util.extension.mapError
import dev.snipme.snipmeapp.util.extension.mapItems
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.Date

const val SNIPPET_PAGE_SIZE = 10

class SnippetRepositoryReal(
    private val errorHandler: ErrorHandler,
    private val service: SnippetDao,
    private val mapper: SnippetResponseMapper
) : SnippetRepository {
    override val updateListener = BehaviorSubject.create<Snippet>()

    override fun snippets(userId: Int): Single<List<Snippet>> =
        service.snippets(userId)
            .mapError { errorHandler.handle(it) }
            .mapItems { mapper(it) }

    override fun snippet(uuid: String, userId: Int): Single<Snippet> =
        service.snippet(uuid.toInt(), userId).map { mapper(it) }
            .mapError { errorHandler.handle(it) }

    override fun create(
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility,
        userId: Int,
        favorite: Boolean
    ): Single<Snippet> {
        return service.create(
            SnippetEntry(
                title = title,
                code = code,
                createdAt = Date().toString(),
                modifiedAt = Date().toString(),
                visibility = visibility.name,
                ownerId = userId,

                language = language,
                favorite = favorite
            )
        )
            .mapError { errorHandler.handle(it) }
            .flatMap { newId ->
                service.snippet(newId.toInt(), userId)
                    .mapError { errorHandler.handle(it) }
                    .map { mapper(it) }
            }
    }

    override fun update(
        uuid: String,
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility,
        userId: Int,
        favorite: Boolean
    ): Single<Snippet> =
        service.update(uuid.toInt(), title, code, language, visibility.name, favorite)
            .mapError { errorHandler.handle(it) }
            .andThen(
                service.snippet(uuid.toInt(), userId)
                    .mapError { errorHandler.handle(it) }
                    .map { mapper(it) }
            )

    override fun delete(uuid: String): Completable =
        service.delete(uuid.toInt()).mapError { errorHandler.handle(it) }

    override fun count() =
        service.count()
            .mapError { errorHandler.handle(it) }
            .map { it }
}