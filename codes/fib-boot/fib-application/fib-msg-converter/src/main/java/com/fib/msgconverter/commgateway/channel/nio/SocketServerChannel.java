package com.fib.msgconverter.commgateway.channel.nio;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.channel.nio.config.SocketChannelConfig;
import com.fib.msgconverter.commgateway.channel.nio.config.SocketServerChannelConfigParser;
import com.fib.msgconverter.commgateway.channel.nio.config.database.SocketChannelConfigLoader;
import com.fib.msgconverter.commgateway.channel.nio.reader.AbstractNioReader;
import com.fib.msgconverter.commgateway.channel.nio.writer.AbstractNioWriter;
import com.fib.msgconverter.commgateway.session.Session;
import com.fib.msgconverter.commgateway.session.SessionManager;
import com.fib.msgconverter.commgateway.util.SensitiveInfoFilter;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ClassUtil;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;

/**
 * Socket服务器通道(nio)
 * 
 * @author 刘恭亮
 * 
 */
public class SocketServerChannel extends Channel {
    public static final int READ_TIME_OUT = 30000;

    protected SocketChannelConfig config;

    protected NioServerThread executor;

    public void sendRequestMessage(byte[] requestMessage, boolean sync,
            int timeout) {
        throw new RuntimeException("SocketServerChannel [" + mainConfig.getId()
                + "] can't send request message!");
    }

    public void sendResponseMessage(byte[] responseMessage) {
        // 1. 取得Session，从中获得源SocketChannel
        Session session = SessionManager.getSession(responseMessage);
        if( null == session){
            // 超时关闭
            return;
        }
        SocketChannel channel = (SocketChannel) session.getSource();

        // 2. 通过源SocketChannel发送应答
        executor.send(channel, responseMessage);
    }

    public void closeSource(Object source) {
        SocketChannel channel = (SocketChannel) source;
        try {
            channel.close();
        } catch (IOException e) {
            // e.printStackTrace();
            logger.error(e);
        }
        // 假装发送，唤醒selector，自动cancel掉key
        // executor.send(channel, null);
    }

    public void shutdown() {
        executor.close();
    }

    public void start() {
        executor = new NioServerThread();
        executor.setListenPort(config.getConnectionConfig().getPort());
        executor.setBacklog(config.getConnectionConfig().getBacklog());
        executor.setCommBufferSize(config.getConnectionConfig()
                .getCommBufferSize());
        // reader
        AbstractNioReader reader = (AbstractNioReader) ClassUtil
                .createClassInstance(config.getReaderConfig().getClassName());
        reader.setLogger(logger);
        reader.setParameters(config.getReaderConfig().getParameters());
        reader.setFilterList(config.getReaderConfig().createFilterList());
        executor.setReaderSeed(reader);
        // writer
        AbstractNioWriter writer = (AbstractNioWriter) ClassUtil
                .createClassInstance(config.getWriterConfig().getClassName());
        writer.setLogger(logger);
        writer.setParameters(config.getWriterConfig().getParameters());
        writer.setFilterList(config.getWriterConfig().createFilterList());
        executor.setWriterSeed(writer);
        // start server
        executor.startServer();
        // run
        executor.start();
    }

    public void loadConfig(InputStream in) {
        if (CommGateway.isConfigDBSupport()) {
            SocketChannelConfigLoader loader = new SocketChannelConfigLoader();
            config = loader.loadConfig(channelConfig.getConnectorId());
        } else {
            SocketServerChannelConfigParser parser = new SocketServerChannelConfigParser();
            config = parser.parse(in);
        }
    }

    /**
     * Nio服务器线程
     * 
     * @author 刘恭亮
     * 
     */
    private class NioServerThread extends Thread {
        private Selector selector = null;
        private ServerSocketChannel server = null;
        private ByteBuffer commBuffer = null;

        private int listenPort = 8888;
        private int backlog = 10;
        private int commBufferSize = 8192;

        private List writeQueue = new ArrayList(512);

        // Reader种子，所有读操作的Reader从它克隆
        private AbstractNioReader readerSeed;
        // Writer种子，所有写操作的Writer从它克隆
        private AbstractNioWriter writerSeed;

        private boolean run = true;

        public void run() {
            // 开始服务
            int keyNum = 0;
            Iterator it = null;
            SelectionKey key = null;
            try {
                while (run) {
                    // 将其他线程待注册的key加入
                    addWriteKey();

                    // select
                    try {
                        // *************************
                        // 当另一个线程register时，如果无新的i/o到达，Selector所在线程会在select()上无限堵塞
                        // ，
                        // 造成另一线程register堵塞。
                        // 改为select(10)可避免此问题。
                        // 最佳方案是 改为由select线程来register

                        keyNum = selector.select(10);
                    } catch (Exception e) {
                        // e.printStackTrace();
                        if (selector.isOpen()) {
                            // logger.error("Selector.select() exception!", e);
                            logger
                                    .error(
                                            MultiLanguageResourceBundle
                                                    .getInstance()
                                                    .getString(
                                                            "SocketChannel.selector.select.error"),
                                            e);
                            continue;
                        } else {
                            // logger.fatal("Selector crashed!", e);
                            logger.fatal(MultiLanguageResourceBundle
                                    .getInstance().getString(
                                            "SocketChannel.selector.crashed"),
                                    e);
                            onFatalException(selector, e);
                            break;
                        }
                    }

                    if (keyNum == 0) {
                        continue;
                    }

                    it = selector.selectedKeys().iterator();
                    while (it.hasNext()) {
                        key = (SelectionKey) it.next();
                        it.remove();
                        if (!key.isValid()) {
                            // logger.error("key[" + key.toString()
                            // + "] is invalid");
                            logger.error(MultiLanguageResourceBundle
                                    .getInstance().getString(
                                            "SocketChannel.key.invalid",
                                            new String[] { key.toString() }));
                            continue;
                        }
                        try {
                            handleKey(key);
                        } catch (CancelledKeyException e) {
                            // e.printStackTrace();
                            logger
                                    .error(
                                            MultiLanguageResourceBundle
                                                    .getInstance()
                                                    .getString(
                                                            "SocketChannel.handleKey.error"),
                                            e);
                        } catch (Exception e) {
                            // e.printStackTrace();
                            // logger.fatal("handleKey exception!", e);
                            logger
                                    .fatal(
                                            MultiLanguageResourceBundle
                                                    .getInstance()
                                                    .getString(
                                                            "SocketChannel.handleKey.error"),
                                            e);
                            throw e;
                        }
                    }

                }
            } catch (Exception e) {
                // logger.fatal("Channel crashed!", e);
                logger.fatal(MultiLanguageResourceBundle.getInstance()
                        .getString("SocketChannel.channel.crashed"), e);
                onFatalException(this, e);
            } finally {
                close();
            }
        }

        public void startServer() {
            // 1. selector
            try {
                selector = Selector.open();
            } catch (IOException e) {
                // e.printStackTrace();
                // logger.error("Open Selector failed!", e);
                logger.error(MultiLanguageResourceBundle.getInstance()
                        .getString("SocketChannel.selector.open.error"), e);
                close();
                ExceptionUtil.throwActualException(e);
            }

            // 2. 初始化通讯缓冲
            commBuffer = ByteBuffer.allocateDirect(commBufferSize);

            // 3. 启动监听
            try {
                server = ServerSocketChannel.open();
                // 配置非堵塞模式
                server.configureBlocking(false);
                // 绑定监听
                ServerSocket serverSocket = server.socket();
                // serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(listenPort), backlog);

                // 注册Accept
                server.register(selector, SelectionKey.OP_ACCEPT);
            } catch (IOException e) {
                // e.printStackTrace();
                // logger.error("Open ServerSocket failed!", e);
                logger
                        .error(
                                MultiLanguageResourceBundle
                                        .getInstance()
                                        .getString(
                                                "SocketServerChannel.handleKey.serverSocket.open.failed"),
                                e);
                close();
                ExceptionUtil.throwActualException(e);
            }

        }

        private void handleKey(SelectionKey key) {
            SocketChannel channel = null;
            if (key.isAcceptable()) {
                if (!key.isValid()) {
                    // throw new RuntimeException(
                    // "ServerSocketChannel is invalid!");
                    throw new RuntimeException(MultiLanguageResourceBundle
                            .getInstance().getString(
                                    "SocketServerChannel.handleKey.invalid"));
                }
                // ServerSocketChannel server = (ServerSocketChannel)
                // key.channel();
                try {
                    channel = server.accept();
                } catch (IOException e) {
                    // e.printStackTrace();
                    // logger.error("accept error :", e);
                    logger
                            .error(
                                    MultiLanguageResourceBundle
                                            .getInstance()
                                            .getString(
                                                    "SocketServerChannel.handleKey.accept.error"),
                                    e);
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (IOException e1) {
                            // e1.printStackTrace();
                        }
                    }
                    if (selector.isOpen() && server.isOpen()) {
                        onAcceptException(server, e);
                        return;
                    } else {
                        onFatalException(server, e);
                        // throw new RuntimeException("accept error :"
                        // + e.getMessage(), e);
                        return;
                    }
                }

                Object source = channel.toString();
                try {
                    channel.configureBlocking(false);

                    // 设置读超时
                    // channel.socket().setSoTimeout(READ_TIME_OUT);
                    channel.register(selector, SelectionKey.OP_READ, readerSeed
                            .clone());
                } catch (IOException e) {
                    // e.printStackTrace();
                    // logger.error("register channel error :", e);
                    logger
                            .error(
                                    MultiLanguageResourceBundle
                                            .getInstance()
                                            .getString(
                                                    "SocketServerChannel.handleKey.register.error"),
                                    e);
                    try {
                        channel.close();
                    } catch (IOException e1) {
                        // e1.printStackTrace();
                    }
                    onRequestMessageReceiveException(source, e);
                    return;
                }

                try {
                    // 设置读缓冲大小
                    channel.socket().setReceiveBufferSize(commBufferSize);
                } catch (SocketException e1) {
                    // e1.printStackTrace();
                    logger.error(MultiLanguageResourceBundle.getInstance()
                            .getString("Socket.setReceiveBufferSize.error"));
                }

                try {
                    // 设置写缓冲大小
                    channel.socket().setSendBufferSize(commBufferSize);
                } catch (SocketException e1) {
                    // e1.printStackTrace();
                    logger.error(MultiLanguageResourceBundle.getInstance()
                            .getString("Socket.setSendBufferSize.error"));
                }

            } else if (key.isReadable()) {
                if (!key.isValid()) {
                    key.cancel();
                    return;
                }
                channel = (SocketChannel) key.channel();
                AbstractNioReader reader = (AbstractNioReader) key.attachment();
                boolean complete = false;
                try {
                    complete = reader.read(channel, commBuffer);
          
                } catch (Exception e) {
                    // System.out.println("接收失败：" + e.getMessage());
                    // e.printStackTrace();
                    // logger.error("reader.read failed!", e);
                    logger.error(MultiLanguageResourceBundle.getInstance()
                            .getString("SocketChannel.read.failed",
                                    new String[] { channel.toString() }), e);
                    try {
                        channel.close();
                    } catch (IOException e1) {
                        // e1.printStackTrace();
                    }
                    onRequestMessageReceiveException(channel, e);
                    return;
                }
                if (complete) {
                    // logger
                    // .info("receive request message :\n"
                    // + CodeUtil.Bytes2FormattedText(reader
                    // .getMessage()));
                    if (null != logger && logger.isInfoEnabled()) {
                        byte[] msg4Log = null;
                        if (CommGateway.isShieldSensitiveFields()) {
                            msg4Log = SensitiveInfoFilter.filtSensitiveInfo(
                                    new String(reader.getMessage()),
                                    CommGateway.getSensitiveFields(),
                                    CommGateway.getSensitiveReplaceChar())
                                    .getBytes();
                        } else {
                            msg4Log = reader.getMessage();
                        }
                        logger
                                .info(MultiLanguageResourceBundle
                                        .getInstance()
                                        .getString(
                                                "SocketServerChannel.handleKey.receiveRequest.success.1",
                                                new String[] {
                                                        channel.toString(),
                                                        CodeUtil
                                                                .Bytes2FormattedText(msg4Log) }));
                    }
                    // 读完报文后，取消该channel“可读状态”的select，产生请求到达事件
                    // key.cancel();
                    // selector.wakeup();
                    key.interestOps(0);

                    // 处理请求消息
                    onRequestMessageArrived(channel, reader.getMessage());
                }

            } else if (key.isWritable()) {
                if (!key.isValid()) {
                    key.cancel();
                    return;
                }
                channel = (SocketChannel) key.channel();
                AbstractNioWriter writer = (AbstractNioWriter) key.attachment();
                boolean complete = false;
                try {
                    complete = writer.write(channel, commBuffer);
                } catch (Exception excp) {
                    // System.out.println("发送失败：" + excp.getMessage());
                    // excp.printStackTrace();
                    // logger.error("writer.write failed!", excp);
                    logger.error(MultiLanguageResourceBundle.getInstance()
                            .getString("SocketChannel.write.failed"), excp);
                    try {
                        channel.close();
                    } catch (IOException e) {
                        // e.printStackTrace();
                    }
                    onResponseMessageSendException(channel,
                            writer.getMessage(), excp);
                    return;
                }
                if (complete) {
                    // logger
                    // .info("send response message :\n"
                    // + CodeUtil.Bytes2FormattedText(writer
                    // .getMessage()));
                    if (null != logger && logger.isInfoEnabled()) {
                        byte[] msg4Log = null;
                        if (CommGateway.isShieldSensitiveFields()) {
                            msg4Log = SensitiveInfoFilter.filtSensitiveInfo(
                                    new String(writer.getMessage()),
                                    CommGateway.getSensitiveFields(),
                                    CommGateway.getSensitiveReplaceChar())
                                    .getBytes();
                        } else {
                            msg4Log = writer.getMessage();
                        }
                        logger
                                .info(MultiLanguageResourceBundle
                                        .getInstance()
                                        .getString(
                                                "SocketServerChannel.handleKey.sendResponse.success",
                                                new String[] { CodeUtil
                                                        .Bytes2FormattedText(msg4Log) }));
                    }
                    // 发送完毕，取消并取消该channel的轮询
                    key.cancel();
                    try {
                        channel.close();
                    } catch (IOException e) {
                        // e.printStackTrace();
                    }
                    // 产生应答发送完毕事件
                    onResponseMessageSent(channel, writer.getOriginalMessage());
                }

            }

        }

        private void addWriteKey() {
            List tmp = null;
            synchronized (writeQueue) {
                if (writeQueue.size() == 0) {
                    return;
                }

                tmp = new ArrayList(writeQueue.size());
                tmp.addAll(writeQueue);
                writeQueue.clear();
            }

            Iterator it = tmp.iterator();
            WriteRequest wr = null;
            while (it.hasNext()) {
                wr = (WriteRequest) it.next();
                AbstractNioWriter w = (AbstractNioWriter) writerSeed.clone();
                w.setMessage(wr.message);
                try {
                    wr.channel.register(selector, SelectionKey.OP_WRITE, w);
                } catch (Exception e) {
                    // e.printStackTrace();
                    logger.error(e);
                    try {
                        wr.channel.close();
                    } catch (IOException e1) {
                        // e1.printStackTrace();
                    }
                    onResponseMessageSendException(wr.channel, wr.message, e);
                }
            }
        }

        private class WriteRequest {
            SocketChannel channel;
            byte[] message;
        }

        /**
         * 发送消息
         * 
         * @param channel
         * @param message
         */
        public void send(SocketChannel channel, byte[] message) {
            WriteRequest wr = new WriteRequest();
            wr.channel = channel;
            wr.message = message;
            synchronized (writeQueue) {
                writeQueue.add(wr);
            }
            selector.wakeup();
        }

        public void close() {
            run = false;

            if (selector != null && selector.isOpen()) {
                try {
                    selector.close();
                } catch (IOException e) {
                    // e.printStackTrace();
                }
                selector = null;
            }

            if (server != null && server.isOpen()) {
                try {
                    server.close();
                } catch (IOException e) {
                    // e.printStackTrace();
                }
                server = null;
            }
        }

        /**
         * @return the commBuffer
         */
        public ByteBuffer getCommBuffer() {
            return commBuffer;
        }

        /**
         * @param commBuffer
         *            the commBuffer to set
         */
        public void setCommBuffer(ByteBuffer commBuffer) {
            this.commBuffer = commBuffer;
        }

        /**
         * @return the listenPort
         */
        public int getListenPort() {
            return listenPort;
        }

        /**
         * @param listenPort
         *            the listenPort to set
         */
        public void setListenPort(int listenPort) {
            this.listenPort = listenPort;
        }

        /**
         * @return the backlog
         */
        public int getBacklog() {
            return backlog;
        }

        /**
         * @param backlog
         *            the backlog to set
         */
        public void setBacklog(int backlog) {
            this.backlog = backlog;
        }

        /**
         * @return the commBufferSize
         */
        public int getCommBufferSize() {
            return commBufferSize;
        }

        /**
         * @param commBufferSize
         *            the commBufferSize to set
         */
        public void setCommBufferSize(int commBufferSize) {
            this.commBufferSize = commBufferSize;
        }

        /**
         * @return the readerSeed
         */
        public AbstractNioReader getReaderSeed() {
            return readerSeed;
        }

        /**
         * @param readerSeed
         *            the readerSeed to set
         */
        public void setReaderSeed(AbstractNioReader readerSeed) {
            this.readerSeed = readerSeed;
        }

        /**
         * @return the writerSeed
         */
        public AbstractNioWriter getWriterSeed() {
            return writerSeed;
        }

        /**
         * @param writerSeed
         *            the writerSeed to set
         */
        public void setWriterSeed(AbstractNioWriter writerSeed) {
            this.writerSeed = writerSeed;
        }

        /**
         * @return the run
         */
        public boolean isRun() {
            return run;
        }

        /**
         * @param run
         *            the run to set
         */
        public void setRun(boolean run) {
            this.run = run;
        }
    }

    public SocketChannelConfig getConnectorConfig() {
        return config;
    }

    public void setConnectorConfig(SocketChannelConfig config) {
        this.config = config;
    }

}
