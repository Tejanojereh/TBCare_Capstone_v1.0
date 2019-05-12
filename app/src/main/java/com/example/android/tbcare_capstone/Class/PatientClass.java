package com.example.android.tbcare_capstone.Class;

import java.io.Serializable;
import java.util.Date;

public class PatientClass extends BaseClass implements Serializable {
    public String TB_CASE_NO;
    public String Patient_id;
    public String Disease_Classification;
    public String Registration_Group;
    public Date Treatment_Date_Start;
    public float Weight;
    public String Partner_id;
}
