

package com.indigo.entity

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.*

/**
 * Entity class representing a Provider.
 * Maps to the 'provider' table in the database.
 */
@Entity
@Table(name = "provider")
class Provider : PanacheEntityBase {

    /**
     * Primary key (auto-incremented).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    /**
     * Name of the provider.
     * Must be unique (assumed to be enforced via DB constraint).
     */
    @Column(nullable = false)
    var name: String = ""

    /**
     * URL for the provider's logo image.
     */
    @Column(name = "logo_url")
    var logoUrl: String = ""

    /**
     * SLA (Service Level Agreement) details embedded as a separate value object.
     */
    @Embedded
    var sla: Sla? = null

    /**
     * Current status of the provider (e.g., ACTIVE, INACTIVE).
     */
    var status: String? = null
}
