package com.indigo.entity

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "provider_channels")
class ProviderChannel : PanacheEntityBase {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    var id: UUID? = null

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    var provider: Provider? = null

    @Column(name = "channel_type", nullable = false)
    var channelType: String = ""

    @Column(name = "price_per_message", nullable = false)
    var pricePerMessage: Double = 0.0

    @Column(name = "created_at")
    var createdAt: Date = Date()
}
