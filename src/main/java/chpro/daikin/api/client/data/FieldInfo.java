
package chpro.daikin.api.client.data;

public interface FieldInfo {
    /**
     * 
     * @return string which represents the field id in the rest response (id1=value1,id2=value2)
     */
    String getId();

    /**
     * 
     * @return true if the string is encoded e.g. by url encoding
     */
    boolean isEncoded();

    /**
     * 
     * @return true if the raw value can be parsed to a number
     */
    boolean isNumber();

    /**
     * 
     * @return If the raw value is a number then this is the divisor to scale the value
     */
    int getScale();

}