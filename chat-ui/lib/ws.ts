let ws: WebSocket;

export const initializeWebSocket = (): Promise<void> => {
  return new Promise((resolve, reject) => {
    const BROKER_ADDRESS = process.env.NEXT_PUBLIC_BROKER_ADDRESS;
    const BROKER_PORT = process.env.NEXT_PUBLIC_BROKER_PORT;
    const url = `ws://${BROKER_ADDRESS}:${BROKER_PORT}`;
    ws = new WebSocket(url);

    ws.onopen = () => {
      resolve(); // Resolve the promise when the WebSocket is open
    };

    ws.onerror = (error) => {
      console.error("WebSocket error: ", error);
      reject(error); // Reject the promise if there's an error
    };
  });
};

export const sendMessage = (message: string) => {
  try {
    ws.send(message);
  } catch (error) {
    console.error("Error sending message: ", error);
  }
};

export const closeWebSocket = () => {
  ws.close();
};

export const isWebSocketInitialized = (): boolean => {
  return ws && ws.readyState === ws.OPEN;
};

export const getWebSocket = (): WebSocket => {
  return ws;
};
