package dev.snipme.snipmeapp.util

object SyntaxPhrases {
    val wordTerminators = arrayOf(" ", ",", ".", ":", "(", ")", "=", "{", "}", "<", ">", "\r", "\n")
    val textTerminators = arrayOf("\"", "\'", "\"\"\"")
    val commentTerminators = arrayOf("//", "#")
    val multilineCommentTerminators = arrayOf(Pair("/*", "*/"), Pair("\'\'\'", "\'\'\'"))

    val keywords: List<String> =
        """#available #column #define #defined #elif #else #else#elseif #endif #error #file #function 
                 #if #ifdef #ifndef #include #line #pragma #selector #undef abstract add after alias 
                 alignas alignof and and_eq andalso as ascending asm assert associatedtype associativity 
                 async atomic_cancel atomic_commit atomic_noexcept auto await base become begin bitand 
                 bitor bnot bor box break bsl bsr bxor case catch chan 
                 bool boolean byte char char16_t char32_t decimal double enum float 
                 checked class compl concept cond const const_cast constexpr continue convenience 
                 covariant crate debugger decltype def default defer deferred defined? deinit 
                 del delegate delete descending didset div do dynamic dynamic_cast dynamictype 
                 elif else elseif elsif end ensure eval event except explicit export extends extension 
                 extern external factory fallthrough false final finally fixed fn for foreach friend 
                 from fun func function get global go goto group guard if impl implements implicit import 
                 in indirect infix init inline inout instanceof int interface internal into is join lambda 
                 lazy left let library local lock long loop macro map match mod module move mut mutable 
                 mutating namespace native new next nil noexcept none nonlocal nonmutating not not_eq 
                 null nullptr object of offsetof operator optional or or_eq orderby orelse out override 
                 package params part partial pass postfix precedence prefix priv private proc protected 
                 protocol pub public pure raise range readonly receive redo ref register reinterpret_cast 
                 rem remove repeat required requires rescue rethrow rethrows retry return right sbyte 
                 sealed select self set short signed sizeof stackalloc static static_assert static_cast 
                 strictfp string struct subscript super switch sync synchronized template then this 
                 thread_local throw throws trait transaction_safe transaction_safe_dynamic transient 
                 true try type typealias typedef typeid typename typeof uint ulong unchecked undef 
                 union unless unowned unsafe unsigned unsized until use ushort using value var virtual 
                 void volatile wchar_t weak when where while willset with xor xor_eq xorauto yield 
                 yieldabstract yieldarguments val list override get set as as? in !in !is is by 
                 constructor delegate dynamic field file init param property receiver setparam data 
                 data expect lateinit crossinline companion annotation actual noinline open reified 
                 suspend tailrec vararg it constraint alter column table all any asc backup database 
                 between check create index replace view procedure unique desc distinct drop exec 
                 exists foreign key full outer having inner insert like limit order primary rownum 
                 top truncate update values"""
            .split(" ")
}