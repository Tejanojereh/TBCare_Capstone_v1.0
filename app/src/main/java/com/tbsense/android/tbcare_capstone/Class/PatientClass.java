package com.tbsense.android.tbcare_capstone.Class;

import java.io.Serializable;
import java.util.Date;

public class PatientClass extends BaseClass implements Serializable {
    public String TB_CASE_NO;
    public String Patient_id;
    public String Disease_Classification;
    public String Registration_Group;
    public Date Treatment_Date_Start;
    public Date First_Medication_Initial_Date;
    public Date First_Medication_End_Date;
    public Date Second_Medication_Initial_Date;
    public Date Second_Medication_End_Date;
    public float Weight;
    public String Partner_id;
}
