/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (c) 2024-present "Nik De Kur"
 */

package dev.nikdekur.nexushub

import dev.nikdekur.nexushub.boot.Environment
import dev.nikdekur.nexushub.dataset.DataSetService
import dev.nikdekur.nexushub.storage.StorageService
import kotlin.time.Duration

open class TestNexusHubServer(
    override val environment: Environment,
    val dataSetService: (TestNexusHubServer) -> DataSetService,
    val storageService: (TestNexusHubServer) -> StorageService
) : AbstractNexusHubServer() {

    override fun buildDataSetService(): DataSetService {
        return dataSetService(this)
    }

    override fun buildStorageService(): StorageService {
        return storageService(this)
    }

    override fun start() {
        super.start()

        logger.info("Starting NexusHub server...")
    }

    override fun stop(gracePeriod: Duration, timeout: Duration) {
        logger.info("Stopping test server...")
        logger.info("Oh, there is no server to stop. Ahahaha!")
    }
}