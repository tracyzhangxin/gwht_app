package Model;

import java.util.Map;

/**
 * Created by Administrator on 2017/2/15.
 */
public class UserInfoModel {
    public String userid;
    public String username;
    public String pwd;
    public String runtime;
    public Map userInfo;
    public UserInfoModel(Map userinfo) {
        this.userid = userinfo.get("userid").toString();
        this.username = userinfo.get("username").toString();
        this.pwd = userinfo.get("password").toString();
        this.runtime = userinfo.get("runtime").toString();
        this.userInfo = userinfo;
    }

    public UserInfoModel() {

    }

    public Map getUserInfo() {
        return this.userInfo;
    }
}
