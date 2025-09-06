package code.traveler.test.space.java.sb.log.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/v1/log")
@Slf4j
public class LogController {

    private static final String logStr =
            "-- Failed to ping stream, streamId=PingStream-71, cause=UNAVAILABLE: io " + "exception\n"
                    + "io.grpc.StatusRuntimeException: UNAVAILABLE: io exception\n"
                    + "\tat io.grpc.Status.asRuntimeException(Status.java:535) ~[grpc-api-1.46.0.jar:1.46.0]\n"
                    + "\tat io.grpc.stub.ClientCalls$StreamObserverToCallListenerAdapter.onClose(ClientCalls.java:487) "
                    + "[grpc-stub-1.46.0.jar:1.46.0]\n"
                    + "\tat io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:562) [grpc-core-1.46.0"
                    + ".jar:1.0"
                    + ".3]\n"
                    + "\tat io.grpc.internal.ClientCallImpl.access$300(ClientCallImpl.java:70) [grpc-core-1.46.0"
                    + ".jar:1.0.3]\n"
                    + "\tat io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInternal"
                    + "(ClientCallImpl"
                    + ".java:743) [grpc-core-1.46.0.jar:1.0.3]\n"
                    + "\tat io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed.runInContext"
                    + "(ClientCallImpl.java:722) [grpc-core-1.46.0.jar:1.0.3]\n"
                    + "\tat io.grpc.internal.ContextRunnable.run(ContextRunnable.java:37) [grpc-core-1.46.0.jar:1.0"
                    + ".3]\n"
                    + "\tat io.grpc.internal.SerializingExecutor.run(SerializingExecutor.java:133) [grpc-core-1.46.0"
                    + ".jar:1.0"
                    + ".3]\n"
                    + "\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149) [?:1.8"
                    + ".0_172]\n"
                    + "\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624) [?:1.8"
                    + ".0_172]\n"
                    + "\tat java.lang.Thread.run(Thread.java:748) [?:1.8.0_172]\n"
                    + "Caused by: java.io.IOException: 远程主机强迫关闭了一个现有的连接。\n"
                    + "\tat sun.nio.ch.SocketDispatcher.read0(Native Method) ~[?:1.8.0_172]\n"
                    + "\tat sun.nio.ch.SocketDispatcher.read(SocketDispatcher.java:43) ~[?:1.8.0_172]\n"
                    + "\tat sun.nio.ch.IOUtil.readIntoNativeBuffer(IOUtil.java:223) ~[?:1.8.0_172]\n"
                    + "\tat sun.nio.ch.IOUtil.read(IOUtil.java:192) ~[?:1.8.0_172]\n"
                    + "\tat sun.nio.ch.SocketChannelImpl.read(SocketChannelImpl.java:380) ~[?:1.8.0_172]\n"
                    + "\tat io.netty.buffer.PooledByteBuf.setBytes(PooledByteBuf.java:253) ~[netty-buffer-4.1.58"
                    + ".Final.jar:4"
                    + ".1.58.Final]\n"
                    + "\tat io.netty.buffer.AbstractByteBuf.writeBytes(AbstractByteBuf.java:1134) ~[netty-buffer-4.1"
                    + ".58.Final"
                    + ".jar:4.1.58.Final]\n"
                    + "\tat io.netty.channel.socket.nio.NioSocketChannel.doReadBytes(NioSocketChannel.java:350) "
                    + "~[netty-transport-4.1.58.Final.jar:4.1.58.Final]\n"
                    + "\tat io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel"
                    + ".java:151) "
                    + "~[netty-transport-4.1.58.Final.jar:4.1.58.Final]\n"
                    + "\tat io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:719) "
                    + "~[netty-transport-4.1"
                    + ".58.Final.jar:4.1.58.Final]\n"
                    + "\tat io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:655) "
                    + "~[netty-transport-4.1.58.Final.jar:4.1.58.Final]\n"
                    + "\tat io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:581) "
                    + "~[netty-transport-4"
                    + ".1.58.Final.jar:4.1.58.Final]\n"
                    + "\tat io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493) ~[netty-transport-4.1.58"
                    + ".Final.jar:4"
                    + ".1.58.Final]\n"
                    + "\tat io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor"
                    + ".java:989) "
                    + "~[netty-common-4.1.58.Final.jar:4.1.58.Final]\n"
                    + "\tat io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) "
                    + "~[netty-common-4.1.58"
                    + ".Final.jar:4.1.58.Final]\n" + "\t... 3 more";

    public static final Integer threadNum = 100;
    ExecutorService executorService = Executors.newScheduledThreadPool(threadNum, new LogThreadFactory());

    @GetMapping("/logTest")
    public String logTest(@RequestParam("logDurationMillis")Integer logDurationMillis,
                          @RequestParam("logAmount")Integer logAmount,
                          @RequestParam("sleepTimeMillis")Integer sleepTimeMillis) {
        AtomicInteger logCountAtomicInteger = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();
        for(int i=0;i<threadNum;i++){
            executorService.execute(() -> {
                long spendTime=0;
                for(;spendTime<logDurationMillis&&logCountAtomicInteger.get()<logAmount;){
                    log.info(logStr);
                    Integer logCount = logCountAtomicInteger.incrementAndGet();
                    long endTime = System.currentTimeMillis();
                    spendTime = endTime-startTime;
                    log.info("打印logCount={}条日志花费{}毫秒",logCount,spendTime);
                    try {
                        Thread.sleep(sleepTimeMillis);
                    } catch (InterruptedException e) {
                        log.error("thread sleep exception:{}",e);
                    }

                }
            });
        }
        return "success";
    }

    class LogThreadFactory implements ThreadFactory {

        private static final String prefix = "log-task";
        private final AtomicInteger threadNumber = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            Thread logThread = new Thread(null, r, prefix + threadNumber.getAndIncrement());
            return logThread;
        }
    }

}
