package io.modelcontextprotocol.sample.server

import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.sse.SSE
import io.ktor.util.collections.ConcurrentMap
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.ReadResourceResult
import io.modelcontextprotocol.kotlin.sdk.ServerCapabilities
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.TextResourceContents
import io.modelcontextprotocol.kotlin.sdk.Tool
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.mcp
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import java.time.Instant

fun createMcpServer(): Server {
    val server = Server(
        Implementation(
            name = "kotlin-sse-server-4010",
            version = "1.0.0"
        ),
        ServerOptions(
            capabilities = ServerCapabilities(
                tools = ServerCapabilities.Tools(listChanged = true),
                resources = ServerCapabilities.Resources(subscribe = true, listChanged = true)
            )
        )
    )

    // Add echo tool
    server.addTool(
        name = "echo",
        description = "Echo back the input text with a prefix",
        inputSchema = Tool.Input(
            properties = buildJsonObject {
                putJsonObject("text") {
                    put("type", "string")
                    put("description", "Text to echo back")
                }
            },
            required = listOf("text")
        )
    ) { request ->
        val text = request.arguments["text"]?.jsonPrimitive?.content ?: "No text provided"
        CallToolResult(
            content = listOf(
                TextContent("Echo from SSE server on port 4010: $text")
            )
        )
    }

    // Add time tool
    server.addTool(
        name = "current_time",
        description = "Get the current server time",
        inputSchema = Tool.Input(
            properties = buildJsonObject {},
            required = emptyList()
        )
    ) { _ ->
        val currentTime = Instant.now().toString()
        CallToolResult(
            content = listOf(
                TextContent("Current server time: $currentTime")
            )
        )
    }

    // Add server info resource
    server.addResource(
        uri = "server://info",
        name = "Server Information",
        description = "Information about this SSE MCP server",
        mimeType = "text/plain"
    ) { _ ->
        ReadResourceResult(
            contents = listOf(
                TextResourceContents(
                    text = """
                        Kotlin MCP Server with SSE Transport
                        ====================================
                        
                        Server: kotlin-sse-server-4010
                        Version: 1.0.0
                        Port: 4010
                        Transport: Server-Sent Events (SSE)
                        
                        Available Tools:
                        - echo: Echo back input text
                        - current_time: Get current server time
                        
                        Available Resources:
                        - server://info: This server information
                        
                        Connect using MCP-compatible clients at:
                        http://localhost:4010/sse
                    """.trimIndent(),
                    uri = "server://info",
                    mimeType = "text/plain"
                )
            )
        )
    }

    return server
}

fun main(): Unit = runBlocking {
    println("Starting Kotlin MCP Server with SSE transport on port 4010...")
    println("Connect using MCP inspector at: http://localhost:4010/sse")
    
    embeddedServer(CIO, host = "0.0.0.0", port = 4010) {
        mcp {
            createMcpServer()
        }
    }.start(wait = true)
}