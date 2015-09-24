package common;

import com.nifeng.core.user.DaoHelper;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by fengni on 2015/9/17.
 */
public class Test {
    @org.junit.Test
    public void testIp(){
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()){
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                if(networkInterface.isUp()) {
                    Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                    while (inetAddressEnumeration.hasMoreElements()) {
                        InetAddress inetAddress = inetAddressEnumeration.nextElement();
                        if(inetAddress instanceof Inet4Address && inetAddress.isSiteLocalAddress()){
                            System.out.println(inetAddress.getHostAddress());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @org.junit.Test
    public void testAnotation(){
        DaoHelper daoHelper = new DaoHelper();
        List<String > packageList = new ArrayList<String>();
        packageList.add("com.nifeng.core.user.model");
        daoHelper.build(packageList);
        Set<String> keySet = DaoHelper.insertMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            System.out.println(key+","+DaoHelper.insertMap.get(key));
        }

        Set<String> keySetUpdate = DaoHelper.updataMap.keySet();
        Iterator<String> iteratorUpdate = keySet.iterator();
        while (iteratorUpdate.hasNext()){
            String key = iteratorUpdate.next();
            System.out.println(key+","+DaoHelper.updataMap.get(key));
        }
    }
}
