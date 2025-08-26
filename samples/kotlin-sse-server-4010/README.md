# Kotlin SSE MCP Server on Port 4010

A simple Model Context Protocol (MCP) server implementation in Kotlin using Server-Sent Events (SSE) transport, configured to run on port 4010.

## Features

- **SSE Transport**: Uses Server-Sent Events for real-time communication
- **Port 4010**: Configured to run on port 4010 as requested
- **Tools**:
  - `echo`: Echo back input text with server identification
  - `current_time`: Get the current server timestamp
- **Resources**:
  - `server://info`: Server information and documentation

## Running the Server

### Prerequisites
- Java 17 or higher
- Gradle

### Build and Run

From the root of the kotlin-sdk repository:

```bash
# Build the server
./gradlew :kotlin-sse-server-4010:build

# Run the server
./gradlew :kotlin-sse-server-4010:run
```

Or run directly with Gradle from this directory:

```bash
./gradlew run
```

## Connecting to the Server

Once running, the server will be available at:
- **SSE Endpoint**: `http://localhost:4010/sse`
- **MCP Inspector**: Use any MCP-compatible inspector to connect

The server will display connection information when started:
```
Starting Kotlin MCP Server with SSE transport on port 4010...
Connect using MCP inspector at: http://localhost:4010/sse
```

## Usage Examples

### Testing with curl

You can test the SSE endpoint:

```bash
curl -N -H "Accept: text/event-stream" http://localhost:4010/sse
```

### Using MCP Inspector

1. Start the server: `./gradlew run`
2. Open MCP inspector in your browser
3. Connect to: `http://localhost:4010/sse`
4. Test the available tools and resources

## Implementation Details

This server demonstrates:
- How to create an MCP server using the official Kotlin SDK
- SSE transport configuration with Ktor
- Tool registration with input validation
- Resource handling for server information
- Proper server capabilities declaration

The implementation follows the MCP protocol specification and is compatible with standard MCP clients and inspectors.