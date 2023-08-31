package com.southwind.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 继承了ChannelHandlerAdapter主要是用于处理网络的读写事件
 *
 * @author 吕茂陈
 * @date 2022-10-13 16:41
 */
@Slf4j
public class ServerHandler1 extends ChannelInboundHandlerAdapter {


	/**
	 * 重写了channelRead读方法，在这个方法内部可以读取客户端的请求信息 客户端数据到来时触发
	 *
	 * @param ctx
	 * @param msg 封装的信息对象
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, StandardCharsets.UTF_8);
		log.info("客户端信息是：{}", body);
		ByteBuf resp = Unpooled.copiedBuffer("no".getBytes(StandardCharsets.UTF_8));
		ctx.writeAndFlush(resp)
			.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
	}


	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// 将发送缓冲区的消息全部写到SocketChannel中
		ctx.flush();
	}

	/**
	 * 发生异常时触发
	 *
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}
}
