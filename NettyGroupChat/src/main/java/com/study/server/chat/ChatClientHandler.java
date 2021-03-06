package com.study.server.chat;

import java.util.Scanner;

import com.study.server.protocol.MessageObject;
import com.study.server.protocol.MessageStatus;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ChatClientHandler extends ChannelHandlerAdapter {
	private Channel client;
	
	public ChatClientHandler() {
		super();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.client = ctx.channel();
		//登录
		MessageObject message = new MessageObject(MessageStatus.LOGIN, System.currentTimeMillis(), "Derry");
		sendMsg(message);
		System.out.println("成功连接至服务器，已执行登录动作");
		session();
	}

	private void session() {
		new Thread() {
			
			@Override
			public void run() {
				System.out.println("Derry,你好，请在控制台输入消息内容");
				Scanner sc = new Scanner(System.in);
				MessageObject message = null;
				do{
					String content = sc.nextLine();
					if("exit".equals(content)){
						//logout
						message = new MessageObject(MessageStatus.LOGOUT, System.currentTimeMillis(), "Derry");
					}else{
						message = new MessageObject(MessageStatus.CHAT, System.currentTimeMillis(), "Derry", content);
					}
				} while(sendMsg(message));
				sc.close();
			}

		}.start();
	}

	private boolean sendMsg(MessageObject msg) {
		this.client.writeAndFlush(msg);
		System.out.println("消息发送成功，请继续输入：");
		return true;
	}
}
