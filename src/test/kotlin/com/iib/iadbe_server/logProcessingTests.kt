package com.iib.iadbe_server

import com.iib.iadbe_server.model.RawLog
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
class LogProcessingTests {

    @Test
    fun readFile() {
        // Locate the file in the resources path
        val resource = ClassPathResource("rawlogs/train_test_kolektor_cflow.log")

        // Ensure the file exists
        assertTrue(resource.exists(), "File should exist")

        // Read the file content
        val fileContent = resource.inputStream.bufferedReader().use { it.readText() }

        // Print the file content (or perform other operations)
        println(fileContent)

        // Optional: Assert that the file content is not empty
        assertTrue(fileContent.isNotEmpty(), "File content should not be empty")
    }

    @Test
    fun retrieveField() {
        // Locate the log file in the test resources
        val resource = ClassPathResource("rawlogs/train_test_mvtec3d_cflow.log")

        // Ensure the file exists
        assertTrue(resource.exists(), "File should exist")

        // Read the file content
        val fileContent = resource.inputStream.bufferedReader().use { it.readText() }

        // Print file content for debugging
        println("File content:")
        println(fileContent)

        // Define regular expressions to extract the metrics
        val imageAUROCRegex = Regex("""image_AUROC\s*\│\s*([0-9]*\.?[0-9]+)""")
        val imagePRORegex = Regex("""image_PRO\s*\│\s*([0-9]*\.?[0-9]+)""")
        val pixelAUROCRegex = Regex("""pixel_AUROC\s*\│\s*([0-9]*\.?[0-9]+)""")
        val pixelPRORegex = Regex("""pixel_PRO\s*\│\s*([0-9]*\.?[0-9]+)""")

        // Extract all matches using regex
        val imageAUROCMatches = imageAUROCRegex.findAll(fileContent).toList()
        val imagePROMatches = imagePRORegex.findAll(fileContent).toList()
        val pixelAUROCMatches = pixelAUROCRegex.findAll(fileContent).toList()
        val pixelPROMatches = pixelPRORegex.findAll(fileContent).toList()

        // Print extracted values for debugging
        println("imageAUROCMatches: ${imageAUROCMatches.map { it.groups[1]?.value }}")
        println("imagePROMatches: ${imagePROMatches.map { it.groups[1]?.value }}")
        println("pixelAUROCMatches: ${pixelAUROCMatches.map { it.groups[1]?.value }}")
        println("pixelPROMatches: ${pixelPROMatches.map { it.groups[1]?.value }}")

        // Ensure that at least one value was found
        assertTrue(imageAUROCMatches.isNotEmpty(), "image_AUROC should be found in the log")
        assertTrue(imagePROMatches.isNotEmpty(), "image_PRO should be found in the log")
        assertTrue(pixelAUROCMatches.isNotEmpty(), "pixel_AUROC should be found in the log")
        assertTrue(pixelPROMatches.isNotEmpty(), "pixel_PRO should be found in the log")

        // Convert the matches to the appropriate data types and create RawLog instances
        val imageAUROCs = imageAUROCMatches.map { it.groups[1]?.value?.toDouble() ?: throw AssertionError("image_AUROC extraction failed") }
        val imagePROs = imagePROMatches.map { it.groups[1]?.value?.toDouble() ?: throw AssertionError("image_PRO extraction failed") }
        val pixelAUROCs = pixelAUROCMatches.map { it.groups[1]?.value?.toDouble() ?: throw AssertionError("pixel_AUROC extraction failed") }
        val pixelPROs = pixelPROMatches.map { it.groups[1]?.value?.toDouble() ?: throw AssertionError("pixel_PRO extraction failed") }

        // Print extracted values for debugging
        println("Extracted imageAUROCs: $imageAUROCs")
        println("Extracted imagePROs: $imagePROs")
        println("Extracted pixelAUROCs: $pixelAUROCs")
        println("Extracted pixelPROs: $pixelPROs")

        // Create instances of RawLog with the extracted values
        val rawLogs = imageAUROCs.mapIndexed { index, imageAUROC ->
            RawLog(
                imageAUROC = imageAUROC,
                imagePRO = imagePROs.getOrElse(index) { 0.0 },
                pixelAUROC = pixelAUROCs.getOrElse(index) { 0.0 },
                pixelPRO = pixelPROs.getOrElse(index) { 0.0 },
                trainingTime = "0" // Assuming default or placeholder for now
            )
        }

        // Perform assertions to check the values
        rawLogs.forEachIndexed { index, rawLog ->
            assertEquals(imageAUROCs.getOrElse(index) { 0.0 }, rawLog.imageAUROC, "image_AUROC at index $index does not match")
            assertEquals(imagePROs.getOrElse(index) { 0.0 }, rawLog.imagePRO, "image_PRO at index $index does not match")
            assertEquals(pixelAUROCs.getOrElse(index) { 0.0 }, rawLog.pixelAUROC, "pixel_AUROC at index $index does not match")
            assertEquals(pixelPROs.getOrElse(index) { 0.0 }, rawLog.pixelPRO, "pixel_PRO at index $index does not match")
        }
}

}
