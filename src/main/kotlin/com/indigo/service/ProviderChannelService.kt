package com.indigo.service

import com.indigo.dto.ChannelRequest
import com.indigo.entity.ProviderChannel
import com.indigo.repository.ProviderChannelRepository
import com.indigo.repository.ProviderRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional

@ApplicationScoped
class ProviderChannelService {

    @Inject
    lateinit var providerChannelRepository: ProviderChannelRepository

    @Inject
    lateinit var providerRepository: ProviderRepository

    @Transactional
    fun createChannels(providerId: Long, request: ChannelRequest): List<ProviderChannel> {
        val provider = providerRepository.findById(providerId)
            ?: throw IllegalArgumentException("Provider with id $providerId not found")

        val channels = request.channels.map { item ->
            val channel = ProviderChannel()
            channel.provider = provider
            channel.channelType = item.type
            channel.pricePerMessage = item.pricePerMessage
            providerChannelRepository.persist(channel)
            channel
        }
        return channels
    }
}
