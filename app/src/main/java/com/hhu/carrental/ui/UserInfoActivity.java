package com.hhu.carrental.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.hhu.carrental.R;
import com.hhu.carrental.bean.BikeInfo;
import com.hhu.carrental.bean.User;
import com.hhu.carrental.main.MainActivity;
import com.hhu.carrental.service.LocationService;
import com.hhu.carrental.util.StatusBarUtils;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.SaveListener;

/**
 * 个人信息
 */
public class UserInfoActivity extends Activity implements View.OnClickListener{

    private BmobUserManager userManager;
    private TextView nametv;
    private Button logout;
    private RelativeLayout rentOut,rentAmount;
    private double latitude;
    private double longitude;
    private User user;
    private LocationService locationService;
    private ImageButton back;
    private BikeInfo bikeInfo = null;
    private BmobGeoPoint newlocation = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.color_title);
        userManager = BmobUserManager.getInstance(this);
        setContentView(R.layout.activity_user_info);
        initView();
    }

    private void initView(){
        nametv = (TextView) findViewById(R.id.username);
        logout = (Button) findViewById(R.id.btn_logout);
        back = (ImageButton) findViewById(R.id.userinfo_back);
        rentOut = (RelativeLayout) findViewById(R.id.layout_rent_bike);
        user = userManager.getCurrentUser(User.class);
        nametv.setText(user.getUsername());
        logout.setOnClickListener(this);
        rentOut.setOnClickListener(this);

        rentAmount = (RelativeLayout)findViewById(R.id.layout_rent_amoutbike);
        rentAmount.setOnClickListener(this);
        back.setOnClickListener(this);

        BDLocationListener myListenter = new MyLocationListenner();
        locationService = new LocationService(getApplicationContext());
        locationService.registerListener(myListenter);
        locationService.start();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_logout:
                userManager.logout();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.layout_rent_bike:
                startActivity(new Intent(this,RentOutActivity.class));
                break;
            case R.id.layout_rent_amoutbike:
                rentAmoutBike();
                startActivity(new Intent(this,MainActivity.class));
                locationService.stop();
                finish();
                break;
            case R.id.userinfo_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void rentAmoutBike(){



        double random0=0.0,random1 = 0.0;
        double varlon,varlat;
        for(int i =0;i<30;i++){
            random0 = (Math.random()/100000)*1000;
            random1 = (Math.random()/100000)*1000;
            random0 = Double.parseDouble(String .format("%.6f",random0));
            random1 = Double.parseDouble(String .format("%.6f",random1));
            varlon = longitude;
            varlat = latitude;
            Log.e("随机数：","random0:"+random0+"random1:"+random1+"longitude:"+longitude+"latitude:"+latitude);
            switch(i%10){
                case 1:
                case 2:
                case 3:
                    varlon = varlon-random0;
                    varlat = varlat-random1;
                    //location = new BmobGeoPoint(longitude-random0,latitude-random1);
                    break;
                case 4:
                case 5:
                    varlon = varlon+random0;
                    varlat = varlat-random1;
                    //location = new BmobGeoPoint(longitude+random0,latitude-random1);
                    break;
                case 7:
                case 8:
                    varlon = varlon-random0;
                    varlat = varlat+random1;
                    //location = new BmobGeoPoint(longitude-random0,latitude+random1);
                    break;
                default:
                    varlon = varlon+random0;
                    varlat = varlat+random1;
                    //location = new BmobGeoPoint(longitude+random0,latitude+random1);
                    break;
            }
            Log.e("坐标","varlon:"+varlon+"varlat:"+varlat);
            if(varlon >100&&varlat>10){
                newlocation = new BmobGeoPoint(varlon,varlat);
                //bikeInfo = new BikeInfo("123456",newlocation,user,"110","山地车","我是一辆单车","20170506");
                generateBikeInfo();
                bikeInfo.save(this,new SaveListener(){
                    @Override
                    public void onSuccess() {
                        Log.e("出租成功出租成功出租成功","----");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.e("出租失败出租失败出租失败",i+s);
                    }
                });
            }

        }



    }


    public class MyLocationListenner implements BDLocationListener {

        private String lastFloor = null;

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null ) {
                return;
            }
            //Log.e("location","notnull");
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    private void generateBikeInfo(){
        int i,k,l,d;
        long j;
        String[] type ={"通勤自行车","旅行自行车","山地自行车","公路自行车","小轮车","死飞车","折叠自行车","速降山地车","越野公路车"
                ,"计时赛自行车","场地赛自行车","斜躺车","协力车","淑女自行车","亲子自行车","水上自行车","海滩自行车"};//单车类型数组
        String[] details={"绿色出行就选共享单车","听说骑共享单车的人都脱单了","让自行车回归城市","生命在于运动,运动就骑共享单车"
                ,"我要让城市变得更美好","用单车温暖你的城市","今天你骑共享单车了吗","先定个小目标，骑100天共享单车"
                ,};//8 标语数组
        i = (int)((Math.random()*100)/6.0);//随机生成单车类型下标
        j = (long)(Math.random()*Math.pow(10,10));//随机生成电话号码
        k = (int)(Math.random()*1000000);//随机生成锁的密码
        l = (int)(Math.random()*500)+10;//随机生成出租时间
        d = (int)(Math.random()*10/1.43);//随机生成口号下标

        String rentTime;
        if(l/60>0){
            rentTime = l/60+"小时"+(l-(l/60)*60)+"分钟";
        }else {
            rentTime = l+"分钟";
        }
        bikeInfo = new BikeInfo(k+"",newlocation,user,"1"+j+"",type[i],details[d],rentTime);

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        locationService.stop();

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
