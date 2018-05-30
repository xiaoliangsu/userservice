package controller;
import com.alibaba.fastjson.JSON;
import com.sun.org.apache.regexp.internal.RE;
import config.GlobalCorsConfig;
import db.DbAdminUserManager;
import db.DbManager;
import db.DbDeviceManage;
import interfaces.ResultInfoInterface;
import model.*;
import model.AddPerson.AddPersonBean;
import model.AddSite.AddSiteBean;
import model.AdminUserBean.AdminUserBean;
import model.AssetHardware.AssetHardwareBean;
import model.AssetHardware.Properties;
import model.AssociaPerson.AssociaPersonBean;
import model.AssociaPerson.Metadata;
import model.ResAdminUserInfoBean.ResAdminUserInfoBean;
import model.ResDeviceList.ResDeviceListBean;
import model.ResSpecBean.ResSpecBean;
import model.SpecBean.SpecBean;
import network.NetworkUtils;
import org.apache.catalina.User;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import sun.nio.ch.Net;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;

@ComponentScan
@Configuration
@EnableAutoConfiguration
@RestController
@RequestMapping("/adminUser")
@Import(GlobalCorsConfig.class)
public class AdminUserController {
    private String adminUsersResult = null;
    private String addAdminUserResult = null;
    private String deleteUserResult = null;
    private String userTenantsResult = null;

    @RequestMapping(value = "/getAdminUsers", method = RequestMethod.GET)
    public String getAdminUsers(@RequestParam(value="sitewhereToken",required = true) String sitewhereToken){
        String url = "http://localhost:8080/sitewhere/api/users?includeDeleted=true";
        adminUsersResult = NetworkUtils.doGetAsync(url, sitewhereToken);
        return adminUsersResult;
    }
    @RequestMapping(value = "/addAdminUsers",method = RequestMethod.POST)
    public String addAdminUsers(@RequestBody AdminUserBean adminUserBean){
        String adminUser=JSON.toJSONString(adminUserBean);
        addAdminUserResult = NetworkUtils.doPostAsync("http://localhost:8080/sitewhere/api/users", adminUser,adminUserBean.getSitewhereToken());
        ResAdminUserInfoBean resAdminUserInfoBean=JSON.toJavaObject(JSON.parseObject(addAdminUserResult), ResAdminUserInfoBean.class);
        DbAdminUserManager.getInstance().init();
        DbAdminUserManager.getInstance().insert(adminUserBean.getUsername(),adminUserBean.getPassword(),resAdminUserInfoBean.getHashedPassword());
        DbAdminUserManager.getInstance().close();
        return addAdminUserResult;
    }

    //删除user
    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public String deleteTenant(@RequestParam(value="name",required = true) String name){
        String url = "http://localhost:8080/sitewhere/api/users/"+name+"?force=true";
        deleteUserResult = NetworkUtils.doDeleteAsync(url, "");
        DbAdminUserManager.getInstance().init();
        DbAdminUserManager.getInstance().delete(name);
        DbAdminUserManager.getInstance().close();
        return deleteUserResult;
    }

    //获取user的tenant list
    @RequestMapping(value = "/getUserTenants", method = RequestMethod.GET)
    public String getUserTenants(@RequestParam(value="authUserId",required = true) String authUserId,@RequestParam(value="page",required = true) String page,@RequestParam(value="pageSize",required = true) String pageSize){
        String url = "http://localhost:8080/sitewhere/api/tenants?includeRuntimeInfo=true&authUserId="+authUserId+"&page="+page+"&pageSize="+pageSize;
        userTenantsResult = NetworkUtils.doGetAsync(url, "");
        return userTenantsResult;
    }



}
