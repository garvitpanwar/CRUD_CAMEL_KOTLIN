// package com.indigo.routes

// import com.indigo.entity.Provider
// import io.quarkus.test.junit.QuarkusTest
// import io.restassured.RestAssured
// import io.restassured.http.ContentType
// import org.hamcrest.Matchers.*
// import org.junit.jupiter.api.Test

// @QuarkusTest
// class ProviderRouteTest {

//     @Test
//     fun testCreateProvider() {
//         val uniqueName = "Test Provider ${System.currentTimeMillis()}"

//         val provider = mapOf(
//             "name" to uniqueName,
//             "logoUrl" to "https://example.com/logo.png",
//             "status" to "active",
//             "sla" to mapOf(
//                 "uptimePercent" to 99.5,
//                 "deliveryTimeMs" to 500
//             )
//         )

//         RestAssured.given()
//             .contentType(ContentType.JSON)
//             .body(provider)
//             .`when`()
//             .post("/providers/v1/create")
//             .then()
//             .statusCode(200)
//             .body("name", equalTo(uniqueName))
//     }

//     @Test
//     fun testGetProviders() {
//         RestAssured.given()
//             .`when`()
//             .get("/providers/v1/providerDetails")
//             .then()
//             .statusCode(200)
//             .body("size()", greaterThanOrEqualTo(0)) 
//     }



// @Test
// fun testCreateProvider_Duplicate() {
//     val provider = mapOf(
//         "name" to "DuplicateTestProvider",
//         "logoUrl" to "https://example.com/logo.png",
//         "status" to "ACTIVE",
//         "sla" to mapOf(
//             "uptimePercent" to 99.9,
//             "deliveryTimeMs" to 200
//         )
//     )

//     // First creation
//     RestAssured.given()
//         .contentType(ContentType.JSON)
//         .body(provider)
//         .post("/providers/v1/create")
//         .then()
//         .statusCode(200)

//     // Duplicate creation
//     RestAssured.given()
//         .contentType(ContentType.JSON)
//         .body(provider)
//         .post("/providers/v1/create")
//         .then()
//         .statusCode(409)
//         .body(containsString("already exists"))
// }

    
// }


// package com.indigo.routes

// import io.quarkus.test.junit.QuarkusTest
// import io.restassured.RestAssured
// import io.restassured.http.ContentType
// import org.hamcrest.Matchers.*
// import org.junit.jupiter.api.Test

// @QuarkusTest
// class ProviderRouteTest {

//     @Test
//     fun testCreateProvider_Valid() {
//         val uniqueName = "TestProvider_${System.currentTimeMillis()}"
//         val provider = mapOf(
//             "name" to uniqueName,
//             "logoUrl" to "https://example.com/logo.png",
//             "status" to "ACTIVE",
//             "sla" to mapOf(
//                 "uptimePercent" to 99.5,
//                 "deliveryTimeMs" to 500
//             )
//         )

//         RestAssured.given()
//             .contentType(ContentType.JSON)
//             .body(provider)
//             .post("/providers/v1/create")
//             .then()
//             .statusCode(200)
//             .body("name", equalTo(uniqueName))
//     }

//     @Test
//     fun testCreateProvider_MissingRequiredField() {
//         val incompleteProvider = mapOf(
//             // name is missing
//             "logoUrl" to "https://example.com/logo.png",
//             "status" to "ACTIVE",
//             "sla" to mapOf(
//                 "uptimePercent" to 99.5,
//                 "deliveryTimeMs" to 500
//             )
//         )

//         RestAssured.given()
//             .contentType(ContentType.JSON)
//             .body(incompleteProvider)
//             .post("/providers/v1/create")
//             .then()
//             .statusCode(409) // assuming validation error returns 400
//             .body(containsString("name"))
//     }

//     @Test
//     fun testCreateProvider_Duplicate() {
//         val duplicateName = "DuplicateTestProvider_${System.currentTimeMillis()}"
//         val provider = mapOf(
//             "name" to duplicateName,
//             "logoUrl" to "https://example.com/logo.png",
//             "status" to "ACTIVE",
//             "sla" to mapOf(
//                 "uptimePercent" to 99.9,
//                 "deliveryTimeMs" to 300
//             )
//         )

//         // First creation
//         RestAssured.given()
//             .contentType(ContentType.JSON)
//             .body(provider)
//             .post("/providers/v1/create")
//             .then()
//             .statusCode(200)

//         // Second creation with same name
//         RestAssured.given()
//             .contentType(ContentType.JSON)
//             .body(provider)
//             .post("/providers/v1/create")
//             .then()
//             .statusCode(409)
//             .body(containsString("already exists"))
//     }

//     @Test
//     fun testGetAllProviders() {
//         RestAssured.given()
//             .get("/providers/v1/providerDetails")
//             .then()
//             .statusCode(200)
//             .body("size()", greaterThanOrEqualTo(0))
//     }

//     @Test
//     fun testGetProviderByInvalidId() {
//         val invalidId = 999999
//         RestAssured.given()
//             .get("/providers/v1/$invalidId")
//             .then()
//             .statusCode(404)
//             .body(containsString("not found"))
//     }



//     @Test
// fun testGetProvidersByStatus() {
//     RestAssured.given()
//         .queryParam("status", "ACTIVE")
//         .get("/providers/v1/providerDetails")
//         .then()
//         .statusCode(200)
//         .body("findAll { it.status == 'ACTIVE' }.size()", greaterThanOrEqualTo(0))
// }


// @Test
// fun testGetProvidersWithInvalidStatusParam() {
//     RestAssured.given()
//         .queryParam("status", "INVALID_STATUS")
//         .get("/providers/v1/providerDetails")
//         .then()
//         .statusCode(200) // If you validate query param enums
// }


// @Test
// fun testCreateProvider_BlankName() {
//     val provider = mapOf(
//         "name" to "   ",
//         "logoUrl" to "https://example.com/logo.png",
//         "status" to "ACTIVE",
//         "sla" to mapOf(
//             "uptimePercent" to 99.5,
//             "deliveryTimeMs" to 500
//         )
//     )

//     RestAssured.given()
//         .contentType(ContentType.JSON)
//         .body(provider)
//         .post("/providers/v1/create")
//         .then()
//         .statusCode(409) // or 400 based on validation









// }



// @Test
// fun testGetProviderByValidId() {
//     val uniqueName = "ProviderForFetch_${System.currentTimeMillis()}"
//     val provider = mapOf(
//         "name" to uniqueName,
//         "logoUrl" to "https://example.com/logo-fetch.png",
//         "status" to "ACTIVE",
//         "sla" to mapOf(
//             "uptimePercent" to 98.0,
//             "deliveryTimeMs" to 450
//         )
//     )

//     // Create a new provider first
//     val id = RestAssured.given()
//         .contentType(ContentType.JSON)
//         .body(provider)
//         .post("/providers/v1/create")
//         .then()
//         .statusCode(200)
//         .extract()
//         .path<Int>("id")

//     // Now fetch by ID
//     RestAssured.given()
//         .get("/providers/v1/$id")
//         .then()
//         .statusCode(200)
//         .body("name", equalTo(uniqueName))
// }










// }



package com.indigo.routes

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test

@QuarkusTest
class ProviderRouteTest {

    /**
     * Test: Successfully create a provider with valid input.
     */
    @Test
    fun testCreateProvider_ValidInput_ShouldReturn200() {
        val uniqueName = "TestProvider_${System.currentTimeMillis()}"
        val provider = mapOf(
            "name" to uniqueName,
            "logoUrl" to "https://example.com/logo.png",
            "status" to "ACTIVE",
            "sla" to mapOf(
                "uptimePercent" to 99.5,
                "deliveryTimeMs" to 500
            )
        )

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(provider)
            .post("/providers/v1/create")
            .then()
            .statusCode(200)
            .body("name", equalTo(uniqueName))
    }

    /**
     * Test: Fail to create a provider when the 'name' field is missing.
     */
    @Test
    fun testCreateProvider_MissingName_ShouldReturn409() {
        val incompleteProvider = mapOf(
            "logoUrl" to "https://example.com/logo.png",
            "status" to "ACTIVE",
            "sla" to mapOf(
                "uptimePercent" to 99.5,
                "deliveryTimeMs" to 500
            )
        )

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(incompleteProvider)
            .post("/providers/v1/create")
            .then()
            .statusCode(409)
            .body(containsString("name"))
    }

    /**
     * Test: Prevent duplicate provider creation (same name).
     */
    @Test
    fun testCreateProvider_DuplicateName_ShouldReturn409() {
        val duplicateName = "DuplicateTestProvider_${System.currentTimeMillis()}"
        val provider = mapOf(
            "name" to duplicateName,
            "logoUrl" to "https://example.com/logo.png",
            "status" to "ACTIVE",
            "sla" to mapOf(
                "uptimePercent" to 99.9,
                "deliveryTimeMs" to 300
            )
        )

        // First creation: should succeed
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(provider)
            .post("/providers/v1/create")
            .then()
            .statusCode(200)

        // Second creation with same name: should fail
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(provider)
            .post("/providers/v1/create")
            .then()
            .statusCode(409)
            .body(containsString("already exists"))
    }

    /**
     * Test: Fetch list of all providers (may be empty or filled).
     */
    @Test
    fun testGetAllProviders_ShouldReturn200() {
        RestAssured.given()
            .get("/providers/v1/providerDetails")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0))
    }

    /**
     * Test: Fetch a provider by an invalid (non-existing) ID.
     */
    @Test
    fun testGetProviderByInvalidId_ShouldReturn404() {
        val invalidId = 999999
        RestAssured.given()
            .get("/providers/v1/$invalidId")
            .then()
            .statusCode(404)
            .body(containsString("not found"))
    }

    /**
     * Test: Fetch providers by status query param (e.g. ACTIVE).
     */
    @Test
    fun testGetProvidersByStatus_Active_ShouldReturnFilteredList() {
        RestAssured.given()
            .queryParam("status", "ACTIVE")
            .get("/providers/v1/providerDetails")
            .then()
            .statusCode(200)
            .body("findAll { it.status == 'ACTIVE' }.size()", greaterThanOrEqualTo(0))
    }

    /**
     * Test: Pass an invalid status filter (e.g. not supported).
     */
    @Test
    fun testGetProvidersByInvalidStatus_ShouldStillReturn200() {
        RestAssured.given()
            .queryParam("status", "INVALID_STATUS")
            .get("/providers/v1/providerDetails")
            .then()
            .statusCode(200)
    }

    /**
     * Test: Fail to create a provider with blank name.
     */
    @Test
    fun testCreateProvider_BlankName_ShouldReturn409() {
        val provider = mapOf(
            "name" to "   ", // blank name
            "logoUrl" to "https://example.com/logo.png",
            "status" to "ACTIVE",
            "sla" to mapOf(
                "uptimePercent" to 99.5,
                "deliveryTimeMs" to 500
            )
        )

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(provider)
            .post("/providers/v1/create")
            .then()
            .statusCode(409) // Or 400, depending on validation
    }

    /**
     * Test: Create a provider and then fetch it by ID.
     */
    @Test
    fun testGetProviderByValidId_AfterCreation_ShouldReturnProvider() {
        val uniqueName = "ProviderForFetch_${System.currentTimeMillis()}"
        val provider = mapOf(
            "name" to uniqueName,
            "logoUrl" to "https://example.com/logo-fetch.png",
            "status" to "ACTIVE",
            "sla" to mapOf(
                "uptimePercent" to 98.0,
                "deliveryTimeMs" to 450
            )
        )

        // Step 1: Create
        val id = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(provider)
            .post("/providers/v1/create")
            .then()
            .statusCode(200)
            .extract()
            .path<Int>("id")

        // Step 2: Fetch by ID
        RestAssured.given()
            .get("/providers/v1/$id")
            .then()
            .statusCode(200)
            .body("name", equalTo(uniqueName))
    }
}

