// package com.indigo.entity

// import jakarta.persistence.Embeddable

// @Embeddable
// class Sla {
//     var uptimePercent: Double? = null
//     var deliveryTimeMs: Int? = null
// }



package com.indigo.entity

import jakarta.persistence.Embeddable

/**
 * SLA (Service Level Agreement) information for a Provider.
 * This class is embeddable inside Provider entity.
 */
@Embeddable
class Sla {

    // The guaranteed uptime percentage for the provider (e.g., 99.9)
    var uptimePercent: Double? = null

    // Maximum expected delivery time in milliseconds
    var deliveryTimeInMs: Int? = null
}
