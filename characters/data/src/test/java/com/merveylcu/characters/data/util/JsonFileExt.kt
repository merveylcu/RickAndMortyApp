package com.merveylcu.characters.data.util

import java.io.BufferedReader

fun jsonContent(jsonFile: String): String {
    val inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(jsonFile)
    val reader = BufferedReader(inputStream.reader())
    val content = StringBuilder()
    reader.use {
        var line = reader.readLine()
        while (line != null) {
            content.append(line)
            line = reader.readLine()
        }
    }
    return content.toString()
}
