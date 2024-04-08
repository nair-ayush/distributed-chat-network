let ws: WebSocket;

export const initializeWebSocket = () => {
  const BROKER_ADDRESS = process.env.NEXT_PUBLIC_BROKER_ADDRESS;
  const BROKER_PORT = process.env.NEXT_PUBLIC_BROKER_PORT;
  const url = `ws://${BROKER_ADDRESS}:${BROKER_PORT}`;
  console.log(url);
  ws = new WebSocket(url);
  ws.onopen = () => {
    console.log("WebSocket connection established");
  };
  ws.addEventListener("message", (event) => {
    console.log("Message received: ", event.data);
  });
  ws.onclose = () => {
    console.log("WebSocket connection closed");
  };
  ws.onerror = (error) => {
    console.error("WebSocket error: ", error);
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
