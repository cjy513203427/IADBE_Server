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
        val resource = ClassPathResource("rawlogs/train_test_kolektor_cfa.log")

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
        val resource = ClassPathResource("rawlogs/train_test_kolektor_cfa.log")

        // Ensure the file exists
        assertTrue(resource.exists(), "File should exist")

        // Read the file content
        val fileContent = resource.inputStream.bufferedReader().use { it.readText() }

        // Print file content for debugging
        println("File content:")
        println(fileContent)

        // Define regular expressions to extract the metrics
        val imageAUROCRegex = Regex("""image_AUROC:\s*(\d+\.\d+)""")
        val imagePRORegex = Regex("""image_PRO:\s*(\d+\.\d+)""")
        val pixelAUROCRegex = Regex("""pixel_AUROC:\s*(\d+\.\d+)""")
        val pixelPRORegex = Regex("""pixel_PRO:\s*(\d+\.\d+)""")

        // Extract values using regex
        val imageAUROCMatch = imageAUROCRegex.find(fileContent)
        val imagePROMatch = imagePRORegex.find(fileContent)
        val pixelAUROCMatch = pixelAUROCRegex.find(fileContent)
        val pixelPROMatch = pixelPRORegex.find(fileContent)

        // Ensure the extracted values are not null
        assertNotNull(imageAUROCMatch, "image_AUROC should be found in the log")
        assertNotNull(imagePROMatch, "image_PRO should be found in the log")
        assertNotNull(pixelAUROCMatch, "pixel_AUROC should be found in the log")
        assertNotNull(pixelPROMatch, "pixel_PRO should be found in the log")

        // Convert the matches to the appropriate data types
        val imageAUROC = imageAUROCMatch?.groups?.get(1)?.value?.toDouble() ?: throw AssertionError("image_AUROC extraction failed")
        val imagePRO = imagePROMatch?.groups?.get(1)?.value?.toDouble() ?: throw AssertionError("image_PRO extraction failed")
        val pixelAUROC = pixelAUROCMatch?.groups?.get(1)?.value?.toDouble() ?: throw AssertionError("pixel_AUROC extraction failed")
        val pixelPRO = pixelPROMatch?.groups?.get(1)?.value?.toDouble() ?: throw AssertionError("pixel_PRO extraction failed")

        // Create an instance of RawLog with the extracted values
        val rawLog = RawLog(
            imageAUROC = imageAUROC,
            imagePRO = imagePRO,
            pixelAUROC = pixelAUROC,
            pixelPRO = pixelPRO,
            trainingTime = "00"
        )

        // Perform assertions to check the values
        assertEquals(imageAUROC, rawLog.imageAUROC, "image_AUROC does not match")
        assertEquals(imagePRO, rawLog.imagePRO, "image_PRO does not match")
        assertEquals(pixelAUROC, rawLog.pixelAUROC, "pixel_AUROC does not match")
        assertEquals(pixelPRO, rawLog.pixelPRO, "pixel_PRO does not match")
    }
}
