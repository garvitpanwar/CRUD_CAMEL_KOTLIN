package com.indigo.service

import com.indigo.dto.ChannelRequest
import com.indigo.entity.ProviderChannel
import com.indigo.repository.ProviderChannelRepository
import com.indigo.repository.ProviderRepository
import io.vertx.core.impl.logging.LoggerFactory
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import kotlin.math.log

@ApplicationScoped
class ProviderChannelService {

    @Inject
    lateinit var providerChannelRepository: ProviderChannelRepository

    @Inject
    lateinit var providerRepository: ProviderRepository

    val logger = LoggerFactory.getLogger(ProviderChannelService::class.java)

    @Transactional
    fun createChannels(providerId: Long, request: ChannelRequest): List<ProviderChannel> {
        val provider = providerRepository.findById(providerId)
            ?: throw IllegalArgumentException("Provider with id $providerId not found")

        val channels = request.channels.map { item ->
            val channel = ProviderChannel()
            channel.provider = provider
            channel.channelType = item.type
            channel.pricePerMessage = item.pricePerMessage
            // providerChannelRepository.persist(channel)
            channel
        }
        providerChannelRepository.persist(channels)
        channels.forEach { channel -> 
        logger.info("Persisting channel: ${channel.channelType} with price: ${channel.pricePerMessage}")
        } // Set createdAt for each channelz
        logger.info("Persist Channel: ${channels}")
        return channels
    }
}
