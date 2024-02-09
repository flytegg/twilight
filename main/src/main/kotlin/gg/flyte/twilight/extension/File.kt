package gg.flyte.twilight.extension

import java.io.File
import java.net.URL
import java.nio.channels.Channels
import java.security.DigestInputStream
import java.security.MessageDigest

/**
 * Represents a file available at a remote URL. This class provides functionality to download the file,
 * calculate its hash (SHA-1), and delete the downloaded file.
 *
 * @property url The URL of the remote file.
 */
class RemoteFile(val url: String) {
    /**
     * The SHA-1 hash of the downloaded file.
     */
    val hash: String

    /**
     * Initializes the RemoteFile by downloading the file, calculating its hash, and deleting the file.
     * The hash is stored in the 'hash' property.
     */
    init {
        download().apply {
            hash = hash("SHA-1")
            delete()
        }
    }

    /**
     * Downloads the remote file and returns it as a File.
     *
     * @param to The local file path where the remote file will be saved.
     * @return The downloaded File.
     */
    private fun download(to: String = "tempRemoteFile"): File = File(to).apply {
        if (exists()) delete()
        Channels.newChannel(URL(url).openStream()).use { outputStream().channel.transferFrom(it, 0, Long.MAX_VALUE) }
        return this
    }
}

/**
 * Calculates the hash of the file's content using the specified message digest algorithm.
 *
 * @param algorithm The message digest algorithm to use for hashing. Defaults to "SHA-256" if not specified.
 * @return The hexadecimal representation of the hash.
 */
fun File.hash(algorithm: String = "SHA-256"): String {
    val digest = MessageDigest.getInstance(algorithm)

    // Create a DigestInputStream to read the file and update the digest
    DigestInputStream(inputStream(), digest).use {
        val buffer = ByteArray(8192)
        while (it.read(buffer) != -1) {
            // Reading the file and updating the digest
        }
    }

    // Convert the digest to its hexadecimal representation
    return bytesToHex(digest.digest())
}

/**
 * Converts a byte array to its hexadecimal representation.
 *
 * @param bytes The input byte array.
 * @return The hexadecimal representation of the byte array.
 */
fun bytesToHex(bytes: ByteArray): String {
    val hexCharacters = CharArray(bytes.size * 2)

    // Iterate over each byte in the array and convert it to hexadecimal
    for (i in bytes.indices) {
        val v = bytes[i].toInt() and 0xFF
        hexCharacters[i * 2] = "0123456789ABCDEF"[v ushr 4]
        hexCharacters[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
    }

    // Create a String from the array of hexadecimal characters
    return String(hexCharacters)
}
