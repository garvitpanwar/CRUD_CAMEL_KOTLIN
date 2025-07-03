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








// package com.indigo.routes

// import io.quarkus.test.junit.QuarkusTest
// import io.restassured.RestAssured
// import io.restassured.http.ContentType
// import org.hamcrest.Matchers.*
// import org.junit.jupiter.api.Test

// @QuarkusTest
// class ProviderRouteTest {

//     @Test
//     fun testGetAllProviders_ShouldReturn200or500() {
//         RestAssured.given()
//             .get("/providers/v1")
//             .then()
//             .statusCode(anyOf(equalTo(200), equalTo(500)))
//             .body("code", anyOf(equalTo(200), equalTo(500)))
//             .body("data", notNullValue())
//     }

//     @Test
//     fun testGetProviderByInvalidId_ShouldReturn200withCode404or500() {
//         val invalidId = 999999
//         RestAssured.given()
//             .get("/providers/v1/$invalidId")
//             .then()
//             .statusCode(anyOf(equalTo(200), equalTo(500)))
//             .body("code", anyOf(equalTo(404), equalTo(500)))
//     }

//     @Test
//     fun testGetProviderByValidId_AfterCreation_ShouldReturn200or500() {
//         val uniqueName = "ProviderForFetch_${System.currentTimeMillis()}"
//         val provider = mapOf(
//             "name" to uniqueName,
//             "logoUrl" to "https://example.com/logo-fetch.png",
//             "status" to "ACTIVE"
//         )

//         // Create provider
//         val response = RestAssured.given()
//             .contentType(ContentType.JSON)
//             .body(provider)
//             .post("/providers/v1")
//             .then()
//             .statusCode(anyOf(equalTo(201), equalTo(500)))
//             .extract()

//         val id = response.path<Int>("data.id")

//         if (id != null) {
//             // Fetch by ID
//             RestAssured.given()
//                 .get("/providers/v1/$id")
//                 .then()
//                 .statusCode(anyOf(equalTo(200), equalTo(500)))
//                 .body("code", anyOf(equalTo(200), equalTo(500)))
//                 .body("data.id", equalTo(id))
//                 .body("data.name", equalTo(uniqueName))
//         }
//     }

//     @Test
//     fun testUpdateProvider_ShouldReturn200or404or500() {
//         val uniqueName = "ProviderUpdate_${System.currentTimeMillis()}"
//         val provider = mapOf(
//             "name" to uniqueName,
//             "logoUrl" to "https://example.com/logo-update.png",
//             "status" to "ACTIVE"
//         )

//         // Create
//         val response = RestAssured.given()
//             .contentType(ContentType.JSON)
//             .body(provider)
//             .post("/providers/v1")
//             .then()
//             .statusCode(anyOf(equalTo(201), equalTo(500)))
//             .extract()

//         val id = response.path<Int>("data.id")

//         val updatedProvider = mapOf(
//             "name" to "${uniqueName}_Updated",
//             "logoUrl" to "https://example.com/logo-updated.png",
//             "status" to "INACTIVE"
//         )

//         if (id != null) {
//             // Update
//             RestAssured.given()
//                 .contentType(ContentType.JSON)
//                 .body(updatedProvider)
//                 .put("/providers/v1/$id")
//                 .then()
//                 .statusCode(anyOf(equalTo(200), equalTo(500)))
//                 .body("code", anyOf(equalTo(200), equalTo(404), equalTo(500)))
//         }
//     }
    

//     @Test
//     fun testDeleteProvider_ShouldReturn200or404or500() {
//         val uniqueName = "ProviderDelete_${System.currentTimeMillis()}"
//         val provider = mapOf(
//             "name" to uniqueName,
//             "logoUrl" to "https://example.com/logo-delete.png",
//             "status" to "ACTIVE"
//         )

//         // Create
//         val response = RestAssured.given()
//             .contentType(ContentType.JSON)
//             .body(provider)
//             .post("/providers/v1")
//             .then()
//             .statusCode(anyOf(equalTo(201), equalTo(500)))
//             .extract()

//         val id = response.path<Int>("data.id")

//         if (id != null) {
//             // Delete
//             RestAssured.given()
//                 .delete("/providers/v1/$id")
//                 .then()
//                 .statusCode(anyOf(equalTo(200), equalTo(500)))
//                 .body("code", anyOf(equalTo(200), equalTo(404), equalTo(500)))
//         }
//     }
// }
