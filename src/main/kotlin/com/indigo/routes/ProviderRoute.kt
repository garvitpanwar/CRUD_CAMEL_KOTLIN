package com.indigo.routes

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
        // REST configuration
        restConfiguration()
            .component("platform-http")
            .bindingMode(RestBindingMode.json)

        // Define REST endpoints
        rest("/providers/v1")
    // POST /providers/v1 - Create a new provider
    .post()
        .consumes("application/json")
        .type(Provider::class.java)
        .to("bean:providerRoute?method=onCreate")

    // GET /providers/v1 - List all providers
    .get()
        .produces("application/json")
        .to("bean:providerRoute?method=onList")

    // GET /providers/v1/{id} - Get by ID
    .get("/{id}")
        .produces("application/json")
        .to("bean:providerRoute?method=onGetById(\${header.id})")

    // PUT /providers/v1/{id} - Update
    .put("/{id}")
        .consumes("application/json")
        .type(Provider::class.java)
        .to("bean:providerRoute?method=onUpdate")

    // DELETE /providers/v1/{id} - Delete
    .delete("/{id}")
        .produces("application/json")
        .to("bean:providerRoute?method=onDelete")


    }




    // GET all providers with JSON exception handling
fun onList(exchange: Exchange): Any {
    return try {
        val providers = providerService.listAll()
        exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
        mapOf(
            "code" to 200,
            "message" to "Fetched all providers successfully",
            "data" to providers
        )
    } catch (e: Exception) {
        exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
        mapOf(
            "code" to 500,
            "message" to "Unexpected error while fetching providers",
            "error" to e.localizedMessage
        )
    }
}

// GET provider by ID with JSON exception handling
fun onGetById(id: Long, exchange: Exchange): Any {
    return try {
        val provider = providerService.findById(id)
        if (provider != null) {
            exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
            mapOf(
                "code" to 200,
                "message" to "Provider fetched successfully",
                "data" to provider
            )
        } else {
            exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
            mapOf(
                "code" to 404,
                "message" to "Provider with ID $id not found"
            )
        }
    } catch (e: Exception) {
        exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
        mapOf(
            "code" to 500,
            "message" to "Unexpected error while fetching provider",
            "error" to e.localizedMessage
        )
    }
}







    fun onCreate(provider: Provider, exchange: Exchange): Any {
        log.info("[CREATE] Incoming provider: $provider")
        return try {
            providerService.create(provider)
            log.info("[CREATE] Provider created successfully: ${provider.name}")
            provider
        } catch (e: Exception) {
            if (e.cause?.message?.contains("unique_provider_name", ignoreCase = true) == true) {
                exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 409)
                log.warn("[CREATE] Duplicate provider name: ${provider.name}")
                "Provider with name '${provider.name}' already exists"
            } else {
                exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
                log.error("[CREATE] Unexpected error: ${e.localizedMessage}")
                "Unexpected error: ${e.localizedMessage}"
            }
        }
    }

    fun onUpdate(provider: Provider, exchange: Exchange): Any {
        val providerId = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
        log.info("[UPDATE] ID = $providerId | Payload = $provider")

        return if (providerId != null) {
            try {
                val updatedProvider = providerService.update(providerId, provider)
                if (updatedProvider != null) {
                    exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
                    log.info("[UPDATE] Provider updated: $updatedProvider")
                    mapOf("code" to 200, "message" to "Provider updated", "data" to updatedProvider)
                } else {
                    exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
                    log.warn("[UPDATE] Provider not found with ID: $providerId")
                    mapOf("code" to 404, "message" to "Provider with ID $providerId not found")
                }
            } catch (e: Exception) {
                exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
                log.error("[UPDATE] Exception: ${e.localizedMessage}")
                mapOf("code" to 500, "message" to "Unexpected error: ${e.localizedMessage}")
            }
        } else {
            exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 400)
            log.warn("[UPDATE] Invalid ID format")
            mapOf("code" to 400, "message" to "Invalid ID in path")
        }
    }

    fun onDelete(exchange: Exchange): Any {
        val providerId = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
        log.info("[DELETE] Request to delete provider with ID: $providerId")

        return if (providerId == null) {
            exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 400)
            log.warn("[DELETE] Invalid ID in path")
            mapOf("code" to 400, "message" to "Invalid ID in path")
        } else {
            try {
                val wasDeleted = providerService.delete(providerId)
                if (wasDeleted) {
                    exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
                    log.info("[DELETE] Provider deleted: ID = $providerId")
                    mapOf("code" to 200, "message" to "Provider deleted successfully", "data" to mapOf("id" to providerId))
                } else {
                    exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
                    log.warn("[DELETE] Provider not found with ID: $providerId")
                    mapOf("code" to 404, "message" to "Provider with ID $providerId not found")
                }
            } catch (e: Exception) {
                exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 500)
                log.error("[DELETE] Exception: ${e.localizedMessage}")
                mapOf("code" to 500, "message" to "Unexpected error: ${e.localizedMessage}")
            }
        }
    }

}
