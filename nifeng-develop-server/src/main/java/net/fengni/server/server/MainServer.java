package net.fengni.server.server;

/**
                           _ooOoo_
                          o8888888o
                          88" . "88
                          (| -_- |)
                          O\  =  /O
                       ____/`---'\____
                     .'  \\|     |//  `.
                    /  \\|||  :  |||//  \
                   /  _||||| -:- |||||-  \
                   |   | \\\  -  /// |   |
                   | \_|  ''\---/''  |   |
                   \  .-\__  `-`  ___/-. /
                 ___`. .'  /--.--\  `. . __
              ."" '<  `.___\_<|>_/___.'  >'"".
             | | :  `- \`.;`\ _ /`;.`/ - ` : | |
             \  \ `-.   \_ __\ /__ _/   .-` /  /
        ======`-.____`-.___\_____/___.-`____.-'======
                           `=---='
        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                 佛祖保佑       永无BUG
 */

public class MainServer extends AbstractServer {

    public MainServer(String[] anArgs) {
        super(anArgs);
    }

    public static void main(String... anArgs) throws Exception {
        new MainServer(anArgs).run();

    }

    @Override
    public void init(Config config) {
        config.setMin_thread(128);
        config.setMax_thread(512);

       /* //初始化消息中心
        Notify.init(config.port);

        NotifyClientConsumer.init(config.port);
        //NsqLeaderSelector.start();
        if(NsqNotifyConfig.isServerNameExist()){
            NotifyServer notifyServer=new NotifyServer();
            notifyServer.init();
        }*/
    }

}
