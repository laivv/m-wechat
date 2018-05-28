import socket
import json
import pdb
import threading
import time

class TcpServer(object):

	def __init__(self):
		self.socket_list = []

	def start(self):
		s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)  
		s.bind(('127.0.0.1',5001))
		s.listen(10)
		print('%s-----TcpServer is running on port 5001'% (time.strftime("%Y-%m-%d %H:%M:%S",time.localtime(time.time()))))
		while True:
			conn,addr = s.accept()
			t = threading.Thread(target=TcpServer.tcp_process,args=(self,conn,addr))
			t.start()

		
	def send_msg(self,uid,content,from_user,msg_type='text'):
		conn = self.check_uid_is_online(uid)
		if conn:
			conn.send(json.dumps({'type':msg_type,'content':content,'from_user':from_user}))
		else:
			#对方未在线，写入数据库
			pass
	
	def send_group_msg(self,group_id,content,from_user,msg_type='text'):
		# 通过群组id获取群聊成员uid列表item_list
		uid_list = []
		for uid in uid_list:
			conn = self.check_uid_is_online(uid)
			if conn:
				conn.send(json.dumps({'type':msg_type,'content':content,'from_user':from_user}))
			else:
				#将未在线的成员消息写入数据库等待在线时发送
				pass

			
	def check_uid_is_online(self,uid):
		for item in self.socket_list:
			if item.get('uid') == uid:
				return item.get('socket')
		return False

	def keep_connect(self,uid,conn):
		if (not self.check_uid_is_online(uid)):
			self.socket_list.append({'socket':conn,'uid':uid})
			print('%s-----客户端建立连接！当前接入客户端数量：%s'% (time.strftime("%Y-%m-%d %H:%M:%S",time.localtime(time.time())),len(self.socket_list)))
		conn.send(json.dumps({'type':'connect'}).encode())

	def dis_connect_by_uid(self,uid,conn):
		for item in self.socket_list:
			if item.get('uid') == uid:
				# conn.send(json.dumps({'type':'disconnect'}))
				conn.close()
				self.socket_list.remove(item)
				return

	def dis_connect_by_socket(self,conn):
		for item in self.socket_list:
			if item.get('socket') == conn:
				# conn.send(json.dumps({'type':'disconnect'}))
				conn.close()
				self.socket_list.remove(item)
				print('%s-----客户端断开连接！当前接入客户端数量：%s,断开的客户端UID:%s'% (time.strftime("%Y-%m-%d %H:%M:%S",time.localtime(time.time())),len(self.socket_list),item.get('uid')))
				return
	
	def get_uid_by_socket(self,conn):
		for item in self.socket_list:
			if item.get('socket') == conn:
				return item.get('uid')
		return None

	
	def tcp_process(self,conn,addr):
		is_run = True
		while is_run :
			raw_str = conn.recv(1024).decode()
			
			try:
				message = json.loads(raw_str)
				if (not message):
					continue
				
				# print(message)
				uid = message.get('uid')
				msg_type = message.get('type')

				if msg_type == 'connect':
					self.keep_connect(uid,conn)

				elif msg_type == 'disconnect':
					self.dis_connect_by_socket(conn)
					
				else:
					content = message.get('content') or False
					
					if content:
						from_user = self.get_uid_by_socket(conn)
						self.send_msg(uid,content,from_user,msg_type)
						
						conn.send(json.dumps({'status':'ok'}).encode())
			except Exception as e:
				print(e)
				is_run = False
	
				self.dis_connect_by_socket(conn)


    	

server = TcpServer()
server.start()
