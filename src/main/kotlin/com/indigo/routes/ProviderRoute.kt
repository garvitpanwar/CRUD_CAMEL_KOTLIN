// package com.indigo.routes

// import com.indigo.entity.Provider
// import com.indigo.service.ProviderService
// import jakarta.enterprise.context.ApplicationScoped
// import jakarta.inject.Named
// import org.apache.camel.Exchange
// import org.apache.camel.builder.RouteBuilder
// import org.apache.camel.model.rest.RestBindingMode

// @ApplicationScoped
// @Named("providerRoute")
// class ProviderRoute(private val providerService: ProviderService) : RouteBuilder() {

//     override fun configure() {
//         // REST configuration
//         restConfiguration()
//             .component("platform-http")
//             .bindingMode(RestBindingMode.json)

//         // Define REST endpoints
//         rest("/providers/v1")
//     // POST /providers/v1 - Create a new provider
//     .post()
//         .consumes("application/json")
//         .type(Provider::class.java)
//         .to("bean:providerRoute?method=onCreate")

//     // GET /providers/v1 - List all providers
//     .get()
//         .produces("application/json")
//         .to("bean:providerRoute?method=onList")

//     // GET /providers/v1/{id} - Get by ID
//     .get("/{id}")
//         .produces("application/json")
//         .to("bean:providerRoute?method=onGetById(\${header.id})")

//     // PUT /providers/v1/{id} - Update
//     .put("/{id}")
//         .consumes("application/json")
//         .type(Provider::class.java)
//         .to("bean:providerRoute?method=onUpdate")

//     // DELETE /providers/v1/{id} - Delete
//     .delete("/{id}")
//         .produces("application/json")
//         .to("bean:providerRoute?method=onDelete")


//     }




//     // GET all providers with JSON exception handling
// fun onList(exchange: Exchange): Any {
//     return try {
//         val providers = providerService.listAll()
//         exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
//         mapOf(
//             "code" to 200,
//             "message" to "Fetched all providers successfully",
//             "data" to providers
//         )
//     } catch (e: Exception) {
//         exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
//         mapOf(
//             "code" to 500,
//             "message" to "Unexpected error while fetching providers",
//             "error" to e.localizedMessage
//         )
//     }
// }

// // GET provider by ID with JSON exception handling
// fun onGetById(id: Long, exchange: Exchange): Any {
//     return try {
//         val provider = providerService.findById(id)
//         if (provider != null) {
//             exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
//             mapOf(
//                 "code" to 200,
//                 "message" to "Provider fetched successfully",
//                 "data" to provider
//             )
//         } else {
//             exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
//             mapOf(
//                 "code" to 404,
//                 "message" to "Provider with ID $id not found"
//             )
//         }
//     } catch (e: Exception) {
//         exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
//         mapOf(
//             "code" to 500,
//             "message" to "Unexpected error while fetching provider",
//             "error" to e.localizedMessage
//         )
//     }
// }







//     fun onCreate(provider: Provider, exchange: Exchange): Any {
//         log.info("[CREATE] Incoming provider: $provider")
//         return try {
//             providerService.create(provider)
//             log.info("[CREATE] Provider created successfully: ${provider.name}")
//             provider
//         } catch (e: Exception) {
//             if (e.cause?.message?.contains("unique_provider_name", ignoreCase = true) == true) {
//                 exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 409)
//                 log.warn("[CREATE] Duplicate provider name: ${provider.name}")
//                 "Provider with name '${provider.name}' already exists"
//             } else {
//                 exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
//                 log.error("[CREATE] Unexpected error: ${e.localizedMessage}")
//                 "Unexpected error: ${e.localizedMessage}"
//             }
//         }
//     }

//     fun onUpdate(provider: Provider, exchange: Exchange): Any {
//         val providerId = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
//         log.info("[UPDATE] ID = $providerId | Payload = $provider")

//         return if (providerId != null) {
//             try {
//                 val updatedProvider = providerService.update(providerId, provider)
//                 if (updatedProvider != null) {
//                     exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
//                     log.info("[UPDATE] Provider updated: $updatedProvider")
//                     mapOf("code" to 200, "message" to "Provider updated", "data" to updatedProvider)
//                 } else {
//                     exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
//                     log.warn("[UPDATE] Provider not found with ID: $providerId")
//                     mapOf("code" to 404, "message" to "Provider with ID $providerId not found")
//                 }
//             } catch (e: Exception) {
//                 exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
//                 log.error("[UPDATE] Exception: ${e.localizedMessage}")
//                 mapOf("code" to 500, "message" to "Unexpected error: ${e.localizedMessage}")
//             }
//         } else {
//             exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 400)
//             log.warn("[UPDATE] Invalid ID format")
//             mapOf("code" to 400, "message" to "Invalid ID in path")
//         }
//     }

//     fun onDelete(exchange: Exchange): Any {
//         val providerId = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
//         log.info("[DELETE] Request to delete provider with ID: $providerId")

//         return if (providerId == null) {
//             exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 400)
//             log.warn("[DELETE] Invalid ID in path")
//             mapOf("code" to 400, "message" to "Invalid ID in path")
//         } else {
//             try {
//                 val wasDeleted = providerService.delete(providerId)
//                 if (wasDeleted) {
//                     exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
//                     log.info("[DELETE] Provider deleted: ID = $providerId")
//                     mapOf("code" to 200, "message" to "Provider deleted successfully", "data" to mapOf("id" to providerId))
//                 } else {
//                     exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
//                     log.warn("[DELETE] Provider not found with ID: $providerId")
//                     mapOf("code" to 404, "message" to "Provider with ID $providerId not found")
//                 }
//             } catch (e: Exception) {
//                 exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
//                 log.error("[DELETE] Exception: ${e.localizedMessage}")
//                 mapOf("code" to 500, "message" to "Unexpected error: ${e.localizedMessage}")
//             }
//         }
//     }

// }



// package com.indigo.route

// import com.indigo.entity.Provider
// import com.indigo.service.ProviderService
// import jakarta.enterprise.context.ApplicationScoped
// import jakarta.inject.Named
// import org.apache.camel.Exchange
// import org.apache.camel.builder.RouteBuilder
// import org.apache.camel.model.rest.RestBindingMode

// @ApplicationScoped
// @Named("providerRoute")
// class ProviderRoute(private val providerService: ProviderService) : RouteBuilder() {

//     override fun configure() {

//         restConfiguration()
//             .component("platform-http")
//             .bindingMode(RestBindingMode.json)

//         rest("/providers/v1")
//             .post()
//                 .consumes("application/json")
//                 .type(Provider::class.java)
//                 .to("direct:createProvider")
//             .get()
//                 .produces("application/json")
//                 .to("direct:getAllProviders")
//             .get("/{id}")
//                 .produces("application/json")
//                 .to("direct:getProviderById")
//             .put("/{id}")
//                 .consumes("application/json")
//                 .type(Provider::class.java)
//                 .to("direct:updateProvider")
//             .delete("/{id}")
//                 .produces("application/json")
//                 .to("direct:deleteProvider")

//         from("direct:createProvider")
//             .log("POST /providers/v1 - Create provider called")
//             .to("bean:providerRoute?method=onCreate")

//         from("direct:getAllProviders")
//             .log("GET /providers/v1 - List providers called")
//             .to("bean:providerRoute?method=onList")

//         from("direct:getProviderById")
//             .log("GET /providers/v1/{id} - Get provider by ID called")
//             .to("bean:providerRoute?method=onGetById(\${header.id})")

//         from("direct:updateProvider")
//             .log("PUT /providers/v1/{id} - Update provider called")
//             .to("bean:providerRoute?method=onUpdate")

//         from("direct:deleteProvider")
//             .log("DELETE /providers/v1/{id} - Delete provider called")
//             .to("bean:providerRoute?method=onDelete")
//     }

//     fun onList(exchange: Exchange): Any {
//         log.info("[LIST] Request received from user")
//         log.info("[LIST] Sending request to database")
//         return try {
//             val providers = providerService.listAll()
//             log.info("[LIST] Response received from database: $providers")
//             exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
//             mapOf("code" to 200, "message" to "Fetched all providers", "data" to providers)
//         } catch (e: Exception) {
//             log.error("[LIST] Exception: ${e.localizedMessage}")
//             exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
//             mapOf("code" to 500, "message" to "Unexpected error", "error" to e.localizedMessage)
//         }
//     }

//     // fun onGetById(id: Long, exchange: Exchange): Any {
//     //     log.info("[GET BY ID] Request received from user for ID = $id")
//     //     log.info("[GET BY ID] Sending request to database for ID = $id")
//     //     return try {
//     //         val provider = providerService.findById(id)
//     //         if (provider != null) {
//     //             log.info("[GET BY ID] Response received - ID: ${provider.id}, Name: ${provider.name}, Status: ${provider.status}, SLA Uptime: ${provider.sla?.uptimePercent}, SLA DeliveryTimeMs: ${provider.sla?.deliveryTimeInMs}")
//     //             exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
//     //             mapOf("code" to 200, "message" to "Fetched provider", "data" to provider)
//     //         } else {
//     //             log.warn("[GET BY ID] Provider with ID = $id not found")
//     //             exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
//     //             mapOf("code" to 404, "message" to "Provider with ID $id not found")
//     //         }
//     //     } catch (e: Exception) {
//     //         log.error("[GET BY ID] Exception: ${e.localizedMessage}")
//     //         exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
//     //         mapOf("code" to 500, "message" to "Unexpected error", "error" to e.localizedMessage)
//     //     }
//     // }



//     fun onGetById(id: Long, exchange: Exchange): Map<String, Any?> {
//     log.info("[GET BY ID] Request received from user for ID = $id")
//     return try {
//         log.info("[GET BY ID] Sending request to database for ID = $id")
//         val provider = providerService.findById(id)

//         if (provider != null) {
//             log.info("[GET BY ID] Response received - ID: ${provider.id}, Name: ${provider.name}, Status: ${provider.status}")
//             exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
//             mapOf(
//                 "code" to 200,
//                 "message" to "Fetched provider",
//                 "data" to mapOf(
//                     "id" to provider.id,
//                     "name" to provider.name,
//                     "status" to provider.status,
//                     "sla" to mapOf(
//                         "uptimePercent" to provider.sla?.uptimePercent,
//                         "deliveryTimeInMs" to provider.sla?.deliveryTimeInMs
//                     )
//                 )
//             )
//         } else {
//             log.warn("[GET BY ID] Provider with ID = $id not found")
//             exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
//             mapOf(
//                 "code" to 404,
//                 "message" to "Provider with ID $id not found",
//                 "data" to null
//             )
//         }

//     } catch (e: IllegalArgumentException) {
//         log.error("[GET BY ID] Invalid request data: ${e.localizedMessage}")
//         exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 400)
//         mapOf(
//             "code" to 400,
//             "message" to "Invalid request data",
//             "error" to e.localizedMessage,
//             "data" to null
//         )

//     } catch (e: Exception) {
//         log.error("[GET BY ID] Unexpected error: ${e.localizedMessage}")
//         exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
//         mapOf(
//             "code" to 500,
//             "message" to "Unexpected server error",
//             "error" to e.localizedMessage,
//             "data" to null
//         )
//     }
// }








//     fun onCreate(provider: Provider, exchange: Exchange): Any {
//         log.info("[CREATE] Request received - Name: ${provider.name}, Status: ${provider.status}, SLA Uptime: ${provider.sla?.uptimePercent}, SLA DeliveryTimeMs: ${provider.sla?.deliveryTimeInMs}")
//         log.info("[CREATE] Sending create request to database")
//         return try {
//             providerService.create(provider)
//             log.info("[CREATE] Provider created - ID: ${provider.id}, Name: ${provider.name}, Status: ${provider.status}, SLA Uptime: ${provider.sla?.uptimePercent}, SLA DeliveryTimeMs: ${provider.sla?.deliveryTimeInMs}")
//             provider
//         } catch (e: Exception) {
//             if (e.cause?.message?.contains("unique_provider_name", ignoreCase = true) == true) {
//                 exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 409)
//                 log.warn("[CREATE] Duplicate provider name: ${provider.name}")
//                 "Provider with name '${provider.name}' already exists"
//             } else {
//                 log.error("[CREATE] Exception: ${e.localizedMessage}")
//                 exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
//                 "Unexpected error: ${e.localizedMessage}"
//             }
//         }
//     }

//     fun onUpdate(provider: Provider, exchange: Exchange): Any {
//         val providerId = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
//         log.info("[UPDATE] Request received - ID: $providerId, Name: ${provider.name}, Status: ${provider.status}, SLA Uptime: ${provider.sla?.uptimePercent}, SLA DeliveryTimeMs: ${provider.sla?.deliveryTimeInMs}")
//         return if (providerId != null) {
//             log.info("[UPDATE] Sending update request to database for ID = $providerId")
//             try {
//                 val updated = providerService.update(providerId, provider)
//                 if (updated != null) {
//                     log.info("[UPDATE] Response received - ID: ${updated.id}, Name: ${updated.name}, Status: ${updated.status}, SLA Uptime: ${updated.sla?.uptimePercent}, SLA DeliveryTimeMs: ${updated.sla?.deliveryTimeInMs}")
//                     exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
//                     mapOf("code" to 200, "message" to "Provider updated", "data" to updated)
//                 } else {
//                     log.warn("[UPDATE] Provider with ID = $providerId not found")
//                     exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
//                     mapOf("code" to 404, "message" to "Provider with ID $providerId not found")
//                 }
//             } catch (e: Exception) {
//                 log.error("[UPDATE] Exception: ${e.localizedMessage}")
//                 exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
//                 mapOf("code" to 500, "message" to "Unexpected error", "error" to e.localizedMessage)
//             }
//         } else {
//             log.warn("[UPDATE] Invalid ID in path")
//             exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 400)
//             mapOf("code" to 400, "message" to "Invalid ID in path")
//         }
//     }

//     fun onDelete(exchange: Exchange): Any {
//         val providerId = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
//         log.info("[DELETE] Request received from user for ID = $providerId")
//         return if (providerId == null) {
//             log.warn("[DELETE] Invalid ID in path")
//             exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 400)
//             mapOf("code" to 400, "message" to "Invalid ID in path")
//         } else {
//             log.info("[DELETE] Sending delete request to database for ID = $providerId")
//             try {
//                 val deleted = providerService.delete(providerId)
//                 if (deleted) {
//                     log.info("[DELETE] Provider deleted in database for ID = $providerId")
//                     exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
//                     mapOf("code" to 200, "message" to "Provider deleted", "data" to mapOf("id" to providerId))
//                 } else {
//                     log.warn("[DELETE] Provider with ID = $providerId not found")
//                     exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
//                     mapOf("code" to 404, "message" to "Provider with ID $providerId not found")
//                 }
//             } catch (e: Exception) {
//                 log.error("[DELETE] Exception: ${e.localizedMessage}")
//                 exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
//                 mapOf("code" to 500, "message" to "Unexpected error", "error" to e.localizedMessage)
//             }
//         }
//     }
// }




package com.indigo.route

import com.indigo.entity.Provider
import com.indigo.service.ProviderService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Named
import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.rest.RestBindingMode

@ApplicationScoped
@Named("providerRoute")
class ProviderRoute(private val providerService: ProviderService) : RouteBuilder() {

    override fun configure() {

        restConfiguration()
            .component("platform-http")
            .bindingMode(RestBindingMode.json)

        rest("/providers/v1")
            .post()
                .consumes("application/json")
                .type(Provider::class.java)
                .to("direct:createProvider")
            .get()
                .produces("application/json")
                .to("direct:getAllProviders")
            .get("/{id}")
                .produces("application/json")
                .to("direct:getProviderById")
            .put("/{id}")
                .consumes("application/json")
                .type(Provider::class.java)
                .to("direct:updateProvider")
            .delete("/{id}")
                .produces("application/json")
                .to("direct:deleteProvider")

        from("direct:createProvider")
            .log("POST /providers/v1 - Create provider called")
            .to("bean:providerRoute?method=onCreate")

        from("direct:getAllProviders")
            .log("GET /providers/v1 - List providers called")
            .to("bean:providerRoute?method=onList")

        from("direct:getProviderById")
            .log("GET /providers/v1/{id} - Get provider by ID called")
            .to("bean:providerRoute?method=onGetById(\${header.id})")

        from("direct:updateProvider")
            .log("PUT /providers/v1/{id} - Update provider called")
            .to("bean:providerRoute?method=onUpdate")

        from("direct:deleteProvider")
            .log("DELETE /providers/v1/{id} - Delete provider called")
            .to("bean:providerRoute?method=onDelete")
    }

    fun onList(exchange: Exchange): Any {
        log.info("[LIST] Request received from user")
        log.info("[LIST] Sending request to database")
        return try {
            val providers = providerService.listAll()
            log.info("[LIST] Response received from database: $providers")
            exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
            mapOf("code" to 200, "message" to "Fetched all providers", "data" to providers)
        } catch (e: Exception) {
            log.error("[LIST] Exception: ${e.localizedMessage}")
            exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
            mapOf("code" to 500, "message" to "Unexpected error", "error" to e.localizedMessage)
        }
    }

    fun onGetById(id: Long, exchange: Exchange): Any {
        log.info("[GET BY ID] Request received from user for ID = $id")
        log.info("[GET BY ID] Sending request to database for ID = $id")
        return try {
            val provider = providerService.findById(id)
            if (provider != null) {
                log.info("[GET BY ID] Response received - ID: ${provider.id}, Name: ${provider.name}, Status: ${provider.status}, SLA Uptime: ${provider.sla?.uptimePercent}, SLA DeliveryTimeMs: ${provider.sla?.deliveryTimeInMs}")
                exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
                mapOf("code" to 200, "message" to "Fetched provider", "data" to provider)
            } else {
                log.warn("[GET BY ID] Provider with ID = $id not found")
                exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
                mapOf("code" to 404, "message" to "Provider with ID $id not found")
            }
        } catch (e: Exception) {
            log.error("[GET BY ID] Exception: ${e.localizedMessage}")
            exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
            mapOf("code" to 500, "message" to "Unexpected error", "error" to e.localizedMessage)
        }
    }

    fun onCreate(provider: Provider, exchange: Exchange): Any {
        log.info("[CREATE] Request received - Name: ${provider.name}, Status: ${provider.status}, SLA Uptime: ${provider.sla?.uptimePercent}, SLA DeliveryTimeMs: ${provider.sla?.deliveryTimeInMs}")
        log.info("[CREATE] Sending create request to database")
        return try {
            providerService.create(provider)
            log.info("[CREATE] Provider created - ID: ${provider.id}, Name: ${provider.name}, Status: ${provider.status}, SLA Uptime: ${provider.sla?.uptimePercent}, SLA DeliveryTimeMs: ${provider.sla?.deliveryTimeInMs}")
            provider
        } catch (e: Exception) {
            if (e.cause?.message?.contains("unique_provider_name", ignoreCase = true) == true) {
                exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 409)
                log.warn("[CREATE] Duplicate provider name: ${provider.name}")
                "Provider with name '${provider.name}' already exists"
            } else {
                log.error("[CREATE] Exception: ${e.localizedMessage}")
                exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
                "Unexpected error: ${e.localizedMessage}"
            }
        }
    }

    fun onUpdate(provider: Provider, exchange: Exchange): Any {
        val providerId = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
        log.info("[UPDATE] Request received - ID: $providerId, Name: ${provider.name}, Status: ${provider.status}, SLA Uptime: ${provider.sla?.uptimePercent}, SLA DeliveryTimeMs: ${provider.sla?.deliveryTimeInMs}")
        return if (providerId != null) {
            log.info("[UPDATE] Sending update request to database for ID = $providerId")
            try {
                val updated = providerService.update(providerId, provider)
                if (updated != null) {
                    log.info("[UPDATE] Response received - ID: ${updated.id}, Name: ${updated.name}, Status: ${updated.status}, SLA Uptime: ${updated.sla?.uptimePercent}, SLA DeliveryTimeMs: ${updated.sla?.deliveryTimeInMs}")
                    exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
                    mapOf("code" to 200, "message" to "Provider updated", "data" to updated)
                } else {
                    log.warn("[UPDATE] Provider with ID = $providerId not found")
                    exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
                    mapOf("code" to 404, "message" to "Provider with ID $providerId not found")
                }
            } catch (e: Exception) {
                log.error("[UPDATE] Exception: ${e.localizedMessage}")
                exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
                mapOf("code" to 500, "message" to "Unexpected error", "error" to e.localizedMessage)
            }
        } else {
            log.warn("[UPDATE] Invalid ID in path")
            exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 400)
            mapOf("code" to 400, "message" to "Invalid ID in path")
        }
    }

    fun onDelete(exchange: Exchange): Any {
        val providerId = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
        log.info("[DELETE] Request received from user for ID = $providerId")
        return if (providerId == null) {
            log.warn("[DELETE] Invalid ID in path")
            exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 400)
            mapOf("code" to 400, "message" to "Invalid ID in path")
        } else {
            log.info("[DELETE] Sending delete request to database for ID = $providerId")
            try {
                val deleted = providerService.delete(providerId)
                if (deleted) {
                    log.info("[DELETE] Provider deleted in database for ID = $providerId")
                    exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
                    mapOf("code" to 200, "message" to "Provider deleted", "data" to mapOf("id" to providerId))
                } else {
                    log.warn("[DELETE] Provider with ID = $providerId not found")
                    exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
                    mapOf("code" to 404, "message" to "Provider with ID $providerId not found")
                }
            } catch (e: Exception) {
                log.error("[DELETE] Exception: ${e.localizedMessage}")
                exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
                mapOf("code" to 500, "message" to "Unexpected error", "error" to e.localizedMessage)
            }
        }
    }
}
