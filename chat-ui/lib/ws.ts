let ws: WebSocket;

export const initializeWebSocket = () => {
  ws = new WebSocket("ws://localhost:3001");
  ws.onopen = () => {
    console.log("WebSocket connection established");
  };
  ws.addEventListener("message", (event) => {
    console.log("Message received: ", event.data);
  });
  ws.onclose = () => {
    console.log("WebSocket connection closed");
  };
};

export const sendMessage = (message: string) => {
  ws.send(message);
};

export const closeWebSocket = () => {
  ws.close();
};

export const getWebSocketInstance = (): WebSocket => {
  if (!ws || ws.readyState !== ws.OPEN) {
    throw new Error("WebSocket instance not initialized");
  }
  return ws;
};
