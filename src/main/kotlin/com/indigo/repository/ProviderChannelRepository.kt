package com.indigo.repository

import com.indigo.entity.ProviderChannel
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProviderChannelRepository : PanacheRepository<ProviderChannel>
