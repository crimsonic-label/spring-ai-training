# The MCP inspector is a developer tool for testing and debugging*
https://github.com/modelcontextprotocol/inspector

1. Run McpRemoteServerApplication app (on 8090 port)

2. run inspector with command `npx @modelcontextprotocol/inspector`

3. to inspect server from jar provide settings:

   - transport type: SSE
   - url: http://localhost:8090/sse

4. connect
5. list tools
6. run createTicket tool and getTicketStatus tool
7. run mcp-client configured with 

   - application.properties:
   
     ```spring.ai.mcp.client.sse.connections.aidemo.url=http://localhost:8090```
    
   - remove `spring-ai-mcp` from `mcp-servers.json` because tools list is read from remote server