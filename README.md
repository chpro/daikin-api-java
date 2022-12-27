# Daikin API

This projects provides CLI and API to communicate with daikin aircondition units. It is based on java 11 and micronaut.

## Used endpoints

- common/basic_info
- aircon/get_control_info
- aircon/get_sensor_info
- aircon/get_week_power
- aircon/get_monitordata

## Run CLI

To get all available options use

`./gradlew run --args="--help"`


`./gradlew run --args="-d"` will print ip addresses of all discovered devices

`./gradlew run --args="-g"` will print the rawdata received by all available endpoints for all discovered devices

To execute for only one device pass the address/hostname of the device.

## API integration

For integration use `chpro.daikin.api.client.ClientService` the methods will call the desired endpoint and
return a `chpro.daikin.api.client.data.RawData` object.

For each available field in the response you can either get the raw value the decoded value or if it is a number the parsed number.

The information how a field needs to be handled is configured via `chpro.daikin.api.client.data.FieldInfo`.
There is an enum `chpro.daikin.api.client.data.Field` which contains already most common field configurations.

## Micronaut 3.7.4 Documentation

- [User Guide](https://docs.micronaut.io/3.7.4/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.7.4/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.7.4/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)
