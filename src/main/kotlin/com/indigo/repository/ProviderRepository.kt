package com.indigo.repository

import com.indigo.entity.Provider
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProviderRepository : PanacheRepository<Provider>
