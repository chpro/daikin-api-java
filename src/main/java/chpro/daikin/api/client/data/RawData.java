
package chpro.daikin.api.client.data;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RawData {

    private static final Logger LOG = LoggerFactory.getLogger(RawData.class);

    protected String rawData;

    protected Map<String, String> parsedRawData;

    public RawData(String rawData) {
        setRawData(rawData);
    }

    public void setRawData(String data) {
        this.rawData = data;
        parsedRawData = Stream.of(data.split(",")).map(str -> str.split("="))
                .collect(Collectors.toMap(str -> str[0], str -> (str.length <= 1 ? "" : str[1])));
        if (LOG.isTraceEnabled()) {
            LOG.trace("Parsed string contains {} entries", parsedRawData.size());
            parsedRawData.forEach((key, value) -> LOG.trace("Key={} : Value={}", key, value));
        }

    }

    public String getRawValue(Field key) {
        return parsedRawData.get(key.id);
    }

    public String getDecodedValue(Field key) {
        String rawValue = getRawValue(key);
        if (key.isDecode()) {
            if (key.isNumber()) {
                return parseHexToString(rawValue);
            } else {
                try {
                    return URLDecoder.decode(rawValue, StandardCharsets.UTF_8.name());
                } catch (UnsupportedEncodingException e) {
                    LOG.error(String.format("Was not able to decode string %s. Returning raw value", rawValue), e);
                }
            }
        }
        return rawValue;
    }

    public BigDecimal getNumberValue(Field key) {
        return BigDecimal.valueOf(Double.parseDouble(getDecodedValue(key))).divide(BigDecimal.valueOf(key.getScale()));
    }

    

    protected String parseHexToString(String hex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            int byteVal = Integer.parseInt(str, 16);
            baos.write(byteVal);
        }
        return new String(baos.toByteArray(), Charset.forName("UTF-8"));
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " [" + rawData + "]";
    }

    public static enum Field {

        // common basic
        DST("dst", false, true, 1), // Value=1
        PV("pv", false, true, 1), // Value=3.20
        PW("pw", false, false, 1), // Value=PASSWORD
        ICON("icon", false, true, 1), // Value=0
        GRP_NAME("grp_name", false, false, 1), // Value=
        LED("led", false, true, 1), // Value=1
        TYPE("type", false, false, 1), // Value=aircon
        LPW_FLAG("lpw_flag", false, true, 1), // Value=0
        MAC("mac", false, false, 1), // Value=D0C5D3C40089
        REG("reg", false, false, 1), // Value=eu
        RADIO1("radio1", false, true, 1), // Value=-42
        POW("pow", false, true, 1), // Value=0
        ID("id", false, false, 1), // Value=USERNAME
        EN_GRP("en_grp", false, true, 1), // Value=0
        SSID1("ssid1", false, false, 1), // Value=SSID
        RET("ret", false, false, 1), // Value=OK
        VER("ver", false, false, 1), // Value=1_2_54
        REV("rev", false, false, 1), // Value=203DE8C
        ERR("err", false, true, 1), // Value=0
        METHOD("method", false, false, 1), // Value=polling
        CPV("cpv", false, true, 1), // Value=3
        ADP_KIND("adp_kind", false, true, 1), // Value=3
        EN_SETZONE("en_setzone", false, true, 1), // Value=1
        PORT("port", false, true, 1), // Value=30050
        NAME("name", true, false, 1), // Value=%4b%6c%69%6d%61%20%45%47
        CPV_MINOR("cpv_minor", false, true, 1), // Value=20
        ADP_MODE("adp_mode", false, false, 1), // Value=run
        LOCATION("location", false, true, 1), // Value=0
        EN_HOL("en_hol", false, true, 1), // Value=1

        // control
        B_SHUM("b_shum", false, true, 1), // Value=0
        B_MODE("b_mode", false, true, 1), // Value=3
        STEMP("stemp", false, true, 1), // Value=23.0
        MODE("mode", false, true, 1), // Value=3
        DMND_RUN("dmnd_run", false, true, 1), // Value=0
        ALERT("alert", false, true, 1), // Value=255
        DFRH("dfrh", false, true, 1), // Value=5
        SHUM("shum", false, true, 1), // Value=0
        // POW("pow", false, true, 1), // Value=0
        B_F_DIR("b_f_dir", false, true, 1), // Value=0
        // RET("ret", false, false, 1), // Value=OK
        DT1("dt1", false, true, 1), // Value=25.0
        DFD1("dfd1", false, true, 1), // Value=0
        DT3("dt3", false, true, 1), // Value=23.0
        DT2("dt2", false, false, 1), // Value=M
        B_STEMP("b_stemp", false, true, 1), // Value=23.0
        DT5("dt5", false, true, 1), // Value=25.0
        DT4("dt4", false, true, 1), // Value=25.0
        DHH("dhh", false, true, 1), // Value=50
        DT7("dt7", false, true, 1), // Value=25.0
        F_RATE("f_rate", false, false, 1), // Value=A
        DFR7("dfr7", false, true, 1), // Value=5
        DFR6("dfr6", false, true, 1), // Value=5
        DFDH("dfdh", false, true, 1), // Value=0
        DFR5("dfr5", false, true, 1), // Value=5
        DFR4("dfr4", false, true, 1), // Value=5
        DH1("dh1", false, false, 1), // Value=AUTO
        DFR3("dfr3", false, false, 1), // Value=A
        DFR2("dfr2", false, true, 1), // Value=5
        DH3("dh3", false, true, 1), // Value=0
        DFR1("dfr1", false, true, 1), // Value=5
        DH2("dh2", false, true, 1), // Value=50
        DH5("dh5", false, true, 1), // Value=0
        DH4("dh4", false, true, 1), // Value=0
        ADV("adv", false, true, 1), // Value=
        DH7("dh7", false, false, 1), // Value=AUTO
        EN_DEMAND("en_demand", false, true, 1), // Value=1
        B_F_RATE("b_f_rate", false, false, 1), // Value=A
        DFD7("dfd7", false, true, 1), // Value=0
        DFD6("dfd6", false, true, 1), // Value=0
        DFD5("dfd5", false, true, 1), // Value=0
        DFD4("dfd4", false, true, 1), // Value=0
        F_DIR("f_dir", false, true, 1), // Value=0
        DFD3("dfd3", false, true, 1), // Value=0
        DFD2("dfd2", false, true, 1), // Value=0

        // sensor
        // RET("ret", false, false, 1), // Value=OK
        OTEMP("otemp", false, true, 1), // Value=5.0
        MOMPOW("mompow", false, true, 1), // Value=1
        HTEMP("htemp", false, true, 1), // Value=20.0
        // ERR("err", false, true, 1), // Value=0
        HHUM("hhum", false, true, 1), // Value=-
        CMPFREQ("cmpfreq", false, true, 1), // Value=0

        // week power data
        // RET("ret", false, false, 1), // Value=OK
        TODAY_RUNTIME("today_runtime", false, true, 1), // Value=0
        DATAS("datas", false, false, 1), // Value=0/100/0/0/100/0/0

        // monitor
        TRTMP("trtmp", true, true, 10), // Value=2b323430
        GX00("gx00", true, true, 1), // Value=3041
        GX01("gx01", true, true, 1), // Value=3441
        RAWRTMP("rawrtmp", true, true, 10), // Value=2b323030
        GX02("gx02", true, true, 1), // Value=30303030
        GX03("gx03", true, true, 1), // Value=3030
        GX04("gx04", true, true, 1), // Value=3430
        GX05("gx05", true, true, 1), // Value=3030
        GX06("gx06", true, true, 1), // Value=30303030
        GX07("gx07", true, true, 1), // Value=30314332
        GX08("gx08", true, true, 1), // Value=00000000
        GX09("gx09", true, true, 1), // Value=46464646
        // MAC("mac", false, false, 1), // Value=d0c5d3c40089
        // MODE("mode", false, true, 1), // Value=33
        FAN("fan", true, true, 1), // Value=303030
        HETMP("hetmp", true, true, 10), // Value=2b323130
        EEPID("eepid", true, true, 1), // Value=30303030
        MONDATA("mondata", false, false, 1), // Value=pv
        // POW("pow", true, true, 1), // Value=30
        POLLINGERRCNT("PollingErrCnt", false, true, 1), // Value=8230
        ROUTERDISCONCNT("RouterDisconCnt", false, true, 1), // Value=297
        // RET("ret", false, true, 1), // Value=OK
        TAP("tap", true, true, 1), // Value=41
        // CPV("cpv", false, true, 1), // Value=3
        HUMID("humid", true, true, 1), // Value=ff
        ITELC("itelc", true, true, 1), // Value=3030303030314445
        LASTRESETTIME("LastResetTime", false, false, 1), // Value=2022/11/15 07:05:43
        GX0A("gx0A", true, true, 1), // Value=36463336
        FANGL("fangl", true, true, 1), // Value=2b313136
        GX0B("gx0B", true, true, 1), // Value=3030
        GX0C("gx0C", true, true, 1), // Value=3030
        // CPV_MINOR("cpv_minor", false, true, 1), // Value=20
        RESETCOUNT("ResetCount", false, true, 1), // Value=119
        CMPFRQ("cmpfrq", true, true, 1), // Value=303030
        OTMP("otmp", true, true, 1); // Value=2b303530

        protected final String id;
        public final boolean decode;
        public final boolean number;
        public final int scale;

        private Field(String id, boolean decode, boolean number, int scale) {
            this.id = id;
            this.decode = decode;
            this.number = number;
            this.scale = scale;
        }

        public String getId() {
            return id;
        }

        public boolean isDecode() {
            return decode;
        }

        public boolean isNumber() {
            return number;
        }

        public int getScale() {
            return scale;
        }
        
        public static Field getById(String id) {
            return Field.valueOf(id.toUpperCase());
        }
    }
}