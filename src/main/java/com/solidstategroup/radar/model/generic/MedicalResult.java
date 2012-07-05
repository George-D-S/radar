package com.solidstategroup.radar.model.generic;


import java.io.Serializable;
import java.util.Date;

/**
 * table: testresult
 */
public class MedicalResult implements Serializable{
    // testcode values on the right - basically a unique id for each result type in the testresult table
    private String id; // id is nhsno
    private Double bloodUrea; // urea
    private Date bloodUreaDate; //
    private Double serumCreatanine; // creatinine
    private Date creatanineDate;
    private Double weight; // weight
    private Date weightDate;
    private Double height; // height
    private Date heightDate;
    private Integer bpSystolic; // BPsys
    private Date bpSystolicDate;
    private Integer bpDiastolic; // BPdia
    private Date bpDiastolicDate; //     
    private Date bpDate;
    private YesNo antihypertensiveDrugs; //  antihypertensive
    private Date antihypertensiveDrugsDate; //  antihypertensive

    public enum YesNo {
        YES("1"),
        NO("0"),
        UNKNOWN("no");

        private String id;

        YesNo(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
        public static YesNo getById(String id) {
            for (YesNo yesNo : values()) {
                if (yesNo.getId().equals(id)) {
                    return yesNo;
                }
            }
            return null;
        }


        @Override
        public String toString() {
            if (this.equals(YES)) {
                return "Yes";
            } else if (this.equals(NO)) {
                return "No";
            } else {
                return "Unknown";
            }
        }
    }

    public Double getBloodUrea() {
        return bloodUrea;
    }

    public void setBloodUrea(Double bloodUrea) {
        this.bloodUrea = bloodUrea;
    }

    public Date getBloodUreaDate() {
        return bloodUreaDate;
    }

    public void setBloodUreaDate(Date bloodUreaDate) {
        this.bloodUreaDate = bloodUreaDate;
    }

    public Double getSerumCreatanine() {
        return serumCreatanine;
    }

    public void setSerumCreatanine(Double serumCreatanine) {
        this.serumCreatanine = serumCreatanine;
    }

    public Date getCreatanineDate() {
        return creatanineDate;
    }

    public void setCreatanineDate(Date creatanineDate) {
        this.creatanineDate = creatanineDate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Date getWeightDate() {
        return weightDate;
    }

    public void setWeightDate(Date weightDate) {
        this.weightDate = weightDate;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Date getHeightDate() {
        return heightDate;
    }

    public void setHeightDate(Date heightDate) {
        this.heightDate = heightDate;
    }

    public Integer getBpSystolic() {
        return bpSystolic;
    }

    public void setBpSystolic(Integer bpSystolic) {
        this.bpSystolic = bpSystolic;
    }

    public Integer getBpDiastolic() {
        return bpDiastolic;
    }

    public void setBpDiastolic(Integer bpDiastolic) {
        this.bpDiastolic = bpDiastolic;
    }

    public Date getBpDate() {
        return bpDate;
    }

    public void setBpDate(Date bpDate) {
        this.bpDate = bpDate;
    }

    public YesNo getAntihypertensiveDrugs() {
        return antihypertensiveDrugs;
    }

    public void setAntihypertensiveDrugs(YesNo antihypertensiveDrugs) {
        this.antihypertensiveDrugs = antihypertensiveDrugs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getBpSystolicDate() {
        return bpSystolicDate;
    }

    public void setBpSystolicDate(Date bpSystolicDate) {
        this.bpSystolicDate = bpSystolicDate;
    }

    public Date getBpDiastolicDate() {
        return bpDiastolicDate;
    }

    public void setBpDiastolicDate(Date bpDiastolicDate) {
        this.bpDiastolicDate = bpDiastolicDate;
    }

    public Date getAntihypertensiveDrugsDate() {
        return antihypertensiveDrugsDate;
    }

    public void setAntihypertensiveDrugsDate(Date antihypertensiveDrugsDate) {
        this.antihypertensiveDrugsDate = antihypertensiveDrugsDate;
    }
}
