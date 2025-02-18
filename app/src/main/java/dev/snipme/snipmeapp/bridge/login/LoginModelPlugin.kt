package dev.snipme.snipmeapp.bridge.login

import io.flutter.plugin.common.BinaryMessenger
import org.koin.core.component.inject
import dev.snipme.snipmeapp.bridge.ModelPlugin
import dev.snipme.snipmeapp.channel.LoginModelBridge as ChannelLoginModelBridge
import dev.snipme.snipmeapp.channel.ModelState as ChannelModelState
import dev.snipme.snipmeapp.channel.LoginModelStateData as ChannelLoginModelStateData
import dev.snipme.snipmeapp.channel.LoginModelEvent as ChannelLoginModelEvent
import dev.snipme.snipmeapp.channel.LoginModelEventData as ChannelLoginModelEventData

class LoginModelPlugin : ModelPlugin<ChannelLoginModelBridge>(), ChannelLoginModelBridge {
    private var oldEvent: LoginEvent? = null
    private var oldState: LoginState? = null
    private val model: LoginModel by inject()

    override fun getState(): ChannelLoginModelStateData = getModelState(model.state.value)

    override fun getEvent(): ChannelLoginModelEventData = getModelEvent(model.event.value)

    override fun resetEvent() {
        model.event.value = Idle
    }

    override fun onSetup(messenger: BinaryMessenger, bridge: ChannelLoginModelBridge?) {
        ChannelLoginModelBridge.setUp(messenger, bridge)
    }

    override fun checkLoginState() {
        model.init()
    }

    override fun loginOrRegister(email: String, password: String) {
        model.loginOrRegister(email, password)
    }

    private fun getModelEvent(loginEvent: LoginEvent): ChannelLoginModelEventData {
        return ChannelLoginModelEventData(
            event = loginEvent.toModelLoginEvent(),
            oldHash = oldEvent?.hashCode()?.toLong() ?: 0,
            newHash = loginEvent.hashCode().toLong(),
        ).also {
            oldEvent = loginEvent
        }
    }

    private fun getModelState(loginState: LoginState): ChannelLoginModelStateData {
        return ChannelLoginModelStateData(
            state = loginState.toModelLoginState(),
            oldHash = oldState?.hashCode()?.toLong() ?: 0,
            newHash = loginState.hashCode().toLong(),
        ).also {
            oldState = loginState
        }
    }

    private fun LoginState.toModelLoginState() =
        when (this) {
            Loaded -> ChannelModelState.LOADED
            else -> ChannelModelState.LOADING
        }

    private fun LoginEvent.toModelLoginEvent() =
        when (this) {
            Logged -> ChannelLoginModelEvent.LOGGED
            else -> ChannelLoginModelEvent.NONE
        }
}