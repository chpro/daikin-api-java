
package chpro.daikin.api.client.impl;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chpro.daikin.api.client.ClientService;
import chpro.daikin.api.client.data.RawData;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.HttpClientRegistry;
import jakarta.inject.Inject;

public class DefaultClientService implements ClientService {

    public static final String URI_AIRCON_GET_MONITORDATA = "aircon/get_monitordata";

    public static final String URI_AIRCON_GET_WEEK_POWER = "aircon/get_week_power";

    public static final String URI_AIRCON_GET_SENSOR_INFO = "aircon/get_sensor_info";

    public static final String URI_AIRCON_GET_CONTROL_INFO = "aircon/get_control_info";

    public static final String URI_COMMON_BASIC_INFO = "common/basic_info";

    private static final Logger LOG = LoggerFactory.getLogger(DefaultClientService.class);

    @Inject
    protected HttpClientRegistry<HttpClient> registry;

    @Override
    public RawData getData(InetAddress address, String path) {
        HttpClient client = registry.getDefaultClient();
        String uri = String.format("http://%s/%s", address.getHostAddress(), path);
        LOG.info("Getting data from endpoint {}", uri);

        String data = client.toBlocking().retrieve(HttpRequest.GET(uri)
                // hack because daikin http server expects Host not host
                .header("Host", address.getHostAddress()));
        LOG.trace("Got response data from endpoint {}: {}", uri, data);
        return new RawData(data);
    }

    @Override
    public RawData getBasicInfo(InetAddress address) {
        return getData(address, URI_COMMON_BASIC_INFO);
    }

    @Override
    public RawData getControlInfo(InetAddress address) {
        return getData(address, URI_AIRCON_GET_CONTROL_INFO);
    }

    @Override
    public RawData getSensorInfo(InetAddress address) {
        return getData(address, URI_AIRCON_GET_SENSOR_INFO);
    }

    @Override
    public RawData getWeekPower(InetAddress address) {
        return getData(address, URI_AIRCON_GET_WEEK_POWER);
    }

    @Override
    public RawData getMonitorData(InetAddress address) {
        return getData(address, URI_AIRCON_GET_MONITORDATA);
    }

}
