package com.southwind.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author 吕茂陈
 * @date 2022-10-13 15:59
 */
@Slf4j
@Component
public class NettyServer {

    /**
     * 用于接收客户端的连接工作
     */
    private final EventLoopGroup bossGroup;
    /**
     * work线程组用于处理数据
     */
    private final EventLoopGroup workGroup;

    public NettyServer() {
        // NioEventLoopGroup是一个线程组，主从多线程模式
        this.bossGroup = new NioEventLoopGroup();
        this.workGroup = new NioEventLoopGroup();
    }


//    @PostConstruct
    public ChannelFuture start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 传入线程组，让其工作
        bootstrap.group(bossGroup, workGroup)
            // 异步 TCP 服务端
            .channel(NioServerSocketChannel.class)
//            .localAddress(new InetSocketAddress(8080))
            // 配置TCP参数，将其中一个参数backlog设置为1024，表明临时存放已完成三次握手的请求的队列的最大长度
            .option(ChannelOption.SO_BACKLOG, 1024)
            // 设置TCP长连接，一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .handler(new LoggingHandler(LogLevel.INFO))
            // 处理客户端的IO事件，
            .childHandler(new ChannelInitializer<SocketChannel>() {
                public void initChannel(SocketChannel sc) throws Exception {
                    sc.pipeline().addLast(new ServerHandler1());
                }
            });
        // 绑定端口，使用sync方法阻塞一直到绑定成功
        ChannelFuture channelFuture = bootstrap.bind(8080).sync();
        log.info("Netty 启动成功！");
        return channelFuture;
    }

    @PreDestroy
    public void close() {
        workGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("Netty 停止！");
    }


}
