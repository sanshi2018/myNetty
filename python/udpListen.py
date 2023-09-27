# 编写一个udp监听程序，并且将所有接收到的数据打印到标准输出，功能类似于netcat程序
#------------------------------------------------------------
import socket

def udp_listener():
    # Create a UDP socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    # Bind the socket to a specific IP address and port
    server_address = ('', 8080)  # Use an empty string to bind to all available interfaces
    sock.bind(server_address)

    print("UDP listener started on {}:{}".format(*server_address))

    while True:
        data, address = sock.recvfrom(4096)  # Receive data and the client's address
        print("Received data from {}: {}".format(address, data.decode()))

if __name__ == '__main__':
    udp_listener()
