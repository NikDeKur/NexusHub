/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (c) 2024-present "Nik De Kur"
 */

package dev.nikdekur.nexushub.packet.`in`

import dev.nikdekur.nexushub.packet.Packet
import dev.nikdekur.nexushub.packet.serialize.PacketDeserializer
import dev.nikdekur.nexushub.packet.serialize.PacketSerializer
import dev.nikdekur.nexushub.packet.type.PacketTypes

class PacketHeartbeat : Packet() {
    override val packetId = PacketTypes.HEARTBEAT.id

    override fun deserialize(deserializer: PacketDeserializer) {
        // No data
    }

    override fun serialize(serializer: PacketSerializer) {
        // No data
    }

    override fun toString(): String {
        return "PacketHeartbeat()"
    }
}