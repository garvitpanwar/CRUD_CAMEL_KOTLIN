

// package com.indigo.entity

// import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
// import jakarta.persistence.*

// /**
//  * Entity class representing a Provider.
//  * Maps to the 'provider' table in the database.
//  */
// @Entity
// @Table(name = "provider")
// class Provider : PanacheEntityBase {

//     /**
//      * Primary key (auto-incremented).
//      */
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     var id: Long? = null

//     /**
//      * Name of the provider.
//      * Must be unique (assumed to be enforced via DB constraint).
//      */
//     @Column(nullable = false)
//     var name: String = ""

//     /**
//      * URL for the provider's logo image.
//      */
//     @Column(name = "logo_url")
//     var logoUrl: String = ""

//     /**
//      * SLA (Service Level Agreement) details embedded as a separate value object.
//      */
//     @Embedded
//     var sla: Sla? = null

//     /**
//      * Current status of the provider (e.g., ACTIVE, INACTIVE).
//      */
//     var status: String? = null
// }


package com.indigo.entity

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.*
import jakarta.persistence.OneToMany
import jakarta.persistence.CascadeType


/**
 * Entity class representing a Provider.
 * Maps to the 'provider' table in the database.
 */
@Entity
@Table(
    name = "provider",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["partner_id"]),
        UniqueConstraint(columnNames = ["name"])
    ]
)
class Provider : PanacheEntityBase {

    /**
     * Primary key (auto-incremented).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    /**
     * Partner ID of the provider.
     * Must be unique.
     */
    @Column(name = "partner_id", nullable = false)
    var partnerId: String = ""

    /**
     * Name of the provider.
     * Must be unique.
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

    @OneToMany(mappedBy = "provider", cascade = [CascadeType.ALL], orphanRemoval = true)
var channels: MutableList<ProviderChannel> = mutableListOf()


}
