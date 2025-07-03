
// package com.indigo.route

// import com.indigo.entity.Provider
// import com.indigo.service.ProviderService
// import com.fasterxml.jackson.databind.ObjectMapper
// import jakarta.enterprise.context.ApplicationScoped
// import jakarta.inject.Named
// import org.apache.camel.Exchange
// import org.apache.camel.builder.RouteBuilder
// import org.apache.camel.model.rest.RestBindingMode
// import org.hibernate.exception.ConstraintViolationException
// import jakarta.inject.Inject

// @ApplicationScoped
// @Named("providerRoute")
// class ProviderRoute(private val providerService: ProviderService) : RouteBuilder() {

//     @Inject
// lateinit var objectMapper: ObjectMapper


//     override fun configure() {

//         // REST Configuration
//         restConfiguration()
//         .apiContextPath("/openapi") // This is where your OpenAPI definition will be served
        
//         .apiProperty("api.title", "My Camel API")
//         .apiProperty("api.version", "1.0.0")

//             .component("platform-http")
//             .bindingMode(RestBindingMode.json)

//         // Global Exception Handlers
//         onException(ConstraintViolationException::class.java)
//             .handled(true)
//             .log("Unique constraint violation: \${exception.message}")
//             .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
//             .setBody { exchange ->
//                 mapOf(
//                     "code" to 400,
//                     "message" to "Duplicate entry",
//                     "error" to exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception::class.java)?.message
//                 )
//             }

//         onException(Exception::class.java)
//             .handled(true)
//             .log("Unhandled error: \${exception}")
//             .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
//             .setBody { exchange ->
//                 mapOf(
//                     "code" to 500,
//                     "message" to "Unexpected error",
//                     "error" to exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception::class.java)?.message
//                 )
//             }

//         // REST Endpoints
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
//             .bean(this, "onCreate")

//         from("direct:getAllProviders")
//             .log("GET /providers/v1 - List providers called")
//             .bean(this, "onList")

//         from("direct:getProviderById")
//             .log("GET /providers/v1/{id} - Get provider by ID called")
//             .bean(this, "onGetById").log("Response: \${body}");

//         from("direct:updateProvider")
//             .log("PUT /providers/v1/{id} - Update provider called")
//             .bean(this, "onUpdate")

//         from("direct:deleteProvider")
//             .log("DELETE /providers/v1/{id} - Delete provider called")
//             .bean(this, "onDelete")
//     }

//     fun onList(exchange: Exchange): Any {
//         log.info("[LIST] Fetching all providers")
//         val providers = providerService.listAll()
//         return mapOf("code" to 200, "message" to "Fetched all providers", "data" to providers)
//     }

//     fun onGetById(exchange: Exchange): Any {
//         val id = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
//         log.info("[GET BY ID] Fetching provider for ID = $id")
//          if (id != null) {
//             val provider = providerService.findById(id)
//             if (provider != null) {
//                 log.info("[GET BY ID] Provider found - ID: " + provider)
//                         val providerJson = objectMapper.convertValue(provider, Map::class.java)

//                 return mapOf("code" to 200, "message" to "Fetched provider", "data" to providerJson)
//             } else {
//                 log.warn("[GET BY ID] Provider with ID = $id not found")
//                 // exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
//                                         // val providerJson = objectMapper.convertValue(mapOf("code" to 404, "message" to "Provider with ID $id not found"), Map::class.java)

//                 // exchange.message.body = providerJson
//                                 // log.warn("providerJson: $providerJson")

// // return providerJson;
//                 return mapOf("code" to 404, "message" to "Provider with ID $id not found")
//                 // exchange.getIn().setBody(mapOf("code" to 404, "message" to "Provider with ID $id not found"))
//             }
//         } else {
//             exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 400)
//            return mapOf("code" to 400, "message" to "Invalid ID in path")
//         }
//     }

//     fun onCreate(exchange: Exchange): Any {
//         val provider = exchange.getIn().getBody(Provider::class.java)
//         log.info("[CREATE] Creating provider: ${provider.name}")
//         providerService.create(provider)
//         exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 201)
//         return mapOf("code" to 201, "message" to "Provider created", "data" to provider)
//     }

//     fun onUpdate(exchange: Exchange): Any {
//         val providerId = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
//         val provider = exchange.getIn().getBody(Provider::class.java)
//         log.info("[UPDATE] Updating provider ID: $providerId")
//         if (providerId != null) {
//             val updated = providerService.update(providerId, provider)
//             if (updated != null) {
//                                         // val providerJson = objectMapper.convertValue(updated, Map::class.java)

//                 return mapOf("code" to 200, "message" to "Provider updated", "data" to updated)
//             } else {
//                 // exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
//                 return mapOf("code" to 404, "message" to "Provider with ID $providerId not found")
//             }
//         } else {
//             // exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 400)
//             return mapOf("code" to 400, "message" to "Invalid ID in path")
//         }
//     }

//     fun onDelete(exchange: Exchange): Any {
//         val providerId = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
//         log.info("[DELETE] Deleting provider ID: $providerId")
//         if (providerId != null) {
//             val deleted = providerService.delete(providerId)
//             if (deleted) {
//                 return mapOf("code" to 200, "message" to "Provider deleted", "data" to mapOf("id" to providerId))
//             } else {
//                 // exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 404)
//                 return mapOf("code" to 404, "message" to "Provider with ID $providerId not found")
//             }
//         } else {
//             // exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 400)
//             return mapOf("code" to 400, "message" to "Invalid ID in path")
//         }
//     }
// }



package com.indigo.route

import com.indigo.entity.Provider
import com.indigo.dto.ChannelRequest
import com.indigo.service.ProviderService
import com.indigo.service.ProviderChannelService
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Named
import jakarta.inject.Inject
import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.rest.RestBindingMode
import org.hibernate.exception.ConstraintViolationException

@ApplicationScoped
@Named("providerRoute")
class ProviderRoute(private val providerService: ProviderService) : RouteBuilder() {

    @Inject
    lateinit var objectMapper: ObjectMapper

    @Inject
    lateinit var providerChannelService: ProviderChannelService

    override fun configure() {

        // REST Configuration
        restConfiguration()
            .apiContextPath("/openapi")
            .apiProperty("api.title", "My Camel API")
            .apiProperty("api.version", "1.0.0")
            .component("platform-http")
            .bindingMode(RestBindingMode.json)

        // Global Exception Handlers
        onException(ConstraintViolationException::class.java)
            .handled(true)
            .log("Unique constraint violation: \${exception.message}")
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
            .setBody { exchange ->
                mapOf(
                    "code" to 400,
                    "message" to "Duplicate entry",
                    "error" to exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception::class.java)?.message
                )
            }

        onException(Exception::class.java)
            .handled(true)
            .log("Unhandled error: \${exception}")
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
            .setBody { exchange ->
                mapOf(
                    "code" to 500,
                    "message" to "Unexpected error",
                    "error" to exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception::class.java)?.message
                )
            }

        // REST Endpoints
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
            .post("/{id}/channels")
                .consumes("application/json")
                .type(ChannelRequest::class.java)
                .to("direct:createProviderChannels")

        from("direct:createProvider")
            .log("POST /providers/v1 - Create provider called")
            .bean(this, "onCreate")

        from("direct:getAllProviders")
            .log("GET /providers/v1 - List providers called")
            .bean(this, "onList")

        from("direct:getProviderById")
            .log("GET /providers/v1/{id} - Get provider by ID called")
            .bean(this, "onGetById")
            .log("Response: \${body}")

        from("direct:updateProvider")
            .log("PUT /providers/v1/{id} - Update provider called")
            .bean(this, "onUpdate")

        from("direct:deleteProvider")
            .log("DELETE /providers/v1/{id} - Delete provider called")
            .bean(this, "onDelete")

        from("direct:createProviderChannels")
            .log("POST /providers/v1/{id}/channels - Create channels called")
            .bean(this, "onCreateChannels").marshal().json()
    }

    fun onList(exchange: Exchange): Any {
        log.info("[LIST] Fetching all providers")
        val providers = providerService.listAll()
        return mapOf("code" to 200, "message" to "Fetched all providers", "data" to providers)
    }

    fun onGetById(exchange: Exchange): Any {
        val id = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
        log.info("[GET BY ID] Fetching provider for ID = $id")
        return if (id != null) {
            val provider = providerService.findById(id)
            if (provider != null) {
                val providerJson = objectMapper.convertValue(provider, Map::class.java)
                mapOf("code" to 200, "message" to "Fetched provider", "data" to providerJson)
            } else {
                mapOf("code" to 404, "message" to "Provider with ID $id not found")
            }
        } else {
            exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 400)
            mapOf("code" to 400, "message" to "Invalid ID in path")
        }
    }

    fun onCreate(exchange: Exchange): Any {
        val provider = exchange.getIn().getBody(Provider::class.java)
        log.info("[CREATE] Creating provider: ${provider.name}")
        providerService.create(provider)
        exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 201)
        return mapOf("code" to 201, "message" to "Provider created", "data" to provider)
    }

    fun onUpdate(exchange: Exchange): Any {
        val providerId = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
        val provider = exchange.getIn().getBody(Provider::class.java)
        log.info("[UPDATE] Updating provider ID: $providerId")
        return if (providerId != null) {
            val updated = providerService.update(providerId, provider)
            if (updated != null) {
                mapOf("code" to 200, "message" to "Provider updated", "data" to updated)
            } else {
                mapOf("code" to 404, "message" to "Provider with ID $providerId not found")
            }
        } else {
            mapOf("code" to 400, "message" to "Invalid ID in path")
        }
    }

    fun onDelete(exchange: Exchange): Any {
        val providerId = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
        log.info("[DELETE] Deleting provider ID: $providerId")
        return if (providerId != null) {
            val deleted = providerService.delete(providerId)
            if (deleted) {
                mapOf("code" to 200, "message" to "Provider deleted", "data" to mapOf("id" to providerId))
            } else {
                mapOf("code" to 404, "message" to "Provider with ID $providerId not found")
            }
        } else {
            mapOf("code" to 400, "message" to "Invalid ID in path")
        }
    }

    fun onCreateChannels(exchange: Exchange): Any {
        val providerId = exchange.getIn().getHeader("id", String::class.java)?.toLongOrNull()
        val request = exchange.getIn().getBody(ChannelRequest::class.java)
        log.info("[CREATE CHANNELS] For provider ID: $providerId with request: $request")
        return if (providerId != null) {
            val channels = providerChannelService.createChannels(providerId, request)
            exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 200)
            exchange.getIn().setBody(mapOf("code" to 201, "message" to "Channels created", "data" to channels))
            //  mapOf("code" to 201, "message" to "Channels created", "data" to channels)
        } else {
            exchange.message.setHeader(Exchange.HTTP_RESPONSE_CODE, 400)
            exchange.getIn().setBody(mapOf("code" to 400, "message" to "Invalid provider ID in path"))
        //      mapOf("code" to 400, "message" to "Invalid provider ID in path")
        }
    }
}

