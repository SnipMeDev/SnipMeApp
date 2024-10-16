package dev.snipme.snipmeapp.domain.repository.snippet

import android.text.SpannableString
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import dev.snipme.snipmeapp.domain.error.ErrorHandler
import dev.snipme.snipmeapp.domain.reaction.UserReaction
import dev.snipme.snipmeapp.domain.snippets.*
import dev.snipme.snipmeapp.util.SyntaxHighlighter.getHighlighted
import dev.snipme.snipmeapp.util.extension.lines
import dev.snipme.snipmeapp.util.extension.newLineChar
import java.util.*

private const val PREVIEW_COUNT = 5

class SnippetRepositoryTest(private val errorHandler: ErrorHandler) : SnippetRepository {

    private val uuid
        get() = UUID.randomUUID().toString()

    private val list by lazy {
        List(25) {
            Snippet(
                uuid = uuid,
                title = "Snippet $it",
                code = getMockCode(code),
                language = getMockLanguage(),
                visibility = SnippetVisibility.PUBLIC,
                isOwner = true,
                owner = Owner(it, "User $it"),
                modifiedAt = Date(),
                numberOfLikes = 5,
                numberOfDislikes = 3,
                userReaction = UserReaction.LIKE
            )
        }
    }
    override val updateListener: BehaviorSubject<Snippet> = BehaviorSubject.create()

    override fun snippets(scope: SnippetScope, page: Int): Single<List<Snippet>> {
        return Single.just(list)
            .map { it.take(SNIPPET_PAGE_SIZE * page) }
            .onErrorResumeNext { throwable -> Single.error(errorHandler.handle(throwable)) }
    }

    override fun snippet(id: String): Single<Snippet> = Single.just(list.find { it.uuid == id })

    override fun create(
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility
    ): Single<Snippet> = Single.just(
        Snippet(
            uuid = uuid,
            title = title,
            code = getMockCode(code),
            language = getMockLanguage(),
            visibility = SnippetVisibility.PUBLIC,
            isOwner = true,
            owner = Owner(0, "login"),
            modifiedAt = Date(),
            numberOfLikes = 0,
            numberOfDislikes = 0,
            userReaction = UserReaction.NONE
        )
    )

    override fun update(
        uuid: String,
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility
    ): Single<Snippet> = Single.just(
        Snippet(
            uuid = uuid,
            title = title,
            code = getMockCode(code),
            language = getMockLanguage(),
            visibility = SnippetVisibility.PUBLIC,
            isOwner = true,
            owner = Owner(0, "login"),
            modifiedAt = Date(),
            numberOfLikes = 5,
            numberOfDislikes = 3,
            userReaction = UserReaction.LIKE
        )
    )

    override fun count(scope: SnippetScope): Single<Int> = Single.just(list.size)

    override fun reaction(uuid: String, reaction: UserReaction): Completable =
        Completable.complete()

    override fun delete(uuid: String): Completable = Completable.complete()

    private fun getPreview(code: String): SpannableString {
        val preview = code.lines(PREVIEW_COUNT).joinToString(separator = newLineChar)
        return getHighlighted(preview)
    }

    private fun getMockCode(code: String) = SnippetCode(raw = code, highlighted = getPreview(code))

    private fun getMockLanguage() = SnippetLanguage(raw = "Java", type = SnippetLanguageType.JAVA)

    private val code =
        """
        /* Block comment */
        import java.util.Date;
        import static AnInterface.CONSTANT;
        import static java.util.Date.parse;
        import static SomeClass.staticField;
        /**
         * Doc comment here for <code>SomeClass</code>
         * @param T type parameter
         * @see Math#sin(double)
         */
        @Annotation (name=value)
        public class SomeClass<T extends Runnable> { // some comment
          private T field = null;
          private double unusedField = 12345.67890;
          private UnknownType anotherString = "Another\nStrin\g";
          public static int staticField = 0;
          public final int instanceFinalField = 0;

          /**
           * Semantic highlighting:
           * Generated spectrum to pick colors for local variables and parameters:
           *  Color#1 SC1.1 SC1.2 SC1.3 SC1.4 Color#2 SC2.1 SC2.2 SC2.3 SC2.4 Color#3
           *  Color#3 SC3.1 SC3.2 SC3.3 SC3.4 Color#4 SC4.1 SC4.2 SC4.3 SC4.4 Color#5
           * @param param1
           * @param reassignedParam
           * @param param2
           * @param param3
           */
          public SomeClass(AnInterface param1, int[] reassignedParam,
                          int param2
                          int param3) {
            int reassignedValue = this.staticField + param2 + param3;
            long localVar1, localVar2, localVar3, localVar4;
            int localVar = "IntelliJ"; // Error, incompatible types
            System.out.println(anotherString + toString() + localVar);
            long time = parse("1.2.3"); // Method is deprecated
            new Thread().countStackFrames(); // Method is deprecated and marked for removal
            reassignedValue ++; 
            field.run(); 
            new SomeClass() {
              {
                int a = localVar;
              }
            };
            reassignedParam = new ArrayList<String>().toArray(new int[CONSTANT]);
          }
        }
        enum AnEnum { CONST1, CONST2 }
        interface AnInterface {
          int CONSTANT = 2;
          void method();
        }
        abstract class SomeAbstractClass {
          protected int instanceField = staticField;
        }
        """.trimIndent()
}
