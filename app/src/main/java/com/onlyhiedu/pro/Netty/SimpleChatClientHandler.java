//package com.onlyhiedu.mobile.Netty;
//
//
//import android.util.Log;
//
//import com.onlyhiedu.mobile.Netty.bean.LoginProto;
//
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//
//public class SimpleChatClientHandler extends SimpleChannelInboundHandler<LoginProto.Login> {
//
//
//    @Override
//    protected void messageReceived(ChannelHandlerContext ctx, LoginProto.Login msg) throws Exception {
//        Log.d("socket", msg.getReply()+"");
//
//    }
//}