package chpro.daikin.api.client;

import java.net.InetAddress;

import chpro.daikin.api.client.data.RawData;

public interface ClientService {
    RawData getBasicInfo(InetAddress address);
    
    RawData getControlInfo(InetAddress address);
    
    RawData getSensorInfo(InetAddress address);
    
    RawData getWeekPower(InetAddress address);
    
    RawData getMonitorData(InetAddress address);
    
    RawData getData(InetAddress address, String path);
}
