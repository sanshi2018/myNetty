# 创建一个websocket客户端，链接http://localhost:8080/ws, 之后用户可以在命令行发送消息
#

# import asyncio
# import websockets

# async def hello():
#     uri = "ws://localhost:8080/ws"
#     async with websockets.connect(uri) as websocket:
#         while True:
#             msg = input(">> ")
#             await websocket.send(msg)
#             print(f">> {msg}")
#             msg = await websocket.recv()
#             print(f"<< {msg}")

# asyncio.get_event_loop().run_until_complete(hello())



import websocket

def on_message(ws, message):
    print("Received message:", message)

def on_error(ws, error):
    print("Error:", error)

def on_close(ws):
    print("WebSocket connection closed")

def on_open(ws):
    # 用户在命令行中输入消息并发送
    while True:
        user_input = input("请输入消息（输入'exit'退出）：")
        if user_input == 'exit':
            break
        ws.send(user_input)

if __name__ == "__main__":
    websocket.enableTrace(True)
    # 指定ws的编码格式为utf-8

    ws = websocket.WebSocketApp("ws://localhost:8080/ws",
                                on_message=on_message,
                                on_error=on_error,
                                on_close=on_close)
    
    ws.on_open = on_open
    ws.run_forever()