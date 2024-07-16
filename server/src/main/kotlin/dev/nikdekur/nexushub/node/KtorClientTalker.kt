package dev.nikdekur.nexushub.node

import dev.nikdekur.ndkore.ext.debug
import dev.nikdekur.nexushub.NexusHub.logger
import dev.nikdekur.nexushub.network.PacketManager
import dev.nikdekur.nexushub.network.addressHash
import dev.nikdekur.nexushub.network.addressStr
import dev.nikdekur.nexushub.network.dsl.IncomingContext
import dev.nikdekur.nexushub.network.transmission.PacketTransmission
import dev.nikdekur.nexushub.packet.Packet
import dev.nikdekur.nexushub.util.CloseCode
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers

class KtorClientTalker(val websocket: DefaultWebSocketServerSession) : ClientTalker {

    override val addressHash = websocket.call.addressHash
    override val addressStr = websocket.call.addressStr


    val packetManager = PacketManager(this, Dispatchers.IO)

    @OptIn(DelicateCoroutinesApi::class)
    override val isOpen: Boolean
        get() = websocket.closeReason.isActive && (!websocket.outgoing.isClosedForSend && !websocket.incoming.isClosedForReceive)

    override var isBlocked: Boolean = false

    override suspend fun send(transmission: PacketTransmission<*>) {
        val bytes = packetManager.processOutgoingTransmission(transmission)
        logger.debug { "[$addressStr] Sending packet ${transmission.packet}" }
        websocket.send(bytes)
    }

    override suspend fun receive(data: ByteArray): IncomingContext<Packet>? {
        return packetManager.processIncomingPacket(data)
    }

    override suspend fun closeWithBlock(code: CloseCode, reason: String) {
        isBlocked = true
        close(code, reason)
    }

    override suspend fun close(code: CloseCode, comment: String) {
        websocket.close(CloseReason(code.code, comment))
        TalkersManager.cleanUp(addressHash)
    }




    override fun equals(other: Any?): Boolean {
        if (other !is KtorClientTalker) return false
        return this.addressHash == other.addressHash
    }

    override fun hashCode(): Int {
        return addressHash
    }

    override fun toString(): String {
        return "KtorTalker(address='$addressHash')"
    }
}