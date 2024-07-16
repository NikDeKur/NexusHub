@file:Suppress("PropertyName", "kotlin:S117")

package dev.nikdekur.nexushub.config

import kotlinx.serialization.Serializable
import net.mamoe.yamlkt.Comment

@Serializable
data class NexusDataConfig(
    @Comment("Interval to clear cached holder data after write/access (in seconds)")
    val cache_expiration: Long = 300,
    @Comment("Maximum number of cached holder data by each scope")
    val cache_max_size: Long = 1000,
    @Comment("Number of threads used at the same time to save data")
    val save_parallelism: Int = 16,
    @Comment("Number of threads used at the same time to sync data")
    val sync_parallelism: Int = 16,
)