package net.fengni.uitl.cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Created by kuntang on 2014/7/7.
 */
public  class RSACoder {

    //private static String pubkeyValue = "305c300d06092a864886f70d0101010500034b00304802410093ab0b899595746017a9b0d52bf8e3af3a773251e31aa6aeedc6744d4de31ed6f46e039200f759d07995fe67b3b16832462878c343addcc56d75b3720eabef210203010001";
    //private static String privateValue = "30820155020100300d06092a864886f70d01010105000482013f3082013b02010002410093ab0b899595746017a9b0d52bf8e3af3a773251e31aa6aeedc6744d4de31ed6f46e039200f759d07995fe67b3b16832462878c343addcc56d75b3720eabef21020301000102404a628e1447629ad5d0f5acdb252461e522096b32aeb8fd1fff2cc9e72b05eec14746b8fa30f1aaee47970789c9fec7f416da931688d3855d7d330f7ad360e3c1022100ee0096c1975e4cb7f8fa99ce6da43235fb185bc9834a5ef11c8ff6c22f0eb3690221009ed5b4b4fa446d526cb2e162c946e5ac469b50a69fac00a7e2cf5a2ca90c3ef9022100b3227055cf87637909061da3d8b440328f065b7785c1014abaf3c4878d81a419022067aeff68885a74b6b2884ec2aabb622004734cc18847c2a1d5581ff8395dea19022100a8bef62cccfe7c55b431931e2b778d4978883b90a0d2bc99181760515e887f84";

   // private static String info = "data={\"tangkun\":\"sad\",\"frfrfr\":\"gtgtgtgt\"}";


    public static void main(String[] args) {


            String privateValue = "30820155020100300d06092a864886f70d01010105000482013f3082013b02010002410093ab0b899595746017a9b0d52bf8e3af3a773251e31aa6aeedc6744d4de31ed6f46e039200f759d07995fe67b3b16832462878c343addcc56d75b3720eabef21020301000102404a628e1447629ad5d0f5acdb252461e522096b32aeb8fd1fff2cc9e72b05eec14746b8fa30f1aaee47970789c9fec7f416da931688d3855d7d330f7ad360e3c1022100ee0096c1975e4cb7f8fa99ce6da43235fb185bc9834a5ef11c8ff6c22f0eb3690221009ed5b4b4fa446d526cb2e162c946e5ac469b50a69fac00a7e2cf5a2ca90c3ef9022100b3227055cf87637909061da3d8b440328f065b7785c1014abaf3c4878d81a419022067aeff68885a74b6b2884ec2aabb622004734cc18847c2a1d5581ff8395dea19022100a8bef62cccfe7c55b431931e2b778d4978883b90a0d2bc99181760515e887f84";
            String info = "{\"topicId\":\"2820151936\",\"rid\":\"31831864\"}";
            System.out.print(RSACoder.encode(privateValue,info));

    }



    public static String encode(String priKey,String data) {
        byte[] signed = null;
        try {
            PKCS8EncodedKeySpec priPKCS8=new PKCS8EncodedKeySpec(hexStrToBytes(priKey));
            KeyFactory keyf=KeyFactory.getInstance("RSA");
            PrivateKey myprikey=keyf.generatePrivate(priPKCS8);// 用私钥对信息生成数字签名
            java.security.Signature signet = java.security.Signature
                    .getInstance("MD5withRSA");
            signet.initSign(myprikey);
            signet.update(data.getBytes("utf-8"));
            signed = signet.sign(); // 对信息的数字签名
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("签名并生成文件失败");
        }
        return bytesToHexStr(signed);
    }
    /**
     * Transform the specified byte into a Hex String form.
     */
    public static final String bytesToHexStr(byte[] bcd) {
        StringBuffer s = new StringBuffer(bcd.length * 2);
        for (int i = 0; i < bcd.length; i++) {
            s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
            s.append(bcdLookup[bcd[i] & 0x0f]);
        }
        return s.toString();
    }

    private static final char[] bcdLookup = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };



    /**
     * Transform the specified Hex String into a byte array.
     */
    public static final byte[] hexStrToBytes(String s) {
        byte[] bytes;
        bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
                    16);
        }
        return bytes;
    }
}
