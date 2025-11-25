package api.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

/**
 * gRPC Client Manager - Singleton Pattern
 * Manages gRPC channel lifecycle and stub creation
 * Demonstrates Singleton Pattern for gRPC client management
 */
public class GrpcClientManager {

    private static GrpcClientManager instance;
    private ManagedChannel channel;
    private String host;
    private int port;

    private GrpcClientManager() {
        // Private constructor for Singleton
    }

    /**
     * Get singleton instance of GrpcClientManager
     * @return GrpcClientManager instance
     */
    public static synchronized GrpcClientManager getInstance() {
        if (instance == null) {
            instance = new GrpcClientManager();
        }
        return instance;
    }

    /**
     * Configure and create gRPC channel
     * @param host gRPC server host
     * @param port gRPC server port
     * @return this manager for chaining
     */
    public GrpcClientManager configure(String host, int port) {
        this.host = host;
        this.port = port;

        // Close existing channel if any
        if (channel != null && !channel.isShutdown()) {
            shutdown();
        }

        // Create new channel
        channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext() // For testing, use TLS in production
                .build();

        return this;
    }

    /**
     * Configure gRPC channel with SSL/TLS
     * @param host gRPC server host
     * @param port gRPC server port
     * @return this manager for chaining
     */
    public GrpcClientManager configureSecure(String host, int port) {
        this.host = host;
        this.port = port;

        // Close existing channel if any
        if (channel != null && !channel.isShutdown()) {
            shutdown();
        }

        // Create secure channel
        channel = ManagedChannelBuilder
                .forAddress(host, port)
                .useTransportSecurity() // Enable TLS
                .build();

        return this;
    }

    /**
     * Get the managed channel
     * @return ManagedChannel
     */
    public ManagedChannel getChannel() {
        if (channel == null || channel.isShutdown()) {
            throw new IllegalStateException("gRPC channel not configured or already shutdown. Call configure() first.");
        }
        return channel;
    }

    /**
     * Check if channel is active
     * @return true if channel is active
     */
    public boolean isChannelActive() {
        return channel != null && !channel.isShutdown() && !channel.isTerminated();
    }

    /**
     * Shutdown the gRPC channel gracefully
     */
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            try {
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                System.out.println("✅ gRPC channel shutdown successfully");
            } catch (InterruptedException e) {
                System.err.println("⚠️ Channel shutdown interrupted, forcing shutdown");
                channel.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Force immediate shutdown of the gRPC channel
     */
    public void shutdownNow() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdownNow();
            System.out.println("✅ gRPC channel forced shutdown");
        }
    }

    /**
     * Get current host
     * @return host
     */
    public String getHost() {
        return host;
    }

    /**
     * Get current port
     * @return port
     */
    public int getPort() {
        return port;
    }

    /**
     * Reset the manager (close channel and clear configuration)
     */
    public void reset() {
        shutdown();
        channel = null;
        host = null;
        port = 0;
    }
}

