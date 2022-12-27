
package chpro.daikin.api.discover.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chpro.daikin.api.discover.DiscoverService;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

@Singleton
public class DefaultDiscoverService implements DiscoverService {
    
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDiscoverService.class);

    @Value("${daikin.api.discover.probe-data}")
    protected String probeData;

    @Value("${daikin.api.discover.bind.port}")
    protected int bindPort;

    @Value("${daikin.api.discover.buffer-size}")
    protected int bufferSize;
    
    @Value("${daikin.api.discover.bind.address}")
    protected String bindAddress;

    @Override
    public Collection<InetAddress> discover() {
        LOG.info("Starting discovery of daikin devices ...");
        
        Set<InetAddress> devices = new HashSet<>();
        
        try (DatagramSocket sendSocket = new DatagramSocket(bindPort + 1, InetAddress.getLocalHost())) {
            sendSocket.setBroadcast(true);
            
            byte[] data = probeData.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(bindAddress), bindPort);
            sendSocket.send(packet);
            LOG.debug("Discovery package sent - {}:{}", packet.getAddress(), + packet.getPort());
    
            // Discovery response command
            byte[] response = new byte[bufferSize];
            DatagramPacket responsePacket = new DatagramPacket(response, response.length);
            sendSocket.receive(responsePacket);
            LOG.debug("Discovery response received - {}:{}", responsePacket.getAddress(), responsePacket.getPort());

            LOG.trace("Got discover response {} ", new String(responsePacket.getData()).trim());
            devices.add(responsePacket.getAddress());
        } catch (Exception e) {
            LOG.error("Failed to discover devices", e);
        }
        
        return devices;
    }
}
