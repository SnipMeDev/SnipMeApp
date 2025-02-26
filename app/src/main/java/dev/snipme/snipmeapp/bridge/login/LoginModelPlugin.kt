package dev.snipme.snipmeapp.bridge.login

import dev.snipme.snipmeapp.bridge.FlowChannelEventStreamHandler
import dev.snipme.snipmeapp.bridge.FlowChannelStateStreamHandler
import dev.snipme.snipmeapp.bridge.ModelPlugin
import dev.snipme.snipmeapp.channel.ChannelLoginModel
import io.flutter.plugin.common.BinaryMessenger
import kotlinx.coroutines.flow.map
import org.koin.core.component.inject
import dev.snipme.snipmeapp.channel.LoginModelEvent as ChannelLoginModelEvent
import dev.snipme.snipmeapp.channel.LoginModelEventData as ChannelLoginModelEventData
import dev.snipme.snipmeapp.channel.LoginModelStateData as ChannelLoginModelStateData
import dev.snipme.snipmeapp.channel.ModelState as ChannelModelState

class LoginModelPlugin : ModelPlugin<ChannelLoginModel>(), ChannelLoginModel {
    private val model: LoginModel by inject()
    private val channelStateFlow by inject<FlowChannelStateStreamHandler>()
    private val channelEventFlow by inject<FlowChannelEventStreamHandler>()

    override fun resetEvent() {
        model.event.value = Idle
    }

    override fun onSetup(messenger: BinaryMessenger, channelModel: ChannelLoginModel?) {
        ChannelLoginModel.setUp(messenger, channelModel)
        channelStateFlow.zip(model.state.map { getModelState(it) })
        channelEventFlow.zip(model.event.map { getModelEvent(it) })
    }

    override fun checkLoginState() {
        model.init()
    }

    override fun loginOrRegister(email: String, password: String) {
        model.loginOrRegister(email, password)
    }

    private fun getModelEvent(loginEvent: LoginEvent): ChannelLoginModelEventData {
        return ChannelLoginModelEventData(
            event = loginEvent.toModelLoginEvent()
        )
    }

    private fun getModelState(loginState: LoginState): ChannelLoginModelStateData {
        return ChannelLoginModelStateData(
            state = loginState.toModelLoginState(),
        )
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