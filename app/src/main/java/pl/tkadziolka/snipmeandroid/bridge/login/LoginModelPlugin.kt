package pl.tkadziolka.snipmeandroid.bridge.login

import io.flutter.plugin.common.BinaryMessenger
import org.koin.core.component.inject
import pl.tkadziolka.snipmeandroid.bridge.Bridge
import pl.tkadziolka.snipmeandroid.bridge.ModelPlugin
import pl.tkadziolka.snipmeandroid.ui.detail.DetailViewState
import pl.tkadziolka.snipmeandroid.ui.login.*

class LoginModelPlugin : ModelPlugin<Bridge.LoginModelBridge>(), Bridge.LoginModelBridge {
    private var oldEvent: LoginEvent? = null
    private var oldState: LoginState? = null
    private val model: LoginModel by inject()

    override fun getState(): Bridge.LoginModelStateData = getModelState(model.state.value)

    override fun getEvent(): Bridge.LoginModelEventData = getModelEvent(model.event.value)

    override fun resetEvent() {
        model.event.value = Idle
    }

    override fun onSetup(messenger: BinaryMessenger, bridge: Bridge.LoginModelBridge?) {
        Bridge.LoginModelBridge.setup(messenger, bridge)
    }

    override fun checkLoginState() {
        model.init()
    }

    override fun loginOrRegister(email: String, password: String) {
        model.loginOrRegister(email, password)
    }

    private fun getModelEvent(loginEvent: LoginEvent): Bridge.LoginModelEventData {
        return Bridge.LoginModelEventData().apply {
            event = loginEvent.toModelLoginEvent()
            oldHash = oldEvent?.hashCode()?.toLong() ?: 0
            newHash = loginEvent.hashCode().toLong()
        }.also {
            oldEvent = loginEvent
        }
    }

    private fun getModelState(loginState: LoginState): Bridge.LoginModelStateData {
        return Bridge.LoginModelStateData().apply {
            state = loginState.toModelLoginState()
            oldHash = oldState?.hashCode()?.toLong() ?: 0
            newHash = loginState.hashCode().toLong()
        }.also {
            oldState = loginState
        }
    }

    private fun LoginState.toModelLoginState() =
        when (this) {
            Loaded -> Bridge.ModelState.LOADED
            else -> Bridge.ModelState.LOADING
        }

    private fun LoginEvent.toModelLoginEvent() =
        when (this) {
            Logged -> Bridge.LoginModelEvent.LOGGED
            else -> Bridge.LoginModelEvent.NONE
        }
}