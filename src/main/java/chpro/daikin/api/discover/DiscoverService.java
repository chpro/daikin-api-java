package chpro.daikin.api.discover;

import java.net.InetAddress;
import java.util.Collection;

public interface DiscoverService {

    Collection<InetAddress> discover();
}
