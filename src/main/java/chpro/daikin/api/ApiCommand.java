
package chpro.daikin.api;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import chpro.daikin.api.client.ClientService;
import chpro.daikin.api.discover.DiscoverService;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.core.util.CollectionUtils;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "api", description = "...", mixinStandardHelpOptions = true)
public class ApiCommand implements Runnable {

    @Option(names = { "-d", "--discover" }, description = "Start discover of daikin devices on network")
    boolean discover;
    
    @Option(names = { "-g", "--get" }, description = "Get and print all available endpoints")
    boolean get;
    
    @Option(names = { "-i", "--ips" }, description = "The ip addresses of the devices for which the request should be sent used with -g (--get) option")
    HashSet<String> inetAddresses;

    @Inject
    DiscoverService discoverService;
    
    @Inject
    ClientService clientService;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(ApiCommand.class, args);
    }

    public void run() {
        // business logic here
        try {
            if (discover) {
                System.out.println("Discovered devices: " + CollectionUtils.toString(discoverService.discover()));
            } else if (get) {
                Collection<InetAddress> addresses = (inetAddresses == null ? discoverService.discover() : inetAddresses.stream().map(x -> mapString2InetAddress(x)).collect(Collectors.toSet()));
                System.out.println("Get: " + CollectionUtils.toString(addresses));
                addresses.stream().forEach(x -> executeGets(x));
            } else {
                System.out.println("No parameter");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void executeGets(InetAddress x) {
        System.out.println(clientService.getBasicInfo(x));
        System.out.println(clientService.getControlInfo(x));
        System.out.println(clientService.getMonitorData(x));
        System.out.println(clientService.getSensorInfo(x));
        System.out.println(clientService.getWeekPower(x));
    }

    protected InetAddress mapString2InetAddress(String addr) {
        try {
            return InetAddress.getByName(addr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}
