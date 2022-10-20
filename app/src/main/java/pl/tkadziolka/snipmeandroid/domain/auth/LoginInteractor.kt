package pl.tkadziolka.snipmeandroid.domain.auth

class LoginInteractor(
    private val identifyUserUseCase: IdentifyUserUseCase,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) {
    fun identify(login: String) = identifyUserUseCase(login)

    fun login(login: String, password: String) = loginUseCase(login, password)

    fun register(login: String, password: String, email: String) = registerUseCase(login, password, email)
}