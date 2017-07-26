package com.harbin.pandian.database;

/**
 * Created by Harbin on 7/23/17.
 */

public class CheckInDetail {

    public String id, quantity, unit, loc_code, loc_name, certificate_code;

    public CheckInDetail(String id, String quantity, String unit, String loc_code, String loc_name, String certificate_code){
        this.id = id;
        this.quantity = quantity;
        this.unit = unit;
        this.loc_code = loc_code;
        this.loc_name = loc_name;
        this.certificate_code = certificate_code;
    }

}
