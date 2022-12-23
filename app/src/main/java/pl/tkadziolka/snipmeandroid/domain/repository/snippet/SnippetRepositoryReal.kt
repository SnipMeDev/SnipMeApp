package pl.tkadziolka.snipmeandroid.domain.repository.snippet

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import pl.tkadziolka.snipmeandroid.domain.error.ErrorHandler
import pl.tkadziolka.snipmeandroid.domain.reaction.UserReaction
import pl.tkadziolka.snipmeandroid.domain.snippets.*
import pl.tkadziolka.snipmeandroid.infrastructure.model.request.CreateSnippetRequest
import pl.tkadziolka.snipmeandroid.infrastructure.model.request.RateSnippetRequest
import pl.tkadziolka.snipmeandroid.infrastructure.remote.SnippetService
import pl.tkadziolka.snipmeandroid.util.extension.mapError
import pl.tkadziolka.snipmeandroid.util.extension.mapItems

const val PAGE_START = 0
const val SNIPPET_PAGE_SIZE = 10
const val ONE_SNIPPET = 1

class SnippetRepositoryReal(
    private val errorHandler: ErrorHandler,
    private val service: SnippetService,
    private val mapper: SnippetResponseMapper
) : SnippetRepository {
    private var count: Int? = null

    override val updateListener = BehaviorSubject.create<Snippet>()

    override fun snippets(scope: SnippetScope, page: Int): Single<List<Snippet>> =
        service.snippets(scope.value(), PAGE_START, SNIPPET_PAGE_SIZE * page)
            .mapError { errorHandler.handle(it) }
            .map { count = it.count; it.results }
            .mapItems { mapper(it) }

    override fun snippet(id: String): Single<Snippet> =
        service.snippet(id).map { mapper(it) }
            .mapError { errorHandler.handle(it) }

    override fun create(
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility
    ): Single<Snippet> =
        service.create(CreateSnippetRequest(title, code, language, visibility.name))
            .mapError { errorHandler.handle(it) }
            .map { mapper(it) }

    override fun update(
        uuid: String,
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility
    ): Single<Snippet> =
        service.update(uuid, CreateSnippetRequest(title, code, language, visibility.name))
            .mapError { errorHandler.handle(it) }
            .map { mapper(it) }

    override fun delete(uuid: String): Completable = service.delete(uuid)

    override fun count(scope: SnippetScope) =
        if (count != null) {
            Single.just(count!!)
        } else {
            service.snippets(scope.value(), PAGE_START, ONE_SNIPPET).map { it.count }
                .mapError { errorHandler.handle(it) }
        }

    override fun reaction(uuid: String, reaction: UserReaction) =
        service.rate(RateSnippetRequest(uuid, reaction.name))
            .mapError { errorHandler.handle(it) }
}