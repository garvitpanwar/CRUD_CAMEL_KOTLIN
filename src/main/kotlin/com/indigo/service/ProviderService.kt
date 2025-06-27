
package com.indigo.service

import com.indigo.entity.Provider
import com.indigo.repository.ProviderRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional

/**
 * Service layer for handling business logic related to Provider entity.
 * Acts as an intermediate between routes and repository layer.
 */
@ApplicationScoped
class ProviderService {

    @Inject
    lateinit var repository: ProviderRepository

    /**
     * Fetch all providers from the database.
     * @return List of all Provider records
     */
    @Transactional
    fun listAll(): List<Provider> = repository.listAll()

    /**
     * Create a new Provider and save it to the database.
     * @param provider the new Provider object to persist
     */
    @Transactional
    fun create(provider: Provider) {
        repository.persist(provider)
    }

    /**
     * Find a provider by its ID.
     * @param id the provider's unique identifier
     * @return the Provider object if found, else null
     */
    @Transactional
    fun findById(id: Long): Provider? = repository.findById(id)

    /**
     * Update an existing provider by its ID.
     * If found, updates its fields and returns the updated object.
     * @param id ID of the provider to update
     * @param updatedProvider the updated data
     * @return updated Provider object or null if not found
     */
    @Transactional
    fun update(id: Long, updatedProvider: Provider): Provider? {
        val existing = repository.findById(id)
        return if (existing != null) {
            existing.name = updatedProvider.name
            existing.logoUrl = updatedProvider.logoUrl
            existing.sla = updatedProvider.sla
            existing.status = updatedProvider.status
            existing
        } else {
            null
        }
    }

    /**
     * Delete a provider by its ID.
     * @param id ID of the provider to delete
     * @return true if deleted, false if not found
     */
    @Transactional
    fun delete(id: Long): Boolean {
        val existing = repository.findById(id)
        return if (existing != null) {
            repository.delete(existing)
            true
        } else {
            false
        }
    }
}
