// package com.indigo.dto

// data class ChannelRequest(
//     val channels: List<ChannelItem>
// )

// data class ChannelItem(
//     val type: String,
//     val pricePerMessage: Double
// )


package com.indigo.dto

data class ChannelRequest(
    val channels: List<ChannelItem> = emptyList()
)

data class ChannelItem(
    val type: String = "",
    val pricePerMessage: Double = 0.0
)
