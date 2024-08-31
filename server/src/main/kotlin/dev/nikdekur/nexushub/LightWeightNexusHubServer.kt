/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (c) 2024-present "Nik De Kur"
 */

package dev.nikdekur.nexushub

import dev.nikdekur.nexushub.boot.Environment
import dev.nikdekur.nexushub.boot.EnvironmentBuilder
import dev.nikdekur.nexushub.service.NexusHubService
import java.util.LinkedList
import kotlin.reflect.KClass
import kotlin.time.Duration

typealias ServiceConstructor = (NexusHubServer) -> NexusHubService

class LightWeightNexusHubServerBuilder {
    var start: Boolean = true
    val services = LinkedList<Pair<ServiceConstructor, KClass<out NexusHubService>>>()
    var environment: Environment = Environment.Empty
    var onStop: (Duration, Duration) -> Unit = { _, _ -> }

    fun service(service: ServiceConstructor, serviceInterface: KClass<out NexusHubService>) {
        services.add(service to serviceInterface)
    }

    fun environment(environment: Environment) {
        this.environment = environment
    }

    inline fun LightWeightNexusHubServerBuilder.environment(block: EnvironmentBuilder.() -> Unit) {
        val env = EnvironmentBuilder().apply(block).build()
        environment(env)
    }


    fun onStop(onStop: (Duration, Duration) -> Unit) {
        this.onStop = onStop
    }

    fun build(): AbstractNexusHubServer {
        val server = object : AbstractNexusHubServer() {
            override fun registerServices() {
                services.forEach { (service, serviceInterface) ->
                    servicesManager.registerService(service(this), serviceInterface)
                }
            }

            override val environment = this@LightWeightNexusHubServerBuilder.environment

            override fun stop(gracePeriod: Duration, timeout: Duration) {
                onStop(gracePeriod, timeout)
            }
        }

        if (start) server.start()

        return server
    }
}


inline fun lightWeightNexusHubServer(block: LightWeightNexusHubServerBuilder.() -> Unit) =
    LightWeightNexusHubServerBuilder().apply(block).build()