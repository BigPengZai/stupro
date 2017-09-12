//package com.onlyhiedu.mobile.Netty;
//
//import com.onlyhiedu.mobile.Netty.bean.LoginProto;
//import com.onlyhiedu.mobile.Utils.SPUtil;
//
//import java.net.InetSocketAddress;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioSocketChannel;
//
//public class TcpClient extends Thread {
//
//    public static String HOST = "192.168.1.219";
//    public static int PORT = 30000;
//
//    public void run() {
//        super.run();
//        EventLoopGroup group = new NioEventLoopGroup();
//        try {
//            Bootstrap bootstrap = new Bootstrap()
//                    .group(group)
//                    .channel(NioSocketChannel.class)
//                    .handler(new SimpleChatClientInitializer());
//            ChannelFuture connect = bootstrap.connect(new InetSocketAddress(
//                    HOST, PORT));
//            if(connect.isCancelled()){
//                Channel channel = connect.sync().channel();
//                LoginProto.Login build = LoginProto.Login.newBuilder().setPhone(SPUtil.getPhone())
//                        .setType(2).build();
//                channel.writeAndFlush(build);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}