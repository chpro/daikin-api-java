
package chpro.daikin.api.client.impl;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.StatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chpro.daikin.api.client.ClientService;
import chpro.daikin.api.client.data.RawData;
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
        String uri = String.format("http://%s/%s", address.getHostAddress(), path);
        LOG.info("Getting data from endpoint {}", uri);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet(uri);
            final String result = client.execute(httpget, response -> {
                LOG.trace("Got response for {} status {}", httpget,new StatusLine(response));
                // Process response message and convert it into a value object
                return EntityUtils.toString(response.getEntity());
            });
            LOG.trace("Response for endpoint {} was: {}", uri, result);
            return new RawData(result);
        } catch (IOException e) {
            LOG.error("Was not able to get response for " + uri, e);
        }
        return new RawData();
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
